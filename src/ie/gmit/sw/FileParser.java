package ie.gmit.sw;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import javax.imageio.ImageIO;

/**
 * @author Patrick Murray
 * @version 0.1
 * @since 1.8
 * 
 * The FileParser class handles all file input/output
 * for the application. 
 */
public class FileParser {
	/**
	 * Create variables
	 */
	private Scanner console = new Scanner(System.in);
	private BlockingQueue<BufferedImage> q;
	private String outDirPath, filename;
	private File dir, outDir;
	private BufferedImage img = null;
	private int i = 0;
	/**
	 * This String array <i>EXTENSIONS</i> contains all the supported image formats
	 */
	private final static String[] EXTENSIONS = new String[] { "jpg", "png"};
	
	/**
	 * The FileParser constructor sets up class and BlockingQueue
	 * 
	 * @param q - BlockingQueue passed by <b>Menu.java</b>
	 */
	public FileParser(BlockingQueue<BufferedImage> q) {
		super();
		this.q = q;
	}
	
	/**
	 * This FilenameFilter <i>IMAGE_FILTER</i> identifies images
	 * based on their extension and only accepts supported extensions
	 */
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };
    
    /**
     * This function asks the user for the directory path and also the
     * output directory path. It then checks if the directory is valid.
     * If it is valid, we iterate through each file and filter them by
     * the supported extensions and then add the image to the <i>BlockingQueue</i>
     */
	public synchronized void addImages() {
		Scanner console = new Scanner(System.in);
		
		System.out.println("Please enter Directory Path");
		String dirPath = console.nextLine();
		System.out.println("Please enter output Directory: ");
		outDirPath = console.nextLine();
		
		dir = new File(dirPath);
		outDir = new File(outDirPath);
		
		/**
		 * Check is <i>dir</i> a directory
		 */
		if (dir.isDirectory()) {
			for (final File f : dir.listFiles(IMAGE_FILTER)) {
				try {
					/**
					 * Read image and add to <i>q</i>
					 */
					img = ImageIO.read(f);
					q.put(img);
					System.out.println("Adding " + f.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/**
		 * Could not find Directory
		 */
		else {
			System.out.println("Directory not found!");
		}
		
		/**
		 * Check is <i>outDir</i> not a directory. If it is not, 
		 * create a directory.
		 */
		if(!outDir.isDirectory()) {
			boolean bool = outDir.mkdir();
			if(bool){
		         System.out.println("Directory created successfully");
		      }else{
		         System.out.println("Sorry couldn’t create specified directory");
		      }
		}
	}
	
	/**
     * This function asks the user for the image path and also the
     * output directory path. Then add the image to the <i>BlockingQueue</i>
     */
	public synchronized void addImage() {
		Scanner console = new Scanner(System.in);
		
		System.out.println("Please enter the Path to Image:");
		String imgPath = console.nextLine();
		System.out.println("Please enter output Directory: ");
		outDirPath = console.nextLine();

		filename = new String(imgPath);
		dir = new File(imgPath);
		
		/**
		 * Try to add image to <i>BlockingQueue</i>
		 */
		try {
			img = ImageIO.read(new File(filename));
			q.put(img);
			System.out.println("Added " + dir.getName() + " to filter");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function prompts user to select what to name to give to
	 * output image. The image is then saved to the directory user chose
	 * 
	 * @param img - BufferedImage to save to file
	 * @throws IOException
	 */
	public synchronized void saveImage(BufferedImage img) throws IOException {
		ImageIO.write(img, "jpg", new File(outDirPath + "/" + i + ".jpg"));
		i++;
	}
}
