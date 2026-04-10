package lt.viko.eif.tsaviscevas.first.spring.service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 * Client responsible for connecting to the server
 * and receiving an XML file containing restaurant data.
 */
public class XMLClient {

    /**
     * Default constructor.
     */
    public XMLClient() {
    }

    /**
     * Connects to the server and receives an XML file.
     * The received file is saved locally as received_restaurant.xml.
     */
    public void receiveXML() {

        try {

            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to server.");

            InputStream inputStream = socket.getInputStream();
            FileOutputStream fileOutputStream =
                    new FileOutputStream("received_restaurant.xml");

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("XML file received.");

            fileOutputStream.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}