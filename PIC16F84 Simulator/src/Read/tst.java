package Read;

import java.io.FileNotFoundException;

public class tst {

    public static void main(String[] args) throws FileNotFoundException{
        ReadLST list = new ReadLST("C:/Users/User/Documents/hi.odt");
        list.initializeScanner();
        String liste = list.readFile();
        System.out.println(liste);
    }
    
}
