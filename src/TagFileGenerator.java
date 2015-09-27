import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

public class TagFileGenerator {
	private static final String nameFile = "listFile.txt";
	
	public static void main(String args[])throws IOException{
		String datapath = "train_tags.txt";
		BufferedReader br1 = new BufferedReader(new FileReader(new File(nameFile)));
		BufferedReader br2 = new BufferedReader(new FileReader(new File(datapath)));
		PrintWriter out = new PrintWriter(new FileOutputStream("id_tags.txt", false));
		HashMap<String, ArrayList<String>> store = new HashMap();
		ArrayList<String> idlist = new ArrayList<String>();
		String tags;
		String line;
		while((line = br2.readLine()) != null){
			String[] arr = line.split("\\s+");
			String id = arr[0];
			for(int i=1; i<arr.length; i++) {
		          tags = arr[i];
		          ArrayList<String> list = new ArrayList<String>();
		          if(store.containsKey(id)){
		        	 list = store.get(id);
		        	 list.add(tags);
		          }else{
		        	  list.add(tags);
		          }
		          store.put(id, list);
		          idlist.add(id);
		    }
		}
		
		while((line = br1.readLine()) != null){
			File file = new File(line);
			String name = file.getName();
			if(idlist.contains(name)){
				String id = name;
				out.print(id + " ");
				ArrayList<String> taglist = store.get(id);
				Collections.sort(taglist);
				for(int j=0; j<taglist.size(); j++){
					String tag = taglist.get(j);
					out.print(tag + " ");
				}
				out.println();
			}else{
				out.println(name + " ");
			}
		}
		
		br1.close();
		br2.close();
		out.close();
	}
	
}