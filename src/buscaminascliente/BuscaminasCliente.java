/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscaminascliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Fer
 */
public class BuscaminasCliente {

     
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server ip as the solve ");
            return;
        }
        tablero client = new tablero(args[0]);
        
        client.run();

    }
    
}
