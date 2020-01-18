package my.pdfConverter.pptToPdf;


//import com.aspose.slides.Presentation;
//import com.aspose.slides.SaveFormat;

import javax.swing.JOptionPane;

//import com.aspose.slides.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

//import org.apache.poi.hslf.HSLFSlideShow;
//import org.apache.poi.hslf.model.Slide;
//import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.ooxml.POIXMLProperties.CoreProperties;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class convertRunnable implements Runnable{
	
	private String path;
	private String name;
	private String dir;
	
	
	private PdfPTable table;
	private double zoom;
	private AffineTransform at;
	
	public convertRunnable(String path, String name, String dir)
	{
		this.path = path;
		this.name = name;
		this.dir = dir;
	}
	
	public void run() {
		
		try {
			ZipSecureFile.setMinInflateRatio(0);
			convertPPT(path,dir, ".pptx");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		
		/*App.textArea.append("Converting " + name + " ...");
    	Presentation pres = new Presentation(path);
    	
    	
    	pres.save(dir +"/"+ name + ".pdf", SaveFormat.Pdf);
    	App.textArea.append("Saved " + name + " to PDF");
    	pres.dispose();
    	
    	//JOptionPane.showMessageDialog(App.frame, "Nothing Selected.");*/
    
		
	}
	
	public void convertPPT(String source, String dest, String fileType) throws Exception
	{
		App.textArea.append("Starting Conversion... \n");
		App.textArea.append(name + " is being converted... \n");
		FileInputStream inputStream = new FileInputStream(source);
		//POIFSFileSystem fs = new POIFSFileSystem(inputStream);
	    zoom = 2;
		at = new AffineTransform();
	    at.setToScale(zoom, zoom);
	    Document pdfDocument = new Document();
	    PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument, new FileOutputStream(dir +"/"+ name + ".pdf"));
	    table = new PdfPTable(1);
	    pdfWriter.open();
	    pdfDocument.open();
	    Dimension pgsize = null;
	    Image slideImage = null;
	    BufferedImage img = null;
	    /*if (fileType.equalsIgnoreCase(".ppt")) {
	        HSLFSlideShow slideShow = new HSLFSlideShow(fs);
	        SlideShow ppt = new SlideShow(slideShow);
	        pgsize = ppt.getPageSize();
	        Slide slide[] = ppt.getSlides();
	        pdfDocument.setPageSize(new Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
	        pdfWriter.open();
	        pdfDocument.open();
	        for (int i = 0; i < slide.length; i++) {
	            img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
	            Graphics2D graphics = img.createGraphics();
	            graphics.setTransform(at);

	            graphics.setPaint(Color.white);
	            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
	            slide[i].draw(graphics);
	            graphics.getPaint();
	            slideImage = Image.getInstance(img, null);
	            table.addCell(new PdfPCell(slideImage, true));
	        }
	    }*/
	    if (fileType.equalsIgnoreCase(".pptx")) {
	        XMLSlideShow ppt = new XMLSlideShow(inputStream);
	        pgsize = ppt.getPageSize();
	        List<XSLFSlide> slide = ppt.getSlides();
	        pdfDocument.setPageSize(new Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
	        pdfWriter.open();
	        pdfDocument.open();
	        readPPTX(ppt);
	    }
	    pdfDocument.add(table);
	    pdfDocument.close();
	    pdfWriter.close();
	    App.textArea.append("Powerpoint file converted to PDF successfully \n");
	    
	    
	    
	}
	
	public void readPPTX(XMLSlideShow pptx) throws Exception
	{
		CoreProperties props = pptx.getProperties().getCoreProperties();
        String title = props.getTitle();
        System.out.println("Title: " + title);
        
        Dimension pgsize = null;
	    Image slideImage = null;
	    BufferedImage img = null;
        int count = 0;
        for (XSLFSlide slide: pptx.getSlides()) {
        	App.textArea.append("Starting slide... " + count + "\n");
        	List<XSLFShape> shapes = slide.getShapes();
        	pgsize = pptx.getPageSize();
        	
        	img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();
            graphics.setTransform(at);

            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
            slide.draw(graphics);
            graphics.getPaint();
            slideImage = Image.getInstance(img, null);
            table.addCell(new PdfPCell(slideImage, true));
            count++;
        }	 
	}

}
