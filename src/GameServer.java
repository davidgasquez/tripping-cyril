package TrippingCyril;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    public static void main(String[] args) {

        // Puerto de escucha
        int port = 8989;

        try {
            // Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
            ServerSocket serverSocket = new ServerSocket(port);

            do {
                // Aceptamos una nueva conexión con accept()
                Socket socketServicio = serverSocket.accept();

                GameProcessor procesador = new GameProcessor(socketServicio);
                procesador.start();
            } while (true);

        } catch (IOException e) {
            System.err.println("Error al escuchar en el puerto " + port);
        }

    }
}
