/**
 * @Ch13FracImageCompress.java
 * @Version 1.0 2010.02.24
 * @Author Xie-Hua Sun 
 */

package process.algorithms;

import process.common.Common;
import process.rw.RAW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;

public class Ch13FracImageCompress extends JFrame implements ActionListener
{
    Image iImage, oImage;
    int   iw, ih;
    int[][] iPix;   
    int[] pixels;          
            
    boolean loadflag  = false,
            runflag   = false;    //ͼ����ִ�б�־ 
            
    FracCompress fcomp;
    Common common;
    
    public Ch13FracImageCompress()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��13�� ����ͼ��ѹ��");
        this.setBackground(Color.lightGray);        
        
        iw = 256;
        ih = 256;
                     
        //�˵�����
        setMenu();
        
        fcomp  = new FracCompress();
        common = new Common();
        
        //�رմ���
        closeWin();
        
        setSize(530, 330);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt)
    {
    	Graphics graph = getGraphics();
    	      	  
        if (evt.getSource() == openItem) 
        {
        	 //�ļ�ѡ��Ի���
            JFileChooser chooser = new JFileChooser();
            common.chooseFile(chooser, "./images/ch13", 1);//����Ĭ��Ŀ¼,�����ļ�
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {
            	iPix = new int[iw][ih];
            	  
                String name = chooser.getSelectedFile().getAbsolutePath();  
                                
                //��ȡRAWͼ��
                RAW reader = new RAW();
                iPix = reader.readRAW(name, iw, ih);
                
			   	//���ֽ�����pixת��Ϊͼ������pixels
            	pixels = common.toPixels(iPix, iw, ih);
            	
            	//�������е����ز���һ��ͼ��
			    ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			    iImage = createImage(ip);  
			    
  	    	    loadflag = true;  
  	            repaint();     	
            }            
        }
        else if (evt.getSource() == compItem)
        {
        	if(loadflag)        	
        	{        		
				fcomp.fracCompress(iPix);
				JOptionPane.showMessageDialog(null,"��Ŀ¼images/compressed,\n"+
	                     "����ͼ��compressed.frc�ɹ�!");	                     										
			}
        }
        else if (evt.getSource() == uncmItem)
        {       	
        	if(loadflag)        	
        	{
        		int[][] om = fcomp.fracDecompress();
        	
			   	//���ֽ�����pixת��Ϊͼ������pixels
	        	pixels = common.toPixels(om, iw, ih);
	        	
	        	//�������е����ز���һ��ͼ��
			    ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
			    oImage = createImage(ip);  
			    
	    	    loadflag = true; 
	    	    runflag = true; 
	            repaint();									
			}
			else
			 	JOptionPane.showMessageDialog(null, "���ȴ�ͼ��!");             	                     
	    }
	    else if (evt.getSource() == juliaItem)
        {
        	double p = -0.74543, q = 0.11301;        
            //double p = 0.11, q = 0.66;
        	pixels = fcomp.JuliaSet(300, 230, p, q); 
        	ImageProducer ip = new MemoryImageSource(300, 230, pixels, 0, 300);
			oImage = createImage(ip);
			common.draw(graph, oImage, 240, 50, "Julia����");	        
        }
        else if (evt.getSource() == kochItem)
        {
        	fcomp.Koch(graph, 25, 100, 275, 100); 
        }
        else if (evt.getSource() == mandItem)
        {
        	pixels = fcomp.Mandelbrot(300, 300); 
        	ImageProducer ip = new MemoryImageSource(300, 300, pixels, 0, 300);
			oImage = createImage(ip);
			common.draw(graph, oImage, 240, 50, "Mendelbrot����");		
        }
        else if (evt.getSource() == fernItem)
        {
        	pixels = fcomp.bfern(220, 260);        	
        	ImageProducer ip = new MemoryImageSource(220, 260, pixels, 0, 220);
			oImage = createImage(ip);
			common.draw(graph, oImage, 240, 50, "Barnsley��Ҷ");			
        }
        else if (evt.getSource() == ifsItem)
           	runIFS(200, 200);
        else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
    
    //����paint()��������ʾͼ����Ϣ��
    public void paint(Graphics g)
    {    	
  	    if(loadflag&&(!runflag))
  	    {
  	    	g.clearRect(0, 0, 530, 330);		  	
			g.drawImage(iImage, 5, 50, this);
			g.drawString("Դͼ��", 120, 320);
		} 
		else if(runflag)
		{
			g.clearRect(0, 0, 530, 330);
			g.drawImage(iImage, 5, 50, this);
			g.drawImage(oImage, 270, 50, this);
			g.drawString("Դͼ��", 120, 320);
			g.drawString("��ѹͼ��", 380, 320);
		}	
    }    
   	 	
   	public void runIFS(int w, int h)
    {
    	byte[] sier = new byte[w*h];
        ImageProducer ip;
        Graphics graph = getGraphics();
               
        //��ʼ��ͼ��x
	    for(int i = 0; i < w*h; i++)
	    	sier[i] = 1; 
	    	   
	    //==============������ʾ===============
	    for(int i = 0 ; i < 10; i++)
	    {	    
		    pixels = common.bin2Rgb(sier, w, h);
		  
		    ip = new MemoryImageSource(w, h, pixels, 0, w);
			oImage = createImage(ip);
			common.draw(graph, oImage, 260, 50, "����"+i+"��");			
			
			JOptionPane.showMessageDialog(null,"��������"+(i+1)+"�ε���");			           
			           
			sier = fcomp.sierpinski(sier, w, h);
		}	            
	    //============������ʾ����============
	    	     
	    pixels = common.bin2Rgb(sier, w, h);
	    ip = new MemoryImageSource(w, h, pixels, 0, w);
		oImage = createImage(ip);	 
		common.draw(graph, oImage, 260, 50, "����10��");					
    }
    
    public static void main(String[] args) 
    {  
        new Ch13FracImageCompress();        
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
    
    //�˵�����
    public void setMenu()
    {    	
        Menu fileMenu = new Menu("�ļ�");
        openItem = new MenuItem("��");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("�˳�");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);       
        
        Menu processMenu = new Menu("ͼ�����");
        compItem = new MenuItem("ѹ   ��");
        compItem.addActionListener(this);
        processMenu.add(compItem);
        
        uncmItem = new MenuItem("��ѹ��");
        uncmItem.addActionListener(this);
        processMenu.add(uncmItem);
        
        Menu fracMenu = new Menu("������ʾ");
        juliaItem = new MenuItem("Julia����");
        juliaItem.addActionListener(this);
        fracMenu.add(juliaItem);
        
        fracMenu.addSeparator();
        kochItem = new MenuItem("Koch����");
        kochItem.addActionListener(this);
        fracMenu.add(kochItem);
        
        fracMenu.addSeparator();
        mandItem = new MenuItem("Mandelbrot����");
        mandItem.addActionListener(this);
        fracMenu.add(mandItem);
        
        fracMenu.addSeparator();
        fernItem = new MenuItem("Barnsley��Ҷ");
        fernItem.addActionListener(this);
        fracMenu.add(fernItem);
        
        fracMenu.addSeparator();
        ifsItem = new MenuItem("IFS����");
        ifsItem.addActionListener(this);
        fracMenu.add(ifsItem);
        
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(processMenu);
        menuBar.add(fracMenu);       
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    MenuItem compItem;
    MenuItem uncmItem;
    MenuItem juliaItem;
    MenuItem kochItem;
    MenuItem mandItem;
    MenuItem fernItem;
    MenuItem ifsItem;
}
