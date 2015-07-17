package net.sharksystem.sharkqr.sharkqr;

import net.sharkfw.knowledgeBase.PeerSemanticTag;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * SharQRWrapper class.
 * Allows to extract data from a <code>PeerSemanticTag</code> and a <code>PublicKey</code>
 * This infromation will be serializable, which allows fragmentation and refragmentation
 * without data loss.
 *
 * @author dennis, max
 */
public class SharkQRWrapper implements Serializable {

    private final static long serialVersionUID = 1;
    /* Exctracted name */
    private String name;
    /* Extracted Key */
    private PublicKey key;
    /* Extracted SubjectIdentifier */
    private String[] SI;

    /*
    * The Constructor needs a <code>PeerSemanticTag</code> and a <code>PublicKey</code>.
    * It thenn will extarcte the data and save it.
    *
    * @param PeerSemanticTag  tag     Tag to be used in the Wrapper
    * @param PublicKey        key     Key that belongs to the tag
    */
    public SharkQRWrapper(PeerSemanticTag tag, PublicKey k) {
        setName(tag.getName());
        setSI(tag.getSI());
        setKey(k);
    }

    @Override
    public String toString() {
        return "SharQR {name="+name+", key ="+key+", SI="+SI.toString()+"}";
    }

    /*
    * Getter-method for the name
    *
    * @return name    Name as String
    */
    public String getName() {
        return name;
    }

    /*
    * Setter-method for the name
    *
    * @param String name      Name to be used
    */
    public void setName(String name) {
        this.name = name;
    }

    /*
    * Getter-method for the key
    *
    * @return key     Key saved in the Wraper
    */
    public PublicKey getKey() {
        return key;
    }

    /*
    * Setter-method for the key
    *
    * @param PublicKey key    PublicKey to be used
    */
    public void setKey(PublicKey key) {
        this.key = key;
    }

    /*
    * Getter-method for the SubjectIdentifiers
    *
    * @return SI  List of SubjectIdentifiers as Strings
    */
    public String[] getSI() {
        return SI;
    }

    /*
    * Setter-method for the SubjectIdentifiers
    *
    * @param String[] si      List of SubjectIdentifiers as Strings
    */
    public void setSI(String[] si) {
        SI = si;
    }

}
