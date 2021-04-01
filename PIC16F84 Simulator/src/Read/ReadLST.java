package Read;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.io.File;

public class ReadLST {

    public File data;
    public Scanner scan;
    public static int counter = 0;
    public ArrayList<String> liste = new ArrayList<String>();

    public ReadLST(String path) {

        this.data = new File(path);

    }

    public Scanner initializeScanner() throws FileNotFoundException {
        this.scan = new Scanner(this.data);
        return this.scan;
    }

    public ArrayList<String> readFile() {

        while (this.scan.hasNextLine()) {
            String line = scan.nextLine();
            liste.add(line);

        }

        return liste;
    }

    

}
