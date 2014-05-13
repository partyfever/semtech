import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * 
 */

/**
 * @author stefan
 * 
 */
public class MyMuesliController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyMuesliService service = new MyMuesliServiceImpl();
		service.startReasoner();
		try {

			List<String> muesliList = service.getAllMuesliClassesNames();
			for (String string : muesliList) {
				System.out.println("Müsli-Class: " + string);
			}

			List<String> allMueslies = service.getAllMueslies();
			for (String string : allMueslies) {
				System.out.println("INGREDIENTS OF " + string);
				Map<String, List<String>> allIngredientsFromMuesli = service
						.getAllIngredientsFromMuesli(string);
			}

			List<OWLClass> ingredientClasses = service
					.getAllIngredientsClasses();
			for (OWLClass owlClass : ingredientClasses) {
				service.getAllIngredientsFromClass(owlClass.getIRI()
						.getFragment());
			}
			
			Map<PropertyType, List<String>> ingriMap = new HashMap<PropertyType, List<String>>();
			List<String> ingriList = new LinkedList<String>();
			ingriList.add("Bircher_Deluxe");
			
			List<String> ingriList2 = new LinkedList<String>();
			ingriList2.add("Banana-Chocs");
			
			List<String> ingriList3 = new LinkedList<String>();
			ingriList3.add("Banane");
			
			ingriMap.put(PropertyType.MUESLIBASE, ingriList);
			ingriMap.put(PropertyType.EXTRAS, ingriList2);
			ingriMap.put(PropertyType.FRUITS, ingriList3);
			
			service.addNewMuesli("AwesomeMŸsli", ingriMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
