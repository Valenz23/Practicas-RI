/*
*  Pablo Valenzuela Álvarez
*  Francisco Javier García Maldonado
*  
*  Recuperación de Información
*  2017/2018
*  Practica 4
*/
package practica4;


/****************************************************************************\
|                                 LIBRERIAS                                    |
\****************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.Paths;
import org.xml.sax.ContentHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.sound.midi.Patch;
import org.xml.sax.SAXException;

//librerias de tika
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

//Librerias de Lucene
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SortedNumericDocValues;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;

/****************************************************************************\
|                              CLASE PRINCIPAL                                 |
\****************************************************************************/
public class Practica4{
    
    /**************************************************************************\
    |                          VARIABLES GLOBALES                              |
    \**************************************************************************/
    
    //Esta lista contiene la direccion de todos los documentos de un directorio
 /*  ArrayList<String> lDocs = new ArrayList<>(); 

    //Hash map contenedor de las palabras del texto y el número de iteraciones
    static HashMap<String, Integer> conteo = new HashMap<>();
    
    //ArrayList de Palabras, para poder ordenar el contenido del HashMap
    static ArrayList<Palabras> listaOrdenada = new ArrayList<>();*/
    
    /**************************************************************************\
    |                            CONSTRUCTOR                                   |
    | @param c -> String que contiene la direccion del fichero                 |
    \**************************************************************************/
  /*  public Practica4(String c) {
        addFile(c);
    } */
    
    
    /**************************************************************************\
    |      FUNCION PARA LEER LOS ARCHIVOS DEL DIRECTORIO RECURSIVAMENTE        |
    \**************************************************************************/
 /*   private void addFile(String s){
        File file = new File(s);
        File[] files = file.listFiles();        
        for (File f : files) {
            if (f.isDirectory()){
                addFile(f.getPath());
            }else{
                String str = f.getPath();
                System.out.println("Añadiendo "+str+" a la lista");
                lDocs.add(str);
                //System.out.println(str);
            }
        }
    }*/
    
    
    /**************************************************************************\
    |                    FUNCION PARA PARSEAR ARCHIVOS                         |
    | @param file -> fichero                                                   |
    | @param ana -> analizador                                                 |
    |                                                                          |
    | @throws java.io.FileNotFoundException*                                   |
    | @throws org.xml.sax.SAXException*                                        |
    | @throws org.apache.tika.exception.TikaException                          |
    \**************************************************************************/
  /*  public static void parsearDatos(File file, Analyzer ana) throws FileNotFoundException, IOException, SAXException, TikaException {

        //usamos tika para sacar los datos del fichero
        InputStream in = new FileInputStream(file);         
        ContentHandler ch = new BodyContentHandler(-1);
        AutoDetectParser parser = new AutoDetectParser();
        parser.parse(in, ch, new Metadata() ,new ParseContext());       
        
        System.out.println("Parseando "+file.getName());
        
        //llamamos a la funcion que usa lucene
        Tokenizar(ana, ch.toString());       
    }*/
    
    
    /**************************************************************************\
    |               FUNCION PARA ORDENAR E IMPRIMIR EL CONTEO                  |
    | @param f -> fichero                                                      |
    | @param s -> String usado para imprimir                                   |
    |                                                                          |
    | @throws java.io.FileNotFoundException                                    |
    \**************************************************************************/
   /* public static void imprimirConteo(File f, String s) throws FileNotFoundException{
        
        //Pasando datos a Array para ser ordenado.
        conteo.entrySet().stream().map((mapita) -> {
            Palabras pal = new Palabras();
            pal.setNomPalabra(mapita.getKey());
            pal.setNumPalabra(mapita.getValue());
            return pal;
        }).forEachOrdered((pal) -> {        
            listaOrdenada.add(pal);
        });
        
        //ORDENANDO
        System.out.println("Ordenando lista de "+f.getName());
       
        cmp c =new cmp();
        listaOrdenada.sort(c);
        
        //Creando archivo para introducir el conteo de palabras
        File archivo = new File (s+"-"+f.getName()+".txt");
        try (PrintWriter escritura = new PrintWriter(archivo)) {
            System.out.println("Imprimiendo conteo de "+f.getName());
            listaOrdenada.forEach((pal) -> {escritura.println(pal.getNomPalabra()+","+pal.getNumPalabra()+";");
            }); //cerramos el printwriter y reseteamos los arrays
        }
        conteo.clear();
        listaOrdenada.clear();
        
    }*/
    
    
    /**************************************************************************\
    |       FUNCION QUE TOKENIZA UN STRING Y LO ALMACENA EN EL HASHMAP         |
    | @param an -> analizador                                                  |
    | @param str -> string a analizar                                          |
    \**************************************************************************/ 
    /*public static void Tokenizar(Analyzer an, String str){
        
        try{               
            try (TokenStream stream = an.tokenStream(null, new StringReader(str))) {
                OffsetAttribute off = stream.addAttribute(OffsetAttribute.class); //guarda la posicion de la palabra
                CharTermAttribute cha = stream.addAttribute(CharTermAttribute.class); //guarda la palabra?
                stream.reset();
                while(stream.incrementToken()){
                    String asd = cha.toString();
                    //System.out.println(cha.toString()+" : ["+off.startOffset()+","+off.endOffset()+"]");
                    //Almacenamos en el hashmap
                    Almacenar(asd);
                }
            } //guarda la posicion de la palabra
        }
        catch(IOException e){ throw new RuntimeException(); }        
        
    }*/
    
    
    /**************************************************************************\
    |         FUNCION QUE  PALABRAS ALMACENA EN EL HASHMAP                     |
    | @param str -> key a almacenar                                            |
    \**************************************************************************/     
   /* public static void Almacenar(String str){
        if(!conteo.containsKey(str)){
            conteo.compute(str, (k,v) -> 1);
        }else{
            conteo.compute(str, (k,v) -> v+1);
        } 
    }*/
    
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
                        
