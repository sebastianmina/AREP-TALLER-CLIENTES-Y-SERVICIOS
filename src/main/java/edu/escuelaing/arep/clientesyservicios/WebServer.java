package edu.escuelaing.arep.clientesyservicios;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class WebServer {
    
    /**
	 * Clase principal encargada de crear el servidor y el cliente y mostrar los
	 * resultados del llamado del archivo
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		while (true) {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(getPort());
			} catch (IOException e) {
				System.err.println("Could not listen on port: 35000.");
				System.exit(1);
			}
			Socket clientSocket = null;
			try {
				System.out.println("Listo para recibir ...");
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}

			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String inputLine, outputLine;
			String[] arch = null;
			String[] elem = null;

			while ((inputLine = in.readLine()) != null) {
				System.out.println("Recibí: " + inputLine);
				if (inputLine.contains("GET")) {
					arch = inputLine.split("/");
					elem = arch[1].split(" ");
					String[] archivo = elem[0].split("\\.");
					System.out.println("sdfsdf: ....... "+archivo[1]);
					if (archivo[1].equals("html") || archivo[1].equals("js") || archivo[1].equals("jpg") || archivo[1].equals("png")) {
						Archivo(elem[0], clientSocket.getOutputStream(), out);
					}
				}
				if (!in.ready()) {
					break;
				}
			}
			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
		}
	}

	/**
	 * Clase encargada de buscar los archivos en una carpeta especifica y mostrarlos
	 * en un navegador
	 * 
	 * @param a
	 * @param clientOutput
	 * @param out
	 * @throws IOException
	 */
	public static void Archivo(String a, OutputStream clientOutput, PrintWriter out) throws IOException {
		String[] archivo = a.split("\\.");
		String carpeta = "/src/resource/";
		String outputLine = null;
		File arch = new File(System.getProperty("user.dir") + carpeta + a);
		if (arch.exists()) {
			if (archivo[1].equals("html")) {
				String lineas;
				FileReader f = new FileReader(System.getProperty("user.dir") + carpeta + a);
				BufferedReader bf = new BufferedReader(f);
				while ((lineas = bf.readLine()) != null) {
					outputLine += lineas;
				}

				bf.close();
				/*
				 * out.println("HTTP/1.1 404 Not Found \r\n" +
				 * "Content-Type: text/html; charset=\"utf-8\" \r\n" + "\r\n"+ outputLine);
				 */

				out.write(("HTTP/1.1 404 Not Found \r\n" + "Content-Type: text/html; charset=\"utf-8\" \r\n" + "\r\n"
						+ outputLine));
			}if (archivo[1].equals("jpg") || archivo[1].equals("png")) {

				System.out.println("entra a la vuelta");

				BufferedImage imagen = ImageIO.read(arch);

				ByteArrayOutputStream bytes = new ByteArrayOutputStream();

				DataOutputStream w = new DataOutputStream(clientOutput);

				ImageIO.write(imagen, archivo[1], bytes);

				w.writeBytes("HTTP/1.1 404 Not Found \r\n");
				w.writeBytes("Content-Type: image/" + archivo[1] + "; charset=\"utf-8\" \r\n");
				w.writeBytes("\r\n");
				w.write(bytes.toByteArray());

				System.out.println(System.getProperty("user.dir") + carpeta + a);

			}if (archivo[1].equals("js")){

				String lineas;
				FileReader f = new FileReader(System.getProperty("user.dir") + carpeta + a);
				BufferedReader bf = new BufferedReader(f);
				while ((lineas = bf.readLine()) != null) {
					outputLine += lineas;
				}

				bf.close();

				out.write(("HTTP/1.1 404 Not Found \r\n" + "Content-Type: text/html; charset=\"utf-8\" \r\n" + "\r\n"
						+ outputLine));
			}
		}
	}

	/**
	 * Clase encargada de retornar el puerto donde se va dar a ver la pagina
	 * 
	 * @return puerto donde inicia la pagina
	 */
	static int getPort() {
		if (System.getenv("PORT") != null) {
			return Integer.parseInt(System.getenv("PORT"));
		}

		return 8080; // returns default port if heroku-port isn't set(i.e. on localhost) }
	}
}
