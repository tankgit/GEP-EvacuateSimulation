package Tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by tank on 4/19/16.
 */
public class ParseCSV {

    public static Vector<String[]> readCSV(File file)
    {
        Vector<String[]> dataList=new Vector<>();
        BufferedReader br=null;
        try{
            br=new BufferedReader(new FileReader(file));
            String line;
            while((line=br.readLine())!=null){
                dataList.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }
}
