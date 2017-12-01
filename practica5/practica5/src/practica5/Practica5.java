/*
*  Pablo Valenzuela Álvarez
*  Francisco Javier García Maldonado
*  
*  Recuperación de Información
*  2017/2018
*  Practica 4
*/
package practica5;


/******************************************************************************\
|                                 LIBRERIAS                                    |
\******************************************************************************/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
//Librerias de Lucene
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.UAX29URLEmailAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

/******************************************************************************\
|                              CLASE PRINCIPAL                                 |
\******************************************************************************/
public class Practica5{
      
    
    /**************************************************************************\
    |                       FUNCION PARA CREAR INDICE                          |
    \**************************************************************************/
    /*
    public static void createIndex(String index, String facet, String path) throws IOException{
        
        Map<String,Analyzer> mip = new HashMap<>(); //se crea un MAP con analizadores para usar cada uno con un campo distinto del indice
        mip.put("Link", new UAX29URLEmailAnalyzer()); //para guardarlos enlaces enteros
        mip.put("EID", new KeywordAnalyzer());  //para que no haga nada
        mip.put("Cited by", new StandardAnalyzer()); //para que no borre el numero

       //cambiar a StandardAnalyzer() si queremos almacenar letras sueltas
       //por defecto se usa mi analizador
        PerFieldAnalyzerWrapper pefe = new PerFieldAnalyzerWrapper(new Analizador(), mip);
        
        //creacion del indice
        FSDirectory indexDir = FSDirectory.open(Paths.get(index));
        IndexWriterConfig config = new IndexWriterConfig(pefe);       
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(indexDir, config);

        //Creando el índice de Facetas
        FSDirectory taxoDir = FSDirectory.open(Paths.get(facet));
        fconfig = new FacetsConfig();
        taxoWriter = new DirectoryTaxonomyWriter(taxoDir);
        
        fconfig.setMultiValued("Autor", true);
        
        //lectura de doucmentos e insercion en el indice
        long startTime = System.currentTimeMillis();
        obtenerDocs(path);
        writer.commit();            
        long endTime = System.currentTimeMillis();            
        System.out.println(writer.numDocs()+" ficheros indexados en: "+(endTime-startTime)+" ms");
        
        
        //Cerrando lecturas
        writer.close();
        taxoWriter.close();
    }
    */ 
    
    /**************************************************************************\
    |                             FUNCION MAIN                                 |
    | @param args                                                              |
    | @throws java.lang.Exception                                              |
    \**************************************************************************/
    public static void main(String[] args) throws Exception {   
        
        String INDEX_DIR = "../resultados/index";
        String FACET_DIR = "../resultados/facet";
        String path = "../prueba";
        
        //String path = "../consultas SCOPUS";
        
       createIndex(INDEX_DIR, FACET_DIR, path);
    }
}
