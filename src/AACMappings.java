import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import util.AssociativeArray;
import java.util.Scanner;
import util.KeyNotFoundException;
import util.NullKeyException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * @author Catie Baker & jana Vadillo
 *
 */
public class AACMappings implements AACPage {

	AssociativeArray<String, AACCategory> Mappings;
	String currentCategory;

	AACCategory holder = new AACCategory("holder");

	AACCategory CurrentAAC = holder;

	/**
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a
	 * collared shirt
	 * 
	 * @param filename the name of the file that stores the mapping information
	 */
	public AACMappings(String filename) {
		this.Mappings = new AssociativeArray<String, AACCategory>();
		this.currentCategory = "";
		try {
			Scanner scanner = new Scanner(new File(filename));

			while (scanner.hasNextLine()) {
				String Line = scanner.nextLine();
				String dividedLine[] = Line.split(" ", 2);

				if (!Line.startsWith(">")) {
					this.CurrentAAC = new AACCategory(dividedLine[1]);
					try {
						this.Mappings.set(dividedLine[0], this.CurrentAAC);
					} catch (Exception e) {
						System.err.println("FAILED because " + e.toString());
					} // try/catch
				} else {
					this.CurrentAAC.addItem(dividedLine[0].substring(1), dividedLine[1]);
				}

			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Given the image location selected, it determines the action to be
	 * taken. This can be updating the information that should be displayed
	 * or returning text to be spoken. If the image provided is a category, 
	 * it updates the AAC's current category to be the category associated 
	 * with that image and returns the empty string. If the AAC is currently
	 * in a category and the image provided is in that category, it returns
	 * the text to be spoken.
	 * @param imageLoc the location where the image is stored
	 * @return if there is text to be spoken, it returns that information, otherwise
	 * it returns the empty string
	 * @throws NoSuchElementException if the image provided is not in the current 
	 * category
	 */
	public String select(String imageLoc) throws NoSuchElementException{

		if (this.currentCategory == ""){
			try {
				this.CurrentAAC = this.Mappings.get(imageLoc);
			} catch (KeyNotFoundException e) {
				throw new NoSuchElementException("No Such Element in home directory");
			} // try to get image, throwing false if not found
			this.currentCategory = imageLoc;
			return("");
		}

		else{
			return this.CurrentAAC.select(imageLoc);

		}
	}

	/**
	 * Provides an array of all the images in the current category
	 * 
	 * @return the array of images in the current category; if there are no images,
	 *         it should return an empty array
	 */
	public String[] getImageLocs() {
		String imageLocArray[];
		imageLocArray = new String[this.Mappings.size()];

		for (int i = 0; i < this.Mappings.size(); i++) {
			try {
				imageLocArray[i] = this.Mappings.getKey(i);
			} catch (Exception e) {
				System.err.println("FAILED because " + e.toString());
			}// try catch to get the elements for each value
		}// itterate through all possible Image Locs

		return imageLocArray;
	}

	/**
	 * Resets the current category of the AAC back to the default
	 * category
	 */
	public void reset() {
		this.currentCategory = "";
		this.CurrentAAC = holder;
	}

	/**
	 * Writes the ACC mappings stored to a file. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a
	 * collared shirt
	 * 
	 * @param filename the name of the file to write the
	 *                 AAC mapping to
	 */
	public void writeToFile(String filename) {
		try {
            FileWriter fWriter = new FileWriter(filename);
			AACCategory AddingCategory;
			for (int i =0;  i< this.Mappings.size(); i++){
				String CategoryImgLoc = this.Mappings.getKey(i);
				AddingCategory = this.Mappings.get(CategoryImgLoc);
				fWriter.write(CategoryImgLoc + " " + AddingCategory.getCategory() + "\n");

				String CategoryImgLocs[] = AddingCategory.getImageLocs();
				for (int n = 0; n < CategoryImgLocs.length; n++){
					fWriter.write(">" + CategoryImgLocs[n] + " " + AddingCategory.select(CategoryImgLocs[n]) + "\n");
				}
			}

			fWriter.close();
		} catch (Exception e) {
            // Print the exception
            System.out.print(e.getMessage());
        } 

	}

	/**
	 * Adds the mapping to the current category (or the default category if
	 * that is the current category)
	 * 
	 * @param imageLoc the location of the image
	 * @param text     the text associated with the image
	 */
	public void addItem(String imageLoc, String text) {
		if (this.currentCategory == ""){
			try{
			this.Mappings.set(imageLoc, new AACCategory(text));
			}
			catch (Exception e){
				System.err.println("FAILED because " + e.toString());

			}
		}
		else{
			this.CurrentAAC.addItem(imageLoc, text);
		}

	}

	/**
	 * Gets the name of the current category
	 * 
	 * @return returns the current category or the empty string if
	 *         on the default category
	 */
	public String getCategory() {
		return this.currentCategory;
	}

	/**
	 * Determines if the provided image is in the set of images that
	 * can be displayed and false otherwise
	 * 
	 * @param imageLoc the location of the category
	 * @return true if it is in the set of images that
	 *         can be displayed, false otherwise
	 */
	public boolean hasImage(String imageLoc) {
		try {
			this.Mappings.get(imageLoc);
		} catch (KeyNotFoundException e) {
			if (this.CurrentAAC.hasImage(imageLoc)){
				return true;
			}
			return false;
		} // try to get image, throwing false if not found
		return true;
	}
}
