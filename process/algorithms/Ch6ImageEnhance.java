/**
 * @Ch6ImageEnhance.java
 * @Version 1.0 2010.02.17
 * @Author Xie-Hua Sun 
 */

package process.algorithms;

import process.common.Common;
import process.common.Hist;
import process.param.ParametersI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;

public class Ch6ImageEnhance extends JFrame implements ActionListener
{        
    Image iImage, iImage2, oImage;
    BufferedImage bImage; 
    
    int   iw, ih;
    int[] pixels;          
             
    boolean loadflag  = false,
            loadflag2 = false,    //��2��ͼ�������־
            runflag   = false,    //ͼ����ִ�б�־ 
            seeflag   = false;    //Ԥ����־ 
    
    //����ѡ�����
    ParametersI p;
    JButton okButton, seeButton;
	JDialog dialog;  
      
    Common common;
    ImageEnhance enhance;
    
    public Ch6ImageEnhance()
    {    
        setTitle("����ͼ����-Java�����ʵ�� ��6�� ͼ����ǿ");
        this.setBackground(Color.lightGray);        
              
        //�˵�����
        setMenu();
        
        common  = new Common();
        enhance = new ImageEnhance();
                
        //�رմ���
        closeWin();
                
        setSize(530, 330);
        setLocation(700, 10);
        setVisible(true);        
    }

