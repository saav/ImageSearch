import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class CombineFeatures {
	private double[][] featureScores;
	private double[] coefficients;
	private double[] combineScore;
	private datafile[] combinedResult;
	private String[] nameList;
	private static final String nameFile = "listFile.txt";
	private static ColorHist CH = new ColorHist();
	private static TextMatching TM = new TextMatching();
	private static VisualConcept VC = new VisualConcept();
	private static VisualKeywords VK = new VisualKeywords();
	public CombineFeatures(double a,double b,double c,double d){
		this.coefficients = new double[4];
		this.coefficients[0] = a;
		this.coefficients[1] = b;
		this.coefficients[2] = c;
		this.coefficients[3] = d;
		this.featureScores = new double[4][1250];
		this.combinedResult = new datafile[1250];
		this.nameList = new String[1250];
		this.combineScore = new double[1250];
		readnameList();
	}
	
	public void setCofficient(double a,double b,double c,double d){
		this.coefficients[0] = a;
		this.coefficients[1] = b;
		this.coefficients[2] = c;
		this.coefficients[3] = d;
	}
	
	private void readnameList(){
		try (BufferedReader br = new BufferedReader(new FileReader(nameFile))) {
		    String line;
		    int count = 0;
		    while ((line = br.readLine()) != null) {
		    	nameList[count] = line;
		    	count++;
		    }
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void retriveResult(String datapath,int datasize){
		try{
			datafile[] temp1 = CH.search(datapath);
			datafile[] temp2 = TM.search(datapath);
			datafile[] temp3 = VC.search(datapath);
			datafile[] temp4 = VK.search(datapath);
			for(int i = 0; i<1250;i++){
				this.featureScores[0][i] = temp1[i].getScore();
				//System.out.println("color Hist "+i+" "+this.featureScores[0][i]);
				this.featureScores[1][i] = temp2[i].getScore();
				//System.out.println("Text Match "+i+" "+this.featureScores[1][i]);
				this.featureScores[2][i] = temp3[i].getScore();
				//System.out.println("VC "+i+" "+this.featureScores[2][i]);
				this.featureScores[3][i] = temp4[i].getScore();
				//System.out.println("VK "+i+" "+this.featureScores[3][i]);
				
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void computeResult(){
		for(int i = 0; i<1250;i++){
			for(int j = 0; j<4; j++){
				this.combineScore[i] += this.coefficients[j]*this.featureScores[j][i]; 
			}
		}
	}
	public BufferedImage[] generateImageList(int resultsize){
		this.combinedResult = new datafile[1250];
		for(int i = 0;i<1250;i++){
			this.combinedResult[i] = new datafile(this.nameList[i],this.combineScore[i]);
		}
		Arrays.sort(this.combinedResult);
		BufferedImage[] images = new BufferedImage[resultsize];
		for(int j = 0; j<resultsize; j++){
			//if(this.combinedResult[j].getScore() == 0)
				//break;
			String name = this.combinedResult[j].getName();
			try{
				BufferedImage temp = ImageIO.read(new File(name));
				images[j] = temp;
			}catch(Exception e){
			}
			System.out.println(this.combinedResult[j].getName()+" "+this.combinedResult[j].getScore());
		}
		
		return images;
	}
}
