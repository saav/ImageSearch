import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class VisualKeywords {
	private static final String testDataFile = "visual_words_for_test_data";
	private static final String programFile = "generate.py";
	private static final String codeBook = "PretrainedCodebook/visual_words_for_training_data";
	private static final String nameFile = "listFile.txt";
	private static double[] testData;
	private static double[][] pretrainedData;
	private static double[] distances;
	private static ArrayList<String> names;
	public VisualKeywords(){
		init();
	}
	private static void init(){
		pretrainedData = new double[1250][593];
		testData = new double[593];
		distances = new double[1250];
		names = new ArrayList<String>(); 
		readInputTodatabase();
	}
	private static void readInputTodatabase(){
		File codebook = new File(codeBook);
		int index = 0;
		
		try (BufferedReader br = new BufferedReader(new FileReader(codebook))) {
		    String line;
		    
		    while ((line = br.readLine()) != null) {
		    	line = line.replace(":", " ");
		    	line = line.trim();
		    	String[] rawData = line.split(" ");
		    	//System.out.println(rawData.length);
		    	for(int i = 0; i<rawData.length;i++){
		    		//System.out.println(rawData[i]);
		    		if(i%2!=0){
		    			pretrainedData[index][i/2] = Double.valueOf(rawData[i]);
		    		}
		    	}
		    }
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	private static void readTestOutput(){
		File codebook = new File(testDataFile);

		
		try (BufferedReader br = new BufferedReader(new FileReader(codebook))) {
		    String line;
		    
		    while ((line = br.readLine()) != null) {
		    	line = line.replace(":", " ");
		    	String[] rawData = line.split(" ");
		    	for(int i = 1; i<rawData.length;i+=2){
		    		
		    			testData[i/2] = Double.valueOf(rawData[i]);
		    	}
		    }
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public datafile[] search(String filename){
		executeCommand(filename);
		readTestOutput();
		ArrayList<String> result = new ArrayList<String>();
		File imageNames = new File(nameFile);
		try{
			extractFileNames(imageNames,names);
		} catch(Exception e){
			e.printStackTrace();
		}
		datafile[] similarity = new datafile[1250];
		for(int index = 0; index<distances.length;index++){
			similarity[index] = new datafile(names.get(index),1-calculateDistance(testData,pretrainedData[index]));
		}
		System.out.println("done");
		return similarity;
	}
	private static void executeCommand(String filename){
		String cmd = "python "+programFile+" -c "+codeBook+" "+filename;
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static double calculateDistance(double[] array1, double[] array2)
    {
		// Euclidean distance
        double Sum = 0.0;
        for(int i = 0; i < array1.length; i++) {
           Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        return Math.sqrt(Sum);
        
        
        // Bhattacharyya distance
		/*double h1 = 0.0;
		double h2 = 0.0;
		int N = array1.length;
        for(int i = 0; i < N; i++) {
        	h1 = h1 + array1[i];
        	h2 = h2 + array2[i];
        }

        double Sum = 0.0;
        for(int i = 0; i < N; i++) {
           Sum = Sum + Math.sqrt(array1[i]*array2[i]);
        }
        double dist = Math.sqrt( 1 - Sum / Math.sqrt(h1*h2));
        return dist; */
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
