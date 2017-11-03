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
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

/**
 *
 * @author Valenz
 */



/******************************************************************************\
|                            ANALIZADOR PROPIO                                 |
\******************************************************************************/
/* Analizador que separa por espacios en blanco, quita signos que no sean letras,
*  pone los tokens en minusculas y quita las palabras vacias
*/
public class Analizador {
    
    private Analyzer mio;
   public Analizador() throws IOException {
       //StandardTokenizer tokenStream = new StandardTokenizer(matchVersion, reader);
        this.mio = CustomAnalyzer.builder()
                .withTokenizer(StandardTokenizerFactory.class)
                .addTokenFilter(WordDelimiterFilterFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class)
                .addTokenFilter(StopFilterFactory.class)
                .addCharFilter("[a-z]", "a")
                .addTokenFilter(StopFilterFactory.class)                      
                .build();
    }
    
    public Analyzer getAnalizador(){
        return mio;
    }
    
}
