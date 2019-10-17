/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscaminascliente;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Fer
 */
public class tablero implements MouseListener  {
    int tamanox;
    int tamanoy;
    jugador Jugador = new jugador();
    JFrame tablero;
    JPanel panelJuego;
    casilla[][] juego;
    HashMap<JButton, casilla> map = new HashMap<JButton, casilla>();
    Scanner in;
    PrintWriter out;
    String serverAddress;
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 5);

    tablero(String serverAddress){
         this.serverAddress = serverAddress;     
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }
    
    public int getTamanox() {
        return tamanox;
    }

    public void setTamanox(int tamanox) {
        this.tamanox = tamanox;
    }

    public int getTamanoy() {
        return tamanoy;
    }

    public void setTamanoy(int tamanoy) {
        this.tamanoy = tamanoy;
    }

    public JFrame getTablero() {
        return tablero;
    }

    public void setTablero(JFrame tablero) {
        this.tablero = tablero;
    }

    public JPanel getPanelJuego() {
        return panelJuego;
    }

    public void setPanelJuego(JPanel panelJuego) {
        this.panelJuego = panelJuego;
    }

    public casilla[][] getJuego() {
        return juego;
    }

    public void setJuego(casilla[][] juego) {
        this.juego = juego;
    }
   
    public String getName() {
        return JOptionPane.showInputDialog(
                tablero, "Escribe tu nombre:",
                "Nombre de jugador",
                JOptionPane.PLAIN_MESSAGE);

    }

    
     public void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    out.println(getName());
                    crearTablero();
                } else if (line.startsWith("NAMEACCEPTED")) {                  
                    tablero.setTitle("Buscaminas - " + line.substring(13));
                    Jugador.setNombre(line.substring(13));
                    textField.setEditable(true);
                } else if (line.startsWith("SIZE")) {
                    String [] arrayan;
                    arrayan = line.split(",");
                    System.out.println(arrayan[0]);
                    crearPanelJuego(Integer.parseInt(arrayan[1]), Integer.parseInt(arrayan[2]));                
                    llenarPanelJuego();
                    agregarPanelesTablero();       
                }else if(line.startsWith("PERDEDOR")){
                  partidaPerdida();
              }else if(line.startsWith("ABIERTAS")){
                   String [] arrayan;
                    arrayan = line.split(",");
                   juego[Integer.parseInt(arrayan[1])][Integer.parseInt(arrayan[2])].casillaTablero.setEnabled(false);
                   juego[Integer.parseInt(arrayan[1])][Integer.parseInt(arrayan[2])].casillaTablero.setText(String.valueOf(Integer.parseInt(arrayan[3])));
              }else if(line.startsWith("ABIERTA")){
                  String [] arrayan;
                    arrayan = line.split(",");
                   juego[Integer.parseInt(arrayan[1])][Integer.parseInt(arrayan[2])].casillaTablero.setEnabled(false);
                   juego[Integer.parseInt(arrayan[1])][Integer.parseInt(arrayan[2])].casillaTablero.setText(String.valueOf(Integer.parseInt(arrayan[3])));
              }
            }
        } finally {
           
            
        }

    }
    
    public void crearTablero(){
        tablero = new JFrame();
        tablero.setTitle("Tablero buscaminas");
         tablero.setSize(1000,600);
        tablero.setLocationRelativeTo(null);               
        tablero.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void crearPanelJuego(int tamanox,int tamanoy){
        panelJuego = new JPanel();
        setTamanox(tamanox);
        setTamanoy(tamanoy);
        panelJuego.setLayout(new GridLayout(tamanox,tamanoy));
       
    }
    
    public void agregarPanelesTablero(){
        tablero.add(panelJuego);    
        tablero.setVisible(true);
        panelJuego.setVisible(true);
        panelJuego.revalidate();
        panelJuego.repaint();
    }
    
     public void llenarPanelJuego(){
     juego = new casilla[tamanox][tamanoy];
    
      for(int i=0;i < tamanox; i++){        
        for(int j=0;j < tamanoy; j++){        
          casilla casillaObjeto = new casilla(i,j);      
                               
              panelJuego.add(casillaObjeto.getCasillaTablero());
              casillaObjeto.getCasillaTablero().addMouseListener(this);
                    
           panelJuego.add(casillaObjeto.getCasillaTablero());
    
            map.put(casillaObjeto.casillaTablero, casillaObjeto);
             juego[i][j] = casillaObjeto;
             
          }
         
      }   
       
      } 
     
   public void partidaPerdida(){
        for(int i=0;i < tamanox; i++){ 
           for(int j=0;j < tamanoy; j++){ 
             juego[i][j].casillaTablero.setEnabled(false);
             juego[i][j].casillaTablero.setBackground(Color.red);
        }  
        }         
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      try{
          System.out.println(e.getButton());
          if(e.getButton() == 1){
              if((map.get(e.getSource()).casillaTablero.isEnabled())){
                  out.println("descubrir " + "," + map.get(e.getSource()).posicionx + "," +  map.get(e.getSource()).posiciony);
              }else{
                   out.println("descubriruna " + "," + map.get(e.getSource()).posicionx + "," +  map.get(e.getSource()).posiciony);
              }              
          }     
            if(e.getButton() == 3){
                
            if((map.get(e.getSource()).casillaTablero.isEnabled())){
                     Image img = ImageIO.read(new FileInputStream("C:\\Users\\Fer\\Documents\\NetBeansProjects\\buscaminasServidor\\src\\images\\bandera.bmp"));
                 map.get(e.getSource()).casillaTablero.setIcon(new ImageIcon(img));
                 Jugador.setSigueJugando(false);
                  out.println("marcar " + "," + map.get(e.getSource()).posicionx + "," +  map.get(e.getSource()).posiciony);
                }          
                 
        }
            
        }catch(IOException j){
        }   
    }

    @Override
    public void mousePressed(MouseEvent e) {
       
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
       
    }

    @Override
    public void mouseExited(MouseEvent e) {
       
    }
}
