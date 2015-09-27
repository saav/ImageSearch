
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

/*path of the dataset, and the size of search result could be changed here*/


public class ImageSearch extends JFrame
                              implements ActionListener {
    JFileChooser fc;
	JPanel contentPane;

	int resultsize = 12;    //size of the searching result
	String datasetpath = "/Users/svarshney/Desktop/Semester 1/cs2108/Assignment1/ImageData/train"; //the path of image dataset
    //ColorHist colorhist = new ColorHist();
    //VisualConcept vc = new VisualConcept();
    //VisualKeywords vk = new VisualKeywords();
    //CombineFeatures cf = new CombineFeatures(0.2,0.1,0.4,0.3);
	CombineFeatures cf ;
    int flag[] = new int[4];
    double coff[] = new double[4];
    JButton openButton, searchButton;
    JCheckBox check[] = new JCheckBox[4];
    JTextField box[] = new JTextField[4];
    
	BufferedImage bufferedimage;
    
	JLabel [] imageLabels = new JLabel [ resultsize ];
	
	File file = null;

    public ImageSearch() {
    	for(int i=0; i<4; i++){
    		flag[i] = 0;
    	}
    	for(int i=0; i<4; i++){
    		coff[i] = 0.25;
    	}
        
        openButton = new JButton("Select an image...",
                createImageIcon("images/Open16.gif"));
        openButton.addActionListener(this);
        
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(searchButton);
        
        JPanel selectPanel = new JPanel();
        selectPanel.setBounds(5, 25, 800, 40);
        selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.X_AXIS));
        //selectPanel.setLayout(new GridLayout(0,8));
        check[0] = new JCheckBox("Color Histogram");
        box[0] = new JTextField();
        box[0].setPreferredSize(new Dimension(5,5));
        box[0].setEditable(true);
        selectPanel.add(check[0]);
        selectPanel.add(box[0]);
        check[0].addActionListener(this);
        box[0].addActionListener(this);
        
        check[1] = new JCheckBox("Text Matching");
        box[1] = new JTextField();
        box[1].setPreferredSize(new Dimension(5,5));
        box[1].setEditable(true);
        selectPanel.add(check[1]);
        selectPanel.add(box[1]);
        check[1].addActionListener(this);
        box[1].addActionListener(this);
        
        check[2] = new JCheckBox("Visual Concept");
        box[2] = new JTextField();
        box[2].setPreferredSize(new Dimension(5,5));
        box[2].setEditable(true);
        selectPanel.add(check[2]);
        selectPanel.add(box[2]);
        check[2].addActionListener(this);
        box[2].addActionListener(this);
        
        check[3] = new JCheckBox("Visual Keyword");
        box[3] = new JTextField();
        box[0].setPreferredSize(new Dimension(5,5));
        box[3].setEditable(true);
        selectPanel.add(check[3]);
        selectPanel.add(box[3]);
        check[3].addActionListener(this);
        box[3].addActionListener(this);
		
    	JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(0,3));
        
        for (int i = 0; i<imageLabels.length;i++){
        	imageLabels[i] = new JLabel();
        	imagePanel.add(imageLabels[i]);
        }
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(0,1));
        topPanel.add(buttonPanel);
        topPanel.add(selectPanel);
        
		contentPane = (JPanel)this.getContentPane();
		setSize(800,900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane.add(topPanel, BorderLayout.PAGE_START);
        //contentPane.add(selectPanel,BorderLayout.SOUTH );
        contentPane.add(imagePanel,BorderLayout.CENTER);
        
        contentPane.setVisible(true);
		setVisible(true);
//        add(logScrollPane, BorderLayout.CENTER);
        
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ImageSearch.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == check[0]){
    		flag[0] = (flag[0] + 1) % 2;
    	}
    	if(e.getSource() == check[1]){
    		flag[1] = (flag[1] + 1) % 2;
    	}
    	if(e.getSource() == check[2]){
    		flag[2] = (flag[2] + 1) % 2;
    	}
    	if(e.getSource() == check[3]){
    		flag[3] = (flag[3] + 1) % 2;
    	}
    	
        //Set up the file chooser.
        if (e.getSource() == openButton) {
        if (fc == null) {
            fc = new JFileChooser();

	    //Add a custom file filter and disable the default
	    //(Accept All) file filter.
            fc.addChoosableFileFilter(new ImageFilter());
            fc.setAcceptAllFileFilterUsed(false);

	    //Add custom icons for file types.
            fc.setFileView(new ImageFileView());

	    //Add the preview pane.
            fc.setAccessory(new ImagePreview(fc));
        } 
        

        //Show it.
        int returnVal = fc.showDialog(ImageSearch.this,
                                      "Select an image..");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();

        }

        fc.setSelectedFile(null);
        }else if (e.getSource() == searchButton) {
        	
        	for(int i=0; i<4; i++){
        		if(flag[i] == 0)
        			coff[i] = 0;
        		else
        			coff[i] = Double.parseDouble(box[i].getText());
        	}
        	System.out.println(coff[0]+ " " +coff[1]+" "+coff[2]+" "+coff[3]);
        	cf = new CombineFeatures(coff[0], coff[1], coff[2], coff[3]);
        	
        	//contentPane.remove();
        	try {
				bufferedimage = ImageIO.read(file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	BufferedImage [] imgs = null;
			try {
				//imgs = colorhist.search (datasetpath, bufferedimage, resultsize);
				cf.retriveResult(file.getAbsolutePath(), 20);
				cf.computeResult();
				imgs = cf.generateImageList(resultsize);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
			for(int i = 0; i<imageLabels.length;i++)
				imageLabels[i].setIcon(new ImageIcon(imgs[i]));
        	
        }
    }

    public static void main(String[] args) {
    	
		ImageSearch example = new ImageSearch();
    }
}
