import java.io.File;
import java.util.Iterator;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;


public class MyMuesli {

	/**
	 * @author Stefan
	 * @param args
	 * @throws OWLOntologyCreationException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException {
		//Laden der Ontologie in den Hauptspeicher mittels OWL API 
		OWLOntologyManager manager=OWLManager.createOWLOntologyManager();

        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new File("mymuesli/mymuesli_new.owl"));
        
        //�bergabe der Ontologie an den HermiT Reasoner
        ReasonerFactory factory = new ReasonerFactory();
        Configuration c = new Configuration();
//        c.reasonerProgressMonitor=new ConsoleProgressMonitor();
        OWLReasoner reasoner=factory.createReasoner(ontology, c);
             
        Iterator<OWLClass> iIt = ontology.getClassesInSignature().iterator();
        while(iIt.hasNext()) {
        	OWLClass classes = iIt.next();
        	if (classes.getIRI().getFragment().toString().equals("Mixer")) {
        		System.out.println("Class: " + classes.getIRI().getFragment());
        		printDirectSubclassesOf("Mixer", reasoner, ontology);
        	}
        	if (classes.getIRI().getFragment().toString().equals("Müsli")) {
        		System.out.println("Class: " + classes.getIRI().getFragment());
        		printDirectSubclassesOf("Müsli", reasoner, ontology);
        	}
        }
        
        
	}
	
	/*
	 * Alle Subclassen einer durch den Klassennamen gegebenen Klasse
	 */
	static void printDirectSubclassesOf(String classname, OWLReasoner r, OWLOntology onto) {
		Iterator<OWLClass> iIt = onto.getClassesInSignature().iterator();
		 while(iIt.hasNext()) {
        	OWLClass classes = iIt.next();
        	if (classes.getIRI().getFragment().toString().equals(classname)) {
        		Iterator<Node<OWLClass>> nodesMixer = r.getSubClasses(classes, true).iterator();
        		while (nodesMixer.hasNext()) {
        			Node<OWLClass> sub = nodesMixer.next();
        			System.out.println("\tThings: " + sub.getRepresentativeElement().getIRI().getFragment());
        		}
        	}
		 }
    }
	
	
}