                       /* neu.add(new IntPoint(cabecera[2], Integer.parseInt(datos[2]))); //año no funciona
                        neu.add(new StoredField(cabecera[2],datos[2])); //año;*/
                        neu.add(new StringField(cabecera[2], datos[2], Field.Store.YES)); //año
                        
                        neu.add(new TextField(cabecera[3], datos[3], Field.Store.YES)); //source_title
                        
                        /*neu.add(new IntPoint(cabecera[4], Integer.parseInt(datos[4]))); //citas nofunciona
                        neu.add(new StoredField(cabecera[4], datos[4])); //citas*/
                        neu.add(new StringField(cabecera[4], datos[4], Field.Store.YES)); //citas
                        
                        neu.add(new TextField(cabecera[5], datos[5], Field.Store.YES)); //links -> usar analizador cxuahdj
                        neu.add(new TextField(cabecera[6], datos[6], Field.Store.YES)); //resumen
                        neu.add(new TextField(cabecera[7], datos[7], Field.Store.YES)); //author keywords
                        neu.add(new TextField(cabecera[8], datos[8], Field.Store.YES)); //index keywords
                        neu.add(new TextField(cabecera[9], datos[9], Field.Store.YES)); //eid -> usar analizador que no haga nada
                        
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
       /* 
        //Creamos varios analizadores de Lucene
        Analyzer whitespace = new WhitespaceAnalyzer();
        Analyzer simple = new SimpleAnalyzer();
        Analyzer standard = new StandardAnalyzer();
        Analizador analizador = new Analizador();
       
        System.out.println("Iniciando Programa");    
        System.out.println("------------------------------------------------------------------------------------------------"); 
        
        //Creamos un objeto Practica1 y le pasamos la direccion del directorio que nos interesa "indexar"  
        String path = "libros";
        Practica4 madn = new Practica4("../"+path);                     

        //creamos una carpeta que contendrá los resultados
        
        File res = new File("../resultados");
        if (!res.exists()) {
            System.out.println("------------------------------------------------------------------------------------------------");
            System.out.println("Creando carpeta: " + res.getName());
            res.mkdir();     
        }
        
        File theDir = new File("../resultados/"+path);
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("------------------------------------------------------------------------------------------------");
            System.out.println("Creando carpeta: " + theDir.getName());
            theDir.mkdir();     
        }
        else{
            System.out.println("------------------------------------------------------------------------------------------------");
            System.out.println("Actualizando carpeta: " + theDir.getName());
            String[]entries = theDir.list();
            for(String s: entries){
                File currentFile = new File(theDir.getPath(),s);
                currentFile.delete();
            }
        }
        
        // Se parsean todos los ficheros pasados como argumento y se extrae el contenido
        for (String file : madn.lDocs) {            
            File f = new File(file);                    
            System.out.println("------------------------------------------------------------------------------------------------");            
            parsearDatos(f,whitespace);            
            //imprimirEnlaces(f,theDir+"/Enlaces");
            //imprimirDatos(f,theDir+"/Datos",tika);
            imprimirConteo(f,theDir+"/Conteo-WhiteSpace");  
            
            parsearDatos(f,simple);            
            imprimirConteo(f,theDir+"/Conteo-Simple");  
            
            parsearDatos(f,standard);            
            imprimirConteo(f,theDir+"/Conteo-Standard");  
            
            parsearDatos(f,analizador);            
            imprimirConteo(f,theDir+"/Conteo-Analizador");  
        }      
        System.out.println("------------------------------------------------------------------------------------------------");  
        System.out.println("Programa finalizado");*/
       
        String INDEX_DIR = "../resultados/index";
        String path = "../prueba";
        //String path = "../consultas SCOPUS";

        
        //TODO multianalizador
        Analizador analyzer = new Analizador();       
        FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);       
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter writer = new IndexWriter(dir, config);

        long startTime = System.currentTimeMillis();    
        obtenerDocs(path,writer);                
        writer.commit();
        
        long endTime = System.currentTimeMillis();
        
        System.out.println(writer.numDocs()+" ficheros indexados en: "+(endTime-startTime)+" ms");
        
        writer.close();        
    }
}
