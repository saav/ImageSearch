import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

public class InvertedFileGenerator {
	
	public static void main(String args[])throws IOException{
		String datapath = "train_tags.txt";
		BufferedReader br = new BufferedReader(new FileReader(new File(datapath)));
		PrintWriter out = new PrintWriter(new FileOutputStream("tags.txt", false));
		HashMap<String, ArrayList<String>> store = new HashMap();
		ArrayList<String> taglist = new ArrayList<String>();
		String tags;
		String line;
		while((line = br.readLine()) != null){
			String[] arr = line.split("\\s+");
			String id = arr[0];
			for(int i=1; i<arr.length; i++) {
		          tags = arr[i];
		          ArrayList<String> list = new ArrayList<String>();
		          if(store.containsKey(tags)){
		        	 list = store.get(tags);
		        	 list.add(id);
		          }else{
		        	  list.add(id);
		          }
		          store.put(tags, list);
		          taglist.add(tags);
		    }
		}
		
		for(int i = 0; i<taglist.size(); i++ ){
			String tag = taglist.get(i);
			out.print(tag + " ");
			ArrayList<String> idlist = store.get(tag);
			for(int j=0; j<idlist.size(); j++){
				String id = idlist.get(j);
				out.print(id + " ");
			}
			out.println();
		}
		
		br.close();
		out.close();
	}
	
}