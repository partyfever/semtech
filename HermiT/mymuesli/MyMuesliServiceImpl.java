import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 * 
 */

/**
 * @author stefan
 * 
 */
public class MyMuesliServiceImpl implements MyMuesliService {

	private OWLReasoner reasoner;
	private OWLOntology ontology;
	private OWLDataFactory dataFactory;
	private PrefixManager prefixManager;
	private OWLOntologyManager manager;
	private IRI ontoIri;

	@Override
	public List<String> getAllMueslies() throws Exception {
		System.out.println("Müslis: ");
		List<String> mueslies = new LinkedList<String>();
		if (reasoner != null) {
			for (OWLClass clazz : ontology.getClassesInSignature()) {
				if (clazz.getIRI().getFragment().equals("Müsli")) {
					NodeSet<OWLClass> muesliSubClasses = reasoner
							.getSubClasses(clazz, true);
					for (Node<OWLClass> node : muesliSubClasses) {
						Set<OWLIndividual> indiv = node
								.getRepresentativeElement().getIndividuals(
										ontology);
						for (OWLIndividual owlNamedIndividual : indiv) {
							System.out.println("\t"
									+ owlNamedIndividual.asOWLNamedIndividual()
											.getIRI().getFragment());
							mueslies.add(owlNamedIndividual
									.asOWLNamedIndividual().getIRI()
									.getFragment());
						}
					}
				}
			}
		} else
			throw new Exception(
					"No reasoner started! Please call start reasoner first!");
		return mueslies;
	}

	@Override
	public List<String> getAllMuesliClassesNames() throws Exception {
		List<String> muesliClasses = new LinkedList<String>();
		if (reasoner != null) {
			for (OWLClass classes : ontology.getClassesInSignature()) {
				if (classes.getIRI().getFragment().equals("MÃ¼sli")) {
					NodeSet<OWLClass> muesliSubClasses = reasoner
							.getSubClasses(classes, true);
					for (Node<OWLClass> node : muesliSubClasses) {
						muesliClasses.add(node.getRepresentativeElement()
								.getIRI().getFragment());
						// System.out.println("Class: " +
						// node.getRepresentativeElement().getIRI().getFragment());
					}
				}
			}
		} else
			throw new Exception(
					"No reasoner started! Please call start reasoner first!");
		return muesliClasses;
	}

	@Override
	public List<OWLClass> getAllMuesliClasses() throws Exception {
		List<OWLClass> muesliClasses = new LinkedList<OWLClass>();
		if (reasoner != null) {
			for (OWLClass classes : ontology.getClassesInSignature()) {
				if (classes.getIRI().getFragment().equals("MÃ¼sli")) {
					NodeSet<OWLClass> muesliSubClasses = reasoner
							.getSubClasses(classes, true);
					for (Node<OWLClass> node : muesliSubClasses) {
						// System.out.println("Class: " +
						// node.getRepresentativeElement().getIRI().getFragment());
						muesliClasses.add(node.getRepresentativeElement());
					}
				}
			}
		} else
			throw new Exception(
					"No reasoner started! Please call start reasoner first!");
		return muesliClasses;
	}

	@Override
	public List<String> getAllMuesliesFromClass(String muesliClassName)
			throws Exception {
		List<OWLClass> classes = getAllMuesliClasses();
		List<String> indivClass = new ArrayList<String>();
		for (OWLClass muesliClass : classes) {
			if (muesliClass.getIRI().getFragment().equals(muesliClassName)) {
				Set<OWLIndividual> indiv = muesliClass.getIndividuals(ontology);
				for (OWLIndividual owlNamedIndividual : indiv) {
					System.out.println("Muesli: "
							+ owlNamedIndividual.asOWLNamedIndividual()
									.getIRI().getFragment());
					indivClass.add(owlNamedIndividual.asOWLNamedIndividual()
							.getIRI().getFragment());
				}
			}

		}
		return indivClass;
	}

