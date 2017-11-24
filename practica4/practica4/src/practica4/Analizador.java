/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package practica4;

/**
 *
 * @author Valenz
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;


/******************************************************************************\
|                            ANALIZADOR PROPIO                                 |
\******************************************************************************/
/*
* nalizador que separa por espacios en blanco, quita signos que no sean letras,
* pone los tokens en minusculas y quita las palabras vacias
*/
public class Analizador extends Analyzer{
    
    Set<String> pVacias;
    
    public Analizador() throws FileNotFoundException, IOException {
        pVacias = new HashSet<>();                
        FileReader f = new FileReader("stopword/english.txt");        
        BufferedReader b = new BufferedReader(f);
        String cadena;
        while((cadena = b.readLine())!=null) {
            pVacias.add(cadena);
        }
        b.close();
    }
    
    
    @Override
    protected Analyzer.TokenStreamComponents createComponents(String string) {
        
        Tokenizer tokenizer = new StandardTokenizer();
        TokenStream filter = new WordDelimiterFilter(tokenizer, 0, CharArraySet.EMPTY_SET); //separa las palabras por comas, puntos ...
        filter = new LowerCaseFilter(filter); //pone en miusculas
        filter = new StandardFilter(filter); //Quita acentos
        filter = new FiltraLetras(filter); //quita las letras que estan solas
        filter = new StopFilter(filter, CharArraySet.copy(pVacias)); //quita las palabras vacias inglesas
        filter = new PorterStemFilter(filter); //hace stemming en inglés
        
        return new Analyzer.TokenStreamComponents(tokenizer,filter);
    }
}
