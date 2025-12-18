package socketclient_java_udp;

//clases necesarias para usar los sockets
import java.net.*;
import java.io.*;

public class SocketClient_JAVA_UDP {

    public static void main(String[] args) {

        // Ya NO vamos a leer del teclado:
        // BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        // Declara las variables de los sockets UDP y la dirección IP
        DatagramSocket socket;
        InetAddress address;

        try {
            // Crea un socket UDP sin especificar un puerto, el sistema asigna automaticamente un puerto libre
            socket = new DatagramSocket();
            // Obtiene la dirección IP, localhost = 127.0.0.1
            address = InetAddress.getByName("localhost");

            // ---- LEER EL ARCHIVO LÍNEA POR LÍNEA ----
            // Crea un lector para leer el archivo mensaje por mensaje (línea por línea).
            BufferedReader lectorArchivo = new BufferedReader(
                    new FileReader("MensajePrueba.txt")
            );
            
            // Declara variables para almacenar cada línea y convertirla a bytes, que es lo que UDP envía.
            String mensaje;
            byte[] mensaje_bytes;
            
            //Declara los paquetes de envío y recepción.
            DatagramPacket paquete;
            DatagramPacket servPaquete;
            
            // Buffer para recibir la respuesta del servidor y convertirla a texto.
            byte[] RecogerServidor_bytes = new byte[256];
            String cadenaMensaje;

            // ---- CICLO: ENVIAR CADA LÍNEA DEL TXT ----
            // Lee una línea del archivo.
            // La condición del ciclo es: mientras no se llegue al final del archivo (null).
            while ((mensaje = lectorArchivo.readLine()) != null) {
                
                // Convierte la línea (String) en un arreglo de bytes para enviarla por UDP.
                mensaje_bytes = mensaje.getBytes();
                // Crea un paquete UDP con
                paquete = new DatagramPacket(
                        mensaje_bytes, // datos a enviar
                        mensaje_bytes.length, // longitud del mensaje
                        address, // IP del servidor
                        6000 // puerto del servidor
                );
                
                // Envía el paquete UDP al servidor.
                socket.send(paquete);

                // Esperar respuesta del servidor
                // Reinicia el buffer de recepción.
                RecogerServidor_bytes = new byte[256];
                // Prepara un paquete vacío donde se recibirá la respuesta.
                servPaquete = new DatagramPacket(RecogerServidor_bytes, 256);
                // Espera a que el servidor envíe un mensaje.
                /***** Este método se queda bloqueado hasta que llega un paquete. *****/
                socket.receive(servPaquete);
                
                // Convierte los bytes recibidos en un texto.
                // .trim() elimina espacios extra y bytes vacíos.
                cadenaMensaje = new String(RecogerServidor_bytes).trim();
                System.out.println("Servidor responde: " + cadenaMensaje);

                // si la línea empieza con "fin" se termina
                if (mensaje.startsWith("fin")) {
                    break;
                }
            }

            lectorArchivo.close();
            socket.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}

