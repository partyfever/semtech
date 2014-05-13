import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;


public class FamilyModifications3 {

	public static final IRI familykb_iri = IRI.create("http://www.example.org/familykb");
	
	public static void printInstancesOfCls(OWLReasoner reasoner, OWLClass cls) {
		Iterator<Node<OWLNamedIndividual>> iIt = reasoner.getInstances(cls, false).iterator();
		System.out.print("-- Instances of Class " + cls.getIRI().getFragment() + ": ");
		while(iIt.hasNext()) {
			OWLNamedIndividual indiv = iIt.next().getRepresentativeElement();
			System.out.print(indiv.getIRI().getFragment());
			if(iIt.hasNext()){
				System.out.print(", ");
			}
		}
		System.out.println();
	}
	
	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws IOException 
	 * @throws OWLOntologyStorageException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		OWLOntology o = m.loadOntologyFromOntologyDocument(new File("myexamples/familykb.swrl.owl"));
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		
		ReasonerFactory factory = new ReasonerFactory();
		Configuration c = new Configuration();
		//c.reasonerProgressMonitor = new ConsoleProgressMonitor();
		OWLReasoner r = factory.createReasoner(o,c);
		
		OWLClass cls_Woman = df.getOWLClass(IRI.create(familykb_iri + "#Woman"));
		printInstancesOfCls(r,cls_Woman);
		
		
		OWLClass cls_Aunt = df.getOWLClass(IRI.create(familykb_iri + "#Aunt"));
		OWLClass cls_Person = df.getOWLClass(IRI.create(familykb_iri + "#Person"));
		OWLClass cls_ChildOfSomeone = df.getOWLClass(IRI.create(familykb_iri + "#ChildOfSomeone"));
		OWLClass cls_Thing = df.getOWLClass(IRI.create("http://www.w3.org/2002/07/owl#Thing"));
		
		OWLObjectProperty op_hasSibling = df.getOWLObjectProperty(IRI.create(familykb_iri + "#hasSibling"));
		OWLObjectProperty op_hasParent = df.getOWLObjectProperty(IRI.create(familykb_iri + "#hasParent"));

		OWLAxiom axiom = df.getOWLSubClassOfAxiom(cls_Aunt, cls_Person);
		AddAxiom addAxiom = new AddAxiom(o, axiom);
		m.applyChange(addAxiom);
		System.out.println("-- Added to ontology: \n" + axiom + "\n");

		OWLAxiom axiom2 = df.getOWLEquivalentClassesAxiom(
				cls_Aunt,
				df.getOWLObjectIntersectionOf(
						cls_Woman,
						df.getOWLObjectSomeValuesFrom(
								op_hasSibling,
								df.getOWLObjectSomeValuesFrom(
										df.getOWLObjectInverseOf(op_hasParent),
										cls_Thing))));
		m.applyChange(new AddAxiom(o,axiom2));
		System.out.println("-- Added to ontology: \n" + axiom2 + "\n");
		
		printInstancesOfCls(r,cls_Aunt);
		r.flush();
		printInstancesOfCls(r,cls_Aunt);
		
		OWLAxiom axiom3 = df.getOWLEquivalentClassesAxiom(
				cls_ChildOfSomeone,
				df.getOWLObjectSomeValuesFrom(op_hasParent, cls_Thing));
		m.applyChange(new RemoveAxiom(o,axiom3));
		System.out.println("-- Removed from ontology: \n" + axiom3 + "\n");
		
		OWLEntityRemover remover = new OWLEntityRemover(m, Collections.singleton(o));
		cls_Person.accept(remover);
		m.applyChanges(remover.getChanges());
		System.out.println("-- Changes: \n" + remover.getChanges() + "\n");
		
		File file = new File("myexamples/familykb_modified.swrl.owl");
		if(!file.exists())
			file.createNewFile();
		file = file.getAbsoluteFile();
		OutputStream outputStream = new FileOutputStream(file);
		m.saveOntology(o, outputStream);
		System.out.println("-- The modified ontology has been written to " + file.getAbsolutePath());
	}

}
