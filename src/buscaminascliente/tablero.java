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
import java.io.FileNotFoundException;
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
    
    int longitudTablerox;
    int longitudTableroy;
    jugador Jugador = new jugador();
    JFrame tablero;
    JPanel panelJuego;
    JPanel panelComenzar = null;
    JButton botonComenzar = new JButton();
    casilla[][] tableroCliente;
    HashMap<JButton, casilla> map = new HashMap<JButton, casilla>();
    Scanner lectorEntrada;
    PrintWriter escritorSalida;
    String serverAddress;
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 5);

    tablero(String serverAddress){
         this.serverAddress = serverAddress;     
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                escritorSalida.println(textField.getText());
                textField.setText("");
            }
        });
    }
    
    public int getTamanox() {
        return longitudTablerox;
    }

    public void setTamanox(int longitudTablerox) {
        this.longitudTablerox = longitudTablerox;
    }

    public int getTamanoy() {
        return longitudTableroy;
    }

    public void setTamanoy(int longitudTableroy) {
        this.longitudTableroy = longitudTableroy;
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
        return tableroCliente;
    }

    public void setJuego(casilla[][] juego) {
        this.tableroCliente = juego;
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
            lectorEntrada = new Scanner(socket.getInputStream());
            escritorSalida = new PrintWriter(socket.getOutputStream(), true);
            
            while (lectorEntrada.hasNextLine()) {
                String line = lectorEntrada.nextLine();
                String [] contenidoEnviadoDeServidor;
                    contenidoEnviadoDeServidor = line.split(",");
                if (line.startsWith("SUBMITNAME")) {
                    
                    crearTablero();
                } else if (line.startsWith("NAMEACCEPTED")) {                  
                    tablero.setTitle("Buscaminas - " + line.substring(13));
                    Jugador.setNombre(line.substring(13));
                    textField.setEditable(true);
                } else if (line.startsWith("SIZE")) {

                    Jugador.setColor((contenidoEnviadoDeServidor[3]));
                    System.out.println("Tamano x:" + Integer.parseInt(contenidoEnviadoDeServidor[1]));
                    crearPanelJuego(Integer.parseInt(contenidoEnviadoDeServidor[1]), Integer.parseInt(contenidoEnviadoDeServidor[2]));                
                    llenarPanelJuego();   
                    crearPanelComienzo();
                                     
                }else if(line.startsWith("PERDEDOR")){
                  partidaPerdida();
              }else if(line.startsWith("ABIERTAS")){
                 
                    descubrirCasillasTablero(Integer.parseInt(contenidoEnviadoDeServidor[1]),Integer.parseInt(contenidoEnviadoDeServidor[2]),String.valueOf(contenidoEnviadoDeServidor[3]));
              }else if(line.startsWith("ABIERTA")){      
                   
                    descubrirCasillasTablero(Integer.parseInt(contenidoEnviadoDeServidor[1]),Integer.parseInt(contenidoEnviadoDeServidor[2]),String.valueOf(contenidoEnviadoDeServidor[3]));
              }else if(line.startsWith("MARCADA")){
                  agregarIconoBandera(Integer.parseInt(contenidoEnviadoDeServidor[1]),Integer.parseInt(contenidoEnviadoDeServidor[2]),contenidoEnviadoDeServidor[3]);
               
              }else if(line.startsWith("DESMARCADA")){
               quitarIconoBandera(Integer.parseInt(contenidoEnviadoDeServidor[1]),Integer.parseInt(contenidoEnviadoDeServidor[2]));
              }else if(line.startsWith("GANADOR")){
                 if(contenidoEnviadoDeServidor[1].equals(Jugador.color)){
                     JOptionPane.showMessageDialog(tablero, "HAS GANADO");
                 }else{
                      JOptionPane.showMessageDialog(tablero, "HAS PERDIDO, HASTA LA PROXIMA");
                 }
              }else if(line.startsWith("EXPLOTADAS")){
                  minasExplotadasEnCasillas(Integer.parseInt(contenidoEnviadoDeServidor[1]),Integer.parseInt(contenidoEnviadoDeServidor[2]));
              }else if(line.startsWith("COMENZAR")){
                       
                   botonComenzar.setText("COMENZAR PARTIDA");    
              }else if(line.startsWith("comenzada")){               
                  removerPanelComienzo();
                  determinarCasillasInicio(Jugador.getColor());
                  agregarPanelesTablero();
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
    
    public void crearPanelComienzo(){
        panelComenzar = new JPanel();
        panelComenzar.setLayout(new GridLayout(1,1));
        botonComenzar.addMouseListener(this);
        tablero.add(panelComenzar);  
        panelComenzar.add(botonComenzar);
        tablero.setVisible(true);
        panelComenzar.setVisible(true);  
    }
    
    public void removerPanelComienzo(){
        tablero.remove(panelComenzar); 
    }
    
    public void agregarPanelesTablero(){
        tablero.add(panelJuego);    
        tablero.setVisible(true);
        panelJuego.setVisible(true);
        panelJuego.revalidate();
        panelJuego.repaint();
    }
    
    public void agregarIconoBandera(int pocisionx, int pocisiony, String color) throws FileNotFoundException, IOException{
         Image img = ImageIO.read(new FileInputStream("C:\\Users\\Fer\\Documents\\NetBeansProjects\\buscaminasServidor\\src\\images\\bandera" + color +".png"));
         tableroCliente[pocisionx][pocisiony].casillaTablero.setIcon(new ImageIcon(img));
    }
    
    public void quitarIconoBandera(int pocisionx, int pocisiony){
         tableroCliente[pocisionx][pocisiony].casillaTablero.setIcon(null);
    }
    
    public void descubrirCasillasTablero(int posicionx, int pocisiony , String numeroMinasAdyacentes){
         tableroCliente[posicionx][pocisiony].casillaTablero.setEnabled(false);
         tableroCliente[posicionx][pocisiony].casillaTablero.setText(numeroMinasAdyacentes);
    }
    
     public void llenarPanelJuego(){
     tableroCliente = new casilla[longitudTablerox][longitudTableroy];
    
      for(int i=0;i < longitudTablerox; i++){        
        for(int j=0;j < longitudTableroy; j++){        
          casilla casillaObjeto = new casilla(i,j);      
                               
              panelJuego.add(casillaObjeto.getCasillaTablero());
              casillaObjeto.getCasillaTablero().addMouseListener(this);
              casillaObjeto.getCasillaTablero().setEnabled(false);
           panelJuego.add(casillaObjeto.getCasillaTablero());
    
            map.put(casillaObjeto.casillaTablero, casillaObjeto);
             tableroCliente[i][j] = casillaObjeto;
             
          }
         
      }   
       
      }
     
     public void determinarCasillasInicio(String colorJugador){
           if(colorJugador.equals("GREEN")){
               
        for(int j=0;j < longitudTableroy; j++){
          tableroCliente[j][0].casillaTablero.setEnabled(true);
          tableroCliente[j][0].casillaTablero.setBackground(Color.GREEN);   
        }   
        }
           
           if(colorJugador.equals("YELLOW")){
               
        for(int j=0;j < longitudTableroy; j++){
          tableroCliente[longitudTablerox-1][j].casillaTablero.setEnabled(true);
          tableroCliente[longitudTablerox-1][j].casillaTablero.setBackground(Color.YELLOW);   
        }   
        }
           
           if(colorJugador.equals("BLUE")){
               
        for(int j=0;j < longitudTableroy; j++){
          tableroCliente[0][j].casillaTablero.setEnabled(true);
          tableroCliente[0][j].casillaTablero.setBackground(Color.BLUE);   
        }   
        }
           
           if(colorJugador.equals("ORANGE")){
               
        for(int j=0;j < longitudTableroy; j++){
          tableroCliente[j][longitudTablerox-1].casillaTablero.setEnabled(true);
          tableroCliente[j][longitudTablerox-1].casillaTablero.setBackground(Color.ORANGE);   
        }   
        }
      }
     
    public void minasExplotadasEnCasillas(int posicionx, int pocisiony){
         tableroCliente[posicionx][pocisiony].casillaTablero.setEnabled(false);
         tableroCliente[posicionx][pocisiony].casillaTablero.setBackground(Color.red);
    }
     
   public void partidaPerdida(){
        for(int i=0;i < longitudTablerox; i++){ 
           for(int j=0;j < longitudTableroy; j++){ 
             tableroCliente[i][j].casillaTablero.setEnabled(false);
             tableroCliente[i][j].casillaTablero.setBackground(Color.red);
        }  
        }
        JOptionPane.showMessageDialog(null, "Ha explotado una mina");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
   
        
          if(e.getButton() == 1){
              if((map.get(e.getSource()) != null)){
              if((map.get(e.getSource()).casillaTablero.isEnabled())){
                 
                  System.out.println("llega a descubrir");
                  escritorSalida.println("descubrir " + "," + map.get(e.getSource()).posicionx + "," +  map.get(e.getSource()).posiciony);
              }
              }
              if(e.getSource().equals(botonComenzar)){
                  if(botonComenzar.getText().equals("COMENZAR PARTIDA")){
                       escritorSalida.println("comenzar ");

              }
              }
          }     
            if(e.getButton() == 3){
                
            if((map.get(e.getSource()).casillaTablero.isEnabled())){        
                  escritorSalida.println("marcar " + "," + map.get(e.getSource()).posicionx + "," +  map.get(e.getSource()).posiciony + "," + Jugador.color);
                }          
                 
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
