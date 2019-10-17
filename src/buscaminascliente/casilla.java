/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscaminascliente;

import javax.swing.JButton;

/**
 *
 * @author Fer
 */
public class casilla {
    
    JButton casillaTablero;
    int posicionx;
    int posiciony;  
    int numero=0;  
    
    
    casilla(int pocisionx,int pocisiony){
        this.casillaTablero = new JButton();    
        
        this.posicionx = pocisionx;
        this.posiciony = pocisiony;       
    }

    public JButton getCasillaTablero() {
        return casillaTablero;
    }

    public void setCasillaTablero(JButton casillaTablero) {
        this.casillaTablero = casillaTablero;
    }

    public int getPosicionx() {
        return posicionx;
    }

    public void setPosicionx(int posicionx) {
        this.posicionx = posicionx;
    }

    public int getPosiciony() {
        return posiciony;
    }

    public void setPosiciony(int posiciony) {
        this.posiciony = posiciony;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    
}