	@Override
	public List<OWLClass> getAllIngredientsClasses() throws Exception {
		List<OWLClass> ingredientsClasses = new LinkedList<OWLClass>();
		if (reasoner != null) {
			for (OWLClass classes : ontology.getClassesInSignature()) {
				if (classes.getIRI().getFragment().equals("Mixer")) {
					NodeSet<OWLClass> ingredientsSubClasses = reasoner
							.getSubClasses(classes, true);
					for (Node<OWLClass> node : ingredientsSubClasses) {
						ingredientsClasses.add(node.getRepresentativeElement());
						// System.out.println("Class: "
						// + node.getRepresentativeElement().getIRI()
						// .getFragment());
					}
				}
			}
		} else
			throw new Exception(
					"No reasoner started! Please call start reasoner first!");
		return ingredientsClasses;
	}

	@Override
	public OWLObjectProperty getIngredientsClass(PropertyType ingredientClass)
			throws Exception {
		if (reasoner != null) {
			for (OWLObjectProperty classes : ontology
					.getObjectPropertiesInSignature()) {
				if (classes.getIRI().getFragment().toString().toUpperCase()
						.equals(ingredientClass.getString().toUpperCase())) {
					return classes;
				}
			}
			throw new Exception("Class not found! Only PropertyTypes allowed!");
		} else
			throw new Exception(
					"No reasoner started! Please call start reasoner first!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MyMuesliService#getAllIngredients()
	 */
	@Override
	public List<String> getAllIngredients() throws Exception {
		List<String> ingredients = new LinkedList<String>();
		if (reasoner != null) {
			for (OWLClass classes : ontology.getClassesInSignature()) {
				if (classes.getIRI().getFragment().toString().equals("Mixer")) {
					System.out.println("IngredientClass: "
							+ classes.getIRI().getFragment());
					Set<OWLIndividual> indivClass = classes
							.getIndividuals(ontology);
					// for (OWLIndividual owlNamedIndividual : indivClass) {
					// System.out.println("\tIngredient: "
					// + owlNamedIndividual.asOWLNamedIndividual()
					// .getIRI().getFragment());
					// }
					// mueslies.add(classes.getIRI().getFragment().toString());
					// printDirectSubclassesOf("Muesli", reasoner, ontology);
				}
			}
		} else
			throw new Exception(
					"No reasoner started! Please call start reasoner first!");
		return ingredients;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MyMuesliService#getAllIngredients()
	 */
	@Override
	public List<String> getAllIngredientsFromClass(String ingredientClassName)
			throws Exception {
		List<OWLClass> ingredientClasses = getAllIngredientsClasses();
		List<String> indivClass = new ArrayList<String>();
		System.out.println("IngredientClass: " + ingredientClassName);
		for (OWLClass muesliClass : ingredientClasses) {
			if (muesliClass.getIRI().getFragment().equals(ingredientClassName)) {
				Set<OWLIndividual> indiv = muesliClass.getIndividuals(ontology);
				for (OWLIndividual owlNamedIndividual : indiv) {
					System.out.println("\tIngredient: "
							+ owlNamedIndividual.asOWLNamedIndividual()
									.getIRI().getFragment());
					indivClass.add(owlNamedIndividual.asOWLNamedIndividual()
							.getIRI().getFragment());
				}
			}

		}
		return indivClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MyMuesliService#getAllIngredients()
	 */
	@Override
	public Map<String, List<String>> getAllIngredientsFromMuesli(
			String muesliName) throws Exception {

		Map<String, List<String>> ingredientsMap = new HashMap();
		Set<OWLNamedIndividual> individualsInSignature = ontology
				.getIndividualsInSignature();
		for (OWLNamedIndividual owlNamedIndividual : individualsInSignature) {

			if (owlNamedIndividual.getIRI().getFragment().equals(muesliName)) {

				Map<OWLObjectPropertyExpression, Set<OWLIndividual>> dataPropertiesInSignature = owlNamedIndividual
						.getObjectPropertyValues(ontology);

				for (OWLObjectPropertyExpression objProp : dataPropertiesInSignature
						.keySet()) {

					List<String> indis = new LinkedList<String>();

					System.out.println(objProp.asOWLObjectProperty().getIRI()
							.getFragment());

					for (OWLIndividual indi : dataPropertiesInSignature
							.get(objProp)) {

						System.out.println("\tIndi: "
								+ indi.asOWLNamedIndividual().getIRI()
										.getFragment());
						System.out
								.println(indi.asOWLNamedIndividual().getIRI());
						indis.add(indi.asOWLNamedIndividual().getIRI()
								.getFragment());
					}

					ingredientsMap.put(objProp.asOWLObjectProperty().getIRI()
							.getFragment(), indis);

				}
			}
		}

		return ingredientsMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MyMuesliService#addNewIngredient(java.lang.String, IngreType)
	 */
	@Override
	public void addNewIngredient(String ingredient, IngreType ingreType) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MyMuesliService#addNewMuesli(java.lang.String, java.util.List)
	 */
	@Override
	public void addNewMuesli(String muesliName,
			Map<PropertyType, List<String>> ingredients) throws Exception {
		System.out.println("1");
		if (reasoner != null) {
			OWLNamedIndividual owlNamedIndividual = dataFactory
					.getOWLNamedIndividual(IRI.create(ontoIri + "#"
							+ muesliName));
			System.out.println("2"+owlNamedIndividual);
			for (PropertyType propType : ingredients.keySet()) {

				OWLObjectProperty ingredientsClass = getIngredientsClass(propType);
				System.out.println("3"+ingredientsClass);
				for (String ingri : ingredients.get(propType)) {

					OWLNamedIndividual ingridientIndividual = getIngridientIndividual(ingri);
					OWLObjectPropertyAssertionAxiom owlObjectPropertyAssertionAxiom = dataFactory
							.getOWLObjectPropertyAssertionAxiom(
									ingredientsClass, owlNamedIndividual,
									ingridientIndividual);

					AddAxiom addAxiom = new AddAxiom(ontology,
							owlObjectPropertyAssertionAxiom);
					manager.applyChange(addAxiom);
				}
			}
			manager.saveOntology(ontology);

		} else
			throw new Exception(
					"No reasoner started! Please call start reasoner first!");
	}

	private OWLNamedIndividual getIngridientIndividual(String ingri)
			throws Exception {
		Set<OWLNamedIndividual> individualsInSignature = ontology
				.getIndividualsInSignature();

		for (OWLNamedIndividual owlNamedIndividual : individualsInSignature) {
			if (owlNamedIndividual.getIRI().getFragment().equals(ingri)) {
				return owlNamedIndividual;
			}
		}
		throw new Exception("Individual not found!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see MyMuesliService#addIngredientToMuesli(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addIngredientToMuesli(String muesliName, String ingredient) {

	}

	@Override
	public void startReasoner() {
		ontoIri = IRI.create("http://www.mymuesli.com/ontologies/mymuesli.owl");
		manager = OWLManager.createOWLOntologyManager();
		dataFactory = OWLManager.getOWLDataFactory();
		prefixManager = new DefaultPrefixManager(
				"http://www.mymuesli.com/ontologies/mymuesli.owl");

		ontology = null;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File(
					"mymuesli/mymuesli_new.owl"));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		// Uebergabe der Ontologie an den HermiT Reasoner
		ReasonerFactory factory = new ReasonerFactory();
		Configuration c = new Configuration();
		reasoner = factory.createReasoner(ontology, c);
	}

	@Override
	public Map<String, List<String>> getAllIngredientsOfEachClasses()
			throws Exception {
		return null;
	}

	@Override
	public List<String> getAllIngredientsClassesName() throws Exception {
		return null;
	}

	@Override
	public List<String> getMuesliesWithIngredient(String ingredient)
			throws Exception {
		return null;
	}

}
