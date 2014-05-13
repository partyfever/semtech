import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;


public class Family {

	/**
	 * reads familykb.swrl.owl, computes the inferred subclass hierarchy, inferred class assertions
	 *  and inferred object property assertions and materializes the inferred axioms in familykb-inferred.swrl.owl 
	 * 
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws IOException 
	 * @throws OWLOntologyStorageException 
	 * @throws UnknownOWLOntologyException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, IOException, UnknownOWLOntologyException, OWLOntologyStorageException {
        // First, we create an OWLOntologyManager object. The manager will load and 
        // save ontologies. 
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        // Now, we create the file from which the ontology will be loaded. 
        // Here the ontology is stored in a file locally in the ontologies subfolder
        // of the examples folder.
        File inputOntologyFile = new File("myexamples/familykb.swrl.owl");
        // We use the OWL API to load the ontology. 
        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(inputOntologyFile);
        // Now we can start and create the reasoner. Since materialisation of axioms is controlled 
        // by OWL API classes and is not natively supported by HermiT, we need to instantiate HermiT 
        // as an OWLReasoner. This is done via a ReasonerFactory object. 
        ReasonerFactory factory = new ReasonerFactory();
        // The factory can now be used to obtain an instance of HermiT as an OWLReasoner. 
        Configuration c=new Configuration();
        c.reasonerProgressMonitor=new ConsoleProgressMonitor();
        OWLReasoner reasoner=factory.createReasoner(ontology, c);

        // The following call causes HermiT to compute the class, object, 
        // and data property hierarchies as well as the class instances. 
        // Hermit does not yet support precomputation of property instances. 
        //reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY, InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_HIERARCHY, InferenceType.DATA_PROPERTY_HIERARCHY, InferenceType.OBJECT_PROPERTY_ASSERTIONS);
        // We now have to decide which kinds of inferences we want to compute. For different types 
        // there are different InferredAxiomGenerator implementations available in the OWL API and 
        // we use the InferredSubClassAxiomGenerator and the InferredClassAssertionAxiomGenerator 
        // here. The different generators are added to a list that is then passed to an 
        // InferredOntologyGenerator. 
        List<InferredAxiomGenerator<? extends OWLAxiom>> generators=new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
        generators.add(new InferredSubClassAxiomGenerator());
        generators.add(new InferredClassAssertionAxiomGenerator());
        generators.add(new InferredPropertyAssertionGenerator());
        // We can now create an instance of InferredOntologyGenerator. 
        InferredOntologyGenerator iog=new InferredOntologyGenerator(reasoner,generators);
        // Before we actually generate the axioms into an ontology, we first have to create that ontology. 
        // The manager creates the for now empty ontology for the inferred axioms for us. 
        OWLOntology inferredAxiomsOntology=manager.createOntology();
        // Now we use the inferred ontology generator to fill the ontology. That might take some 
        // time since it involves possibly a lot of calls to the reasoner.    
        iog.fillOntology(manager, inferredAxiomsOntology);
        // Now the axioms are computed and added to the ontology, but we still have to save 
        // the ontology into a file. Since we cannot write to relative files, we have to resolve the 
        // relative path to an absolute one in an OS independent form. We do this by (virtually) creating a 
        // file with a relative path from which we get the absolute file.  
        File inferredOntologyFile=new File("myexamples/familykb-inferred.swrl.owl");
        if (!inferredOntologyFile.exists())
            inferredOntologyFile.createNewFile();
        inferredOntologyFile=inferredOntologyFile.getAbsoluteFile();
        // Now we create a stream since the ontology manager can then write to that stream. 
        OutputStream outputStream=new FileOutputStream(inferredOntologyFile);
        // We use the same format as for the input ontology.
        manager.saveOntology(inferredAxiomsOntology, new OWLXMLOntologyFormat(), outputStream);
        // Now that ontology that contains the inferred axioms should be in the ontologies subfolder 
        // (you Java IDE, e.g., Eclipse, might have to refresh its view of files in the file system) 
        // before the file is visible.  
        System.out.println("The ontology in myexamples/family-inferred.swrl.owl should now contain all inferred subclass, class assertion and object property axioms (you might need to refresh the IDE file view). ");
	}

}
