package Read;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class tst {

    public static void main(String[] args) throws FileNotFoundException{
        ReadLST list = new ReadLST("C:/Users/User/Documents/test.txt");
        list.initializeScanner();
        ArrayList<String> liste = new ArrayList<String>();
        liste = list.readFile();
        System.out.println(liste.get(10));
    }
    
}
