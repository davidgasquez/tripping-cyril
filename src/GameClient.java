package ServerGame;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameClient {

    public static void main(String[] args) {

        String host = "localhost";
        int port = 8989;

        Socket socketServicio;

        try {
            // Creamos un socket que se conecte a "host" y "port":
            socketServicio = new Socket(host, port);

            //Crea el input y input stream
            InputStream inputStream = socketServicio.getInputStream();
            OutputStream outputStream = socketServicio.getOutputStream();

            //Crea una clase printwriter imprime por outputSream
            PrintWriter outPrinter = new PrintWriter(outputStream, true);
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            Scanner keyboard = new Scanner(System.in);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String salida;
            while (true) {

                //Envio mi número
                System.out.println("Te toca adivinar:");
                System.out.print("\tIntroduce un número -> ");
                int myint = keyboard.nextInt();
                outPrinter.println("" + myint);

                //Recibo si es menor, mayor o correcto
                salida = input.readLine();
                System.out.println("\tRespuesta del Servidor: " + salida);

                //Terminar el programa si es correcto
                if (salida.equals("¡Correcto!")) {
                    break;
                }

                //Leo el número que el servidor me manda
                salida = input.readLine();
                System.out.println("Le toca adivinar al Servidor");
                System.out.println("\tEl servidor cree que has pensado el número " + salida);

                //Contesto
                System.out.print("\tContesta, el número que has pensado, ¿es mayor, menor o correcto? -> ");
                String resp;
                do {
                    resp = reader.readLine();
                } while (!resp.equals("mayor") && !resp.equals("menor") && !resp.equals("correcto"));
                outPrinter.println(resp);

                //Si el servidor ha adivinado mi número, termino el programa.
                if (resp.equals("correcto")) {
                    break;
                }
            }

            socketServicio.close();

            // Excepciones:
        } catch (UnknownHostException e) {
            System.err.println("Error: Nombre de host no encontrado.");
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al abrir el socket.");
        }
    }
}