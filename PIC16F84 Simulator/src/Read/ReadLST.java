package Read;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class ReadLST{

    public File data;
    public Scanner scan;
    public String content="";

    public ReadLST(String path){

        this.data = new File(path);

    }

    public Scanner initializeScanner() throws FileNotFoundException{
        this.scan = new Scanner(this.data);
        return this.scan;
    }

    public String readFile(){
        while(this.scan.hasNextLine()){
            this.content = content.concat(this.scan.nextLine() + "\n");
        }

        return this.content;
    }






    
}
