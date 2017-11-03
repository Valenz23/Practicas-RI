/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package practica3;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilterFactory;
<<<<<<< HEAD
import org.apache.lucene.analysis.standard.StandardTokenizer;
=======
>>>>>>> master
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

/**
 *
 * @author Valenz
 */
<<<<<<< HEAD


=======
/*************************************************************************
Analizador que separa por espacios en blanco, quita signos que no sean letras,
* pone los tokens en minusculas y quita las palabras vacias
***************************************************************************/
>>>>>>> master
public class Analizador {
    
    private Analyzer mio;

<<<<<<< HEAD
   public Analizador() throws IOException {
       //StandardTokenizer tokenStream = new StandardTokenizer(matchVersion, reader);
=======
    public Analizador() throws IOException {
>>>>>>> master
        this.mio = CustomAnalyzer.builder()
                .withTokenizer(StandardTokenizerFactory.class)
                .addTokenFilter(WordDelimiterFilterFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class)
<<<<<<< HEAD
                .addTokenFilter(StopFilterFactory.class)
                .addCharFilter("[a-z]", "a")
=======
                .addTokenFilter(StopFilterFactory.class)                      
>>>>>>> master
                .build();
    }
    
    public Analyzer getAnalizador(){
        return mio;
    }
    
}
