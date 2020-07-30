//기존 TraxViewer
package trax;


/*
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class TraxViewer extends JFrame  {
	Button [][] btns;
	InGame IG;
	private JPanel TraxPane;
	private JFrame frame;
	private ImageIcon title_image;
	private JPanel Game_Set_Pane;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	final int FRAME_WIDTH = 800;
	final int FRAME_HEIGHT = 800;
	//final int FRAME_WIDTH = (int)screenSize.getWidth() / 2;
	//final int FRAME_HEIGHT = (int)screenSize.getHeight() / 2;
	final int TRAX_WIDTH = 44*64;
	final int TRAX_HEIGHT = 44*64;
	final int x_len = 64;
	final int y_len = 64;
	public Game_Set G_S;
	public TraxViewer() // �깮�꽦�옄
	{
		frame = new JFrame();
		TraxPane = new JPanel();
		Game_Set_Pane = new JPanel();
		Point View_Location = new Point(64*44/2-44*8, 64*44/2-44*8);
		JScrollPane scroll;
		frame.setLocation((screenSize.width-FRAME_WIDTH)/2, (screenSize.height-FRAME_HEIGHT)/2);
		frame.setLayout(new BorderLayout());
		
		
		TraxPane.setLayout(new GridLayout(x_len,y_len));
		
		title_image = new ImageIcon("images/trax_tile_1.png");
		btns = new Button[y_len][x_len];
		TraxPane.setPreferredSize(new Dimension(TRAX_WIDTH,TRAX_HEIGHT));
		frame.setIconImage(title_image.getImage());
		IG = new InGame(btns,this);
		
		//Game_Set_Pane
		Game_Set_Pane.setPreferredSize(new Dimension(200,(int)TRAX_HEIGHT));
		Game_Set_Pane.setLayout(new BorderLayout());
		G_S= new Game_Set(Game_Set_Pane, this);
		//Game_Set_Pane.add(G_S.getReset());
		frame.add(Game_Set_Pane,BorderLayout.WEST);
		//
		//
				
		for(int i=0;i<y_len;i++){
			for(int j=0;j<x_len;j++){
				this.btns[i][j] = new Button();
				btns[i][j].setBounds(44*j,44*i,44,44);
				btns[i][j].btn.addMouseListener(new Mouse());
				TraxPane.add(btns[i][j].getBtn());
				btns[i][j].getBtn().putClientProperty("y", i);
				btns[i][j].getBtn().putClientProperty("x", j);
			}
		}
		Button.IG = this.IG;
		scroll = new JScrollPane(TraxPane);
		scroll.getViewport().setViewPosition(View_Location);
		frame.add(scroll,BorderLayout.CENTER);
		frame.setSize(FRAME_WIDTH + 200,FRAME_HEIGHT);
		frame.setResizable(false);
		frame.setTitle("Trax - Team B");
		frame.setVisible(true);
	}
	public void Init_Trax(){
		IG.Init();
		Mouse.check_res = 23;
		G_S.Game_Set_Init();
		IG.ai.ai_win_white_check.common_path_Init();
		IG.ai.ai_win_black_check.common_path_Init();
		IG.ai.AI_white_path.clear();
		IG.ai.datapo_white.clear();
		IG.ai.AI_black_path.clear();
		IG.ai.datapo_black.clear();
		for(int i=0;i<y_len;i++){
			for(int j=0;j<x_len;j++){
				IG.exist[i][j] = 0;
				IG.check_tile_type[i][j] = 0;
				for(int p=0;p<4;p++){
					for(int q=0;q<3;q++){
						IG.valid_tile[i][j][p][q]=0;
					}
				}
				btns[i][j].Init();
			}
		}
	}
}
