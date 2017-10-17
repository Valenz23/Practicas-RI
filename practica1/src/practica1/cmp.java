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
public class cmp implements Comparator<Palabras>{
    
    
    //Sobreescribimos la clase comparar
    @Override
    public int compare(Palabras p1, Palabras p2) {
        
        if(p1.getNumPalabra() > p2.getNumPalabra()){
            return 1;
        }else if(p1.getNumPalabra() < p2.getNumPalabra()){
            return -1;
        }else{
            return 0;
        }
    }
}
