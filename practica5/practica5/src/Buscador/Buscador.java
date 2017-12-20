/*
*  Pablo Valenzuela Álvarez
*  Francisco Javier García Maldonado
*  
*  Recuperación de Información
*  2017/2018
*  Practica 5
*/
package Buscador;


/******************************************************************************\
|                                 LIBRERIAS                                    |
\******************************************************************************/
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//Librerias de Lucene
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.DrillDownQuery;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/******************************************************************************\
|                              CLASE PRINCIPAL                                 |
\******************************************************************************/
public class Buscador{
    
    /* Variables globales */
    private static IndexReader reader;
    private static IndexSearcher searcher;
    private static TaxonomyReader taxoReader;
    private static FacetsConfig fconfig;
    private static FacetsCollector fcollector;     
    private static BooleanQuery bq;
    
    /**************************************************************************\
    |                                COSTRUCTOR                                |
    | @param index: indice                                                     |
    | @param facet: facetas                                                    |
    \**************************************************************************/
    public static void inicializar(String index, String facet) throws IOException{
        
        Directory indexDir = FSDirectory.open(Paths.get(index));
        Directory taxoDir = FSDirectory.open(Paths.get(facet));
        reader = DirectoryReader.open(indexDir);
        searcher = new IndexSearcher(reader);
        taxoReader = new DirectoryTaxonomyReader(taxoDir);
        fconfig = new FacetsConfig();
        fcollector = new FacetsCollector();
        //fcollector = new FacetsCollector(true); //asi almacenamos los scores        
    }
    
    /**************************************************************************\
    |                             FUNCIÓN BUSQUEDA                             |
    | @param field: campo de búsqueda                                          |
    | @param query: consulta a realizar                                        |
    | @param cantidad_docs: cantidad de documentos devueltos                   |
    \**************************************************************************/
    public static TopDocs busqueda(String field, String query, int cantidad_docs) throws ParseException, IOException{
        
        QueryParser parser = new QueryParser(field, new Analizador()); //pongo mi super-analizador
        StringTokenizer str = new StringTokenizer(query);
        List<Query> lq = new ArrayList<>();
        
        while (str.hasMoreElements()) {
            String nextElement = (String)str.nextElement();            
            Query q = parser.parse(nextElement);  
            if(q.toString().length()!=0){                
                //System.out.println(q.toString());
                lq.add(q);
            }
        }
        
        BooleanQuery.Builder constructor = new BooleanQuery.Builder();
        for (Query query1 : lq) {
            constructor.add(query1,BooleanClause.Occur.MUST);
        }        
        bq = constructor.build();
        
        fcollector = new FacetsCollector();
        return FacetsCollector.search(searcher, bq, cantidad_docs, fcollector);
        
    }
    
    /**************************************************************************\
    |                             FUNCIÓN FACETAS                              |
    | @param top: array con las facetas de la búsqueda realizada                                        |
    \**************************************************************************/
    public static void muestraFacetas(TopDocs top) throws IOException{       
        
        Facets fCounts = new FastTaxonomyFacetCounts(taxoReader, fconfig, fcollector);                
        List<FacetResult> allDims = fCounts.getAllDims(10); 
        
        System.out.println("Total hits: "+top.totalHits);
        System.out.println("Categorias "+allDims.size());
        
        for(FacetResult fr: allDims){
            System.out.println("Dimension: "+fr.dim);
            for(LabelAndValue lav: fr.labelValues){
                System.out.println("    "+lav.label+":: -> "+lav.value);
            }
        }
    }
    
    /**************************************************************************\
    |                      FUNCIÓN QUE MUESTRA RESULTADOS                      |
    | @param top: array con los resultados de la búsqueda                      |
    \**************************************************************************/
    public static void muestraResults(TopDocs top) throws IOException{
        
        System.out.println("Resultados:");
        
        for (ScoreDoc mec : top.scoreDocs) {
            Document d = searcher.doc(mec.doc);
            System.out.println(mec.score+" "+d.get("Title"));
        }
        
    }
    
    /**************************************************************************\
    |                       FUNCIÓN PARA DRILL DOWN                            |
    | @param faceta: faceta de búsqueda                                        |
    | @param query: consulta de búsqueda                                       |
    | @param cantidad_docs: cantiodad de documentos máximos devueltos          |
    \**************************************************************************/
    public static TopDocs hacerDrillDown(String faceta, String query, int cantidad_docs) throws ParseException, IOException{
        
        //System.out.println(bq.toString());
        fcollector = new FacetsCollector();
        DrillDownQuery ddq = new DrillDownQuery(fconfig, bq);
        ddq.add(faceta, query);
        //System.out.println(ddq.toString());
        return FacetsCollector.search(searcher, ddq, cantidad_docs, fcollector);
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
        
        TopDocs top = busqueda("Abstract", "Population of pets", 20); //busqueda        
                
        top = hacerDrillDown("Año", "2017",20); //busqueda haciendo drill down
        
        // top = hacerDrillDown("Autor", "M", 20);
        
        // top = hacerDrillDown("Autor", "Perk", 20);
        
        muestraFacetas(top);
        
        muestraResults(top);
        
    }

}
