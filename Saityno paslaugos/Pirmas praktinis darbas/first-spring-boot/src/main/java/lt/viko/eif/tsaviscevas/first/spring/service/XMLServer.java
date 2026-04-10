package lt.viko.eif.tsaviscevas.first.spring.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server responsible for sending an XML file containing restaurant data
 * to a connected client.
 */
public class XMLServer {

    /**
     * Default constructor.
     */
    public XMLServer() {
    }

    /**
     * Starts the server and sends the restaurant XML file to the client.
     * The XML file is loaded from the application resources.
     */
    public void sendXML() {

        try {

            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            InputStream fileInputStream =
                    getClass().getClassLoader().getResourceAsStream("restaurant.xml");

            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("XML file sent.");

            fileInputStream.close();
            socket.close();
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}