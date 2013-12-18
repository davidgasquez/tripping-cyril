package TrippingCyril;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameProcessor extends Thread {

    // Referencia a un socket para enviar/recibir las peticiones/respuestas
    private Socket socketServicio;
    //Número aleatorio
    private Random random = new Random();
    //Mínimo y maximo para ir acotando
    private int min, max;

    // Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
    public GameProcessor(Socket socketServicio) {
        this.socketServicio = socketServicio;
    }

    @Override
    public void run() {
        try {
            sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        procesa();
    }

    // Aquí es donde se realiza el procesamiento realmente:
    private void procesa() {
        try {
            // Obtiene los flujos de escritura/lectura
            InputStream inputStream = socketServicio.getInputStream();
            OutputStream outputStream = socketServicio.getOutputStream();

            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            PrintWriter printer = new PrintWriter(outputStream, true);

            //Número que ha pensado el servidor
            Random random = new Random();
            int number = random.nextInt(100);

            //Número que el servidor cree que es el mío
            int guess = random.nextInt(100);
            min = 0;
            max = 100;

            int gnumber;
            do {
                //El servidor recibe mi número
                gnumber = Integer.parseInt(input.readLine());

                //Línea opcional, solo para depuración.
                System.out.println("El número que he pensado es " + number + ", y el número que me has dado es el " + gnumber + ".");

                //El servidor mira si he acertado con mi gnumber (guessnumber) y me dice si e smayor,menor o correcto
                printer.println(think(number, gnumber));
                if (think(number, gnumber).equals("¡Correcto!")) {
                    break;
                }

                //Manda el número que cree que he pensado
                printer.println(guess);

                //Recibe mi respuesta, según su número
                String answer = input.readLine();
                System.out.println("Tu número es " + answer + ".");

                //Actualiza los valores de min y max según el número que ha pensado y la respuesta dada por el cliente
                guess = choose(guess, answer);
                System.out.println("El número que creo que es el tuyo es: " + guess);
                if (guess == -1) {
                    break;
                }

            } while (true);

            socketServicio.close();


        } catch (IOException e) {
            System.err.println("Error al obtener los flujo de entrada/salida.");
        }

    }

    //Aquí comrpueba mi número con el que el ha pensado
    private String think(int number, int gnumber) {
        if (gnumber > number)
            return " El número es menor al que me has dado. ";
        else if (gnumber < number)
            return " El número es mayor al que me has dado. ";
        else
            return "¡Correcto!";
    }

    //Aquí acota su rango para el siguiente intento
    private int choose(int number, String answer) {
        if (answer.equals("mayor")) {
            min = number;
        } else if (answer.equals("menor")) {
            max = number;
        } else if (answer.equals("correcto")) {
            return -1;
        }
        System.out.println("El intervalo donde voy a decir mi número es: [" + min + ", " + max + "].");
        if (max < min) {
            max = 100;
            min = 0;
        }
        return random.nextInt(max - min) + min;
    }
}
