/*
*  Pablo Valenzuela Álvarez
*  Francisco Javier García Maldonado
*  
*  Recuperación de Información
*  2017/2018
*  Practica 1
*/
package practica1;


/******************************************************************************\
|                                 LIBRERIAS                                    |
\******************************************************************************/
import com.google.common.io.Files;
import com.googlecode.mp4parser.util.Path;
import com.uwyn.jhighlight.tools.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import org.xml.sax.ContentHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.tika.Tika;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.metadata.Metadata;


import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.apache.tika.sax.Link;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.tika.parser.Parser;

import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.xml.sax.SAXException;

/******************************************************************************\
|                              CLASE PRINCIPAL                                 |
\******************************************************************************/
public class Practica1{
    
/******************************************************************************\
|                            VARIABLES GLOBALES                                |
\******************************************************************************/
    
    //Esta lista contiene la direccion de todos los documentos de un directorio
    ArrayList<String> lDocs = new ArrayList<>(); 

    //Hash map contenedor de las palabras del texto y el número de iteraciones
    static HashMap<String, Integer> conteo = new HashMap<>();
    
    
    static ArrayList<Palabras> listaOrdenada = new ArrayList<>();
    
/******************************************************************************\
|                                CONSTRUCTOR                                   |
\******************************************************************************/
    public Practica1(String c) {
        addFile(c);
    }     
/******************************************************************************\
|                      FUNCION PARA DETECTAR IDIOMA                            |
\******************************************************************************/
    public static String identifyLanguage(String text) throws IOException {
        LanguageDetector identifier  = new  OptimaizeLangDetector().loadModels();
        LanguageResult idioma = identifier.detect(text);
        //System.out.println("XXXXXX"+idioma.getLanguage());
        return idioma.getLanguage();
    }      
/******************************************************************************\
|                        FUNCION PARA EXTRAER LINKS                            |
\******************************************************************************/
    public static void imprimirEnlaces(File file, String s) throws Exception {
        //Creamos objetos de tipo parser y metadata
        Parser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();

        //Creamos el contenedor de los enlaces
        LinkContentHandler enlaces = new LinkContentHandler();   
        //Creamos el stream para los archivos
        InputStream stream = new FileInputStream(file);   

        //System.out.println("seleccionando links ");

        try {parser.parse(stream, enlaces, metadata,new ParseContext());}
        finally {stream.close();}

        //Creamos una lista con todos los enlaces
        List<Link> lista  = new LinkedList<Link>();

        //System.out.println(metadata.toString());
        //System.out.println(handler.toString());
        lista = enlaces.getLinks();

        //Para cada enlace guarda la url en un archivo
        System.out.println("Imprimiendo enlaces de "+file.getName());
        File f = new File (s+"-"+file.getName()+".txt");
        PrintWriter pw = new PrintWriter(f);
        for (Link enlace : lista) {        
            if(enlace.getUri()!="")
                //System.out.println("link:"+enlace.getUri());  
                pw.println("Link: "+enlace.getUri());        
        }
        pw.close();    
    }    
/******************************************************************************\
|             FUNCION PARA LEER LOS ARCHIVOS DEL DIRECTORIO                    |
\******************************************************************************/
    private void addFile(String s){
        File file = new File(s);
        File[] files = file.listFiles();        
        for (File f : files) {
            if (f.isDirectory()){
                addFile(f.getPath().toString());
            }else{
                String str = f.getPath();
                System.out.println("Añadiendo "+str+" a la lista");
                lDocs.add(str);
                //System.out.println(str);
            }
        }
    }
/******************************************************************************\
|                     FUNCION PARA PARSEAR ARCHIVOS                            |
\******************************************************************************/
    public static void parsearDatos(File file) throws FileNotFoundException, IOException, SAXException, TikaException {

        InputStream in = new FileInputStream(file); 
        Metadata meta = new Metadata();
        ContentHandler ch = new BodyContentHandler(-1);
        ParseContext pc = new ParseContext();

        AutoDetectParser parser = new AutoDetectParser();

        parser.parse(in, ch, meta,pc);
        StringTokenizer st = new StringTokenizer(ch.toString());

        //Metodo para hacer conteo de palabras
        System.out.println("Parseando "+file.getName());
        while (st.hasMoreTokens()) {
            //quitamos signos de puntuacion varios y ponemos las palabras en minuscula
            String asd = st.nextToken().replaceAll("\\.", "").replaceAll("\\,", "")
                                        .replaceAll("\\;","").replaceAll("\\?","")
                                        .replaceAll("\\:","").replaceAll("\\!","")
                                        .replaceAll("\\=","").replaceAll("\\_","")
                                        .replaceAll("\\-"," ").replaceAll("\"","")
                                        .replaceAll("[0-9]","").replaceAll("\\(","")
                                        .replaceAll("\\)","").replaceAll("\\%","")
                                        .replaceAll("\\$","").replaceAll("\\/","")
                                        .replaceAll("\\[","").replaceAll("\\]","")
                                        .replaceAll("\\#","").replaceAll("\\{","")
                                        .replaceAll("\\}","").replaceAll("\'", "")
                                        .toLowerCase();
            //System.out.println(asd);
            if(!conteo.containsKey(asd)){
                conteo.compute(asd, (k,v) -> 1);
            }else{
                conteo.compute(asd, (k,v) -> v+1);
            }             
        }
    }
/******************************************************************************\
|                     FUNCION PARA IMPRIMIR LOS DATOS                          |
\******************************************************************************/
    public static void imprimirDatos(File file, String s, Tika tika) throws IOException, TikaException{        

        String type = tika.detect(file); //detecta el tipo de archivo

        InputStream is = new FileInputStream(file);
        AutoDetectReader detector = new AutoDetectReader(is);
        Charset charSet = detector.getCharset();
        //System.out.println("Encoding "+ charSet.toString());
        // Metadata met = new Metadata();

        //String aux = tika.parseToString(is, met);
        //System.out.println(aux);
        //System.out.println("Metadatos::"+met.toString());

        // String docDate = met.get("CreatorDate");
        //String docType = met.get("Content-Type");
        //System.out.println("Tipo::"+docType+" Fecha::"+docDate);       

        String text = tika.parseToString(file);
        //System.out.println("lenguaje::"+identifyLanguage(text));
        //System.out.println("Contenido del documento");
        //System.out.println(text);
        
        System.out.println("Imprimiendo datos de "+file.getName());
        File f = new File (s+".txt");        

        try (PrintWriter pw = new PrintWriter(new FileWriter(f,true))) {
            pw.println("Nombre:\t\t"+file.getName());
            pw.println("Tipo:\t\t"+type);
            pw.println("Codificación:\t"+charSet.toString());
            pw.println("Idioma:\t\t"+identifyLanguage(text));
            pw.println("---------------------------------------------------------------------------------------------------------------");
        } 
    }
/******************************************************************************\
|                 FUNCION PARA ORDENAR Y IMPRIMIR EL CONTEO                    |
\******************************************************************************/
    public static void imprimirConteo(File f, String s) throws FileNotFoundException{
        //Pasando datos a Array para ser ordenado.
        Palabras palabra = new Palabras();
        for(int i=0; i<conteo.size(); i++){
            for(Map.Entry<String, Integer> mapita : conteo.entrySet()){
                palabra.nomPalabra = mapita.getKey();
                palabra.numPalabras=mapita.getValue();
                listaOrdenada.add(palabra);
            }
        }
        
        //ORDENANDO
        System.out.println("\nOrdenando...");
        
        listaOrdenada.sort(c);
        
        //Creando archivo para introducir el conteo de palabras
        File archivo = new File (s+"-"+f.getName()+".txt");
        PrintWriter escritura = new PrintWriter(archivo);

        System.out.println("Imprimiendo conteo de "+f.getName());
        for(int i=0; i < conteo.size(); i++){
            for(Map.Entry<String, Integer> mapita : conteo.entrySet()){
                escritura.println("Clave:\t"+ mapita.getKey() + "\t\t valor:\t" + mapita.getValue());
            }
            escritura.close();

            //reseteo el hashmap
            conteo.clear();
        }
        
        
        
    }
/******************************************************************************\
|                                 FUNCION MAIN                                 |
\******************************************************************************/
    public static void main(String[] args) throws Exception {

        // Creaamos una instancia de Tika con la configuracion por defecto
        Tika tika = new Tika();
        System.out.println("Iniciando Programa");    
        System.out.println("------------------------------------------------------------------------------------------------"); 
        
        //Creamos un objeto Practica1 y le pasamos la direccion del directorio que nos interesa "indexar"  
        String path = "libros";
        Practica1 madn = new Practica1("../"+path);                     

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

            File f = new File(file);//
            System.out.println("------------------------------------------------------------------------------------------------");            
            parsearDatos(f);
            imprimirEnlaces(f,theDir+"/Enlaces");
            imprimirDatos(f,theDir+"/Datos",tika);
            imprimirConteo(f,theDir+"/Conteo");                      
        }      
        System.out.println("------------------------------------------------------------------------------------------------");  
        System.out.println("Programa finalizado");
    }
}
