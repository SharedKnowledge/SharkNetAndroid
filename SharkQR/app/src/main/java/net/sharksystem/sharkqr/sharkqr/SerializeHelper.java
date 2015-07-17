package net.sharksystem.sharkqr.sharkqr;
import java.util.*;
import java.io.*;
import android.util.Base64;

/*
* SerializeHelper allows to convert Strings into InputStreams and viceversa.
* It offers needed Utility for save streaming of data.
*/
public class SerializeHelper {

    /**
     * Reads the object from Base64 string.
     *
     * @param String s                 String to be converted
     * @return o                       deserialized Object
     *
     * @throws IOException             In case the data couldn’t be read
     * @throws ClassNotFoundException  In case a wrong class was passed
     */
    public static Object fromString( String s ) throws IOException, ClassNotFoundException {
        byte [] data = Base64.decode( s, Base64.DEFAULT );
        ObjectInputStream ois = new ObjectInputStream( new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    /**
     * Converts an object implementing Serializable to a Base64 string.
     *
     * @param Serializable o           String to be converted
     * @return String based on o       Serialized object as string
     *
     * @throws IOException             In case the data couldnt be read
     */
    public static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

}
