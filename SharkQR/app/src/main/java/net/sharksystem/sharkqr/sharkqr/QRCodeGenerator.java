package net.sharksystem.sharkqr.sharkqr;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;
import android.view.View.*;
import android.graphics.Color;

import java.io.IOException;

/*
* Generates an QRCode based on an <code>PeerSemanticTag</code> and a Key
*
*/
public class QRCodeGenerator {

    /* Bitmap to represent the QRCode */
    private Bitmap bmp;

    /*
    * This Constructor resets the BitMap
    */
    public QRCodeGenerator() {
        bmp = null;
    }

    /*
    * Creates the QRCode.
    *
    * @param SharkQRWrapper wrap  SharkWrapper class containing personal data.
    *
    * @return bmp                Bitmap containing the QRCdode
    */
    public Bitmap create(SharkQRWrapper wrap) {

        try {
            String content = SerializeHelper.toString(wrap);

            QRCodeWriter writer = new QRCodeWriter();

            try {
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }

        } catch ( IOException e ) {

        }

        return bmp;

    }

}
