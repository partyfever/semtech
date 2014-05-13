import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public interface MyMuesliService {

	public List<String> getAllMuesliesFromClass(String muesliClassName)
			throws Exception;

	public List<String> getAllMuesliClassesNames() throws Exception;

	public List<OWLClass> getAllMuesliClasses() throws Exception;

	public List<String> getAllIngredients() throws Exception;

	public Map<String, List<String>> getAllIngredientsOfEachClasses()
			throws Exception;

	public void addNewIngredient(String ingredient, IngreType ingreType)
			throws Exception;

	public void addNewMuesli(String muesliName,
			Map<PropertyType, List<String>> ingredients) throws Exception;

	public void addIngredientToMuesli(String muesliName, String ingredient)
			throws Exception;

	public void startReasoner();

	public List<OWLClass> getAllIngredientsClasses() throws Exception;

	public List<String> getAllIngredientsClassesName() throws Exception;

	List<String> getAllIngredientsFromClass(String ingredientClassName)
			throws Exception;

	Map<String, List<String>> getAllIngredientsFromMuesli(String muesliName)
			throws Exception;

	List<String> getAllMueslies() throws Exception;

	List<String> getMuesliesWithIngredient(String ingredient) throws Exception;

	OWLObjectProperty getIngredientsClass(PropertyType ingredientClass) throws Exception;
}
