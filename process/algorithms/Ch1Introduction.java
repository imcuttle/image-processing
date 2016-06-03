/**
 * @Ch1Introduction.java
 * @Version 1.0 2010.02.27
 * @Author Xie-Hua Sun 
 */

package process.algorithms;

import process.common.Common;
import process.param.ResultShow;
import process.rw.BMPReader;
import process.rw.PGM;
import process.rw.RAW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.io.FileInputStream;
import java.io.IOException;

public class Ch1Introduction extends JFrame implements ActionListener
{
    Image iImage, iImage2, oImage;
   
    int   iw, ih;
    int[] pix, pix2;    
                 
    boolean loadflag = false,
            loadflag2= false,
            runflag  = false;    //ͼ����ִ�б�־ 
            
    Common common;
    Introduction introduction;
    
    public Ch1Introduction()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��1�� ����");
        this.setBackground(Color.lightGray);        
              
        //�˵�����
        setMenu();
        
        introduction = new Introduction();
        common = new Common();
                
        //�رմ���
        closeWin();
        
        setSize(530, 330);
        setLocation(700, 10);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt)
    {
    	Graphics graph = getGraphics();
    	ResultShow result = null;
    	      	  
        if (evt.getSource() == openItem) 
        {
        	//�ļ�ѡ��Ի���
            JFileChooser chooser = new JFileChooser();            
            common.chooseFile(chooser, "./images/ch1", 4);//����Ĭ��Ŀ¼,�����ļ�
            int r = chooser.showOpenDialog(null);                                 
            
            MediaTracker tracker = new MediaTracker(this);
                                      
            if(r == JFileChooser.APPROVE_OPTION) 
            {
            	String name = chooser.getSelectedFile().getAbsolutePath();
                
                //ȡ�ļ������� 
                String fname = chooser.getSelectedFile().getName(); 
                
                int len = fname.length();
                
                //ȡ�ļ�������չ��
                String exn = fname.substring(len-3, len);
                String imn = fname.substring(0,len-4);
                
                if(runflag)//��ʼ�� 
	            {            	
	            	loadflag  = false;
	            	loadflag2 = false;
	            	runflag   = false;	            	
	            }
	            
	            if(exn.equalsIgnoreCase("bmp"))//BMPͼ��
                {
                	MemoryImageSource mis = null;          
                	BMPReader bmp = new BMPReader();
				  	try
				  	{
				  	    FileInputStream fin = new FileInputStream(name);
				  	    mis = bmp.getBMPImage(fin);
				  	}
				  	catch(IOException e1){System.out.println("Exception!");}
				  	if(!loadflag)
				    {
					  	iImage = createImage(mis); 
					  	iw = iImage.getWidth(null);
						ih = iImage.getHeight(null);						
						loadflag = true;				    
						repaint();
					}
					else if(loadflag && (!runflag))
				    {			        
					    iImage2 = createImage(mis);
					    common.draw(graph, iImage, "ԭͼ1", iImage2, "ԭͼ2");
					    loadflag2 = true;					    	    			    	
				    }	
				}
				else if(exn.equalsIgnoreCase("pgm")) //pgmͼ��
                {
                	setTitle("��1�� ���� PGMͼ����ʾ ���� ���ƻ�");
                    PGM pgm = new PGM();
                    pgm.readPGMHeader(name);
                    char a0 = pgm.getCh0();
                    char a1 = pgm.getCh1();
                    iw = pgm.getWidth();
                    ih = pgm.getHeight();
                	int maxpix = pgm.getMaxpix();
                	pix = pgm.readData(iw, ih, 5);   //P5-Gray image
                	ImageProducer ip = new MemoryImageSource(iw, ih, pix, 0, iw);
		            iImage = createImage(ip);
		            common.draw(graph, iImage, a0, a1, iw, ih, maxpix);          
		        }
                else if(exn.equalsIgnoreCase("ppm")) //ppmͼ��
                {
                	setTitle("��1�� ���� PPMͼ����ʾ ���� ���ƻ�");
                    PGM pgm = new PGM();
                    pgm.readPPMHeader(name);
                    char a0 = pgm.getCh0();
                    char a1 = pgm.getCh1();
                    iw = pgm.getWidth();
                    ih = pgm.getHeight();
                    int maxpix = pgm.getMaxpix();
                	pix = pgm.readData(iw, ih, 6);       //P6-Color image
                 	ImageProducer ip = new MemoryImageSource(iw, ih, pix, 0, iw);
		            iImage = createImage(ip);
		            common.draw(graph, iImage, a0, a1, iw, ih, maxpix);		            
                }
                else if(exn.equalsIgnoreCase("raw"))     //RAWͼ��
                {
                	setTitle("��1�� ���� RAWͼ����ʾ ���� ���ƻ�");
                	iw = 256;
                	ih = 256;
                	int[][] iPix = new int[iw][ih];            	
                                
	                //��ȡRAWͼ��
	                RAW reader = new RAW();
	                iPix = reader.readRAW(name, iw, ih);
	                
				   	//���ֽ�����pixת��Ϊͼ������pixels
	            	pix = common.toPixels(iPix, iw, ih);
	            	
	            	//�������е����ز���һ��ͼ��
				    ImageProducer ip = new MemoryImageSource(iw, ih, pix, 0, iw);
				    iImage = createImage(ip);  
				    
	  	    	    loadflag = true;  
	  	            repaint(); 
                }                  
                else                                //GIF,JPG,PNG
                {                       
	                if(!loadflag)
				    {
		                //װ��ͼ��
					    iImage = common.openImage(name,tracker);    
					    //ȡ����ͼ��Ŀ�͸�
					    iw = iImage.getWidth(null);
					    ih = iImage.getHeight(null);
					    loadflag = true;				    
					    repaint();
				    }
				    else if(loadflag && (!runflag))
				    {			        
					    iImage2 = common.openImage(name,tracker);
					    common.draw(graph, iImage, "ԭͼ1", iImage2, "ԭͼ2");					    
					    loadflag2 = true;	    			    	
				    }
			    }				               
            } 
        }
        else if (evt.getSource() == analItem)
        {
        	setTitle("��1�� ���� ����ͳ�Ʒ����� ���� ���ƻ�");
        	if(loadflag)        	
        	{ 
        	    pix = common.grabber(iImage, iw, ih);
				
				//������
				double entropy = introduction.getEntropy(pix, iw, ih);
				String entstr = "ͼ����:";
				
				//����Ҷ�ƽ��ֵ
				double average = introduction.getAverage(pix, iw, ih);
				String avrstr = "�Ҷ�ƽ��ֵ:";
				
				//�Ҷ���ֵ
				double median = introduction.getMedian(pix, iw, ih);
				String medstr = "(100,100)5X5����Ҷ���ֵ:";
				
				//���������
				double sqsum = introduction.getSqsum(pix, iw, ih);
				String sqstr = "������:";
				
				runflag = common.draw(graph, iImage, entstr, entropy, avrstr, average, 
				                      medstr, median, sqstr, sqsum);																											
			}
        }
        else if (evt.getSource() == histItem)
        {
            setTitle("��1�� ���� ֱ��ͼ ���� ���ƻ�");       	
        	if(loadflag)        	
        	{
        		pix = common.grabber(iImage, iw, ih);
				
				//����ֱ��ͼ
				int[] hist = common.getHist(pix, iw, ih);
				int max = common.maximum(hist);
    	        hist = common.normalize(hist, max);//�淶��Ϊ[0, 255]
				common.draw(graph, hist, max);
				runflag = true;       		
			}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == distItem)
        {   
            setTitle("��1�� ���� ͼ�������ͼ������ ���� ���ƻ�");
            
           	if(loadflag)
        	{        		
				pix = common.grabber(iImage, iw, ih);
				if(loadflag2)
				{
				    pix2 = common.grabber(iImage2, iw, ih);
				    
				    String supstr = "��ȷ�����";				
				    double supdis = introduction.getSupDis(pix, pix2, iw, ih);
				    
				    String rmsstr = "����������";
				    double rmsdis = introduction.getRmsDis(pix, pix2, iw, ih);
				    
				    String msestr = "�������";
				    double mse = introduction.getMSE(pix, pix2, iw, ih);
				    
				    String psnrstr = "��ֵ�����";
				    double psnr = introduction.getPSNR(pix, pix2, iw, ih);
				    				    
				    result = new ResultShow(graph, supstr, supdis, rmsstr, rmsdis, 
				                    msestr, mse, psnrstr, psnr);
				    result.show();
				    runflag = true;				    
				}
				else
				    JOptionPane.showMessageDialog(null,"��򿪵�2��ͼ��!");
			}
			else
				JOptionPane.showMessageDialog(null,"���ȴ�ͼ��!");	        	        	
        }  
	    else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
    
    public void paint(Graphics g)
    {
    	if(loadflag)
    	{
    		g.clearRect(0, 0, 530, 350);        	
            g.drawImage(iImage,  5,   50, null);        
    	    g.drawString("ԭͼ", 120, 320);
    	} 
    }
    
    public static void main(String[] args) 
    {  
        new Ch1Introduction();        
    }
    
    private void closeWin()
    {
    	addWindowListener(new WindowAdapter()
        {  
            public void windowClosing(WindowEvent e) 
            {  
                System.exit(0);
            }
        });
    }
    
    void setMenu()
    {
    	//�˵�����
        Menu fileMenu = new Menu("�ļ�");
        openItem = new MenuItem("��");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("�˳�");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);        
        
        Menu processMenu = new Menu("ͼ����");
        analItem = new MenuItem("����������");
        analItem.addActionListener(this);
        processMenu.add(analItem);
        
        processMenu.addSeparator();              
        histItem = new MenuItem("ֱ��ͼ");
        histItem.addActionListener(this);
        processMenu.add(histItem);
        
        processMenu.addSeparator();
        distItem = new MenuItem("����������");
        distItem.addActionListener(this);
        processMenu.add(distItem); 
        
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);       
        menuBar.add(processMenu);
        setMenuBar(menuBar);   
    }      
    
    MenuItem openItem;
    MenuItem exitItem;
    
    MenuItem analItem;
    MenuItem histItem;
    MenuItem distItem;  
}
