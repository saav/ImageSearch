import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
public class VisualConcept {
	private static ArrayList<ArrayList<Integer>> mapping;
	private static ArrayList<double[]> database1000d;
	private static ArrayList<double[]> database24d;
	private static final String datapath = "/Users/svarshney/Desktop/Semester 1/cs2108/Assignment1/ImageData/train/data";
	private static final String mappingpath = "VC Mappings.txt";
	public VisualConcept(){
		database1000d = new ArrayList<double[]>();
		database24d = new ArrayList<double[]>();
		mapping = new ArrayList<ArrayList<Integer>>();
		try{
			init();
			readMapping();
			reduceDimension();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	private static void init() throws FileNotFoundException{
		File dir = new File(datapath);  //path of the dataset
		Stack<File> s = new Stack<File>();
		
		s.push(dir);
		while (!s.isEmpty()){
			File f = s.pop();
			if(f.exists()){
				if(f.isDirectory()){
					File[] subDir = f.listFiles();
					for(int i = 0;i<subDir.length;i++){
						s.push(subDir[i]);
					}
				} else {
					//System.out.println(f.getAbsolutePath());
					if(Utils.getExtension(f).equals("txt")) {
						double[] array = new double[1000];
						try (BufferedReader br = new BufferedReader(new FileReader(f))) {
						    String line;
						   
						    while ((line = br.readLine()) != null) {
						       String[] rawData = line.split(" ");
						       
						       
						       for(int i = 0;i<rawData.length;i++){
						    	   array[i] = Double.valueOf(rawData[i]);
						    	  // System.out.println(array[i]);
						       }
						       database1000d.add(array);
						    }
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	private static void readMapping() throws IOException{
		File mappingFile = new File(mappingpath);
		try (BufferedReader br = new BufferedReader(new FileReader(mappingFile))) {
		    String line;
		    int count =0;
		    while ((line = br.readLine()) != null) {
		       String[] array = line.split(" ");
		       
		       mapping.add(new ArrayList<Integer>());
		       for(int i = 1; i<array.length;i++){
		    	   mapping.get(count).add(Integer.valueOf(array[i])-1);
		       }
		       count++;
		    }
		}
	}
	private static void reduceDimension(){
		for(int i = 0;i<database1000d.size();i++){
			database24d.add(new double[24]);
			for(int j = 0; j<24;j++){
				double max = -1000000;
				for(int k = 0; k<mapping.get(j).size();k++){
					if(database1000d.get(i)[mapping.get(j).get(k)]>max){
						max = database1000d.get(i)[mapping.get(j).get(k)];
					}
					database24d.get(i)[j] = max;
				}
			}
		}
	}
	private static void generateListFile(String datapath,String outputPath) throws FileNotFoundException{
		File dir = new File(datapath);  //path of the dataset

		File listFile = new File(outputPath);
		//System.out.println(dir.exists());
		FileOutputStream out = new FileOutputStream(outputPath);

		String path = dir.getAbsolutePath()+"\n";
		path = path.replace("\\", "\\\\");
		byte[] data = path.getBytes();
		try {
			out.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
	
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void runClassification(String listfilename) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("/Users/svarshney/Desktop/Semester 1/cs2108/Assignment1/FeatureExtractor/semanticFeature/image_classification.exe",listfilename);
		pb.directory(new File("/Users/svarshney/Desktop/Semester 1/cs2108/Assignment1/FeatureExtractor/semanticFeature"));
		Process process = pb.start();
		//System.out.println(process.exitValue());
		//System.out.println(file);
		try {
			//process.wait(1000);
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			int count =0;

			while ((line = br.readLine()) != null) {
			  System.out.println(line);
			  count++;
			  if(count>100){
				  process.destroy();
			  }
			}
		} catch (Exception e){
			process.destroy();
			System.out.println("Fail to run exe");
			e.printStackTrace();
		}
	}
	private static double[] generatescore(String file,String listfilename) throws IOException{
		double[] score = new double[1000];
		//System.out.println(listfilename);
		runClassification(listfilename);
		return extractFromTestFile(file, score);
	}
	
	private static double[] extractFromTestFile(String file, double[] score) throws FileNotFoundException, IOException {
		File result = new File(file);
		InputStream is = new FileInputStream(result);
	
		try (BufferedReader br = new BufferedReader(new FileReader(result))) {
		    String line;
		  
		    while ((line = br.readLine()) != null) {
		       String[] array = line.split(" ");
		       
		       mapping.add(new ArrayList<Integer>());
		       for(int i = 0; i<array.length;i++){
		    	   score[i] = Double.valueOf(array[i]);
		    	   //System.out.println(score[i]);
		       }
		       
		    }
		}
		
		return score;
	}
	public datafile[] search(String filename) throws FileNotFoundException, IOException{
		File file = new File(filename);
		datafile[] distances = new datafile[database1000d.size()];
		File imageName = new File("listFile.txt");
		ArrayList<String> names = new ArrayList<String>();
		//System.out.println("TEST"+file.getAbsolutePath());
		try{
			generateListFile(file.getAbsolutePath(),"testImage.txt");
		} catch(Exception e){
			e.printStackTrace();
		}
		extractFileNames(imageName, names);
		String fname = file.getAbsolutePath().replace(".jpg","");
		//System.out.println(fname);
		double[] result = generatescore(fname+".txt","/Users/svarshney/Desktop/Semester 1/cs2108/Assignment1/ImageSearch/testImage.txt");
		for(int i = 0;i<database1000d.size();i++){
			
			distances[i] = new datafile(names.get(i),1-calculateDistance(result,database1000d.get(i)));
		}
		System.out.println("Done");
		return distances;
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
	public static double calculateDistance(double[] array1, double[] array2)
    {
		// Euclidean distance
		double Sum = 0.0;
		double Sum1 = 0.0;
		double Sum2 = 0.0;
        for(int i = 0; i < array1.length; i++) {
           Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
           Sum1 += Math.pow(array1[i],2.0);
           Sum2 += Math.pow(array2[i], 2.0);
        }
        
        return Math.sqrt(Sum/Math.sqrt(Sum1*Sum2));
        
    }
}
class datafile implements Comparable<datafile>{
	private String filename;
	private double score;
	public datafile(String name, double score){
		this.filename = name;
		this.score = score;
	}
	public String getName(){
		return this.filename;
	}
	public double getScore() {
		return this.score;
	}
	@Override
    public int compareTo(datafile a){

		return new Double(a.score*100000-this.score*100000).intValue();
		
		
	}
}