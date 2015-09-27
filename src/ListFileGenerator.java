import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
public class ListFileGenerator {

	public ListFileGenerator(){
		
	}
	public static void generateListFile(String datapath,String outputPath) throws IOException{
		File dir = new File(datapath);  //path of the dataset

		File listFile = new File(outputPath);
		//System.out.println(dir.exists());
		FileOutputStream out = new FileOutputStream(outputPath);
		Stack<File> s = new Stack<File>();
		int count = 0;
		s.push(dir);
	
		while (!s.isEmpty()){
			File f = s.pop();
			//System.out.println(f.exists());
			
			if(f.exists()){
				if(f.isDirectory()){
					File[] subDir = f.listFiles();
					for(int i = 0;i<subDir.length;i++){
						s.push(subDir[i]);
					}
					
				} else {
					System.out.println(f.getAbsolutePath());
					if(!Utils.getExtension(f).equals("sift")&&!Utils.getExtension(f).equals("txt")) {
						String path = f.getAbsolutePath()+"\n";
						byte[] data = path.getBytes();
						out.write(data);
						count++;
					}
				}
			}
		}
		out.close();
		System.out.println("List File Generation Finished.");
		System.out.println("Record "+count+" files at "+outputPath);
	}
	
	public static void main(String[] args){
		
		String datapath = "/Users/svarshney/Desktop/Semester 1/cs2108/Assignment1/ImageData/test/data";
		
		String outputPath = "/Users/svarshney/Desktop/Semester 1/cs2108/Assignment1/ImageData/test/listFile-test.txt";
		try{
			generateListFile(datapath,outputPath);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
