package ie.gmit.sw;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Patrick Murray
 * @version 0.1
 * @since 1.8
 * 
 * The class Menu is initialised by the Runner class.
 * This class displays the menu to the user, user has 4
 * options to choose from and a relevant function is called 
 * upon afterwards
 */
public class Menu {
	/**
	 * Create Variables
	 */
	private Scanner console = new Scanner(System.in);
	private int option;
	private boolean keepRunning = true;
	/**
	 * Create the BlockingQueue of type BufferedImage.
	 * The BlockingQueue is capacity bounded to 5
	 */
	private BlockingQueue<BufferedImage> q = new ArrayBlockingQueue<>(5);

	/**
	 * Create an instance of FileParser
	 * 
	 * @param q - BlockingQueue
	 */
	private FileParser parser = new FileParser(q);
	/**
	 * Create an instance of Filter
	 * 
	 * @param q - BlockingQueue
	 * @param parser - Instance of FileParser
	 */
	private Filter filter = new Filter(q, parser);
	

	/**
	 * Menu constructor tries to call <b>init()</b> to set up menu.
	 * 
	 * @exception e - Print error
	 */
	public Menu() {
		try {
			init();
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	/**
	 * Initialises the Menu
	 * Menu displays 4 options and calls a
	 * function depending on user input
	 *  
	 *  @throws NumberFormatException
	 *  @throws IOException
	 */
	public void init() throws NumberFormatException, IOException {
		/** 
		 * The Menu is surrounded by a do/while which keeps running until
		 * <i>keepRunning</i> is set to false when option 4 is selected
		 * 
		 * if option 1 is selected perform addImage function,
		 * if option 2 id=s selected perform add filter function
		 * if 3 is selected leave the program
		 */
		
		do {
			System.out.println("***************************************************");
			System.out.println("* GMIT - Dept. Computer Science & Applied Physics *");
			System.out.println("*                                                 *");
			System.out.println("*    A Multithreaded Image Filtering System       *");
			System.out.println("*                                                 *");
			System.out.println("***************************************************");
			System.out.println("1) Enter Image Directory");
			System.out.println("2) Select Single Image");
			System.out.println("3) Choose your custom filter");
			System.out.println("4) Quit");
			System.out.println("Select Option [1-4]>");
			option = console.nextInt();
			switch(option) {
			/**
			 * Option 1 - Add a directory of images to <i>BlockingQueue</i>
			 */
			case 1:
				parser.addImages();
				break;
			/**
			* Option 2 - Add an image to <i>BlockingQueue</i>
			*/
			case 2:
				parser.addImage();
				break;
			/**
			* Option 3 - Choose a filter and then start the filter Thread
			*/
			case 3:
				filter.chooseFilter();
				new Thread(filter).start();
				break;
			/**
			 * Option 4 - Set <i>keepRunning</i> to false and exit
			 */
			case 4:
				System.out.println("See you again!");
				keepRunning = false;
				break;
			/**
			 * Invalid option entered
			 */
			default:
				System.out.println("Invalid option entered!");
				break;
			}
		} while (keepRunning == true);
	}
}


