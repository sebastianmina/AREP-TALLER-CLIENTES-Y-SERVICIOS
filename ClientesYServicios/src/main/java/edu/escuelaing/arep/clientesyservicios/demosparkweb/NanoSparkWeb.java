/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arep.clientesyservicios.demosparkweb;

import edu.escuelaing.arep.clientesyservicios.httpserver.Handler;
import edu.escuelaing.arep.clientesyservicios.httpserver.HttpServer;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NanoSparkWeb implements Handler {

    private int httpPort;
    private static NanoSparkWeb _theInstance = new NanoSparkWeb();
    private HttpServer hserver = new HttpServer();
    Map<String, BiFunction<HttpRequest, HttpResponse, String>> functions = new HashMap();

    private NanoSparkWeb(){
        hserver.registerHandler(this, "/");
    }

    public static NanoSparkWeb getInstance(){
        return _theInstance;
    }

    private Map<String, BiFunction<HttpRequest, HttpResponse, String>> bodyMap = new HashMap();

    /**
     * Corre el servidor
     */
    void startServer(){
        try {
            hserver.startServer(httpPort);
        } catch (IOException ex) {
            Logger.getLogger(NanoSparkWeb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * asigna el puerto del servidor
     * @param serverPort int
     */
    public void port(int serverPort) {
        this.httpPort = serverPort;
    }

    /**
     * obtiene el path
     * @param path string
     * @param body bifunction
     */
    public void get(String path, BiFunction<HttpRequest, HttpResponse, String> body) {

        bodyMap.put(path, body);
    }

    public BiFunction<HttpRequest, HttpResponse, String> getValue(String path){
        return bodyMap.get(path);
    }

    @Override
    public String handle(String path, HttpRequest req, HttpResponse res) {
        if (functions.containsKey(path)) {
            return httpHeader() + functions.get(path).apply(req , res);
        }
        return httpError();
    }

    private String httpError() {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Title of the document</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Error</h1>\n"
                + "</body>\n"
                + "</html>\n";
    }

    private String httpHeader() {
        return "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n";
    }
}
