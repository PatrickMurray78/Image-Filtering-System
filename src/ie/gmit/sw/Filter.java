package ie.gmit.sw;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import javax.imageio.ImageIO;

/**
 * @author Patrick Murray
 * @version 0.1
 * @since 1.8
 * 
 * The Filter class handles all filtering of images, this
 * includes choosing which filter the user wishes to use 
 * and also applying this filter on every image in the queue.
 * This class implements Runnable and implements the run method.
 */
public class Filter implements Runnable {
	/**
	 * Create Variables
	 */
	private Scanner console = new Scanner(System.in);
	private BlockingQueue<BufferedImage> q;
	private FileParser p;
	private String filename = null;
	private BufferedImage img, output;
	private float[][] filter;


	/**
	 * The Filter constructor sets up class and BlockingQueue.
	 * It also gets passed a FileParser instance which we use in 
	 * this class.
	 * 
	 * @param q - BlockingQueue
	 * @param parser - FileParser
	 */
	public Filter(BlockingQueue<BufferedImage> q, FileParser parser) {
		super();
		this.q = q;
		this.p = parser;
	}

	/**
	 * This function allows the user to select which filter they would like to
	 * use to filter their image(s)
	 * 
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public synchronized void chooseFilter() throws NumberFormatException, IOException {
		/**
		 * This filter menu displays the 11 filters the user can choose from
		 */
		System.out.println("Please select a filter: ");
		System.out.println("1) Identity filter");
		System.out.println("2) Edge detection filter");
		System.out.println("3) Edge detectionV2 filter");
		System.out.println("4) Laplacian filter");
		System.out.println("5) Sharpen filter");
		System.out.println("6) Vertical lines filter");
		System.out.println("7) Horizontal lines filter");
		System.out.println("8) Diagional 45 lines filter");
		System.out.println("9) Sobel horizontal filter");
		System.out.println("10) Sobel vertical filter");
		System.out.println("11) Box Blur");
		System.out.println("Select Option [1-11]>");
		int option = console.nextInt();
		
		/**
		 * Apply the correct filter depending on the user input. User is notified if they 
		 * enter an option which is not valid and they will be brought back to main menu.
		 */
		if (option == 1) {
			filter = new float[][] { { 0, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } };
		} else if (option == 2) {
			filter = new float[][] { { -1, -1, -1 }, { -1, 8, -1 }, { -1, -1, -1 } };
		} else if (option == 3) {
			filter = new float[][] { { -1, 0, 1 }, { 0, 0, 0 }, { -1, 0, 1 } };
		} else if (option == 4) {
			filter = new float[][] { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };
		} else if (option == 5) {
			filter = new float[][] { { 0, -1, 0 }, { -1, 5, -1 }, { 0, -1, 0 } };
		} else if (option == 6) {
			filter = new float[][] { { -1, 2, 1 }, { -1, 2, -1 }, { -1, 2, -1 } };
		} else if (option == 7) {
			filter = new float[][] { { -1, -1, -2 }, { 2, 2, 2 }, { -1, -1, -1 } };
		} else if (option == 8) {
			filter = new float[][] { { -1, -1, 2 }, { -1, 2, -1 }, { 2, -1, -1 } };
		} else if (option == 9) {
			filter = new float[][] { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
		} else if (option == 10) {
			filter = new float[][] { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
		} else if (option == 11) {
			filter = new float[][] { { (float) 0.111, (float) 0.111, (float) 0.111 },
				{ (float) 0.111, (float) 0.111, (float) 0.111 }, { (float) 0.111, (float) 0.111, (float) 0.111 } };
		}
		else {
			System.out.println("Invalid filter selected!");
			return;
		}
	}
	
	/**
	 * This function applies the filter to each image in the <i>BlockingQueue</i>
	 * the image is then saved to file. If the user tried to apply the filter without
	 * selecting an image or images to filter, they will be notified and returned
	 * to the main menu.
	 * 
	 * @throws IOException
	 */
	public synchronized void applyFilter(BufferedImage img) throws IOException {
			int width = img.getWidth();
			int height = img.getHeight();
			output = new BufferedImage(width, height, img.getType());
	
			/**
			 * Loop through the x and y pixels of image
			 */
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					float red = 0f, green = 0f, blue = 0f;
					/**
					 * Loop through the filter and apply it to image pixels
					 */
					for (int i = 0; i < filter.length; i++) {
						for (int j = 0; j < filter[i].length; j++) {
							/**
							 * Get surrounding pixels
							 */
							int xCoord = (x - filter.length / 2 + i + width) % width;
							int yCoord = (y - filter[i].length / 2 + j + height) % height;
	
							/**
							 * Split the pixels into their RGB
							 */
							int rgb = img.getRGB(xCoord, yCoord);
							int newRed = (rgb >> 16) & 0xff;
							int newGreen = (rgb >> 8) & 0xff;
							int newBlue = rgb & 0xff;
							
							/**
							 * The filter is applied to these RGB pixels and the result is added
							 * on to the red, green and blue totals
							 */
							red += (newRed * filter[i][j]);
							green += (newGreen * filter[i][j]);
							blue += (newBlue * filter[i][j]);
						}
					}
					int updatedRed, updatedGreen, updatedBlue;
					/**
					 * The minimum value is set to 0 and the maximum value is set to 255
					 */
					updatedRed = Math.min(Math.max((int) (red * 1), 0), 255);
					updatedGreen = Math.min(Math.max((int) (green * 1), 0), 255);
					updatedBlue = Math.min(Math.max((int) (blue * 1), 0), 255);
					/**
					 * We put these three variables red, green and blue together to create
					 * an RGB color and apply it to the image.
					 */
					output.setRGB(x, y, new Color(updatedRed, updatedGreen, updatedBlue).getRGB());
				}
			}
			/**
			 * Save image to file
			 */
			p.saveImage(output);
	}

	/**
	 * Called upon by <b>Menu.java</b>.
	 * Each image is removed from the <i>BlockingQueue</i>
	 * and passed as a parameter to <i>applyFilter</i>
	 */
	@Override
	public void run() {
		while(true) {
			try {
				img = q.take();
				applyFilter(img);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}

