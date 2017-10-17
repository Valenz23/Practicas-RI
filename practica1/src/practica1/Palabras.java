/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.util.Comparator;

/**
 *
 * @author Franelas
 */
public class Palabras{
    private String nomPalabra;
    private int numPalabra;

    //Constructores
    public Palabras(String nom, int num){
        this.nomPalabra=nom;
        this.numPalabra=num;
    }
    
    public Palabras(){
    }
    
    
    
    //Metodos GET
    public String getNomPalabra(){
        return this.nomPalabra;
    }
    
    public int getNumPalabra(){
        return this.numPalabra;
    }
    
    
    
    //Metodos SET
    public void setNomPalabra(String n){
        this.nomPalabra=n;
    }
    
    public void setNumPalabra(int n){
        this.numPalabra=n;
    }
}
