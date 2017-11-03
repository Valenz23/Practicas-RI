/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

import java.io.IOException;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

/**
 *
 * @author Valenz
 */


public class FiltraLetras extends TokenFilter {

    
    /* El constructor para nuestro token filter personal solo llama al constructor
     * de TokenFilter. Este constructor guarda el token stream en una variable
     * llamada this.input.
     */
    public FiltraLetras(TokenStream tokenStream) {
        super(tokenStream);
    }

    /* Como en la clase FlusSingTokenizer, nosotros vamos a guardar el texto del
     * token en un objeto CharTermAttribute. Además, vamos a usar un objeto 
     * PositionINcrementAttribute para almacenar el incremento de la posición del
     * token. Lucene usa este "latter attribute" para determinar la posición de
     * un token. Dado un flujo de tokens como "This", "is", "", "some", y "text",
     * vamos a asegurarnos de que "This" está guardado en la posición 1, "is" en
     * en la posición 2, "some" en la posición 3, y "text" en la posición 4.
     * Nota: nosotros hemos ignorado completamente el string vacio que estaba en
     * la posición 3 del flujo original.
     */
    protected CharTermAttribute charTermAttribute =
        addAttribute(CharTermAttribute.class);
    protected PositionIncrementAttribute positionIncrementAttribute =
        addAttribute(PositionIncrementAttribute.class);

    /* Like we did in the PlusSignTokenizer class, we need to override the
     * incrementToken() function to save the attributes of the current token.
     * We are going to pass over any tokens that are empty strings and save
     * all others without modifying them. This function should return true if
     * a new token was generated and false if the last token was passed.
     */
    @Override
    public boolean incrementToken() throws IOException {

        // Loop over tokens in the token stream to find the next one
        // that is not empty
        String nextToken = null;
        while (nextToken == null) {

            // Reached the end of the token stream being processed
            if (! this.input.incrementToken()) {
                return false;
            }

            // Get text of the current token and remove any
            // leading/trailing whitespace.
            String currentTokenInStream = 
            this.input.getAttribute(CharTermAttribute.class).toString().trim();
        
            // Save the token if it is not an empty string
            if (currentTokenInStream.length() > 1) { //cambio el 0 por 1
                nextToken = currentTokenInStream;
            }
        }

        // Save the current token
        this.charTermAttribute.setEmpty().append(nextToken);
        this.positionIncrementAttribute.setPositionIncrement(1);
        return true;
    }
}