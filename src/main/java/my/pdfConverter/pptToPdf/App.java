package my.pdfConverter.pptToPdf;

import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;
import com.aspose.slides.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App 
{
	private static JFileChooser fC;
	private static String selectedDir;
	private static int slide;
	private static File[] files;
	private static JProgressBar bar;
	private static JPanel otherPanel;
	
	public static JTextArea textArea;
	public static JTextArea pathArea;
	
	public static JFrame frame;
	
	
	
	
    public static void main( String[] args )
    {
    	
    	frame = new JFrame();
    	frame.setSize(600, 400);
    	frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    	
    	
    	JPanel container = new JPanel();
    	JPanel buttonPanel = new JPanel();
    	JPanel platPanel = new JPanel();
    	JPanel outputPanel = new JPanel();
    	otherPanel = new JPanel();
    	
    	
    	final JButton openButton = new JButton("Open File");
    	final JButton outButton = new JButton("Out");
    	
    	openButton.setEnabled(false);
    	
    	final JRadioButton pcButton = new JRadioButton("PC");
    	pcButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			textArea.append("PC Selected \n");
    		}
    	});
    	
    	final JRadioButton macButton = new JRadioButton("Mac");
    	macButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			textArea.append("Mac Selected \n");
    		}
    	});
    	
    	macButton.setSelected(true);
    	
    	ButtonGroup b1 = new ButtonGroup();
    	b1.add(pcButton);
    	b1.add(macButton);
    	
    	
    	buttonPanel.add(outButton);
    	buttonPanel.add(openButton);
    	buttonPanel.setBorder( new TitledBorder("Select Your Ouput File and Files to be Converted"));
    	
    	
    	platPanel.add(pcButton);
    	platPanel.add(macButton);
    	platPanel.setBorder( new TitledBorder("Select Your Platform - Performance Differences"));
    	
    	
    	//--- console ---
    	textArea = new JTextArea(5,30);
    	textArea.setLineWrap(true);
    	textArea.setWrapStyleWord(true);
    	//--- scroll pane for text Area ---
    	JScrollPane scrollPane = new JScrollPane(textArea);
    	textArea.setEditable(false);
    	DefaultCaret caret = (DefaultCaret)textArea.getCaret();
    	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    	
    	outputPanel.add(scrollPane);
    	outputPanel.setBorder( new TitledBorder("Console"));
    	
    	
    	JLabel path = new JLabel("Path:");
    	
    	
    	
    	pathArea = new JTextArea(1,30);
    	pathArea.setLineWrap(true);
    	pathArea.setWrapStyleWord(true);
    	pathArea.setEditable(false);
    	
    	buttonPanel.add(path);
    	buttonPanel.add(pathArea);

    	
    	
    	container.add(buttonPanel, BorderLayout.NORTH);
    	container.add(platPanel, BorderLayout.SOUTH);
    	container.add(outputPanel);
    	//container.add(otherPanel, BorderLayout.SOUTH);
    	frame.add(container);
    	
    	
    	openButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			
    			textArea.append("Converting files... \n");
    			fC = new JFileChooser();
    			
    			fC.setMultiSelectionEnabled(true);
    			
    			fC.setCurrentDirectory(new File("/Users/kieran/Documents/Documents – Kieran’s MacBook Pro/Programming/Sem 1 - Year 2/"));
    	    	
    	    	FileNameExtensionFilter filter = new FileNameExtensionFilter("PowerPoint Files", "ppt", "pptx");
    	    	
    	    	fC.setFileFilter(filter);
    	    	
    	    	
    	    	int returnVal = fC.showOpenDialog(null);
    	    	
    	    	if(returnVal == JFileChooser.APPROVE_OPTION && selectedDir != null) {
    	            files = fC.getSelectedFiles();
    	            
    	            
    	            
    	            if(pcButton.isSelected())
    	            {
    	            	textArea.append("Starting Runnables... \nConverting all files... \n");
    	            	 ExecutorService service = Executors.newFixedThreadPool(files.length);
    	            	 for(int i= 0; i<files.length; i++)
    	    	            {
    	    	            	Runnable r = new convertRunnable(files[i].getAbsolutePath(), files[i].getName(), selectedDir);   
    	    	            	service.execute(r);
    	    	            }
    	            	 
    	            }
    	            if(macButton.isSelected())
    	            {
    	            	for(int i= 0; i<files.length; i++)
	    	            {
    	            		try {
    	            			//textArea.append("Converting " + files[i].getName());
    	            			Runnable r = new convertRunnable(files[i].getAbsolutePath(), files[i].getName(), selectedDir);   
    	            			Thread t = new Thread(r);
    	            			t.start();
    	            			t.join();
    	            			//textArea.append("\n\n\nDone!");
    	            			//JOptionPane.showMessageDialog(frame, "Conversion Done.");
    	            		}
    	            		catch(Exception e2)
    	            		{
    	            			e2.printStackTrace();
    	            		}
	    	            	
	    	            }
    	            }
    	        }
    	    	else {
    	    		JOptionPane.showMessageDialog(frame, "Nothing Selected.");
    	    	}
    		}
    	});
    	
    	
    	outButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			fC = new JFileChooser();
    			fC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    			fC.setAcceptAllFileFilterUsed(false);
    			
    			fC.setCurrentDirectory(new File("/Users/kieran/Documents/Documents – Kieran’s MacBook Pro/Programming/Sem 1 - Year 2/"));
    			
    			//fC.setCurrentDirectory(new java.io.File("."));
    			

    	    	
    			if(fC.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
    				
    	            File file = fC.getSelectedFile();
    	            
    	            selectedDir = file.getAbsolutePath() + "/";
    	            
    	            openButton.setEnabled(true);
    	            pathArea.setText(selectedDir + "\n");
    	        }
    	    	else {
    	    		JOptionPane.showMessageDialog(frame, "Nothing Selected.");
    	    	}
    		}
    	});
    	
    	
    	
    	
    	
    	frame.setVisible(true);
    }
}
