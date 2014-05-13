import java.io.File;
import java.util.Iterator;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;


public class FamilyInferredABoxPrettyPrinter {
	
	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException {

		//Laden der Ontologie in den Hauptspeicher mittels OWL API 
		OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(
        		new File("myexamples/familykb.swrl.owl"));
        
        //Uebergabe der Ontologie an den HermiT Reasoner
        ReasonerFactory factory = new ReasonerFactory();
        Configuration c=new Configuration();
        //c.reasonerProgressMonitor=new ConsoleProgressMonitor();
        OWLReasoner reasoner=factory.createReasoner(ontology, c);
        
        //�ber 'ontology' haben Sie nun Zugriff auf die Ontologie-Struktur 
        // und �ber 'reasoner' k�nnen Sie Schlussfolgerungen abfragen 
        
        Iterator<OWLNamedIndividual> iIt = ontology.getIndividualsInSignature().iterator();
        while(iIt.hasNext()) {
        	OWLNamedIndividual indiv = iIt.next();
        	System.out.println("\nIndividual: " + indiv.getIRI().getFragment());
        	Iterator<Node<OWLClass>> cIt = reasoner.getTypes(indiv, false).iterator();
           	System.out.print("  Types: ");
        	while(cIt.hasNext()) {
            	OWLClass cls = cIt.next().getRepresentativeElement();
            	System.out.print(cls.getIRI().getFragment());
            	if(cIt.hasNext()) {
            		System.out.print(", ");
            	}
            }
        	System.out.println();
        	
        	System.out.print("  Facts: ");
        	
        	Iterator<OWLEntity> propertyIt = ontology.getSignature().iterator();
    		boolean isFirstFact = true;
        	while(propertyIt.hasNext()) {
        		OWLEntity property = propertyIt.next();
        		if(property.isOWLObjectProperty() && !property.isTopEntity() ) {
                	Iterator<Node<OWLNamedIndividual>> targetIndivIt = 
                			reasoner.getObjectPropertyValues(indiv, property.asOWLObjectProperty())
                			.iterator();
                	while(targetIndivIt.hasNext()) {
                		OWLNamedIndividual targetIndiv = targetIndivIt.next().getRepresentativeElement();
                		if(!isFirstFact) {System.out.print(", \n    ");} else {isFirstFact=false;}
                		System.out.print(
                				property.getIRI().getFragment() + " " +
                				targetIndiv.getIRI().getFragment()		
                		);               		
                	}
        		}
        	}
        }
	}
}
















