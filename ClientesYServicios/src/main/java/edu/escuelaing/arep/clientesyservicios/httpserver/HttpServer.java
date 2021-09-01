package edu.escuelaing.arep.clientesyservicios.httpserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private int port;
    private Map<String, Handler> handlers = new HashMap<>();

    public HttpServer(){
        super();
    }

    public void registerHandler(Handler h, String prefix){

        handlers.put(prefix, h);
    }

    public void startServer(int httpPort) throws  IOException{
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while(running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean pathRead = false;
            String path = "";
            while ((inputLine = in.readLine()) != null) {
                if(!pathRead){
                    path = inputLine.split(" ")[1];
                    System.out.println("Path read: " + path);
                    pathRead = true;
                }
                System.out.println("Recibi: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            String res = "";
            for(String key: handlers.keySet()){
                if(path.startsWith(key)){
                    System.out.println("Hello "+path.substring(key.length()));
                    String newPath = path.substring(key.length());
                    res = handlers.get(key).handle(newPath, null , null);
                }
            }

            if(res==null){
                outputLine = getDefaultOkOuput();
            } else {
                outputLine = res;
            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
            serverSocket.close();
    }

    private String getDefaultOkOuput(){
        return  "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n" + "<!DOCTYPE html>\n" + "<html>\n"
                + "<head>\n" + "<meta charset=\"UTF-8\">\n" + "<title>Title of the document</title>\n" + "</head>\n"
                + "<body>\n" + "<h1>Mi propio mensaje</h1>\n" + "</body>\n" + "</html>\n";
    }

    private void setPort(int port) {
        this.port = port;
    }

    private int getPort() {
        return port;
    }
}
