package trax;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Mouse implements MouseListener {
	int x,y;
	static int check_res = 23;
	JButton entered;
	@Override
	public void mouseEntered(MouseEvent e){
		entered = (JButton) e.getSource();
		y = (int) entered.getClientProperty("y");
		x = (int) entered.getClientProperty("x");
		String s = "За : " + y + ", ї­ : " + x;
		entered.setToolTipText(s);
	}
	@Override
	public void mouseClicked(MouseEvent e)
	{
		int std = 0;
		JButton clicked = (JButton) e.getSource();
		y = (int) clicked.getClientProperty("y");
		x = (int) clicked.getClientProperty("x");
		if(Button.IG.win == 0){
			if(Button.IG.first == 1){
				std = Button.IG.proper_place(x,y);
				if(std == 1){
					if(e.getButton() == 3 && Button.IG.check_tile_type[y][x] != 0){
						Button.IG.changeTile(1,x,y,clicked);
						Button.IG.set_max_min_x(x);
						Button.IG.set_max_min_y(y);
						Button.IG.viewer.G_S.print_Log(x, y, Button.IG.check_tile_type[y][x]);
						Button.IG.checkWinWhite_8tile(y,x);
						Button.IG.checkWinBlack_8tile(y,x);
						if(Button.IG.win==0 && Button.IG.checked_win_tiles[y][x]==0 && Button.IG.exist[y][x]==1) {
							Button.IG.make_zero();
							Button.IG.start_x=x;
							Button.IG.start_y=y;
							Button.IG.checkWin_white(x,y);
							Button.IG.check_win_cnt=-1;
							System.out.println("");
							
							if(Button.IG.win==0) {
								Button.IG.make_zero();
								Button.IG.checkWin_black(x,y);
								Button.IG.check_win_cnt=-1;
								System.out.println("");
							}
						}
						if(Button.IG.win == 0){
							Button.IG.All_click_cnt++;
							Button.IG.viewer.G_S.Set_Turn();
						}
						//
						Button.IG.ai.AI_win_check(x, y);
						Button.IG.ai.ai_win_white_check.line_win_check(10);
						Button.IG.ai.ai_win_black_check.line_win_check(11);
						//
					}
					else{
						if(Button.IG.exist[y][x]==0){
							Button.IG.changeTile(0,x,y,clicked);
						}
					}
				}
			}
			else{
				if(e.getButton() == 3 && Button.IG.check_tile_type[y][x] != 0 && x == 32 && y == 32){
					Button.IG.first = 1;
					Button.IG.changeTile(1,x,y,clicked);
					Button.IG.viewer.G_S.print_Log(x, y, Button.IG.check_tile_type[y][x]);
					Button.IG.All_click_cnt++;
					Button.IG.viewer.G_S.Set_Turn();
					Button.IG.max_x = 32;
					Button.IG.min_x = 32;
					Button.IG.max_y = 32;
					Button.IG.min_y = 32;
					//
					Button.IG.ai.AI_win_check(x, y);
					//
				}
				else if (e.getButton() == 1){
					Button.IG.viewer.G_S.Set_Turn();
					Button.IG.changeTile(0, 32, 32, Button.IG.btns[32][32].getBtn());
				}
			}
		}
		else if(Button.IG.win == 1){
			if(Button.IG.win_color == 1){
				check_res = JOptionPane.showConfirmDialog(null,"The White Team Win\nCongratulation\nStart a new game?","Game is over.",JOptionPane.YES_NO_OPTION);
			}
			else if(Button.IG.win_color == 2){
				check_res = JOptionPane.showConfirmDialog(null,"The Black Team Win\nCongratulation\nStart a new game?","Game is over.",JOptionPane.YES_NO_OPTION);
			}
			if(check_res == JOptionPane.YES_OPTION){
				Button.IG.viewer.Init_Trax();
			}
		}
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
