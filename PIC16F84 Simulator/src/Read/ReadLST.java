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

    public ReadLST(String path) { //Constructor with File-Path

        this.data = new File(path);

    }

    public Scanner initializeScanner() throws FileNotFoundException { //setting up the File Scanner with fitting File
        this.scan = new Scanner(this.data);
        return this.scan;
    }

    public ArrayList<String> readFile() { //Read the File and save in new String ArrayList, return new list

        while (this.scan.hasNextLine()) {
            String line = scan.nextLine();
            liste.add(line);

        }

        return liste;
    }

    public ArrayList<Integer> parseHex() { //Parsing Hex Codes in new Integer ArrayList, return new list

        ArrayList<Integer> hexlist = new ArrayList<Integer>();

        for (int i = 0; i < liste.size(); i++) {

            hexlist.add(i, Integer.parseInt(liste.get(i), 16)); 

        }

        return hexlist;
    }

}
