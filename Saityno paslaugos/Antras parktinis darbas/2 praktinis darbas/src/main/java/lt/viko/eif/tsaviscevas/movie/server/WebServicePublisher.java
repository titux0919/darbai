package lt.viko.eif.tsaviscevas.movie.server;

import javax.xml.ws.Endpoint;

public class WebServicePublisher {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/movies", new MovieServiceImpl());
        System.out.println("SOAP service running at http://localhost:8080/movies?wsdl");
    }
}