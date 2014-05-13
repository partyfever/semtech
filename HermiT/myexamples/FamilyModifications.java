import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;


public class FamilyModifications {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws IOException 
	 * @throws OWLOntologyStorageException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		OWLOntology o=m.loadOntologyFromOntologyDocument(new File("myexamples/familykb.swrl.owl"));
		
        //�bergabe der Ontologie an den HermiT Reasoner
        ReasonerFactory factory = new ReasonerFactory();
        Configuration c = new Configuration();
//        c.reasonerProgressMonitor=new ConsoleProgressMonitor();
        OWLReasoner reasoner=factory.createReasoner(o, c);
        
//        OWLClass woman = 
        
        File file = new File("myexamples/familykb_modified.swrl.owl");
        if (!file.exists()) {
        	file.createNewFile();
        }
        file = file.getAbsoluteFile();
        OutputStream os = new FileOutputStream(file);
        m.saveOntology(o);
//        os.write(m);
        
	}

}
