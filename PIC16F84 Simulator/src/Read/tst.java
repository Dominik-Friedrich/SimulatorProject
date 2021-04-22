package Read;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class tst {

    public static void main(String[] args) throws FileNotFoundException{
        ReadLST list = new ReadLST("C:/Users/User/Downloads/TPicSim_20200520_1/TPicSim1.LST");
        list.initializeScanner();
        ArrayList<String> liste = new ArrayList<String>();
        //liste = list.readFile();
        System.out.println(liste);
    
    }
    
}
