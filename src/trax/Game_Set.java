package trax;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.*;


public class Game_Set {
	private JButton Game_Set_Init = new JButton();
	private JButton AI_Button = new JButton();
	private JPanel panel;
	private JPanel First_P;
	private JPanel Second_P;
	private JPanel Third_P;
	public TraxViewer viewer;
	private JScrollPane Log_Scroll;
	private JLabel Turn;
	private JTextArea Log;
	private int line_attack_std_x = 0;
	private int line_attack_std_y = 0;
	final int white = 10;
	final int black = 11;
	public Game_Set(JPanel panel, TraxViewer viewer){
		this.panel = panel;
		this.viewer = viewer;
		Turn = new JLabel("No Game Stared");
		First_P = new JPanel();
		Third_P = new JPanel();
		
		ImageIcon ori_Icon = new ImageIcon("images/trax_background.png");;
		Image ori_Img = ori_Icon.getImage();
		Image change_Img = ori_Img.getScaledInstance(60,60,Image.SCALE_SMOOTH);
		ImageIcon use_Icon = new ImageIcon(change_Img);
		
		ImageIcon ori_Icon_2 = new ImageIcon("images/ai-icon.png");;
		Image ori_Img_2 = ori_Icon_2.getImage();
		Image change_Img_2 = ori_Img_2.getScaledInstance(60,60,Image.SCALE_SMOOTH);
		ImageIcon use_Icon_2 = new ImageIcon(change_Img_2);
		
		Game_Set_Init.setPreferredSize(new Dimension(60,60));
		Game_Set_Init.setIcon(use_Icon);
		AI_Button.setPreferredSize(new Dimension(60,60));
		AI_Button.setIcon(use_Icon_2);
		
		//Game_Set_Init.setBorderPainted(false);//경계선지우기
		//Game_Set_Init.setFocusPainted(false);//선택될 때 태두리
		Game_Set_Init.setContentAreaFilled(false);//버튼 근처 버튼 배경지우기
		AI_Button.setContentAreaFilled(false);

		//First Panel
		First_P.setPreferredSize(new Dimension(200,200));
		First_P.setLayout(null);
		Game_Set_Init.setBounds(100-30,0,60,60);
		AI_Button.setBounds(100-30,60,60,60);
		First_P.add(Game_Set_Init);
		First_P.add(AI_Button);
		
		
		//Second Panel
		Log = new JTextArea();
		Log.setEditable(false);
		Log.setPreferredSize(new Dimension(200,10000));
		Log.setLayout(new FlowLayout());
		//Second_P.add(Log);
		Log_Scroll = new JScrollPane(Log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		Log_Scroll.setPreferredSize(new Dimension(200,700));
		//Second_P.add(Log_Scroll);
		//Third Panel
		Third_P.setPreferredSize(new Dimension(200,20));
		Turn.setSize(new Dimension(200,20));
		Turn.setLocation(0,780);
		Third_P.add(Turn);
		//
		
		Game_Set_Init.addMouseListener(
				new MouseAdapter(){
					public void mouseClicked(MouseEvent e)
					{
						int res = 0;
						if(e.getButton() == 1){
							res = JOptionPane.showConfirmDialog(null,"Start a new game?","New Game",JOptionPane.YES_NO_OPTION);
						}
						if(res == JOptionPane.YES_OPTION){
							viewer.Init_Trax();
						}
					}
					public void mouseEntered(MouseEvent e){
						JButton entered = (JButton) e.getSource();
						String s = "New Game";
						entered.setToolTipText(s);
					}
				}
		);
		AI_Button.addMouseListener(
				new MouseAdapter(){
					public void mouseClicked(MouseEvent e)
					{
						int line_tile_white = -1,line_tile_black = -1;
						if(viewer.IG.All_click_cnt%2 == 0){//white
							if(viewer.IG.All_click_cnt == 0){
								viewer.IG.first = 1;
								viewer.IG.check_tile_type[32][32] = 3;//3 5
								viewer.IG.changeTile(1,32,32,viewer.IG.btns[32][32].getBtn());
								viewer.IG.viewer.G_S.print_Log(32, 32, viewer.IG.check_tile_type[32][32]);
								viewer.IG.All_click_cnt++;
								viewer.IG.viewer.G_S.Set_Turn();
								viewer.IG.max_x = 32;
								viewer.IG.min_x = 32;
								viewer.IG.max_y = 32;
								viewer.IG.min_y = 32;
								//
								viewer.IG.ai.AI_win_check(32, 32);
								//
							}
							else{
								//System.out.printf("%d min white = %d\n, black = %d", viewer.IG.All_click_cnt,viewer.IG.ai.ai_win_white_check.min_path.min_cnt,viewer.IG.ai.ai_win_black_check.min_path.min_cnt);
								//viewer.IG.ai.ai_win_white_check.line_win_check(white);
								//viewer.IG.ai.ai_win_black_check.line_win_check(black);
								//line_tile_black = viewer.IG.ai.ai_win_black_check.line_attack_depend(black);
								/*if(viewer.IG.ai.ai_win_white_check.line_path.line_attack_valid){
									System.out.printf("FFFFFFFFFFFFFFFFFFFFFF  %d\n",viewer.IG.ai.ai_win_white_check.line_path.which_line_tile);
								}*/
								
								if(viewer.IG.ai.ai_win_white_check.min_path.min_cnt == 1){
									viewer.IG.ai.ai_win_white_check.select_AI_tile(white,1);
									if(viewer.IG.ai.ai_win_white_check.min_path.third_attack_valid){
										viewer.IG.ai.AI_win_check(viewer.IG.ai.ai_win_white_check.min_path.third_x + viewer.IG.ai.ai_win_white_check.min_x - 1, viewer.IG.ai.ai_win_white_check.min_path.third_y + viewer.IG.ai.ai_win_white_check.min_y - 1);
									}
									else {
										viewer.IG.ai.AI_win_check(viewer.IG.ai.ai_win_white_check.min_path.next_x + viewer.IG.ai.ai_win_white_check.min_x - 1, viewer.IG.ai.ai_win_white_check.min_path.next_y + viewer.IG.ai.ai_win_white_check.min_y - 1);
									}
								}/*
								else if(line_attack_std_x == 0 && viewer.IG.ai.ai_win_black_check.min_path.min_cnt>=2 && viewer.IG.ai.ai_win_black_check.line_path.max == 4 && line_tile_black != -1){
									viewer.IG.ai.ai_win_black_check.line_attack_set(line_tile_black);
									viewer.IG.ai.AI_win_check(viewer.IG.ai.ai_win_black_check.line_path.next_x, viewer.IG.ai.ai_win_black_check.line_path.next_y );
									line_attack_std_x = 1;
								}*/
								else if(viewer.IG.ai.ai_win_white_check.min_path.min_cnt >= viewer.IG.ai.ai_win_black_check.min_path.min_cnt){//1공격 2방어
									viewer.IG.ai.ai_win_black_check.select_AI_tile(black,2);
									viewer.IG.ai.AI_win_check(viewer.IG.ai.ai_win_black_check.min_path.next_x + viewer.IG.ai.ai_win_black_check.min_x - 1, viewer.IG.ai.ai_win_black_check.min_path.next_y + viewer.IG.ai.ai_win_black_check.min_y - 1);
								}
								else{
									viewer.IG.ai.ai_win_white_check.select_AI_tile(white,1);
									viewer.IG.ai.AI_win_check(viewer.IG.ai.ai_win_white_check.min_path.next_x + viewer.IG.ai.ai_win_white_check.min_x - 1, viewer.IG.ai.ai_win_white_check.min_path.next_y + viewer.IG.ai.ai_win_white_check.min_y - 1);
								}
								//System.out.printf("%d AI BUtton %d %d %d first dir %d second dir %d\n",viewer.IG.All_click_cnt, viewer.IG.ai.ai_win_white_check.min_path.min_cnt,viewer.IG.ai.ai_win_white_check.min_path.S_point.y,viewer.IG.ai.ai_win_white_check.min_path.S_point.x,viewer.IG.ai.ai_win_white_check.min_path.first_dir,viewer.IG.ai.ai_win_white_check.min_path.second_dir);
							}
						}
						else{//black
							//System.out.printf("%d min white = %d, black = %d\n", viewer.IG.All_click_cnt,viewer.IG.ai.ai_win_white_check.min_path.min_cnt,viewer.IG.ai.ai_win_black_check.min_path.min_cnt);
							//viewer.IG.ai.ai_win_white_check.line_win_check(white);
							//viewer.IG.ai.ai_win_black_check.line_win_check(black);
							//line_tile_white = viewer.IG.ai.ai_win_white_check.line_attack(white);
							//line_tile_black = viewer.IG.ai.ai_win_black_check.line_attack(black);
							if(viewer.IG.ai.ai_win_black_check.min_path.min_cnt == 1){
								viewer.IG.ai.ai_win_black_check.select_AI_tile(black,1);	
								if(viewer.IG.ai.ai_win_black_check.min_path.third_attack_valid){
									viewer.IG.ai.AI_win_check(viewer.IG.ai.ai_win_black_check.min_path.third_x + viewer.IG.ai.ai_win_black_check.min_x - 1, viewer.IG.ai.ai_win_black_check.min_path.third_y + viewer.IG.ai.ai_win_black_check.min_y - 1);
								}
								else{
									viewer.IG.ai.AI_win_check(viewer.IG.ai.ai_win_black_check.min_path.next_x + viewer.IG.ai.ai_win_black_check.min_x - 1, viewer.IG.ai.ai_win_black_check.min_path.next_y + viewer.IG.ai.ai_win_black_check.min_y - 1);
								}
							}
							else if(viewer.IG.ai.ai_win_black_check.min_path.min_cnt >= viewer.IG.ai.ai_win_white_check.min_path.min_cnt){//1공격 2방어
								viewer.IG.ai.ai_win_white_check.select_AI_tile(white,2);
								viewer.IG.ai.AI_win_check(viewer.IG.ai.ai_win_white_check.min_path.next_x + viewer.IG.ai.ai_win_white_check.min_x - 1, viewer.IG.ai.ai_win_white_check.min_path.next_y + viewer.IG.ai.ai_win_white_check.min_y - 1);
							}
							else{
								viewer.IG.ai.ai_win_black_check.select_AI_tile(black,1);
								viewer.IG.ai.AI_win_check(viewer.IG.ai.ai_win_black_check.min_path.next_x + viewer.IG.ai.ai_win_black_check.min_x - 1, viewer.IG.ai.ai_win_black_check.min_path.next_y + viewer.IG.ai.ai_win_black_check.min_y - 1);
							}
							//System.out.printf("%d AI BUtton %d %d %d first dir %d second dir %d\n",viewer.IG.All_click_cnt, viewer.IG.ai.ai_win_black_check.min_path.min_cnt,viewer.IG.ai.ai_win_black_check.min_path.S_point.y,viewer.IG.ai.ai_win_black_check.min_path.S_point.x,viewer.IG.ai.ai_win_black_check.min_path.first_dir,viewer.IG.ai.ai_win_black_check.min_path.second_dir);
						}
					}
					public void mouseEntered(MouseEvent e){
						JButton entered = (JButton) e.getSource();
						String s = "AI BUTTON";
						entered.setToolTipText(s);
					}
				}
		);
		panel.add(First_P,BorderLayout.NORTH);
		panel.add(Log_Scroll,BorderLayout.CENTER);
		panel.add(Third_P,BorderLayout.SOUTH);
		//panel.add(Log_Scroll);
		//panel.add(Turn, BorderLayout.SOUTH);
	}
	public void print_Log(int x, int y, int tile_type){
		String s;
		if (viewer.IG.All_click_cnt%2==0){
			s = "W : ";
		}
		else{
			s = "B : ";
		}
		s = s + "행 : " + y + ", 열 : " + x;
		if(tile_type == 1 || tile_type == 3){
			s = s + " 타일 종류 : /\n";
		}
		else if(tile_type == 2 || tile_type == 4){
			s = s + " 타일 종류 : \\\n";
		}
		else if(tile_type == 5 || tile_type == 6){
			s = s + " 타일 종류 : +\n";
		}
		Log.append(s);
	}
	public void Set_Turn(){
		if (viewer.IG.All_click_cnt%2==0){
			Turn.setText("White to Play");
			Turn.setForeground(Color.gray);
		}
        else{
        	Turn.setText("Black to Play");
        	Turn.setForeground(Color.black);
        }
	}
	public void Game_Set_Init(){
		Turn.setText("No Game Stared");
		Log.setText("");
	}
}