    public void actionPerformed(ActionEvent evt)
    {
    	Graphics graph = getGraphics();
    	      	  
        if (evt.getSource() == openItem) 
        {
        	//�ļ�ѡ��Ի���
            JFileChooser chooser = new JFileChooser();
            common.chooseFile(chooser, "./images", 0);//����Ĭ��Ŀ¼,�����ļ�
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                String name = chooser.getSelectedFile().getAbsolutePath();
                if(runflag)
                { 
                    loadflag  = false;
                    runflag   = false;
                }   
			    if(!loadflag)
			    {
	                //װ��ͼ��
				    iImage = common.openImage(name, tracker);    
				    //ȡ����ͼ��Ŀ�͸�
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    loadflag = true;
				    repaint();				    
			    }			               
            }            
        }
        else if (evt.getSource() == stretchItem)
        {
        	setTitle("��6�� ͼ����ǿ �Աȶ���չ ���� ���ƻ�");
        	if(loadflag)        	
        	{
        		p = new ParametersI();
        	    setPanelI(p, "�Աȶ���չ", 1);
        	    showSpaceEnhance(graph, 0, "�Աȶ���չ");        	    					
			}
        }
        else if (evt.getSource() == balanceItem)
        {
        	setTitle("��6�� ͼ����ǿ ֱ��ͼ���Ȼ�  ���� ���ƻ�");
        	if(loadflag)        	
        	    showSpaceEnhance(graph, 1, "ֱ��ͼ���Ȼ�");    	    						
		}
        else if (evt.getSource() == histItem)
        {        	
        	if(loadflag)
        	{        		
	            pixels = common.grabber(iImage, iw, ih);
	            
				//��ʾͼ���ֱ��ͼ
				Hist h = new Hist("���Ȼ�ǰ");
				
				//��������
				h.getData(pixels, iw, ih);
				h.setLocation(10, 320);								
			}
			if(runflag)
        	{
        		pixels = common.grabber(oImage, iw, ih);
	            
				//��ʾͼ���ֱ��ͼ
				Hist h = new Hist("���Ȼ���");
				
				//��������
				h.getData(pixels, iw, ih);
				h.setLocation(310, 320);
        	}					
        }
        else if(evt.getSource() == seeButton)//Ԥ��
        {        	
			p = new ParametersI();
			common.draw(graph, p.getx1(), p.gety1(), p.getx2(), p.gety2(), 
			            "�Աȶ���չ���ӻ�Ԥ��");           	
        }
        else if (evt.getSource() == threshItem)
        {
        	setTitle("��6�� ͼ����ǿ ��ֵ�˲� ���� ���ƻ�");
        	if(loadflag)       	
        	    showSpaceEnhance(graph, 2, "��ֵ�˲�");        	    
        }
        else if (evt.getSource() == averItem)
        {
        	setTitle("��6�� ͼ����ǿ ��ֵ�˲� ���� ���ƻ�");
        	if(loadflag)       	
        	    showSpaceEnhance(graph, 3, "��ֵ�˲�");        	    
        }
        else if (evt.getSource() == medianItem)
        {
        	setTitle("��6�� ͼ����ǿ ��ֵ�˲� ���� ���ƻ�");
        	if(loadflag)        	
        	{
        		p = new ParametersI("����ѡ��", "3X3", "1X5", "5X1", "5X5");
        	    setPanelI(p, "��ֵ�˲�", 0);
        	    showSpaceEnhance(graph, 4, "��ֵ�˲�");        	    	   	
        	}
        }
        else if (evt.getSource() == lowItem)
        {
        	setTitle("��6�� ͼ����ǿ ��ͨģ���˲� ���� ���ƻ�");
        	if(loadflag)
        	{
        		p = new ParametersI("ģ��ѡ��", "h1", "h2", "h3");
        	    setPanelI(p, "��ͨģ���˲�", 0);
        	    showSpaceEnhance(graph, 5, "��ͨģ���˲�");        	    
        	}
        }
        else if (evt.getSource() == highItem)
        {
        	setTitle("��6�� ͼ����ǿ ��ͨģ���˲� ���� ���ƻ�");
        	if(loadflag)       	
        	{
        	    p = new ParametersI("ģ��ѡ��", "H1", "H2", "H3", "H4", "H5");
        	    setPanelI(p, "��ͨģ���˲�", 0);
        	    showSpaceEnhance(graph, 6, "��ͨģ���˲�");        	    
        	}
        }
        else if (evt.getSource() == blpfItem)
        {
        	setTitle("��6�� ͼ����ǿ Butterworth��ͨ�˲� ���� ���ƻ�");
        	if(loadflag)        	
        	{ 
        	    if(iw == 256&&ih==256)//0:BHPF��ͨ�˲�
        		    showFreqFilter(graph, 0, "�������(����0~255)", 
        		                   "100", "BLPF�˲�");	        		
				else
				{				
				    JOptionPane.showMessageDialog(null, "��������256X256ͼ��!");
				    loadflag = false;
				}
        	}
        }
        else if (evt.getSource() == bhpfItem)
        {
        	setTitle("��6�� ͼ����ǿ Butterworth��ͨ�˲� ���� ���ƻ�");
        	if(loadflag)        	
        	{
        		if(iw == 256&&ih==256)//1:BHPF��ͨ�˲�
        		    showFreqFilter(graph, 1, "�������(����0~255)", 
        		                   "150", "BHPF�˲�");	        		
				else
				{				
				    JOptionPane.showMessageDialog(null, "��������256X256ͼ��!");
				    loadflag = false;
				}	
        	}
        }
        else if (evt.getSource() == elpfItem)
        {
        	setTitle("��6�� ͼ����ǿ ָ����ͨ�˲� ���� ���ƻ�");
        	if(loadflag)        	
        	{
        		if(iw == 256&&ih==256)//2:ELPF��ͨ�˲�
        		    showFreqFilter(graph, 2, "�������(����0~255)", 
        		                   "150", "ELPF�˲�");	        		
				else
				{				
				    JOptionPane.showMessageDialog(null, "��������256X256ͼ��!");
				    loadflag = false;
				}	
        	}
        }
        else if (evt.getSource() == ehpfItem)
        {
        	setTitle("��6�� ͼ����ǿ ָ����ͨ�˲� ���� ���ƻ�");
        	if(loadflag)        	
        	{
        		if(iw == 256&&ih==256)//3:BHPF��ͨ�˲�
        		    showFreqFilter(graph, 3, "�������(����0~255)", 
        		                   "150", "EHPF�˲�");	        		
				else
				{				
				    JOptionPane.showMessageDialog(null, "��������256X256ͼ��!");
				    loadflag = false;
				}	
        	}
        }
        else if (evt.getSource() == kirItem)
        {
        	setTitle("��6�� ͼ����ǿ Kirsch�� ���� ���ƻ�");
        	if(loadflag)       	
        	    showSharp(graph, 1, "Kirsch��");        		
        }
        else if (evt.getSource() == lapItem)
        {
        	setTitle("��6�� ͼ����ǿ Laplace�� ���� ���ƻ�");
        	if(loadflag)        	
        	    showSharp(graph, 2, "Laplace��");        		
        }
        else if (evt.getSource() == preItem)
        {
        	setTitle("��6�� ͼ����ǿ Prewitt�� ���� ���ƻ�");
        	if(loadflag)        	
        	    showSharp(graph, 3, "Prewitt��");        		
        }
        else if (evt.getSource() == robItem)
        {
        	setTitle("��6�� ͼ����ǿ Roberts�� ���� ���ƻ�");
        	if(loadflag)        	
        	    showSharp(graph, 0, "Roberts��");        		
        }
        else if (evt.getSource() == sobItem)
        {
        	setTitle("��6�� ͼ����ǿ Sobel�� ���� ���ƻ�");
        	if(loadflag)        	
        	    showSharp(graph, 5, "Sobel��");        		
        }						
        else if (evt.getSource() == okButton)
           	dialog.dispose(); 
        else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
        
