package trax;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.JOptionPane;

public class Button extends JFrame{
	public JButton btn = new JButton();
	int click_cnt = 0;
	static InGame IG;
	final int x_len = 64;
	final int y_len = 64;
	final int tile_cnt = 6;
	
	public void Init(){
		btn.setIcon(IG.back);
		click_cnt = 0;
	}
	
	JButton getBtn(){
		return btn;
	}
	
	public Button()
	{
		super();
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setContentAreaFilled(false);
		btn = new JButton(new ImageIcon("images/trax_background.png"));
	}
	
	public void set_image(int a){
		if(a==10){
			btn.setIcon(IG.back);
		}
		else if(a>20){
			btn.setIcon(IG.tile_image_win[a-21]);
		}
		else{
			btn.setIcon(IG.tile_image[a-1]);
		}
	}
	
}
