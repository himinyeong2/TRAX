package trax;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class InGame {
	public int [][]checked_win_tiles;
	private int garo_white=0, garo_black=0, sero_white=0, sero_black=0;
	public ImageIcon [] tile_image;
	public ImageIcon [] tile_image_tmp;
	public ImageIcon [] tile_image_win;
	public ImageIcon back;
	public int check_win_cnt = -1;
	public int [][] exist;
	public int first = 0;
	public int [][] check_tile_type;
	public int [][][][] valid_tile;
	public int last_x=-1,last_y=-1;
	public int win_color = 0;
	public int start_x, start_y,win=0;
	public int max_x,min_x,max_y,min_y;
	public Button [][] btns;
	public int All_click_cnt = 0;
	public int [][][] tile_dir;
	final int x_len = 64;
	final int y_len = 64;
	final int north = 0;
	final int east = 1;
	final int south = 2;
	final int west = 3;
	final int tile_cnt = 6;
	final int tile_1 = 1;
	final int tile_2 = 2;
	final int tile_3 = 3;
	final int tile_4 = 4;
	final int tile_5 = 5;
	final int tile_6 = 6;
	final int white = 10;
	final int black = 11;
	TraxViewer viewer;
	AI ai;
	public InGame(Button [][] btns, TraxViewer viewer){
		this.viewer = viewer;
		this.btns = btns;
		checked_win_tiles = new int[y_len][x_len];
		tile_dir = new int[y_len][x_len][4];
		tile_image = new ImageIcon[tile_cnt];
		back = new ImageIcon("images/trax_background.png");
		for(int i=0;i<tile_cnt;i++){
			String s1 = "images/trax_tile_";
			String s2 = ".png";
			String res;
			res = s1 + String.valueOf(i+1) + s2;
			tile_image[i] = new ImageIcon(res);
		}
		tile_image_tmp = new ImageIcon[tile_cnt];
		for(int i=0;i<tile_cnt;i++){
			String s1 = "images/trax_tile_";
			String s2 = "_tmp.png";
			String res;
			res = s1 + String.valueOf(i+1) + s2;
			tile_image_tmp[i] = new ImageIcon(res);
		}
		tile_image_win = new ImageIcon[tile_cnt];
		for(int i=0;i<tile_cnt;i++){
			String s1 = "images/trax_tile_win_";
			String s2 = ".png";
			String res;
			res = s1 + String.valueOf(i+1) + s2;
			tile_image_win[i] = new ImageIcon(res);
		}
		valid_tile = new int[y_len][x_len][4][3];
		exist = new int[y_len][x_len];
		check_tile_type = new int[y_len][x_len];
		ai = new AI(this);
	}
	
	public void Init(){
		check_win_cnt = -1;
		last_x = -1;
		last_y = -1;
		first = 0;
		start_x = -1;
		start_y = -1;
		win = 0;
		win_color = 0;
		All_click_cnt = 0;
		for(int i=0;i<y_len;i++){
			for(int j=0;j<x_len;j++){
				exist[i][j] = 0;
				check_tile_type[i][j] = 0;
				for(int p=0;p<4;p++){
					for(int q=0;q<3;q++){
						valid_tile[i][j][p][q]=0;
					}
				}
				for(int p=0;p<4;p++){
					tile_dir[i][j][p] = 0;
				}
				btns[i][j].Init();
			}
		}
		make_zero();
	}
	
	public void make_zero() {
		for(int i=0; i<x_len; i++) {
			for(int j=0; j<y_len; j++) {
				checked_win_tiles[j][i] = 0;
			}
		}
		garo_white=0; 
		garo_black=0; 
		sero_white=0; 
		sero_black=0;
	}
	
	public void tile_dir_set(int x, int y){
		if(check_tile_type[y][x] == tile_1 || check_tile_type[y][x] == tile_2 || check_tile_type[y][x] == tile_6){
			tile_dir[y][x][north] = black;
		}
		else{
			tile_dir[y][x][north] = white;
		}
		if(check_tile_type[y][x] == tile_1 || check_tile_type[y][x] == tile_4 || check_tile_type[y][x] == tile_6){
			tile_dir[y][x][east] = white;
		}
		else{
			tile_dir[y][x][east] = black;
		}
		if(check_tile_type[y][x] == tile_1 || check_tile_type[y][x] == tile_2 || check_tile_type[y][x] == tile_5){
			tile_dir[y][x][south] = white;
		}
		else{
			tile_dir[y][x][south] = black;
		}
		if(check_tile_type[y][x] == tile_1 || check_tile_type[y][x] == tile_4 || check_tile_type[y][x] == tile_5){
			tile_dir[y][x][west] = black;
		}
		else{
			tile_dir[y][x][west] = white;
		}
	}
	
	public int checkWin_white(int x, int y) {
		int std = 0;
		if(win==1) {
			win_color = 1;
			return 1;
		}
		
		//System.out.println(x+" , "+y);
		int north_on=0;
		int south_on=0;
		int east_on=0;
		int west_on=0;
		
		checked_win_tiles[y][x]++;
		check_win_cnt++;
		if(check_win_cnt>2) {
			checked_win_tiles[start_y][start_x]=0;
			if(x==start_x && y==start_y) {
				System.out.printf("error EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE|n");
				win++;
				win_color = 1;
				btns[y][x].set_image(check_tile_type[y][x] + 20);
				return 1;
			}
		}
		
		if(check_tile_type[y][x]==1) {
			east_on++;
			south_on++;
			if(garo_white>0 || sero_white>0) {
				garo_white=0;
				sero_white=0;
			}
		}
		else if(check_tile_type[y][x]==2) {
			west_on++;
			south_on++;
			if(garo_white>0 || sero_white>0) {
				garo_white=0;
				sero_white=0;
			}
		}
		else if(check_tile_type[y][x]==3) {
			west_on++;
			north_on++;
			if(garo_white>0 || sero_white>0) {
				garo_white=0;
				sero_white=0;
			}
		}
		else if(check_tile_type[y][x]==4) {
			east_on++;
			north_on++;
			if(garo_white>0 || sero_white>0) {
				garo_white=0;
				sero_white=0;
			}
		}
		else if(check_tile_type[y][x]==5) {
			south_on++;
			north_on++;
			sero_white++;
			if(sero_white==8) {
				win=1;
				System.out.println("white win");
				win_color = 1;
				btns[y][x].set_image(check_tile_type[y][x] + 20);
				return 1;
			}
			if(garo_white>0) {
				garo_white=0;
			}
		}
		else if(check_tile_type[y][x]==6) {
			east_on++;
			west_on++;
			garo_white++;
			if(garo_white==8) {
				win=1;
				System.out.println("white win");
				win_color = 1;
				btns[y][x].set_image(check_tile_type[y][x] + 20);
				return 1;
			}
			if(sero_white>0) {
				sero_white=0;
			}
		}
		
		if(north_on==1 && y>0) {
			for(int i=0; i<3; i++) {
				if(check_tile_type[y-1][x]==valid_tile[y][x][north][i] && checked_win_tiles[y-1][x]==0) {
					std = checkWin_white(x,y-1);
				}
			}
		}
		
		if(east_on==1 && x<63) {
			for(int i=0; i<3; i++) {
				if(check_tile_type[y][x+1]==valid_tile[y][x][east][i] &&  checked_win_tiles[y][x+1]==0) {
					std = checkWin_white(x+1,y);
				}
			}
		}
		
		if(south_on==1 && y<63) {
			for(int i=0; i<3; i++) {
				if(check_tile_type[y+1][x]==valid_tile[y][x][south][i] && checked_win_tiles[y+1][x]==0) {
					std = checkWin_white(x,y+1);
				}
			}
		}
		
		if(west_on==1 && x>0) {
			for(int i=0; i<3; i++) {
				if(check_tile_type[y][x-1]==valid_tile[y][x][west][i] && checked_win_tiles[y][x-1]==0) {
					std = checkWin_white(x-1,y);
				}
			}
		}
		if(std == 1){
			btns[y][x].set_image(check_tile_type[y][x] + 20);
		}
		check_win_cnt--;
		checked_win_tiles[start_y][start_x]=1;
		return std;
	}
	
	public int checkWin_black(int x, int y) {
		int std = 0;
		if(win==1) {
			win_color = 2;
			return 1;
		}
		
		//System.out.println(x+" , "+y);
		int north_on=0;
		int south_on=0;
		int east_on=0;
		int west_on=0;
		
		checked_win_tiles[y][x]++;
		check_win_cnt++;
		if(check_win_cnt>2) {
			checked_win_tiles[start_y][start_x]=0;
			if(x==start_x && y==start_y) {
				win++;
				win_color = 2;
				btns[y][x].set_image(check_tile_type[y][x] + 20);
				return 1;
			}
		}
		
		if(check_tile_type[y][x]==1) {
			west_on++;
			north_on++;
			if(garo_black>0 || sero_black>0) {
				garo_black=0;
				sero_black=0;
			}
		}
		else if(check_tile_type[y][x]==2) {
			east_on++;
			north_on++;
			if(garo_black>0 || sero_black>0) {
				garo_black=0;
				sero_black=0;
			}
		}
		else if(check_tile_type[y][x]==3) {
			east_on++;
			south_on++;
			if(garo_black>0 || sero_black>0) {
				garo_black=0;
				sero_black=0;
			}
		}
		else if(check_tile_type[y][x]==4) {
			west_on++;
			south_on++;
			if(garo_black>0 || sero_black>0) {
				garo_black=0;
				sero_black=0;
			}
		}
		else if(check_tile_type[y][x]==5) {
			east_on++;
			west_on++;
			garo_black++;
			if(garo_black==8) {
				win=1;
				System.out.println("black win");
				win_color = 2;
				btns[y][x].set_image(check_tile_type[y][x] + 20);
				return 1;
			}
			if(sero_black>0) {
				sero_black=0;
			}
		}
		else if(check_tile_type[y][x]==6) {
			north_on++;
			south_on++;
			sero_black++;
			if(sero_black==8) {
				win=1;
				System.out.println("black win");
				win_color = 2;
				btns[y][x].set_image(check_tile_type[y][x] + 20);
				return 1;
			}
			if(garo_black>0) {
				garo_black=0;
			}
		}
		
		if(north_on==1 && y>0) {
			for(int i=0; i<3; i++) {
				if(check_tile_type[y-1][x]==valid_tile[y][x][north][i] && checked_win_tiles[y-1][x]==0) {
					std = checkWin_black(x,y-1);
				}
			}
		}
		
		if(east_on==1 && x<63) {
			for(int i=0; i<3; i++) {
				if(check_tile_type[y][x+1]==valid_tile[y][x][east][i] &&  checked_win_tiles[y][x+1]==0) {
					std = checkWin_black(x+1,y);
				}
			}
		}
		
		if(south_on==1 && y<63) {
			for(int i=0; i<3; i++) {
				if(check_tile_type[y+1][x]==valid_tile[y][x][south][i] && checked_win_tiles[y+1][x]==0) {
					std = checkWin_black(x,y+1);
				}
			}
		}
		
		if(west_on==1 && x>0) {
			for(int i=0; i<3; i++) {
				if(check_tile_type[y][x-1]==valid_tile[y][x][west][i] && checked_win_tiles[y][x-1]==0) {
					std = checkWin_black(x-1,y);
				}
			}
		}
		if(std == 1){
			btns[y][x].set_image(check_tile_type[y][x] + 20);
		}
		check_win_cnt--;
		checked_win_tiles[start_y][start_x]=1;
		return std;
	}
	
	public void changeTile(int std, int x, int y, JButton btn) {
		int ran;
		if(std == 0){
			if(first == 0){
				ran = 2+2*((btns[y][x].click_cnt)%2);
				btns[y][x].click_cnt++;
			}
			else ran = ran_tile(x,y);
			
			check_tile_type[y][x] = ran+1;
			btn.setIcon(tile_image_tmp[ran]);
		}
		else if(std == 1 && exist[y][x] == 0){
			exist[y][x]=1;
			btn.setIcon(tile_image[check_tile_type[y][x] - 1]);
			valid_T(x,y);
			auto_set(x,y);
			System.out.printf("%d %d\n", y,x);
		}
		else if(std > 20){
			exist[y][x] = 1;
			btn.setIcon(tile_image[std - 20 - 1]);
			check_tile_type[y][x] = std - 20;
			set_max_min_x(x);
			set_max_min_y(y);
			valid_T(x,y);
			auto_set(x,y);
		}
		if(last_y>=0 && (last_y != y || last_x != x) && exist[last_y][last_x] ==0){
			btns[last_y][last_x] .set_image(10);
			check_tile_type[last_y][last_x] = 0;
		}
		last_x = x;
		last_y = y;
	}
	
	public void auto_set(int x, int y){
		int [] po = new int [4];
		int change = 0;
		po = where_tile(x,y);
		if(exist[y][x] == 0){
			int cnt=0,std=0;
			for(int i=0;i<4;i++){
				if(po[i] == 1){
					cnt++;
				}
			}
			if(cnt == 1) return;
			else if(cnt == 2){
				if((po[0] == 1 && po[2] == 1) || (po[1] == 1 && po[3] == 1)){
					if(po[0] == 1 && po[2] == 1 && change == 0){
						if(check_tile_type[y-1][x]==tile_1 || check_tile_type[y-1][x]==tile_2 || check_tile_type[y-1][x]==tile_5){
							if(check_tile_type[y+1][x]==tile_3 || check_tile_type[y+1][x]==tile_4 || check_tile_type[y+1][x]==tile_5){
								std = 1;
							}
						}
						if(check_tile_type[y-1][x]==tile_3 || check_tile_type[y-1][x]==tile_4 || check_tile_type[y-1][x]==tile_6){
							if(check_tile_type[y+1][x]==tile_1 || check_tile_type[y+1][x]==tile_2 || check_tile_type[y+1][x]==tile_6){
								std = 2;
							}
						}
						if(std == 1){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_5;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_5);
						}
						else if(std == 2){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_6;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_5);
						}
						else return;
					}
					if(po[1] == 1 && po[3] == 1 && change == 0){
						if(check_tile_type[y][x-1]==tile_2 || check_tile_type[y][x-1]==tile_3 || check_tile_type[y][x-1]==tile_5){
							if(check_tile_type[y][x+1]==tile_1 || check_tile_type[y][x+1]==tile_4 || check_tile_type[y][x+1]==tile_5){
								std = 1;
							}
						}
						if(check_tile_type[y][x-1]==tile_1 || check_tile_type[y][x-1]==tile_4 || check_tile_type[y][x-1]==tile_6){
							if(check_tile_type[y][x+1]==tile_2 || check_tile_type[y][x+1]==tile_3 || check_tile_type[y][x+1]==tile_6){
								std = 2;
							}
						}
						if(std == 1){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_5;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_5);
						}
						else if(std == 2){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_6;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_6);
						}
						else return;
					}
				}
				
				if(po[0] == 1 && change == 0){
					if(po[1] == 1){
						if(check_tile_type[y-1][x]==tile_3 || check_tile_type[y-1][x]==tile_4 || check_tile_type[y-1][x]==tile_6){
							if(check_tile_type[y][x+1]==tile_1 || check_tile_type[y][x+1]==tile_4 || check_tile_type[y][x+1]==tile_5){
								std = 1;
							}
						}
						if(check_tile_type[y-1][x]==tile_1 || check_tile_type[y-1][x]==tile_2 || check_tile_type[y-1][x]==tile_5){
							if(check_tile_type[y][x+1]==tile_2 || check_tile_type[y][x+1]==tile_3 || check_tile_type[y][x+1]==tile_6){
								std = 2;
							}
						}
						if(std == 1){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_2;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_2);
						}
						else if(std == 2){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_4;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_4);
						}
						else return;
					}
					else if(po[3] == 1){
						if(check_tile_type[y-1][x]==tile_3 || check_tile_type[y-1][x]==tile_4 || check_tile_type[y-1][x]==tile_6){
							if(check_tile_type[y][x-1]==tile_2 || check_tile_type[y][x-1]==tile_3 || check_tile_type[y][x-1]==tile_5){
								std = 1;
							}
						}
						if(check_tile_type[y-1][x]==tile_1 || check_tile_type[y-1][x]==tile_2 || check_tile_type[y-1][x]==tile_5){
							if(check_tile_type[y][x-1]==tile_1 || check_tile_type[y][x-1]==tile_4 || check_tile_type[y][x-1]==tile_6){
								std = 2;
							}
						}
						if(std == 1){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_1;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_1);
						}
						else if(std == 2){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_3;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_3);
						}
						else return;
					}
				}
				else if(po[2] == 1 && change == 0){
					if(po[1] == 1){
						if(check_tile_type[y][x+1]==tile_2 || check_tile_type[y][x+1]==tile_3 || check_tile_type[y][x+1]==tile_6){
							if(check_tile_type[y+1][x]==tile_3 || check_tile_type[y+1][x]==tile_4 || check_tile_type[y+1][x]==tile_5){
								std = 1;
							}
						}
						if(check_tile_type[y][x+1]==tile_1 || check_tile_type[y][x+1]==tile_4 || check_tile_type[y][x+1]==tile_5){
							if(check_tile_type[y+1][x]==tile_1 || check_tile_type[y+1][x]==tile_2 || check_tile_type[y+1][x]==tile_6){
								std = 2;
							}
						}
						if(std == 1){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_1;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_1);
						}
						else if(std == 2){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_3;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_3);
						}
						else return;
					}
					else if(po[3] == 1){
						if(check_tile_type[y][x-1]==tile_1 || check_tile_type[y][x-1]==tile_4 || check_tile_type[y][x-1]==tile_6){
							if(check_tile_type[y+1][x]==tile_3 || check_tile_type[y+1][x]==tile_4 || check_tile_type[y+1][x]==tile_5){
								std = 1;
							}
						}
						if(check_tile_type[y][x-1]==tile_2 || check_tile_type[y][x-1]==tile_3 || check_tile_type[y][x-1]==tile_5){
							if(check_tile_type[y+1][x]==tile_1 || check_tile_type[y+1][x]==tile_2 || check_tile_type[y+1][x]==tile_6){
								std = 2;
							}
						}
						if(std == 1){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_2;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_2);
						}
						else if(std == 2){
							exist[y][x] = 1;
							check_tile_type[y][x] = tile_4;
							valid_T(x,y);
							change = 1;
							btns[y][x].set_image(tile_4);
						}
						else return;
					}
				}
				
			}
			else if(cnt == 3){
				int tile_num_1=0, tile_num_2=0, tile_num_3=0, tile_num_4=0, tile_num_5=0, tile_num_6=0;
				if(po[0] == 1){
					if(check_tile_type[y-1][x] == tile_1 || check_tile_type[y-1][x] == tile_2 || check_tile_type[y-1][x] == tile_5){
						tile_num_3++;
						tile_num_4++;
						tile_num_5++;
					}
					else if(check_tile_type[y-1][x] == tile_3 || check_tile_type[y-1][x] == tile_4 || check_tile_type[y-1][x] == tile_6){
						tile_num_1++;
						tile_num_2++;
						tile_num_6++;
					}
				}
				if(po[1] == 1){
					if(check_tile_type[y][x+1] == tile_1 || check_tile_type[y][x+1] == tile_4 || check_tile_type[y][x+1] == tile_5){
						tile_num_2++;
						tile_num_3++;
						tile_num_5++;
					}
					else if(check_tile_type[y][x+1] == tile_2 || check_tile_type[y][x+1] == tile_3 || check_tile_type[y][x+1] == tile_6){
						tile_num_1++;
						tile_num_4++;
						tile_num_6++;
					}
				}
				if(po[2] == 1){
					if(check_tile_type[y+1][x] == tile_1 || check_tile_type[y+1][x] == tile_2 || check_tile_type[y+1][x] == tile_6){
						tile_num_3++;
						tile_num_4++;
						tile_num_6++;
					}
					else if(check_tile_type[y+1][x] == tile_3 || check_tile_type[y+1][x] == tile_4 || check_tile_type[y+1][x] == tile_5){
						tile_num_1++;
						tile_num_2++;
						tile_num_5++;
					}
				}
				if(po[3] == 1){
					if(check_tile_type[y][x-1] == tile_1 || check_tile_type[y][x-1] == tile_4 || check_tile_type[y][x-1] == tile_6){
						tile_num_2++;
						tile_num_3++;
						tile_num_6++;
					}
					else if(check_tile_type[y][x-1] == tile_2 || check_tile_type[y][x-1] == tile_3 || check_tile_type[y][x-1] == tile_5){
						tile_num_1++;
						tile_num_4++;
						tile_num_5++;
					}
				}
				if(tile_num_1 == 3){
					exist[y][x] = 1;
					check_tile_type[y][x] = tile_1;
					valid_T(x,y);
					change = 1;
					btns[y][x].set_image(tile_1);
				}
				else if(tile_num_2 == 3){
					exist[y][x] = 1;
					check_tile_type[y][x] = tile_2;
					valid_T(x,y);
					change = 1;
					btns[y][x].set_image(tile_2);
				}
				else if(tile_num_3 == 3){
					exist[y][x] = 1;
					check_tile_type[y][x] = tile_3;
					valid_T(x,y);
					change = 1;
					btns[y][x].set_image(tile_3);
				}
				else if(tile_num_4 == 3){
					exist[y][x] = 1;
					check_tile_type[y][x] = tile_4;
					valid_T(x,y);
					change = 1;
					btns[y][x].set_image(tile_4);
				}
				else if(tile_num_5 == 3){
					exist[y][x] = 1;
					check_tile_type[y][x] = tile_5;
					valid_T(x,y);
					change = 1;
					btns[y][x].set_image(tile_5);
				}
				else if(tile_num_6 == 3){
					exist[y][x] = 1;
					check_tile_type[y][x] = tile_6;
					valid_T(x,y);
					change = 1;
					btns[y][x].set_image(tile_6);
				}
				if(change == 0)return;
			}
		}
		if(po[0] == 0){
			auto_set(x,y-1);
		}
		if(po[1] == 0){
			auto_set(x+1,y);
		}
		if(po[2] == 0){
			auto_set(x,y+1);
		}
		if(po[3] == 0){
			auto_set(x-1,y);
		}
		
		if(win==0 && checked_win_tiles[y][x]==0 && exist[y][x]==1) {
			make_zero();
			start_x=x;
			start_y=y;
			checkWin_white(x,y);
			check_win_cnt=-1;
			System.out.println("");
			
			if(win==0) {
				checked_win_tiles[y][x]=0;
				make_zero();
				checkWin_black(x,y);
				check_win_cnt=-1;
				System.out.println("");
			}
		}
	}
	
	public int ran_tile(int x, int y){
		int [] po = new int [4];
		po = where_tile(x,y);
		int ran_num=8;
		int cnt=0;
		for(int i=0;i<4;i++){
			if(po[i] == 1){
				cnt++;
			}
		}
		if(cnt == 1){
			if(po[0] == 1){
				while(true){
					ran_num = valid_tile[y-1][x][south][btns[y][x].click_cnt%3];
					if(ran_num!=0)break;
					btns[y][x].click_cnt++;
				}
			}
			if(po[1] == 1){
				while(true){
					ran_num = valid_tile[y][x+1][west][btns[y][x].click_cnt%3];
					if(ran_num!=0)break;
					btns[y][x].click_cnt++;
				}
			}
			if(po[2] == 1){
				while(true){
					ran_num = valid_tile[y+1][x][north][btns[y][x].click_cnt%3];
					if(ran_num!=0)break;
					btns[y][x].click_cnt++;
				}
			}
			if(po[3] == 1){
				while(true){
					ran_num = valid_tile[y][x-1][east][btns[y][x].click_cnt%3];
					if(ran_num!=0)break;
					btns[y][x].click_cnt++;
				}
			}
		}
		else if(cnt == 2){
			int [] only = new int[3];
			int i,j,k=0;
			if(po[0] == 1){
				if(po[1] == 1){
					for(i=0;i<3;i++){
						for(j=0;j<3;j++){
							if(valid_tile[y-1][x][south][i] == valid_tile[y][x+1][west][j]){
								only[k]=valid_tile[y-1][x][south][i];
								k++;
							}
						}
					}
				}
				else if(po[2] == 1){
					for(i=0;i<3;i++){
						for(j=0;j<3;j++){
							if(valid_tile[y-1][x][south][i] == valid_tile[y+1][x][north][j]){
								only[k]=valid_tile[y-1][x][south][i];
								k++;
							}
						}
					}
				}
				else if(po[3] == 1){
					for(i=0;i<3;i++){
						for(j=0;j<3;j++){
							if(valid_tile[y-1][x][south][i] == valid_tile[y][x-1][east][j]){
								only[k]=valid_tile[y-1][x][south][i];
								k++;
							}
						}
					}
				}
			}
			else if(po[1] == 1){
				if(po[2] == 1){
					for(i=0;i<3;i++){
						for(j=0;j<3;j++){
							if(valid_tile[y][x+1][west][i] == valid_tile[y+1][x][north][j]){
								only[k]=valid_tile[y][x+1][west][i];
								k++;
							}
						}
					}
				}
				else if(po[3] == 1){
					for(i=0;i<3;i++){
						for(j=0;j<3;j++){
							if(valid_tile[y][x+1][west][i] == valid_tile[y][x-1][east][j]){
								only[k]=valid_tile[y][x+1][west][i];
								k++;
							}
						}
					}
				}
			}
			else if(po[2] == 1){
				if(po[3] == 1){
					for(i=0;i<3;i++){
						for(j=0;j<3;j++){
							if(valid_tile[y+1][x][north][i] == valid_tile[y][x-1][east][j]){
								only[k]=valid_tile[y+1][x][north][i];
								k++;
							}
						}
					}
				}
			}
			while(true){
				ran_num = only[btns[y][x].click_cnt%3];
				if(ran_num!=0)break;
				btns[y][x].click_cnt++;
			}
		}
		btns[y][x].click_cnt++;
		return ran_num-1;
	}
	
	public int [] where_tile(int x, int y){
		int [] po = new int [4];
		if(x == 0){
			po[3] = -1;
		}
		else if(exist[y][x-1] == 1){
			po[3] = 1;
		}
		if(y == 0){
			po[0] = -1;
		}
		else if(exist[y-1][x] == 1){
			po[0] = 1;
		}
		if(x == x_len - 1){
			po[1] = -1;
		}
		else if(exist[y][x+1] == 1){
			po[1] = 1;
		}
		if(y == y_len - 1){
			po[2] = -1;
		}
		else if(exist[y+1][x] == 1){
			po[2] = 1;
		}
		return po;
	}
	public void valid_T(int x, int y){
		tile_dir_set(x,y);
		if(check_tile_type[y][x] == 1){//0遺� 1 �룞 2 �궓 3�꽌
			valid_tile[y][x][west][0] = valid_tile[y][x][east][0] = 2;
			valid_tile[y][x][west][1] = valid_tile[y][x][south][0] = valid_tile[y][x][east][1] = valid_tile[y][x][north][0] = 3;
			valid_tile[y][x][south][1] = valid_tile[y][x][north][1] = 4;
			valid_tile[y][x][west][2] = valid_tile[y][x][south][2] = 5;
			valid_tile[y][x][east][2] = valid_tile[y][x][north][2] = 6;
			
		}
		else if(check_tile_type[y][x] == 2){
			valid_tile[y][x][west][0] = valid_tile[y][x][east][0] = 1;
			valid_tile[y][x][south][0] = valid_tile[y][x][north][0] = 3;
			valid_tile[y][x][west][1] = valid_tile[y][x][south][1] = valid_tile[y][x][east][1] = valid_tile[y][x][north][1] = 4;
			valid_tile[y][x][south][2] = valid_tile[y][x][east][2] = 5;
			valid_tile[y][x][west][2] = valid_tile[y][x][north][2] = 6;
			
		}
		else if(check_tile_type[y][x] == 3){
			valid_tile[y][x][west][0] = valid_tile[y][x][south][0] = valid_tile[y][x][east][0] = valid_tile[y][x][north][0] = 1;
			valid_tile[y][x][south][1] = valid_tile[y][x][north][1] = 2;
			valid_tile[y][x][west][1] = valid_tile[y][x][east][1] = 4;
			valid_tile[y][x][east][2] = valid_tile[y][x][north][2] = 5;
			valid_tile[y][x][west][2] = valid_tile[y][x][south][2] = 6;
			
		}
		else if(check_tile_type[y][x] == 4){
			valid_tile[y][x][south][0] = valid_tile[y][x][north][0] = 1;
			valid_tile[y][x][west][0] = valid_tile[y][x][south][1] = valid_tile[y][x][east][0] = valid_tile[y][x][north][1] = 2;
			valid_tile[y][x][west][1] = valid_tile[y][x][east][1] = 3;
			valid_tile[y][x][west][2] = valid_tile[y][x][north][2] = 5;
			valid_tile[y][x][south][2] = valid_tile[y][x][east][2] = 6;
			
			
		}
		else if(check_tile_type[y][x] == 5){
			valid_tile[y][x][east][0] = valid_tile[y][x][north][0] = 1;
			valid_tile[y][x][west][0] = valid_tile[y][x][north][1] = 2;
			valid_tile[y][x][west][1] = valid_tile[y][x][south][0] = 3;
			valid_tile[y][x][south][1] = valid_tile[y][x][east][1] = 4;
			valid_tile[y][x][west][2] = valid_tile[y][x][south][2] = valid_tile[y][x][east][2] = valid_tile[y][x][north][2] = 5;
			
		}
		else if(check_tile_type[y][x] == 6){
			valid_tile[y][x][west][0] = valid_tile[y][x][south][0] = 1;
			valid_tile[y][x][south][1] = valid_tile[y][x][east][0] = 2;
			valid_tile[y][x][east][1] = valid_tile[y][x][north][0] = 3;
			valid_tile[y][x][west][1] = valid_tile[y][x][north][1] = 4;
			valid_tile[y][x][west][2] = valid_tile[y][x][south][2] = valid_tile[y][x][east][2] = valid_tile[y][x][north][2] = 6;
			
		}
	}
	public int proper_place(int x, int y){
		int [] po;
		if(exist[y][x] == 1){
			return -1;
		}
		po = where_tile(x,y);
		for(int i=0;i<4;i++){
			if(po[i] == 1){
				return 1;
			}
		}
		return 0;
	}
	

	public int CheckBlack_8tileEnd(int i, int j, int before_i,int before_j) {
	      int start_i = i, start_j = j;
	      while(check_tile_type[i][j]!=0) {
	         checked_win_tiles[i][j]++;
	         checked_win_tiles[before_i][before_j]++;
	         if (check_tile_type[i][j] == 1) {
	            if (before_i == i - 1 && before_j == j) {
	               before_i = i;
	               before_j = j;
	               j = j - 1;
	            } else {
	               before_i = i;
	               before_j = j;
	               i = i - 1;
	            }
	         } else if (check_tile_type[i][j] == 2) {
	            if (before_i == i - 1 && before_j == j) {
	               before_i = i;
	               before_j = j;
	               j = j + 1;
	            } else {
	               before_i = i;
	               before_j = j;
	               i = i - 1;
	            }
	         } else if (check_tile_type[i][j] == 3) {
	            if (before_i == i + 1 && before_j == j) {
	               before_i = i;
	               before_j = j;
	               j = j + 1;
	            } else {
	               before_i = i;
	               before_j = j;
	               i = i + 1;
	            }
	         } else if (check_tile_type[i][j] == 4) {
	            if (before_i == i + 1 && before_j == j) {
	               before_i = i;
	               before_j = j;
	               j = j - 1;

	            } else {
	               before_i = i;
	               before_j = j;
	               i = i + 1;
	            }
	         }

	         else if (check_tile_type[i][j] == 5) {
	            if (before_i == i && before_j == j - 1) {
	               before_i = i;
	               before_j = j;
	               j = j + 1;
	            } else {
	               before_i = i;
	               before_j = j;
	               j = j - 1;
	            }
	         }

	         else if (check_tile_type[i][j] == 6) {
	            if (before_i == i - 1 && before_j == j) {
	               before_i = i;
	               before_j = j;
	               i = i + 1;
	            } else {
	               before_i = i;
	               before_j = j;
	               i = i - 1;
	            }
	         }
	         if(start_i== i && start_j == j) {
	            return 0;
	         }
	      }

	      return 64 * before_i + before_j;
	   }
	   public int CheckWhite_8tileEnd(int i, int j, int before_i, int before_j) {
	      int start_i = i, start_j = j;
	      while (check_tile_type[i][j] != 0) {
	         checked_win_tiles[i][j]++;
	         checked_win_tiles[before_i][before_j]++;
	         if (check_tile_type[i][j] == 1) { // about tile img  1
	            if (before_i == i + 1 && before_j == j) {
	               before_i = i;
	               before_j = j;
	               j = j + 1;
	            } else {
	               before_i = i;
	               before_j = j;
	               i = i + 1;
	            } // inner if - else
	         }
	         else if (check_tile_type[i][j] == 2) { // about tile img  2
	            if (before_i == i + 1 && before_j == j) {
	               before_i = i;
	               before_j = j;
	               j = j - 1;
	            } else {
	               before_i = i;
	               before_j = j;
	               i = i + 1;
	            } // inner if - else
	         }
	         else if (check_tile_type[i][j] == 3) { // about tile img  3
	            if (before_i == i - 1 && before_j == j) {
	               before_i = i;
	               before_j = j;
	               j = j - 1;
	            } else {
	               before_i = i;
	               before_j = j;
	               i = i - 1;
	            }
	         }
	         else if (check_tile_type[i][j] == 4) { // about tile img  4
	            if (before_i == i - 1 && before_j == j) {
	               before_i = i;
	               before_j = j;
	               j = j + 1;
	            } else {
	               before_i = i;
	               before_j = j;
	               i = i - 1;
	            } // inner if - else
	         } 
	         else if (check_tile_type[i][j] == 5) { // about tile img  5
	            if (before_i == i - 1 && before_j == j) {
	               before_i = i;
	               before_j = j;
	               i = i + 1;

	            } else {
	               before_i = i;
	               before_j = j;
	               i = i - 1;
	            } // inner if - else
	         } 
	         else if (check_tile_type[i][j] == 6) { // about tile img  6
	            if (before_i == i && before_j == j - 1) {
	               before_i = i;
	               before_j = j;
	               j = j + 1;
	            } else {
	               before_i = i;
	               before_j = j;
	               j = j - 1;
	            } // inner if - else
	         }
	         if(start_i== i && start_j == j) {
	            return 0;
	         }
	      }
	      return 64 * before_i + before_j;
	   } // 
	   public boolean checkWinBlack_8tile(int i_idx, int j_idx) {
	      int ld_i = 0, ld_j = 0;
	      int rd_i=0, rd_j=0;
	      int endRd_i = 0, endRd_j = 0;
	      int endLd_i = 0, endLd_j = 0;
	      int xDistance = 0, yDistance = 0;
	      if (check_tile_type[i_idx][j_idx] == 1) {
	         ld_i = i_idx - 1;
	         ld_j = j_idx;
	         rd_i = i_idx;
	         rd_j = j_idx - 1;
	      } else if (check_tile_type[i_idx][j_idx] == 2) {
	         ld_i = i_idx - 1;
	         ld_j = j_idx;
	         rd_i = i_idx;
	         rd_j = j_idx + 1;
	      } else if (check_tile_type[i_idx][j_idx] == 3) {
	         ld_i = i_idx + 1;
	         ld_j = j_idx;
	         rd_i = i_idx;
	         rd_j = j_idx + 1;
	      } else if (check_tile_type[i_idx][j_idx] == 4) {
	         ld_i = i_idx + 1;
	         ld_j = j_idx;
	         rd_i = i_idx;
	         rd_j = j_idx - 1;
	      } else if (check_tile_type[i_idx][j_idx] == 5) {
	         ld_i = i_idx;
	         ld_j = j_idx - 1;
	         rd_i = i_idx;
	         rd_j = j_idx + 1;
	      } else if (check_tile_type[i_idx][j_idx] == 6) {
	         ld_i = i_idx - 1;
	         ld_j = j_idx;
	         rd_i = i_idx + 1;
	         rd_j = j_idx;
	      } else {
	         return false;
	      }

	      endLd_i = CheckBlack_8tileEnd(ld_i, ld_j, i_idx, j_idx) / 64;
	      endLd_j = CheckBlack_8tileEnd(ld_i, ld_j, i_idx, j_idx) % 64;
	      endRd_i = CheckBlack_8tileEnd(rd_i, rd_j, i_idx, j_idx) / 64;
	      endRd_j = CheckBlack_8tileEnd(rd_i, rd_j, i_idx, j_idx) % 64;

	      xDistance = endRd_j - endLd_j;
	      yDistance = endRd_i - endLd_i;
	      if (xDistance >= 7) {
	         if ((check_tile_type[endLd_i][endLd_j] == 1 || check_tile_type[endLd_i][endLd_j] == 4
	               || check_tile_type[endLd_i][endLd_j] == 5)
	               &&( check_tile_type[endRd_i][endRd_j] == 2 || check_tile_type[endRd_i][endRd_j] == 3
	                     || check_tile_type[endRd_i][endRd_j] == 5)) {
	            
	            if(isOuterMost(endLd_j,"left") && isOuterMost(endRd_j,"right")) {
	               win++;
	               win_color = 2;
	               change();
	               System.out.println("승리");
	            }
	            return true;
	         }
	      } else if (xDistance <= -7) {
	         if ((check_tile_type[endRd_i][endRd_j] == 1 || check_tile_type[endRd_i][endRd_j] == 4
	               || check_tile_type[endRd_i][endRd_j] == 5)
	               &&( check_tile_type[endLd_i][endLd_j] == 2 || check_tile_type[endLd_i][endLd_j] == 3
	                     || check_tile_type[endLd_i][endLd_j] == 5)) {
	            
	            if(isOuterMost(endRd_j,"left") && isOuterMost(endLd_j,"right")) {
	               win++;
	               win_color = 2;
	               change();
	               System.out.println("승리");
	            }
	            return true;
	         }
	      }
	      if (yDistance >= 7) {
	         if ((check_tile_type[endLd_i][endLd_j] == 1 ||check_tile_type[endLd_i][endLd_j] == 2
	               || check_tile_type[endLd_i][endLd_j] == 6)
	               && (check_tile_type[endRd_i][endRd_j] == 3 ||check_tile_type[endRd_i][endRd_j] == 4
	                     ||check_tile_type[endRd_i][endRd_j] == 6)) {
	            if(isOuterMost(endLd_i, "up")&&isOuterMost(endRd_i,"down")) {
	            win++;
	            win_color = 2;
	            change();
	            System.out.println("승리");
	            return true;
	            }
	         }
	      } else if (yDistance <= -7) {
	         if ((check_tile_type[endRd_i][endRd_j] == 1 || check_tile_type[endRd_i][endRd_j] == 2
	               || check_tile_type[endRd_i][endRd_j] == 6)
	               && (check_tile_type[endLd_i][endLd_j] == 3 || check_tile_type[endLd_i][endLd_j] == 4
	                     || check_tile_type[endLd_i][endLd_j] == 6)) {
	            if(isOuterMost(endRd_i, "up") && isOuterMost(endLd_i,"down")) {
	            win++;
	            win_color = 2;
	            change();
	            System.out.println("승리");
	            return true;
	            }
	         }
	      }
	      make_zero();
	      return false;
	   }
	   public boolean checkWinWhite_8tile(int i_idx, int j_idx) {
	      int rd_i = 0, rd_j = 0;
	      int ld_i = 0, ld_j = 0;
	      int endRd_i = 0, endRd_j = 0;
	      int endLd_i = 0, endLd_j = 0;
	      int xDistance = 0, yDistance = 0;
	      if (check_tile_type[i_idx][j_idx] == 1) {
	         ld_i = i_idx + 1;
	         ld_j = j_idx;
	         rd_i = i_idx;
	         rd_j = j_idx + 1;
	      } else if (check_tile_type[i_idx][j_idx] == 2) {
	         ld_i = i_idx + 1;
	         ld_j = j_idx;
	         rd_i = i_idx;
	         rd_j = j_idx - 1;
	      } else if (check_tile_type[i_idx][j_idx] == 3) {
	         ld_i = i_idx - 1;
	         ld_j = j_idx;
	         rd_i = i_idx;
	         rd_j = j_idx - 1;
	      } else if (check_tile_type[i_idx][j_idx] == 4) {
	         ld_i = i_idx - 1;
	         ld_j = j_idx;
	         rd_i = i_idx;
	         rd_j = j_idx + 1;
	      } else if (check_tile_type[i_idx][j_idx] == 5) {
	         ld_i = i_idx - 1;
	         ld_j = j_idx;
	         rd_i = i_idx + 1;
	         rd_j = j_idx;

	      } else if (check_tile_type[i_idx][j_idx] == 6) {
	         ld_i = i_idx;
	         ld_j = j_idx - 1;
	         rd_i = i_idx;
	         rd_j = j_idx + 1;
	      } else
	         return false;// checkWinWhite_8tile()

	      endLd_i = CheckWhite_8tileEnd(ld_i, ld_j, i_idx, j_idx) / 64;
	      endLd_j = CheckWhite_8tileEnd(ld_i, ld_j, i_idx, j_idx) % 64;
	      endRd_i = CheckWhite_8tileEnd(rd_i, rd_j, i_idx, j_idx) / 64;
	      endRd_j = CheckWhite_8tileEnd(rd_i, rd_j, i_idx, j_idx) % 64;

	      xDistance = endRd_j - endLd_j;
	      yDistance = endRd_i - endLd_i;
	      
	      if (xDistance >= 7) {
	         if ((check_tile_type[endLd_i][endLd_j] == 2 || check_tile_type[endLd_i][endLd_j] == 3
	               || check_tile_type[endLd_i][endLd_j] == 6)
	               &&( check_tile_type[endRd_i][endRd_j] == 1 || check_tile_type[endRd_i][endRd_j] == 4
	                     || check_tile_type[endRd_i][endRd_j] == 6)) {
	            
	            if(isOuterMost(endLd_j,"left") && isOuterMost(endRd_j,"right")) {
	               win++;
	               win_color = 1;
	               change();
	               System.out.println("승리");
	            }
	            return true;
	         }
	      } else if (xDistance <= -7) {
	         if ((check_tile_type[endRd_i][endRd_j] == 2 || check_tile_type[endRd_i][endRd_j] == 3
	               || check_tile_type[endRd_i][endRd_j] == 6)
	               &&( check_tile_type[endLd_i][endLd_j] == 1 || check_tile_type[endLd_i][endLd_j] == 4
	                     || check_tile_type[endLd_i][endLd_j] == 6)) {
	            
	            if(isOuterMost(endRd_j,"left") && isOuterMost(endLd_j,"right")) {
	               win++;
	               win_color = 1;
	               change();
	               System.out.println("승리");
	            }
	            return true;
	         }

	      }if (yDistance >= 7) {
	         if ((check_tile_type[endLd_i][endLd_j] ==3 ||check_tile_type[endLd_i][endLd_j] == 4
	               || check_tile_type[endLd_i][endLd_j] == 5)
	               && (check_tile_type[endRd_i][endRd_j] == 1 ||check_tile_type[endRd_i][endRd_j] == 2
	                     ||check_tile_type[endRd_i][endRd_j] == 5)) {
	            if(isOuterMost(endLd_i, "up")&&isOuterMost(endRd_i,"down")) {
	            win++;
	            win_color = 1;
	            change();
	            System.out.println("승리");
	            return true;
	            }
	         }
	      } else if (yDistance <= -7) {
	         if ((check_tile_type[endRd_i][endRd_j] == 3 || check_tile_type[endRd_i][endRd_j] == 4
	               || check_tile_type[endRd_i][endRd_j] == 5)
	               && (check_tile_type[endLd_i][endLd_j] == 1 || check_tile_type[endLd_i][endLd_j] == 2
	                     || check_tile_type[endLd_i][endLd_j] == 5)) {
	            if(isOuterMost(endRd_i, "up") && isOuterMost(endLd_i,"down")) {
	            win++;
	            win_color = 1;
	            change();
	            System.out.println("승리");
	            return true;
	            }
	         }
	      }
	      make_zero();
	      return false;
	   }
	   public boolean isOuterMost(int end, String str) {
	      int i,j;
	      if(str.equals("left")) {
	         for(i=0;i<64;i++) {
	            for(j=0;j<64;j++) {
	               if(check_tile_type[j][i]!=0) {
	                  if(i>=end)
	                     return true;
	                  else
	                     return false;
	               }
	            }
	         }
	      }else if(str.equals("right")) {
	         for(i=63;i>=0;i--) {
	            for(j=63;j>=0;j--) {
	               if(check_tile_type[j][i]!=0) {
	                  if(i<=end)
	                     return true;
	                  else
	                     return false;
	               }
	            }
	         }
	      }else if(str.equals("up")) {
	         for(i=0;i<64;i++) {
	            for(j=0;j<64;j++) {
	               if(check_tile_type[i][j]!=0) {
	                  if(end<=i)
	                     return true;
	                  else return false;
	               }
	            }
	         }
	      }else if(str.equals("down")) {
	         for(i=63;i>=0;i--) {
	            for(j=63;j>=0;j--) {
	               if(check_tile_type[i][j]!=0) {
	                  if(end>=i)
	                     return true;
	                  else return false;
	               }
	            }
	         }
	      }
	      return false;
	   }
	   public void change() {
	      int i,j;
	         for(i=0;i<64;i++) {
	            for(j=0;j<64;j++) {
	               if(checked_win_tiles[i][j]!=0) {
	                  btns[i][j].set_image(check_tile_type[i][j]+20);
	               }
	            }
	         }
	   }
	   public void set_max_min_x(int x){
		   if(max_x < x){
			   max_x = x;
		   }
		   if(min_x > x){
			   min_x = x;
		   }
	   }
	   public void set_max_min_y(int y){
		   if(max_y < y){
			   max_y = y;
		   }
		   if(min_y > y){
			   min_y = y;
		   }
	   }
}
