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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.apache.lucene.facet.range.LongRange;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.DrillDownQuery;
import org.apache.lucene.facet.DrillSideways;
import org.apache.lucene.facet.DrillSideways.DrillSidewaysResult;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.facet.range.LongRangeFacetCounts;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.TaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.TaxonomyFacetSumValueSource;
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
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
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
        //fcollector = new FacetsCollector(true); //asi almacenamos los scores
        
    }
    
    /**************************************************************************\
    |                       FUNCION PA BUSCAR                                  |
    \**************************************************************************/
    public static TopDocs busqueda(String field, String query, int tam) throws ParseException, IOException{
        
        //quitar
        QueryParser parser = new QueryParser(field, new KeywordAnalyzer());
        Query q = parser.parse(query);
        
        //System.out.println(q.toString());
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
        
        //TopDocs top = busqueda("Abstract", "cat", 20); //busqueda
        
        //System.out.println(top.totalHits);
        
        /*for (ScoreDoc mec : top.scoreDocs) {
            Document d = searcher.doc(mec.doc);
            System.out.println(mec.score+" "+d.get("Title"));
        }*/
        
       // Facets facetas;
              
        //conteo de resultados
       /*facetas = new FastTaxonomyFacetCounts(taxoReader, fconfig, fcollector);
        
        //iteramos sobre las facetas        
        List<FacetResult> totalDims = facetas.getAllDims(10);
       System.out.println("Total de categorias "+totalDims.size());
        for (FacetResult fr : totalDims) {
           System.out.println("Categoria: "+fr.dim);
            for (LabelAndValue lav : fr.labelValues) {
                System.out.println("Etiqueta: "+lav.label+" || valor -> "+lav.value);
            }
        }*/
        
        //FacetResult fr = facetas.getTopChildren(5, "Autor");
        
        //suma de scores
       /* facetas = new TaxonomyFacetSumValueSource(taxoReader, fconfig, fcollector, DoubleValuesSource.SCORES);
       // System.out.println("Sumamos los scores");
        FacetResult frs = facetas.getTopChildren(10, "Año");
        for(LabelAndValue lav: frs.labelValues){
           // System.out.println("Etiqueta: "+lav.label+" || valor -> "+lav.value);
        }*/
        
        //por rango
       /* FacetsCollector fcRango = new FacetsCollector();
        LongRange[] rangos = new LongRange[3];
        rangos[0] = new LongRange("10000 a.C.-2000",Long.MIN_VALUE, true, 2000, true);
        rangos[1] = new LongRange("2001-2010", 2007, true, 2012, true);
        rangos[2] = new LongRange("2010-Ahora", 2012, true, Long.MAX_VALUE, true);        
        //FacetsCollector.search(searcher, new MatchAllDocsQuery(), 10, fcRango); //devolver en topdocs
        LongRangeFacetCounts faceiter = new LongRangeFacetCounts("Año", fcRango, rangos);
        //System.out.println("Por rangos");
        FacetResult frs123 = faceiter.getTopChildren(0, "Año");
        for(LabelAndValue lav: frs123.labelValues){
           // System.out.println("Etiqueta: "+lav.label+" || valor -> "+lav.value);
        }*/
        
        //drill down preba
        
        
        String query = "population of pets";
        String field = "Abstract";
        QueryParser parser = new QueryParser(field, new StandardAnalyzer());
        StringTokenizer str = new StringTokenizer(query);
        List<Query> lq = new ArrayList<>();
        while (str.hasMoreElements()) {
            String nextElement = (String)str.nextElement();
            //System.out.println(nextElement);
            Query q = parser.parse(nextElement);            
            if(q.toString().length()!=0){                
                System.out.println(q.toString());
                lq.add(q);
            }
        }
        
        BooleanQuery.Builder constructor = new BooleanQuery.Builder();
        for (Query query1 : lq) {
            constructor.add(query1,BooleanClause.Occur.MUST);
        }
        
        BooleanQuery bq = constructor.build();
        
        
        /*QueryParser parser = new QueryParser("Year", new KeywordAnalyzer());
        Query q = parser.parse("2018");*/
        DrillDownQuery ddq = new DrillDownQuery(fconfig, bq);
        //ddq.add("Autor", "SM");          
        //ddq.add("Año", "2017"); 
        //ddq.add("Año", "2015");
        System.out.println("Filtramos query ["+ddq.toString()+"]");
        
        //FacetsCollector fc1 = new FacetsCollector();
        TopDocs td1 = FacetsCollector.search(searcher, ddq, 10, fcollector);
        System.out.println("    Total hits: "+td1.totalHits);
        Facets fcCounts2 = new FastTaxonomyFacetCounts(taxoReader, fconfig, fcollector);
        List<FacetResult> allDims = fcCounts2.getAllDims(10);
        
        System.out.println("Categorias "+allDims.size());
        for(FacetResult fr: allDims){
            System.out.println("Dimension: "+fr.dim);
            for(LabelAndValue lav: fr.labelValues){
                System.out.println("    "+lav.label+":: -> "+lav.value);
            }
        }
        
        for(ScoreDoc sd: td1.scoreDocs){
            Document d = searcher.doc(sd.doc);
            System.out.println("   Doc Score -> "+sd.score+":: -> "+d.get("Title"));
        }
        
    }
}
