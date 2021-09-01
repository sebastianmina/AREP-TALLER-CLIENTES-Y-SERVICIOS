/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.escuelaing.arep.clientesyservicios.demosparkweb;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.function.BiFunction;

public class NanoSpark {

    /**
     * obtiene el path y el body
     * @param path string
     * @param body bifunction
     */
    public static void get(String path, BiFunction<HttpRequest, HttpResponse, String> body){
        NanoSparkWeb nanosvr = NanoSparkWeb.getInstance();
        nanosvr.get(path, body);
    }

    public static void port(int port){
        NanoSparkWeb server = NanoSparkWeb.getInstance();
        server.port(port);
    }

    public static void startServer(){
        NanoSparkWeb server = NanoSparkWeb.getInstance();
        server.startServer();
    }
}
