"""Author: Rajkumar Pandi"""

package check;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.util.Triple;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javafx.scene.control.Separator;
//import com.sun.java.util.jar.pack.Package.File;
import com.sun.javafx.collections.MappingChange.Map;


public class Test {

public static void main(String[] args) throws Exception {

    String serializedClassifier = "classifiers/english.muc.7class.distsim.crf.ser.gz";
    AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
    File[] files = new File("resources/").listFiles(); 
    for(File file : files){
    	if(file.isFile()){
    		 System.out.println("File: " + file.getName());
    	}
    String fileContents = IOUtils.slurpFile(file.getName());
    System.out.println("__________________");
    ArrayList<String> model = new ArrayList<String>();
    model.add("ORGANIZATION");model.add("PERSON");model.add("LOCATION");model.add("DATE");model.add("TIME");model.add("MONEY");
    model.add("PERCENT");
    /*****************/
    for (int q =0;q<model.size();q++){
    ArrayList<String> array_p = new ArrayList<String>();
    List<Triple<String, Integer, Integer>> list = classifier.classifyToCharacterOffsets(fileContents);
    for (Triple<String, Integer, Integer> item : list) {
    	String constr = new String(item.first());
    	if (constr.equals(model.get(q))){
    		array_p.add(fileContents.substring(item.second(), item.third()));
    		}
    }
    /****************/
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for(int i=0;i<array_p.size();i++){            
    Integer count = map.get(array_p.get(i));       
    map.put(array_p.get(i), count==null?1:count+1);
    }
    String model_name =model.get(q);
    System.out.println(model_name);
    /**************/
    String m_f = file.getName();
    checkcsv(map,model_name,m_f);
    }
   /*for (Entry<String, Integer> entry : map.entrySet()){
    	System.out.println(entry.getKey());
    }*/
    	}
}

public static void checkcsv(HashMap<String,Integer>m, String model_name,String m_f) throws IOException {
try{
	List<String> g =new ArrayList<String>();
    List<Integer> v =new ArrayList<Integer>(m.values());
    Collections.sort(v,Collections.reverseOrder());
    for (Entry<String, Integer> entry : m.entrySet()){
    	for(int j = 0; j < 10; j++){
    		if(entry.getValue() == v.get(j)){
    			g.add(entry.getKey());
    			//System.out.println(entry.getKey());
    		}
    	}
    }
    Set<String> uniqueGas = new HashSet<String>(g);
    System.out.println(uniqueGas);
    
    String res = String.join(",", uniqueGas);
	System.out.println(res);
    String output="/home/rajkumar/Top10entities";
    String in_file = m_f;
    FileWriter writer = new FileWriter(output,true);
    String [] y = res.split(",");
   // String [] o= in_file.split("N");
    //System.out.println(o[0]);
    //System.out.println(o[1]);
    writer.write("------------------------------------------------------------------\n");
    writer.write("Day"+":"+in_file+"<==>"+model_name+'\n');
    writer.write("-------------------------------------------------------------------\n");
    for(int u=0;u<y.length;u++)
    	writer.append(y[u]+'\n');
    writer.append("\n");
    writer.flush();
    writer.close();
 }catch(Exception e){
	System.out.println("[]");
 }

  }
 }
