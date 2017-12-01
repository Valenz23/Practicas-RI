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
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/******************************************************************************\
|                              CLASE PRINCIPAL                                 |
\******************************************************************************/
public class Practica5{
    
    /* Variables globales */
    private static IndexReader reader;
    private static IndexSearcher searcher;
    private static TaxonomyReader taxoReader;
    private static FacetsConfig fconfig;
    private static FacetsCollector fcollector;     
    
    /**************************************************************************\
    |                       COSTRUCTOR                                         |
    \**************************************************************************/
    public static void inicializar(String index, String facet) throws IOException{
        
        Directory indexDir = FSDirectory.open(Paths.get(index));
        Directory taxoDir = FSDirectory.open(Paths.get(facet));
        reader = DirectoryReader.open(indexDir);
        searcher = new IndexSearcher(reader);
        taxoReader = new DirectoryTaxonomyReader(taxoDir);
        fconfig = new FacetsConfig();
        fcollector = new FacetsCollector();
        
    }
    
    /**************************************************************************\
    |                       FUNCION PA BUSCAR                                  |
    \**************************************************************************/
    public static TopDocs busqueda(String field, String query, int tam) throws ParseException, IOException{
        
        //quitar
        QueryParser parser = new QueryParser(field, new KeywordAnalyzer());
        Query q = parser.parse(query);
        
        System.out.println(q.toString());
        return FacetsCollector.search(searcher, q, tam, fcollector);
        
    }
    
    
    /**************************************************************************\
    |                             FUNCION MAIN                                 |
    | @param args                                                              |
    | @throws java.lang.Exception                                              |
    \**************************************************************************/
    public static void main(String[] args) throws Exception {   
        
        String INDEX_DIR = "../resultados/index";
        String FACET_DIR = "../resultados/facet";
        
        inicializar(INDEX_DIR, FACET_DIR);
        
        TopDocs top = busqueda("Authors", "lacerda dourado cardoso", 20); //busqueda
        
        System.out.println(top.totalHits);
        
        for (ScoreDoc mec : top.scoreDocs) {
            Document d = searcher.doc(mec.doc);
            System.out.println(mec.score+" "+d.get("Title"));
        }
        
    }
}
