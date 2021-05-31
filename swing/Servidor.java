/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import swing.Cliente;
/**
 *
 * @author Azucena
 */
public class Servidor {

    JFrame ventana;
    JButton btnEnviar;
    JTextField mensaje;
    JTextArea area;
    JPanel pnlContenedor;
    JPanel pnlBoton;
    JScrollPane scroll;
    ServerSocket servidor;
    Socket socket;
    BufferedReader lector;
    PrintWriter escritor;
    JLabel lblNombre;
    JTextField nombre;
    
    public Servidor() {
        interfaz();
    }

    public void interfaz() {
        ventana = new JFrame("Servidor");
        ventana.setLocationRelativeTo(null);
        lblNombre = new JLabel("     Nickname: ---> ");
        nombre = new JTextField(5);
        btnEnviar = new JButton("Enviar");
        mensaje = new JTextField(4);
        area = new JTextArea(10, 12);
        scroll = new JScrollPane(area);
        pnlContenedor = new JPanel();
        pnlContenedor.setLayout(new GridLayout(1, 2));
        pnlContenedor.add(scroll);
        pnlBoton = new JPanel();
        pnlBoton.setLayout(new GridLayout(2, 2));
        pnlBoton.add(lblNombre);
        pnlBoton.add(nombre);
        pnlBoton.add(mensaje);
        pnlBoton.add(btnEnviar);
        ventana.setLayout(new BorderLayout());
        ventana.add(pnlContenedor, BorderLayout.NORTH);
        ventana.add(pnlBoton, BorderLayout.SOUTH);
        ventana.setSize(300, 250);
        ventana.setVisible(true);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Thread principal = new Thread(new Runnable() {
            public void run() {
                try {
                    servidor = new ServerSocket(9000);
                    while (true) {
                        socket = servidor.accept();
                        leer();
                        escribir();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        principal.start();
    }

    public void leer() {
        Thread leerHilo = new Thread(new Runnable() {
            public void run() {
                try {
                    lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        String msjRecibido = lector.readLine();
                        area.append("@Cliente:" + msjRecibido + "\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        leerHilo.start();
    }

    public void escribir() {
        Thread escribirHilo = new Thread(new Runnable() {
            public void run() {
                try {
                    
                    escritor = new PrintWriter(socket.getOutputStream(), true);
                    btnEnviar.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String enviarMsj = mensaje.getText();
                            escritor.println(enviarMsj);
                            mensaje.setText("");
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        escribirHilo.start();
    }

    public static void main(String[] args) {
        new Servidor();
    }

}
