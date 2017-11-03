/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

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
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

/**
 *
 * @author Valenz
 */
/*************************************************************************
Analizador que separa por espacios en blanco, quita signos que no sean letras,
* pone los tokens en minusculas y quita las palabras vacias
***************************************************************************/
public class Analizador extends Analyzer{
    
    Set<String> pVacias;
    
    //TODO cargar fichero y rellenar el set con palabras vacias
    

    public Analizador() throws FileNotFoundException, IOException {
        pVacias = new HashSet<>();                
        FileReader f = new FileReader("stopword/english.txt");        
        BufferedReader b = new BufferedReader(f);
        String cadena;
        while((cadena = b.readLine())!=null) {
            //System.out.println(cadena);
            pVacias.add(cadena.toString());
        }
        b.close();
    }
    
        
    @Override
    protected TokenStreamComponents createComponents(String string) {
        
        Tokenizer tokenizer = new StandardTokenizer();
        TokenStream filter = new WordDelimiterFilter(tokenizer, 0, CharArraySet.EMPTY_SET);
        filter = new LowerCaseFilter(filter);
        filter = new FiltraLetras(filter);
        filter = new StopFilter(filter, CharArraySet.copy(pVacias));        
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        return new TokenStreamComponents(tokenizer,filter);
    }
    
}
