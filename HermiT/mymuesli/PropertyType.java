/**
 * 
 */

/**
 * @author Schnitzi
 * 
 */
public enum PropertyType {
	NUTS("hasNuts"), FRUITS("hasFruits"), MUESLIBASE("hasBase"), EXTRAS("hasExtras"), BASEREFINE(
			"hasRefine");
	
	private String string;

	private PropertyType(String string) {
		this.setString(string);
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
