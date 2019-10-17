/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscaminascliente;

/**
 *
 * @author Fer
 */
public class jugador {
    String nombre;
    boolean sigueJugando= true;
    boolean ganador= false;
    tablero tableroCliente;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isSigueJugando() {
        return sigueJugando;
    }

    public void setSigueJugando(boolean sigueJugando) {
        this.sigueJugando = sigueJugando;
    }

    public boolean isGanador() {
        return ganador;
    }

    public void setGanador(boolean ganador) {
        this.ganador = ganador;
    }

    public tablero getTableroCliente() {
        return tableroCliente;
    }

    public void setTableroCliente(tablero tableroCliente) {
        this.tableroCliente = tableroCliente;
    }
    
    
}
