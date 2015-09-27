import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class TextMatching {
	private static final String nameFile = "listFile.txt";
	private static final String tagFile = "id_tags.txt";
	HashMap<String, ArrayList<String>> store = new HashMap();
	ArrayList<String> idList = new ArrayList<String>();
	
	private static final String tagFileTest = "test-id_tags.txt";
	HashMap<String, ArrayList<String>> storeTest = new HashMap();
	ArrayList<String> idListTest = new ArrayList<String>();
	private static ArrayList<String> names;
	
	public TextMatching(){
		initDatabase();
		initTest();
		names = new ArrayList<String>();
	}
	
	public void initDatabase(){
		try(BufferedReader br = new BufferedReader(new FileReader(new File(tagFile)))){
			String line;
			while((line = br.readLine()) != null){
				String[] arr = line.split("\\s+");
				ArrayList<String> tagList = new ArrayList(Arrays.asList(arr));
				String id = tagList.remove(0);
				store.put(id, tagList);
		        idList.add(id);
			}
			br.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void initTest(){
		try(BufferedReader br = new BufferedReader(new FileReader(new File(tagFileTest)))){
			String line;
			while((line = br.readLine()) != null){
				String[] arr = line.split("\\s+");
				ArrayList<String> tagList = new ArrayList(Arrays.asList(arr));
				String id = tagList.remove(0);
				storeTest.put(id, tagList);
		        idListTest.add(id);
			}
			br.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public datafile[] search(String filename){
		File file = new File(filename);
		String name = file.getName();
		//System.out.println(name);
		
		File imageNames = new File(nameFile);
		try{
			extractFileNames(imageNames,names);
		} catch(Exception e){
			e.printStackTrace();
		}
		datafile[] similarity = new datafile[1250];
		ArrayList<String> query;
		ArrayList<String> document;
		double value = 0;
		if( storeTest.get(name).size() == 0){
			//System.out.println(name + " has no tags");
			for(int index = 0; index<1250; index++){
				similarity[index] = new datafile(names.get(index),0.0);
			}
		}else{
			query = storeTest.get(name);
			//System.out.println(query);
			for(int index = 0; index<1250; index++){
				document = store.get(idList.get(index));			
				value = calculateDistance(query, document);
				similarity[index] = new datafile(names.get(index),value);
			}
		}
		
		System.out.println("done");
		return similarity;
	}
	
	public static double calculateDistance(ArrayList<String> query, ArrayList<String> doc){
		// dot-product distance and probability
		if(doc.size() == 0){
			return 0.0;
		}
		double distance = 0;
		double count = 0;
		String word;
		for(int i=0; i<query.size(); i++){
			word = query.get(i);
			if(doc.contains(word))
				count++;
		}
		distance = count/doc.size();
		return distance;
	}
	
	private static void extractFileNames(File imageName, ArrayList<String> names)
			throws IOException, FileNotFoundException {
		try (BufferedReader br = new BufferedReader(new FileReader(imageName))) {
		    String line;
		    
		    while ((line = br.readLine()) != null) {
		    	names.add(line);
		    }
		}
	}
}
