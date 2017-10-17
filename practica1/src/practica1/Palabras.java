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
public class Palabras implements Comparator{
    String nomPalabra;
    int numPalabras;

    //Constructor
    public Palabras(){
    }
    
    //Sobreescribimos la clase comparar
    @Override
    public int compare(Object o1, Object o2) {
        Palabras pal1 = new Palabras();
        Palabras pal2 = new Palabras();
        //To change body of generated methods, choose Tools | Templates.
        //throw new UnsupportedOperationException("Not supported yet.");
        if(o1 == null && o2 == null){
            return 0;
        }else if(pal1 != null && pal2 != null){
            return -1;
        }else if(pal1 == null && pal2 == null){
            return 1;
        }
        
        return 0;
    }
}
