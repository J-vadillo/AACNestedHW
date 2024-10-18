import java.util.NoSuchElementException;
import util.AssociativeArray;
import util.KeyNotFoundException;

/**
 * Represents the mappings for a single category of items that should
 * be displayed
 * 
 * @author Catie Baker &  Jana Vadillo
 *
 */
public class AACCategory implements AACPage {

	// +--------+------------------------------------------------------
	// | Fields |
	// +--------+
	/**
	 * the array being used to hold all of the values we need
	 */
	AssociativeArray<String, String> categoryItems;
	/**
	 * the name of the current category
	 */
	String categoryName;

	// +--------------+------------------------------------------------
	// | Constructors |
	// +--------------+

	/**
	 * Creates a new empty category with the given name
	 * 
	 * @param name the name of the category
	 */
	public AACCategory(String name) {
		this.categoryItems = new AssociativeArray<String, String>();
		this.categoryName = name;
	} // AACategory(String)

	// +------------------+--------------------------------------------
	// | Standard Methods |
	// +------------------+

	/**
	 * Adds the image location, text pairing to the category
	 * 
	 * @param imageLoc the location of the image
	 * @param text     the text that image should speak
	 */
	public void addItem(String imageLoc, String text) {
		try {
			this.categoryItems.set(imageLoc, text);
		} catch (Exception e) {
			System.err.println("FAILED because " + e.toString());
		} // try/catch

	}// addItem(str, str)

	/**
	 * Returns an array of all the images in the category
	 * 
	 * @return the array of image locations; if there are no images,
	 *         it should return an empty array
	 */
	public String[] getImageLocs() {
		String imageLocArray[];
		imageLocArray = new String[this.categoryItems.size()];

		for (int i = 0; i < this.categoryItems.size(); i++) {
			try {
				imageLocArray[i] = this.categoryItems.getKey(i);
			} catch (Exception e) {
			}// try catch to get the elements for each value
		}// itterate through all possible Image Locs

		return imageLocArray;
	}//getImageLocs();



	/**
	 * Returns the name of the category
	 * 
	 * @return the name of the category
	 */
	public String getCategory() {
		return this.categoryName;
	}//getCategory();

	/**
	 * Returns the text associated with the given image in this category
	 * 
	 * @param imageLoc the location of the image
	 * @return the text associated with the image
	 * @throws NoSuchElementException if the image provided is not in the current
	 *                                category
	 */
	public String select(String imageLoc) throws NoSuchElementException {
		String imgtext;
		try{
			 imgtext = this.categoryItems.get(imageLoc);
		} catch (KeyNotFoundException e){
			throw new NoSuchElementException("No such element");
		}// try to get image, throwing an exception if it does not exist
		return imgtext;
	}// select(String)

	/**
	 * Determines if the provided images is stored in the category
	 * 
	 * @param imageLoc the location of the category
	 * @return true if it is in the category, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		try{
			this.categoryItems.get(imageLoc);
	   } catch (KeyNotFoundException e){
		   return false;
	   } // try to get image, throwing false if not found
		return true;
	}//hasImage(String)
}
