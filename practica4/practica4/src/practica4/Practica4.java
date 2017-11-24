/*
*  Pablo Valenzuela Álvarez
*  Francisco Javier García Maldonado
*  
*  Recuperación de Información
*  2017/2018
*  Practica 4
*/
package practica4;


/******************************************************************************\
|                                 LIBRERIAS                                    |
\****************************************************************_*************/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

/****************************************************************************\
|                              CLASE PRINCIPAL                                 |
\****************************************************************************/
public class Practica4{
    
    /**************************************************************************\
    |         FUNCION PARA OBTENER LOS DOCUMENTOS A INDEXAR                    |
    | @param path -> lugar donde estan los fichero a indexar                   |
    \**************************************************************************/   
    public static void obtenerDocs(String path, IndexWriter writer){   
        
        File file = new File(path);
        File[] files = file.listFiles();        
        for (File f : files) {
            if (f.isDirectory()){
                obtenerDocs(f.getPath(),writer);
            }else{
                System.out.println("Obteniendo documentos de "+f.getName());
                
                String fichero = f.getPath();
                String linea;
                String[] cabecera, datos;
                
                try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
                    linea = br.readLine();
                    cabecera = linea.split(",");                    
                    while ((linea = br.readLine()) != null) {         
                                                
                        datos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                        
                        Document neu = new Document();                       
                        
                        neu.add(new TextField(cabecera[0], datos[0], Field.Store.YES)); //autores
                        neu.add(new TextField(cabecera[1], datos[1], Field.Store.YES)); //titulo
                        
                        neu.add(new IntPoint(cabecera[2], Integer.parseInt(datos[2]))); //año 
                        neu.add(new StoredField(cabecera[2],datos[2])); //año;*/
                        neu.add(new TextField(cabecera[2], datos[2], Field.Store.YES)); //necesario si queremos hacer busquedas con el
                        
                        neu.add(new TextField(cabecera[3], datos[3], Field.Store.YES)); //source_title
                        
                        int date=0; //si no hago esto, si el campo esta vacio falla
                        if(!datos[4].isEmpty())
                            date = Integer.parseInt(datos[4]);                        
                        neu.add(new IntPoint(cabecera[4], date)); //citas
                        neu.add(new StoredField(cabecera[4], datos[4])); //citas*/
                        neu.add(new TextField(cabecera[4], String.valueOf(date), Field.Store.YES)); //necesario si queremos hacer busquedas con el
                        
                        neu.add(new TextField(cabecera[5], datos[5], Field.Store.YES)); //links 
                        neu.add(new TextField(cabecera[6], datos[6], Field.Store.YES)); //resumen
                        neu.add(new TextField(cabecera[7], datos[7], Field.Store.YES)); //author keywords
                        neu.add(new TextField(cabecera[8], datos[8], Field.Store.YES)); //index keywords
                        neu.add(new TextField(cabecera[9], datos[9], Field.Store.YES)); //eid
                        
                        writer.addDocument(neu);                        
                    }
                } 
                catch (IOException e) {}                
            }
        }   
    }
    
    /**************************************************************************\
    |                             FUNCION MAIN                                 |
    | @param args                                                              |
    | @throws java.lang.Exception                                              |
    \**************************************************************************/
    public static void main(String[] args) throws Exception {
       
       
        String INDEX_DIR = "../resultados/index";
        String path = "../prueba";
        //String path = "../consultas SCOPUS";
        
        Map<String,Analyzer> mip = new HashMap<>(); //se crea un MAP con analizadores para usar cada uno con un campo distinto del indice
        mip.put("Link", new UAX29URLEmailAnalyzer()); //para guardarlos enlaces enteros
        mip.put("EID", new KeywordAnalyzer());  //para que no haga nada
        mip.put("Cited by", new StandardAnalyzer()); //para que no borre el numero

       //cambiar a StandardAnalyzer() si queremos almacenar letras sueltas
       //por defecto se usa mi analizador
        PerFieldAnalyzerWrapper pefe = new PerFieldAnalyzerWrapper(new Analizador(), mip);
        
        //creacion del indice
        FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexWriterConfig config = new IndexWriterConfig(pefe);       
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //lectura de doucmentos e insercion en el indice
        try (IndexWriter writer = new IndexWriter(dir, config)) {
            long startTime = System.currentTimeMillis();
            obtenerDocs(path,writer);
            writer.commit();            
            long endTime = System.currentTimeMillis();            
            System.out.println(writer.numDocs()+" ficheros indexados en: "+(endTime-startTime)+" ms");
        }        
    }
}
