package process.param;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import javax.swing.*;

public class ParametersPanel2 extends Frame implements ActionListener
{
	String str1, str2, par1, par2;
	Button ok;
	Label pLabel, qLabel;
	TextField pField, qField;
    Checkbox xbox, ybox;
    
	public ParametersPanel2(String s1, String s2, String p1, String p2)//�ı���
	{		
		setTitle("����ѡ��");
		setLayout(new FlowLayout());
		str1 = s1;
		str2 = s2;
		par1 = p1;
		par2 = p2;
		 
		ok = new Button("ȷ��");
		pLabel = new Label(str1);
		qLabel = new Label(str2);
		pField = new TextField(p1, 5);
		qField = new TextField(p2, 5);
		ok.addActionListener(this);
								
		add(pLabel);		
		add(pField);
		add(qLabel);
		add(qField);
		add(ok);
		
		setSize(220, 100);
		setVisible(true);
		/*setBorder(new CompoundBorder(
			BorderFactory.createTitledBorder("����ѡ��"),
			BorderFactory.createEmptyBorder(10, 10, 50, 10)));*/
	}
	
	public ParametersPanel2(String s1, String s2) //��ť
	{
		setTitle("��ѡ��������");
		CheckboxGroup cbg = new CheckboxGroup();
		xbox = new Checkbox(s1, cbg, true);
		ybox = new Checkbox(s2, cbg, true);
		add(xbox); add(ybox);
		setSize(220, 100);
		setVisible(true);
		/*setBorder(new CompoundBorder(
		    BorderFactory.createTitledBorder("��ѡ��������"),
		    BorderFactory.createEmptyBorder(10, 10, 50, 10)));*/
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		par1 = pField.getText();
		par2 = pField.getText();
	}
	
	public float getPadx() 
	{
		return Float.parseFloat(par1);
	}
	
	public float getPady() 
	{
		return Float.parseFloat(par2);
	}
	
	public int getRadioState()
	{
		if(xbox.getState())      return 0;
		else if(ybox.getState()) return 1;
		else return 0;
	}
}