    public void paint(Graphics g) 
    {    	  
        if (loadflag)
        {
        	g.clearRect(0, 0, 260, 350); 
        	g.drawImage(iImage, 5, 50, null);
            g.drawString("ԭͼ", 120, 320);
            setBackground(Color.white);
        }             
    }
    
    /*******************************************************
     * type -- 0:����һ��okButton; 1:�ж���Button
     *******************************************************/
    private void setPanelI(ParametersI p, String s, int type)
    {
    	JPanel buttonsPanel = new JPanel();
    	dialog = new JDialog(this, s + " ����ѡ��", true);     
        Container contentPane = getContentPane();
		Container dialogContentPane = dialog.getContentPane();

		dialogContentPane.add(p, BorderLayout.CENTER);
		dialogContentPane.add(buttonsPanel, BorderLayout.SOUTH);
		
		if(type == 1)
		{
		    seeButton    = new JButton("Ԥ��");  
	        seeButton.addActionListener(this);		
            buttonsPanel.add(seeButton);
        }
        okButton     = new JButton("ȷ��");				
        okButton.addActionListener(this);
        buttonsPanel.add(okButton);
        dialog.pack();
		dialog.setLocation(0,320);     //���öԻ�������Ļ������
        dialog.show();	        
    }
    
    /*************************************************
     * type - �ͺ�. 0:BLPF 1:BHPF 2:ELPF 3:EHPF
     * name - ���ͼ������ַ���
     *************************************************/    
    public void showSpaceEnhance(Graphics graph, int type, String name)
    {
    	pixels = common.grabber(iImage, iw, ih);
		switch(type)
		{
			case 0: //�Աȶ���չ
			        int[] pixMap = enhance.pixelsMap(p.getx1(), p.gety1(), 
			                                         p.getx2(), p.gety2()); 
			        pixels = enhance.stretch(pixels, pixMap, iw, ih);
			        break;
			case 1: //ֱ��ͼ���Ȼ�
				    int[] histogram = common.getHist(pixels, iw, ih);//ȡֱ��ͼ
				    pixels = enhance.histequal(pixels, histogram, iw, ih);
				    break;		
	        case 2: pixels = enhance.threshold(pixels, iw, ih);
	                break;
	        case 3: pixels = enhance.average(pixels, iw, ih);
	                break;
	        case 4: pixels = enhance.median(pixels, iw, ih, p.getRadioState4());
	                break;
	        case 5: pixels = enhance.lowpass(pixels, iw, ih, p.getRadioState3());
	                break;
	        case 6: pixels = enhance.highpass(pixels, iw, ih, p.getRadioState3());
	                break;
	    }    
	    //�������е����ز���һ��ͼ��
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		oImage = createImage(ip);
		common.draw(graph, iImage, "ԭͼ", oImage, name);
		runflag = true;		
    }
    
    /*************************************************
     * type - �ͺ�. 0:BLPF 1:BHPF 2:ELPF 3:EHPF
     * str  - �Ի���˵���ַ���
     * val  - �Ի������Ĭ��ֵ�ַ���
     * name - ���ͼ������ַ���
     *************************************************/    
    public void showFreqFilter(Graphics graph, int type, String str, 
                               String val, String name)
    {
    	pixels = common.grabber(iImage, iw, ih);
	    pixels = enhance.BEfilter(pixels, iw, ih, type, 
		         common.getParam(str, val));		
						
		//�������е����ز���һ��ͼ��
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		oImage = createImage(ip);
	    common.draw(graph, iImage, "ԭͼ", oImage, name);
		runflag = true;	
    }
    
    /*************************************************
     * type - �����ͺ�. 0:Roberts 1:Kirsch  2:Laplace 
     *                  3:Prewitt 5:Sobel
     * name - ���ͼ������ַ���
     *************************************************/    
    public void showSharp(Graphics graph, int type, String name)
    {
    	pixels = common.grabber(iImage, iw, ih);
		if(type != 0)
		    pixels = enhance.detect(pixels, iw, ih, type, 0, false);		    
		else if(type == 0)
		    pixels = enhance.robert(pixels, iw, ih, type, false);		    	    	    
		//�������е����ز���һ��ͼ��
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		oImage = createImage(ip);
		common.draw(graph, iImage, "ԭͼ", oImage, name);
		runflag = true;
    }
    
