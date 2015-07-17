package net.sharkystem.sharkqr.sharkqr.tests;

import android.test.AndroidTestCase;
import android.test.*;
import android.test.suitebuilder.annotation.SmallTest;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.security.key.SharkKeyGenerator;
import net.sharkfw.security.key.SharkKeyPairAlgorithm;
import net.sharksystem.sharkqr.sharkqr.MainSharkQRActivity;
import net.sharksystem.sharkqr.sharkqr.SerializeHelper;
import net.sharksystem.sharkqr.sharkqr.SharkQRWrapper;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Arrays;

public class ApplicationTest extends ActivityUnitTestCase<MainSharkQRActivity>{

    MainSharkQRActivity activity;
    PeerSemanticTag peer;
    PublicKey key;

    public ApplicationTest() {
        super(MainSharkQRActivity.class);
    }

    protected void setUp() throws Exception {

        super.setUp();
        activity = getActivity();

        // Setup Peer
        String[] aliceSIs = new String[] { "http://www.sharksystem.net/alice.html" };
        String[] aliceAddr = new String[] { "mail://alice@wonderland.net" };

        peer = InMemoSharkKB.createInMemoPeerSemanticTag("Alice", aliceSIs, aliceAddr);

        // Setup local keys
        SharkKeyGenerator keyGenerator = new SharkKeyGenerator(SharkKeyPairAlgorithm.RSA, 50);
        key = keyGenerator.getPublicKey();

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSharkQRWrapper() {
        SharkQRWrapper wrapper = new SharkQRWrapper(peer, key);
        assertEquals(wrapper.getName(), peer.getName());
        assertEquals(wrapper.getKey(), key);
        assertEquals(wrapper.getSI().toString(), peer.getSI().toString());
    }

    public void testSerializeHelper() {
        SharkQRWrapper wrapper = new SharkQRWrapper(peer, key);
        try {
            String content = SerializeHelper.toString(wrapper);
            try {
                SharkQRWrapper result = (SharkQRWrapper) SerializeHelper.fromString(content);
                assertEquals(result.getKey(), wrapper.getKey());
                assertEquals(result.getName(), wrapper.getName());
                assertEquals(Arrays.toString(result.getSI()), Arrays.toString(wrapper.getSI()));
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}