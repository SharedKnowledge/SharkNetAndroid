package net.sharksystem.sharkqr.sharkqr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import android.app.AlertDialog;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.util.Log;
import android.content.DialogInterface;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.*;
import net.sharkfw.security.key.*;

import java.io.IOException;
import java.security.*;

/*
* ActivityClass that allows SharkQR to be used on Android devices
*
* Extends <code>Activity</code> and implements <code>OnClickListener</code>
* to enable Android event triggers.
*/
public class MainSharkQRActivity extends Activity implements OnClickListener {

    /* Button to trigger code-scanning */
    private Button scanBtn;
    /* SharkQRWrapper containing the data of the most recent scan */
    private SharkQRWrapper scanResult;

    @Override
    /*
    * Method equivalent to an Constructor in Android
    *
    * @param Bundle savedInstanceState    State-information passed to the application by Android
    */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shark_qr);

        scanResult = null;
        scanBtn = (Button) findViewById(R.id.QRscan);
        scanBtn.setOnClickListener(this);

        // Setup local keys
        SharkKeyGenerator keyGenerator = new SharkKeyGenerator(SharkKeyPairAlgorithm.RSA, 50);
        PublicKey key = keyGenerator.getPublicKey();

        // Setup Peer
        String[] aliceSIs = new String[] { "http://www.sharksystem.net/alice.html" };
        String[] aliceAddr = new String[] { "mail://alice@wonderland.net" };

        PeerSemanticTag alice = InMemoSharkKB.createInMemoPeerSemanticTag("Alice", aliceSIs, aliceAddr);

        // Initiate serializable wrapper
        SharkQRWrapper wrap = new SharkQRWrapper(alice, key);

        // Generate QR Code
        ImageView QRcodeView;
        QRcodeView = (ImageView) findViewById(R.id.QRcode);
        QRCodeGenerator generator = new QRCodeGenerator();

        // Display QR Code
        Bitmap bmp = generator.create(wrap);
        if (bmp != null)
            QRcodeView.setImageBitmap(bmp);

    }

    /*
    * Event to be triggered by pressing the scan-button
    *
    * @param View v   Since Android uses an evolved version of MVP we have to pass the View that needs to be changed
    */
    public void onClick(View v) {
        if ( v.getId() == R.id.QRscan ) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
            scanIntegrator.initiateScan();
        }
    }

    /*
    * This determines how our app will respond in case of an ActivityResult by the QR Code Scanner.
    * Therefore multiple parameters need to be passed.
    *
    * @param int requestCode
    * @param resultCode
    * @param Intent intent
    */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {

            String scanContent = scanningResult.getContents();

            try {

                scanResult = (SharkQRWrapper) SerializeHelper.fromString(scanContent.toString());

                // Ab hier steht das SharkWRWrapper Objekt aus dem QR-Code zur Verfügung

                String scanFormat = scanningResult.getFormatName();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainSharkQRActivity.this);
                builder.setMessage("Möchtest du den Public Key von " + scanResult.getName() + " speichern?").setTitle("Speichern?");

                builder.setPositiveButton("speichern", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        // Ab hier hat der User das Speichern bestätigt
                        // @todo Mit Hilfe von scanResult.getKey()den PublicKey in der PKStorage speichern

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainSharkQRActivity.this);
                        alertDialog.setTitle("Gespeichert");
                        alertDialog.setMessage(scanResult.getName() + " erfolgreich gespeichert");
                        AlertDialog dialog1 = alertDialog.create();
                        dialog1.show();

                    }

                });

                builder.setNegativeButton("verwerfen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_shark_qr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