    public static void main(String[] args) 
    {  
        new Ch6ImageEnhance();        
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
    
    private void setMenu()
    {
    	//�˵�����
        Menu fileMenu = new Menu("�ļ�");
        openItem = new MenuItem("��");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("�˳�");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
        
        //������ǿ---------------------------    
        Menu spaceMenu = new Menu("������ǿ");
        stretchItem = new MenuItem("�Աȶ���չ");
        stretchItem.addActionListener(this);
        spaceMenu.add(stretchItem);
        
        spaceMenu.addSeparator();        
        balanceItem = new MenuItem("ֱ��ͼ���Ȼ�");
        balanceItem.addActionListener(this);
        spaceMenu.add(balanceItem);
               
        histItem = new MenuItem("��ʾֱ��ͼ");
        histItem.addActionListener(this);
        spaceMenu.add(histItem);
        
        spaceMenu.addSeparator();        
        threshItem = new MenuItem("��ֵ�˲�");
        threshItem.addActionListener(this);
        spaceMenu.add(threshItem);
        
        averItem = new MenuItem("��ֵ�˲�");
        averItem.addActionListener(this);
        spaceMenu.add(averItem);
        
        spaceMenu.addSeparator();        
        medianItem = new MenuItem("��ֵ�˲�");
        medianItem.addActionListener(this);
        spaceMenu.add(medianItem);
        
        spaceMenu.addSeparator();        
        lowItem = new MenuItem("��ͨģ���˲�");
        lowItem.addActionListener(this);
        spaceMenu.add(lowItem);
        
        highItem = new MenuItem("��ͨģ���˲�");
        highItem.addActionListener(this);
        spaceMenu.add(highItem);
        
        //Ƶ����ǿ-------------------------
        Menu freqMenu = new Menu("Ƶ����ǿ");
        blpfItem = new MenuItem("BLPF(��)");
        blpfItem.addActionListener(this);
        freqMenu.add(blpfItem);
        
        bhpfItem = new MenuItem("BHPF(��)");
        bhpfItem.addActionListener(this);
        freqMenu.add(bhpfItem);
        
        freqMenu.addSeparator();
        elpfItem = new MenuItem("ELPF(��)");
        elpfItem.addActionListener(this);
        freqMenu.add(elpfItem);
        
        ehpfItem = new MenuItem("EHPF(��)");
        ehpfItem.addActionListener(this);
        freqMenu.add(ehpfItem);
                       
        //--------------��---------------
        Menu sharpMenu = new Menu("ͼ����");
        kirItem = new MenuItem("Kirsch");
        kirItem.addActionListener(this);
        sharpMenu.add(kirItem);
        
        sharpMenu.addSeparator();
        lapItem = new MenuItem("Laplace");
        lapItem.addActionListener(this);
        sharpMenu.add(lapItem);
        
        sharpMenu.addSeparator();          
        preItem = new MenuItem("Prewitt");
        preItem.addActionListener(this);
        sharpMenu.add(preItem);
        
        sharpMenu.addSeparator();
        robItem = new MenuItem("Roberts");
        robItem.addActionListener(this);
        sharpMenu.add(robItem);
        
        sharpMenu.addSeparator();
        sobItem = new MenuItem("Sobel");
        sobItem.addActionListener(this);
        sharpMenu.add(sobItem);
                
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(spaceMenu);
        menuBar.add(freqMenu);
        menuBar.add(sharpMenu);
        setMenuBar(menuBar);      
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    MenuItem stretchItem;
    MenuItem balanceItem; //���Ȼ�
    MenuItem histItem;    //��ʾֱ��ͼ
    
    MenuItem threshItem;
    MenuItem averItem;
    MenuItem medianItem;
    MenuItem lowItem;     //��ͨģ���˲�
    MenuItem highItem;    //��ͨģ���˲�
    MenuItem blpfItem;    //Butterworth��ͨ�˲�
    MenuItem bhpfItem;    //Butterworth��ͨ�˲� 
    MenuItem elpfItem;    //ָ����ͨ�˲�
    MenuItem ehpfItem;    //ָ����ͨ�˲� 
    
    MenuItem kirItem;     //kirsch����
    MenuItem lapItem;     //laplace����
    MenuItem preItem;     //prewitt����
    MenuItem robItem;     //roberts����
    MenuItem sobItem;     //sobel����
}
