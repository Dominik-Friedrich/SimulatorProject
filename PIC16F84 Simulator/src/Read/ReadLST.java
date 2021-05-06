package Read;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.io.File;

public class ReadLST {

	public File data;
	public Scanner scan;
	public ArrayList<String> liste = new ArrayList<String>();

	/**
	 * 
	 * @param path of file to read
	 */
	public ReadLST(String path) {

		this.data = new File(path);
		try {
			initializeScanner();
			readFile();
		} catch (Exception e) {
			System.out.println("Error reading File");
		}
	}

	/**
	 * Setting up the File Scanner with fitting File
	 * @throws FileNotFoundException when File isn't found
	 */
	public void initializeScanner() throws FileNotFoundException { // 
		this.scan = new Scanner(this.data);
	}

	/**
	 * Read the File and save in new String ArrayList
	 */
	public void readFile() {

		while (this.scan.hasNextLine()) {
			String arr[];
			String line = scan.nextLine();
			if (!line.startsWith(" ")) {
				arr = line.split(" ");
				if (arr[1].length() == 4) {
					liste.add(arr[1]);
				}

			}
		}
	}

	/**
	 * Parsing Hex Codes in new Integer ArrayList
	 * @return new Integer ArrayList
	 */
	public ArrayList<Integer> parseHex() {

		ArrayList<Integer> hexlist = new ArrayList<Integer>();

		for (int i = 0; i < liste.size(); i++) {

			hexlist.add(i, Integer.parseInt(liste.get(i), 16));

		}
		return hexlist;
	}

}
