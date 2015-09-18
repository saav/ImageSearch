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
		FileOutputStream out = new FileOutputStream(outputPath);
		Stack<File> s = new Stack<File>();
		int count = 0;
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
					if(!Utils.getExtension(f).equals("sift")) {
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
		
		String datapath = "E:\\study\\study2015sem1\\CS2108\\Assignment1\\ImageData\\train\\data";
		
		String outputPath = "E:\\study\\study2015sem1\\CS2108\\Assignment1\\ImageData\\train\\listFile.txt";
		try{
			generateListFile(datapath,outputPath);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
