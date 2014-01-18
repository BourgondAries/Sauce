package sauce;

import ttl.Ips;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JLabel;




public class Main extends JFrame
{
	public Main()
	{
		add(new Screen());
		setTitle("Sauce");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(300, 280);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}
	
	public static void main(String[] args)
	{
		// Let's encapsulate the entire program in one huge try-catch,
		// we'll be notified about any exceptions immediately.
		try 
		{
			System.out.println("heelosaouce!!!");
	//		new Main();
			
			Ips ips = new Ips(30.f);
			
			long x = Calendar.getInstance().get(Calendar.MILLISECOND);
			for (int i = 0; i < 100; ++i)
			{
//				System.out.println(ips);
				System.out.println(i);
				Thread.sleep(3);
				ips.limit();
			} 
			long y = Calendar.getInstance().get(Calendar.MILLISECOND);
			
			System.out.println(y - x);
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
	}
}
