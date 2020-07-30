package trax;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class AI_win_check {
	private int [][] maze;
	private int [][] exist;
	private int [][][] tile_dir;
	private int width,height;
	private int path_cnt = 0;
	private List<Integer> path_index;
	private List<AI_path> tmp_common_path;
	public AI_path min_path;
	public AI_path line_path;
	public int min_x,min_y,max_x,max_y;
	final int north = 0;
	final int east = 1;
	final int south = 2;
	final int west = 3;
	final int white = 10;
	final int black = 11;
	final int maze_std = 30;
	final int only_one = -10;
	final int start_std = 1000;
	final int end_std = 2000;
	final int other_point = 8;
	final int tile_1 = 1;
	final int tile_2 = 2;
	final int tile_3 = 3;
	final int tile_4 = 4;
	final int tile_5 = 5;
	final int tile_6 = 6;
	InGame IG;
	
	public AI_win_check(int max_x, int min_x, int max_y, int min_y, int [][] exist, int [][][] tile_dir, InGame IG){
		this.min_x = min_x;
		this.min_y = min_y;
		this.max_x = max_x;
		this.max_y = max_y;
		this.exist = exist;
		this.tile_dir = tile_dir;
		this.IG = IG;
		width = max_x - min_x + 1 + 2;
		height = max_y - min_y + 1 + 2;
		path_cnt = 0;
	}
	
	public void common_path_Init(){
		tmp_common_path.clear();
	}
	
	public void set_maze(List<AI_path> common_path, int color){
		maze = new int[height][width];
		path_index = new ArrayList<Integer>();
		tmp_common_path = common_path;
		for(int i = min_y; i <= max_y; i++){
			for(int j = min_x; j <= max_x; j++){
				if(exist[i][j] == 1){
					maze[i - min_y + 1][j - min_x + 1] = -1;
				}
			}
		}
		for(int i = 0; i < common_path.size(); i++){
			maze[common_path.get(i).S_point.y - min_y + 1][common_path.get(i).S_point.x - min_x + 1] = -(maze_std + path_cnt);
			maze[common_path.get(i).E_point.y - min_y + 1][common_path.get(i).E_point.x - min_x + 1] = -(maze_std + path_cnt);
			path_index.add(i);
			path_cnt++;
		}
		min_path = find_maze(common_path,color);
		if(min_path != null) System.out.printf(" %d Min_path = %d %d %d\n",IG.All_click_cnt, min_path.min_cnt,min_path.S_point.y,min_path.S_point.x);
	}
	
	public int select_AI_tile(int color, int att_dep){
		int tile_type = 0;
		int m_x = min_path.next_x + min_x - 1, m_y = min_path.next_y + min_y - 1;
		if(min_path != null){
			if(att_dep == 1){//°ø
				if(min_path.third_attack_valid)	{
					tile_type = which_win_tile(min_path.second_dir, min_path.third_dir, color);
					m_x = min_path.third_x + min_x - 1;
					m_y = min_path.third_y + min_y - 1;
				}
				else tile_type = which_win_tile(min_path.first_dir, min_path.second_dir,color);
			}
			else if(att_dep == 2){//¹æ¾î
				tile_type = which_interrupted_tile(color);
			}
			System.out.printf("which ai ? %d y = %d x = %d\n", tile_type, m_y,m_x);
			IG.changeTile(20+tile_type, m_x, m_y, IG.btns[m_y][m_x].getBtn());
			IG.viewer.G_S.print_Log(m_x, m_y, IG.check_tile_type[m_y][m_x]);
			IG.checkWinWhite_8tile(m_y,m_x);
			IG.checkWinBlack_8tile(m_y,m_x);
			if(IG.win==0 && IG.checked_win_tiles[m_y][m_x]==0 && IG.exist[m_y][m_x]==1) {
				IG.make_zero();
				IG.start_x=m_x;
				IG.start_y=m_y;
				IG.checkWin_white(m_x,m_y);
				IG.check_win_cnt=-1;
				System.out.println("");
				
				if(IG.win==0) {
					IG.make_zero();
					IG.checkWin_black(m_x,m_y);
					IG.check_win_cnt=-1;
					System.out.println("");
				}
			}
			if(IG.win == 0){
				IG.All_click_cnt++;
				IG.viewer.G_S.Set_Turn();
			}
		}
		return 0;
	}
	
	public void line_win_check(int color){
		int start_x, start_y, end_x, end_y, std_x, std_y;
		int max_x = 0, max_y = 0, max_x_index = 1024, max_y_index = 1024;
		int x_1 = 0, x_2 = 0, y_1 = 0, y_2 = 0;
		line_path = new AI_path();
		for(int i = 0; i < tmp_common_path.size(); i++){
			start_x = tmp_common_path.get(i).S_point.x;
			start_y = tmp_common_path.get(i).S_point.y;
			end_x = tmp_common_path.get(i).E_point.x;
			end_y = tmp_common_path.get(i).E_point.y;
			//System.out.printf("%d %d %d %d\n", start_x,start_y,end_x,end_y);
			std_x = start_x - end_x;
			//System.out.printf("%d FF\n", std_x);
			if(std_x < 0) std_x = -std_x;
			std_x = std_x + 1;
			//System.out.printf("%d FF2\n", std_x);
			if(max_x < std_x){
				max_x = std_x;
				max_x_index = i;
			}
		}
		if(max_x>3){
			line_path.max = max_x;
			if(tmp_common_path.get(max_x_index).S_point.x < tmp_common_path.get(max_x_index).E_point.x){
				line_path.S_point = tmp_common_path.get(max_x_index).S_point;
				line_path.E_point = tmp_common_path.get(max_x_index).E_point;
			}
			else{
				line_path.S_point = tmp_common_path.get(max_x_index).E_point;
				line_path.E_point = tmp_common_path.get(max_x_index).S_point;
			}
			if(line_path.S_point.x == IG.min_x && line_path.E_point.x == IG.max_x){
				line_path.line_attack_valid = true;
				if(!(tile_dir[line_path.S_point.y][line_path.S_point.x][east] == color && exist[line_path.S_point.y][line_path.S_point.x + 1] == 0) &&
						!(tile_dir[line_path.E_point.y][line_path.E_point.x][west] == color && exist[line_path.S_point.y][line_path.S_point.x - 1] == 0)){
					if(max_x == 8){
						 if(tile_dir[line_path.S_point.y][line_path.S_point.x][north] == color && exist[line_path.S_point.y - 1][line_path.S_point.x] == 0){
								line_path.which_line_tile = which_win_tile(north,west,color);
								line_path.which_dir = 1;
								line_path.x_or_y = 1;
							}
							else if(tile_dir[line_path.S_point.y][line_path.S_point.x][south] == color && exist[line_path.S_point.y + 1][line_path.S_point.x] == 0){
								line_path.which_line_tile = which_win_tile(south,west,color);
								line_path.which_dir = 1;
								line_path.x_or_y = 1;
							}
							else if(tile_dir[line_path.E_point.y][line_path.E_point.x][north] == color && exist[line_path.E_point.y - 1][line_path.E_point.x] == 0){
								line_path.which_line_tile = which_win_tile(north,east,color);
								line_path.which_dir = 2;
								line_path.x_or_y = 1;
							}
							else if(tile_dir[line_path.E_point.y][line_path.E_point.x][south] == color && exist[line_path.E_point.y + 1][line_path.S_point.x] == 0){
								line_path.which_line_tile = which_win_tile(south,east,color);
								line_path.which_dir = 2;
								line_path.x_or_y = 1;
							}
							else if(tile_dir[line_path.S_point.y][line_path.S_point.x][west] == color && exist[line_path.S_point.y][line_path.S_point.x - 1] == 0){
								line_path.which_line_tile = which_win_tile(west, west, color);
								line_path.which_dir = 1;
								line_path.x_or_y = 1;
							}
							else if(tile_dir[line_path.E_point.y][line_path.E_point.x][east] == color && exist[line_path.E_point.y][line_path.E_point.x + 1] == 0){
								line_path.which_line_tile = which_win_tile(east,east,color);
								line_path.which_dir = 2;
								line_path.x_or_y = 1;
							}
					}
					if(tile_dir[line_path.S_point.y][line_path.S_point.x][west] == color && exist[line_path.S_point.y][line_path.S_point.x - 1] == 0){
						line_path.which_line_tile = which_win_tile(west, west, color);
						line_path.which_dir = 1;
						line_path.x_or_y = 1;
					}
					else if(tile_dir[line_path.E_point.y][line_path.E_point.x][east] == color && exist[line_path.E_point.y][line_path.E_point.x + 1] == 0){
						line_path.which_line_tile = which_win_tile(east,east,color);
						line_path.which_dir = 2;
						line_path.x_or_y = 1;
					}
					else if(tile_dir[line_path.S_point.y][line_path.S_point.x][north] == color && exist[line_path.S_point.y - 1][line_path.S_point.x] == 0){
						line_path.which_line_tile = which_win_tile(north,west,color);
						line_path.which_dir = 1;
						line_path.x_or_y = 1;
					}
					else if(tile_dir[line_path.S_point.y][line_path.S_point.x][south] == color && exist[line_path.S_point.y + 1][line_path.S_point.x] == 0){
						line_path.which_line_tile = which_win_tile(south,west,color);
						line_path.which_dir = 1;
						line_path.x_or_y = 1;
					}
					else if(tile_dir[line_path.E_point.y][line_path.E_point.x][north] == color && exist[line_path.E_point.y - 1][line_path.E_point.x] == 0){
						line_path.which_line_tile = which_win_tile(north,east,color);
						line_path.which_dir = 2;
						line_path.x_or_y = 1;
					}
					else if(tile_dir[line_path.E_point.y][line_path.E_point.x][south] == color && exist[line_path.E_point.y + 1][line_path.S_point.x] == 0){
						line_path.which_line_tile = which_win_tile(south,east,color);
						line_path.which_dir = 2;
						line_path.x_or_y = 1;
					}
				}
			}
			System.out.printf(" line path %d %d, %d %d\n", line_path.S_point.y,line_path.S_point.x, line_path.E_point.y,line_path.E_point.x);
		}
		
	}
	
	public void line_attack_set(int tile_type){
		int m_x = line_path.next_x, m_y = line_path.next_y;
		System.out.printf("which ai line ? %d y = %d x = %d\n", tile_type, m_y,m_x);
		IG.changeTile(20+tile_type, m_x, m_y, IG.btns[m_y][m_x].getBtn());
		IG.viewer.G_S.print_Log(m_x, m_y, IG.check_tile_type[m_y][m_x]);
		IG.checkWinWhite_8tile(m_y,m_x);
		IG.checkWinBlack_8tile(m_y,m_x);
		if(IG.win==0 && IG.checked_win_tiles[m_y][m_x]==0 && IG.exist[m_y][m_x]==1) {
			IG.make_zero();
			IG.start_x=m_x;
			IG.start_y=m_y;
			IG.checkWin_white(m_x,m_y);
			IG.check_win_cnt=-1;
			System.out.println("");
			
			if(IG.win==0) {
				IG.make_zero();
				IG.checkWin_black(m_x,m_y);
				IG.check_win_cnt=-1;
				System.out.println("");
			}
		}
		if(IG.win == 0){
			IG.All_click_cnt++;
			IG.viewer.G_S.Set_Turn();
		}
	}
	
	public int line_attack_depend(int color){
		int tile = -1;
		if(tile_dir[line_path.E_point.y][line_path.E_point.x][east] == color){
			if(tile_dir[line_path.E_point.y - 1][line_path.E_point.x][east] == color){
				line_path.next_x = line_path.E_point.x + 1;
				line_path.next_y = line_path.E_point.y;
				if(color == white){
					tile = tile_3;
				}
				else if(color == black){
					tile = tile_1;
				}
			}
			else if(tile_dir[line_path.E_point.y + 1][line_path.E_point.x][east] == color){
				line_path.next_x = line_path.E_point.x + 1;
				line_path.next_y = line_path.E_point.y;
				if(color == white){
					tile = tile_2;
				}
				else if(color == black){
					tile = tile_4;
				}
			}
		}
		if(tile == -1 && tile_dir[line_path.S_point.y][line_path.S_point.x][west] == color){
			if(tile_dir[line_path.S_point.y - 1][line_path.S_point.x][west] == color){
				line_path.next_x = line_path.S_point.x - 1;
				line_path.next_y = line_path.S_point.y;
				if(color == white){
					tile = tile_4;
				}
				else if(color == black){
					tile = tile_2;
				}
			}
			else if(tile_dir[line_path.S_point.y + 1][line_path.S_point.x][west] == color){
				line_path.next_x = line_path.S_point.x - 1;
				line_path.next_y = line_path.S_point.y;
				if(color == white){
					tile = tile_1;
				}
				else if(color == black){
					tile = tile_3;
				}
			}	
		}/*
		if(tile == -1){
			if(tile_dir[line_path.E_point.y][line_path.E_point.x][north] == color){
				if(tile_dir[line_path.E_point.y - 1][line_path.E_point.x - 1][east] == color || exist[line_path.E_point.y - 1][line_path.E_point.x - 1] == 0){
					if(color == white){
						tile = tile_2;
					}
					else if(color == black){
						tile = tile_4;
					}
				}
				else if(exist[line_path.E_point.y - 1][line_path.E_point.x - 1] == 1){
					if(color == white){
						tile = tile_5;
					}
					else if(color == black){
						tile = tile_6;
					}
				}
			}
			else if(tile_dir[line_path.E_point.y][line_path.E_point.x][south] == color){
				if(tile_dir[line_path.E_point.y - 1][line_path.E_point.x - 1][east] == color || exist[line_path.E_point.y - 1][line_path.E_point.x - 1] == 0){
					if(color == white){
						tile = tile_3;
					}
					else if(color == black){
						tile = tile_1;
					}
				}
				else if(exist[line_path.E_point.y - 1][line_path.E_point.x - 1] == 1){
					if(color == white){
						tile = tile_5;
					}
					else if(color == black){
						tile = tile_6;
					}
				}
			}
			if(tile == -1){
				if(tile_dir[line_path.S_point.y][line_path.S_point.x][north] == color){
					if(tile_dir[line_path.S_point.y - 1][line_path.S_point.x - 1][east] == color || exist[line_path.S_point.y - 1][line_path.E_point.x - 1] == 0){
						if(color == white){
							tile = tile_2;
						}
						else if(color == black){
							tile = tile_4;
						}
					}
					else if(exist[line_path.E_point.y - 1][line_path.E_point.x - 1] == 1){
						if(color == white){
							tile = tile_5;
						}
						else if(color == black){
							tile = tile_6;
						}
					}
				}
				if(tile_dir[line_path.E_point.y][line_path.E_point.x][south] == color){
					if(tile_dir[line_path.E_point.y - 1][line_path.E_point.x - 1][east] == color || exist[line_path.E_point.y - 1][line_path.E_point.x - 1] == 0){
						if(color == white){
							tile = tile_3;
						}
						else if(color == black){
							tile = tile_1;
						}
					}
					else if(exist[line_path.E_point.y - 1][line_path.E_point.x - 1] == 1){
						if(color == white){
							tile = tile_5;
						}
						else if(color == black){
							tile = tile_6;
						}
					}
				}
			}
		}*/
		return tile;
	}
	public int which_interrupted_tile(int color){
		int tile_type = which_win_tile(min_path.first_dir, min_path.second_dir, color);
		int real_x = min_path.next_x + min_x - 1, real_y = min_path.next_y + min_y - 1;
		min_path.interrupted_tile = -1;
		
		if(min_path.first_dir == north){
			if(color == white){
				if(tile_type == tile_1){
					if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_2;
					}
					else if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_5;
					}
				}
				else if(tile_type == tile_2){
					if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_1;
					}
					else if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_5;
					}
				}
				else if(tile_type == tile_5){
					if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_1;
					}
					else if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_2;
					}
				}
				min_path.possible_tile.add(tile_1);
				min_path.possible_tile.add(tile_2);
				min_path.possible_tile.add(tile_5);
			}
			else if(color == black){
				if(tile_type == tile_3){
					if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_4;
					}
					else if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_6;
					}
				}
				else if(tile_type == tile_4){
					if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_3;
					}
					else if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_6;
					}
				}
				else if(tile_type == tile_6){
					if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_3;
					}
					else if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_4;
					}
				}
				min_path.possible_tile.add(tile_3);
				min_path.possible_tile.add(tile_4);
				min_path.possible_tile.add(tile_6);
			}
		}
		else if(min_path.first_dir == east){
			if(color == white){
				if(tile_type == tile_2){
					if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_3;
					}
					else if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_6;
					}
				}
				else if(tile_type == tile_3){
					if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_2;
					}
					else if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_6;
					}
				}
				else if(tile_type == tile_6){
					if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_3;
					}
					else if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_2;
					}
				}
				min_path.possible_tile.add(tile_2);
				min_path.possible_tile.add(tile_3);
				min_path.possible_tile.add(tile_6);
			}
			else if(color == black){
				if(tile_type == tile_1){
					if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_4;
					}
					else if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_5;
					}
				}
				else if(tile_type == tile_4){
					if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_1;
					}
					else if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_5;
					}
				}
				else if(tile_type == tile_5){
					if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_1;
					}
					else if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_4;
					}
				}
				min_path.possible_tile.add(tile_1);
				min_path.possible_tile.add(tile_4);
				min_path.possible_tile.add(tile_5);
			}
		}
		else if(min_path.first_dir == south){
			if(color == white){
				if(tile_type == tile_3){
					if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_4;
					}
					else if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_5;
					}
				}
				else if(tile_type == tile_4){
					if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_3;
					}
					else if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_5;
					}
				}
				else if(tile_type == tile_5){
					if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_4;
					}
					else if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_3;
					}
				}
				min_path.possible_tile.add(tile_3);
				min_path.possible_tile.add(tile_4);
				min_path.possible_tile.add(tile_5);
			}
			else if(color == black){
				if(tile_type == tile_1){
					if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_2;
					}
					else if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_6;
					}
				}
				else if(tile_type == tile_2){
					if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_1;
					}
					else if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_6;
					}
				}
				else if(tile_type == tile_6){
					if(exist[real_y][real_x + 1] == 0){
						min_path.interrupted_tile = tile_2;
					}
					else if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_1;
					}
				}
				min_path.possible_tile.add(tile_1);
				min_path.possible_tile.add(tile_2);
				min_path.possible_tile.add(tile_6);
			}
		}
		else if(min_path.first_dir == west){
			if(color == white){
				if(tile_type == tile_1){
					if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_4;
					}
					else if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_6;
					}
				}
				else if(tile_type == tile_4){
					if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_1;
					}
					else if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_6;
					}
				}
				else if(tile_type == tile_6){
					if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_4;
					}
					else if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_1;
					}
				}
				min_path.possible_tile.add(tile_1);
				min_path.possible_tile.add(tile_4);
				min_path.possible_tile.add(tile_6);
			}
			else if(color == black){
				if(tile_type == tile_2){
					if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_3;
					}
					else if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_5;
					}
				}
				else if(tile_type == tile_3){
					if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_2;
					}
					else if(exist[real_y][real_x - 1] == 0){
						min_path.interrupted_tile = tile_5;
					}
				}
				else if(tile_type == tile_5){
					if(exist[real_y - 1][real_x] == 0){
						min_path.interrupted_tile = tile_2;
					}
					else if(exist[real_y + 1][real_x] == 0){
						min_path.interrupted_tile = tile_3;
					}
				}
				min_path.possible_tile.add(tile_2);
				min_path.possible_tile.add(tile_3);
				min_path.possible_tile.add(tile_5);
			}
		}
		if(min_path.interrupted_tile != -1){
			return min_path.interrupted_tile;
		}
		for(int i=0;i<min_path.possible_tile.size();i++){
			System.out.printf("FF %d\n", min_path.possible_tile.get(i));
			if(tile_type != min_path.possible_tile.get(i) && min_path.possible_tile.get(i) != min_path.interrupted_tile){
				return min_path.possible_tile.get(i);
			}
		}
		return -1;
	}
	
	public int which_win_tile(int first_dir, int second_dir, int color){// 0 1 2 3 north east south west
		int [] po = new int[4];
		//System.out.printf("win tile %d %d %d\n", first_dir,second_dir,color);
		if(first_dir == north){
			po[south] = color;
		}
		else if(first_dir == east){
			po[west] = color;
		}
		else if(first_dir == south){
			po[north] = color;
		}
		else if(first_dir == west){
			po[east] = color;
		}
		po[second_dir] = color;
		if(po[north] == color){
			if(color == white){
				if(po[east] == white) return tile_4;
				if(po[south] == white) return tile_5;
				if(po[west] == white) return tile_3;
			}
			else if(color == black){
				if(po[east] == black) return tile_2;
				if(po[south] == black) return tile_6;
				if(po[west] == black) return tile_1;
			}
		}
		else if(po[east] == color){
			if(color == white){
				if(po[south] == white) return tile_1;
				if(po[west] == white) return tile_6;
			}
			else if(color == black){
				if(po[south] == black) return tile_3;
				if(po[west] == black) return tile_5;
			}
		}
		else if(po[south] == color){
			if(po[west] == white) return tile_2;
			else if(po[west] == black) return tile_4;
		}
		System.out.printf("win tile po %d %d %d %d\n", po[0],po[1],po[2],po[3]);
		return -2;
	}
	
	public AI_path find_maze(List<AI_path> common_path, int color){
		int min_index = 0, min = 999;
		System.out.printf("\nMaze print start\n");
		for(int i = 0; i < path_index.size(); i++){
			start_check(common_path.get(i), color);
			//System.out.printf("third = %d %d, %d, %d %d\n",common_path.get(i).third_y + min_y - 1, common_path.get(i).third_x + min_x - 1, common_path.get(i).path.size(), common_path.get(i).second_dir, common_path.get(i).third_dir);
			//System.out.printf(", %d %d\n", common_path.get(i).S_point.y, common_path.get(i).S_point.x);
			//if(common_path.get(i).third_attack_valid){
				//int tile;
				//tile = which_win_tile(common_path.get(i).second_dir, common_path.get(i).third_dir, color);
				//System.out.printf("tile = %d, %d %d, %d A %d\n", tile, common_path.get(i).S_point.y, common_path.get(i).S_point.x, common_path.get(i).min_cnt, common_path.get(i).path.size());
			//}
		}
		for(int i = 0; i < common_path.size(); i++){
			if(min > common_path.get(i).min_cnt){
				min_index = i;
				min = common_path.get(i).min_cnt;
			}
		}
		if(common_path.size() != 0){
			return common_path.get(min_index);
		}
		System.out.printf("\nMaze print finish %d \n",path_cnt);
		path_index.clear();
		return null;
	}
	
	public void start_check(AI_path start_path, int color){
		int [] maze_exist;
		int [][] tmp_maze = new int[height][width];
		int [][][] maze_dir = new int[height][width][4];
		int travel_x, travel_y, start_dir, second_dir = -1, min_cnt = 9999, route_cnt = 9999;
		for(int i = min_y; i <= max_y; i++){
			for(int j = min_x; j <= max_x; j++){
				tmp_maze[i - min_y + 1][j - min_x + 1] = maze[i - min_y + 1][j - min_x + 1];
			}
		}
		for(int i = min_y; i <= max_y; i++){
			for(int j = min_x; j <= max_x; j++){
				for(int k = 0; k < 4; k++){
					maze_dir[i - min_y + 1][j - min_x + 1][k] = tile_dir[i][j][k];
				}
			}
		}
		tmp_maze[start_path.S_point.y - min_y + 1][start_path.S_point.x - min_x + 1] = start_std;
		tmp_maze[start_path.E_point.y - min_y + 1][start_path.E_point.x - min_x + 1] = end_std;
		travel_x = start_path.S_point.x - min_x + 1;
		travel_y = start_path.S_point.y - min_y + 1;
		start_dir = where_start(tmp_maze,maze_dir,travel_x,travel_y, color);
		if(start_dir == north){
			travel_y = travel_y - 1;
			//System.out.printf("north\n %d %d\n", travel_y,travel_x);
		}
		else if(start_dir == east){
			travel_x = travel_x + 1;
			//System.out.printf("east\n %d %d\n", travel_y,travel_x);
		}
		else if(start_dir == south){
			travel_y = travel_y + 1;
			//System.out.printf("south\n %d %d\n", travel_y,travel_x);
		}
		else if(start_dir == west){
			travel_x = travel_x - 1;
			//System.out.printf("west\n %d %d\n", travel_y,travel_x);
		}
		else {
			System.out.printf("\n\ndir error\n\n");
		}
		start_path.next_x = travel_x;
		start_path.next_y = travel_y;
		start_path.first_dir = start_dir;
		
		/*garbage
		for(int i = min_y - 1; i <= max_y + 1; i++){
			for(int j = min_x - 1; j <= max_x + 1; j++){
				System.out.printf("%5d", tmp_maze[i - min_y + 1][j - min_x + 1]);
			}
			System.out.printf("\n");
		}
		System.out.printf("move maze countinue\n");
		*/
		tmp_maze[travel_y][travel_x] = -2;
		maze_exist = find_maze_exist(tmp_maze,travel_x,travel_y,maze_dir, color);
		
		if(maze_exist[north] == 1){
			route_cnt = check_all_route(travel_x, travel_y - 1, tmp_maze, maze_dir, color, 0, start_path);
			if(min_cnt > route_cnt){
				second_dir = north;
				min_cnt = route_cnt;
				if(start_path.garbage_dir == -1){
					start_path.third_dir = -1;
					start_path.third_attack_valid = false;
				}
				else{
					start_path.third_dir = start_path.garbage_dir;
					start_path.third_attack_valid = true;
				}
			}
		}
		if(maze_exist[east] == 1){
			route_cnt = check_all_route(travel_x + 1, travel_y, tmp_maze, maze_dir, color, 0, start_path);
			if(min_cnt > route_cnt){
				second_dir = east;
				min_cnt = route_cnt;
				if(start_path.garbage_dir == -1){
					start_path.third_dir = -1;
					start_path.third_attack_valid = false;
				}
				else{
					start_path.third_dir = start_path.garbage_dir;
					start_path.third_attack_valid = true;
				}
			}
		}
		if(maze_exist[south] == 1){
			route_cnt = check_all_route(travel_x, travel_y + 1, tmp_maze, maze_dir, color, 0, start_path);
			if(min_cnt > route_cnt){
				second_dir = south;
				min_cnt = route_cnt;
				if(start_path.garbage_dir == -1){
					start_path.third_dir = -1;
					start_path.third_attack_valid = false;
				}
				else{
					start_path.third_dir = start_path.garbage_dir;
					start_path.third_attack_valid = true;
				}
			}
		}
		if(maze_exist[west] == 1){
			route_cnt = check_all_route(travel_x - 1, travel_y, tmp_maze, maze_dir, color, 0, start_path);
			if(min_cnt > route_cnt){
				second_dir = west;
				min_cnt = route_cnt;
				if(start_path.garbage_dir == -1){
					start_path.third_dir = -1;
					start_path.third_attack_valid = false;
				}
				else{
					start_path.third_dir = start_path.garbage_dir;
					start_path.third_attack_valid = true;
				}
			}
		}
		
		start_path.second_dir = second_dir;
		if(second_dir == north) {
			start_path.third_x = start_path.next_x;
			start_path.third_y = start_path.next_y - 1;
		}
		else if(second_dir == east) {
			start_path.third_x = start_path.next_x + 1;
			start_path.third_y = start_path.next_y;
		}
		else if(second_dir == south) {
			start_path.third_x = start_path.next_x;
			start_path.third_y = start_path.next_y + 1;
		}
		else if(second_dir == west) {
			start_path.third_x = start_path.next_x - 1;
			start_path.third_y = start_path.next_y;
		}
		if(start_path.S_point == start_path.E_point){
			start_path.third_attack_valid = false;
			start_path.third_dir = -1;
		}
		if(start_path.third_attack_valid == true && !(exist[start_path.third_y - 1 + min_y - 1][start_path.third_x + min_x - 1] == 1 || exist[start_path.third_y + 1 + min_y - 1][start_path.third_x + min_x - 1] == 1 || exist[start_path.third_y + min_y - 1][start_path.third_x - 1 + min_x - 1] == 1 || exist[start_path.third_y + min_y - 1][start_path.third_x + 1 + min_x - 1] == 1)){
			start_path.third_attack_valid = false;
			start_path.third_dir = -1;
		}
		if(start_path.third_attack_valid == true){
			start_path.min_cnt = min_cnt;
		}
		else{
			start_path.min_cnt = min_cnt+1;
		}
		/*
		for(int i = min_y - 1; i <= max_y + 1; i++){
			for(int j = min_x - 1; j <= max_x + 1; j++){
				System.out.printf("%5d", tmp_maze[i - min_y + 1][j - min_x + 1]);
			}
			System.out.printf("\n");
		}
		*/
		/*
		System.out.printf("start_check result y = %d, x = %d\n", start_path.S_point.y,start_path.S_point.x);
		System.out.printf("start_check result first_dir = %d, min_cnt = %d\n", start_path.first_dir,start_path.min_cnt);
		System.out.printf("start_check result second_dir = %d, min_cnt = %d\n", start_path.second_dir,min_cnt+1);
		*/
	}
	
	public int check_all_route(int x, int y, int [][] tmp_maze, int [][][] maze_dir, int color, int refer_cnt, AI_path AP){
		int [] maze_exist, other_point_dir;
		int real_maze, cnt = 9999, min_cnt = 9999, in_cnt = 0, std = 1, other_point_std = 0, other_point_x, other_point_y, start_dir, third_dir = -1;
		real_maze = tmp_maze[y][x];
		maze_exist = find_maze_exist(tmp_maze,x,y,maze_dir, color);
		tmp_maze[y][x] = -2;
		/*delete
		for(int i = min_y - 1; i <= max_y + 1; i++){
			for(int j = min_x - 1; j <= max_x + 1; j++){
				System.out.printf("%5d", tmp_maze[i - min_y + 1][j - min_x + 1]);
			}
			System.out.printf("\n");
		}
		System.out.printf("%d %d %d %d while\n", maze_exist[north], maze_exist[east], maze_exist[south], maze_exist[west]);
		System.out.printf("move maze while\n");
		try{
			Thread.sleep(1*1000);
		}catch(InterruptedException ex){
			
		}
		*/
		
		if(refer_cnt == 0){
			AP.garbage_dir = -1;
		}
		
		//exit
		if(maze_exist[north] == end_std || maze_exist[east] == end_std || maze_exist[south] == end_std || maze_exist[west] == end_std){
			//System.out.printf("%d %d %d %d end %d %d\n", maze_exist[north], maze_exist[east], maze_exist[south], maze_exist[west], y, x);
			tmp_maze[y][x] = real_maze;
			return 0;
		}
		if(refer_cnt > 7){
			return 8888;
		}
		//other_point
		//north
		if(maze_exist[north] == other_point){
			int other_real_maze;
			other_point_x = select_other_point(x, y - 1, tmp_maze, color) / 64;
			other_point_y = select_other_point(x, y - 1, tmp_maze, color) % 64;
			other_real_maze = tmp_maze[other_point_y][other_point_x];
			other_point_dir = find_maze_exist(tmp_maze, other_point_x, other_point_y, maze_dir, color);
			start_dir = where_start(tmp_maze, maze_dir, other_point_x, other_point_y, color);
			if(start_dir == north){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x, other_point_y - 1, tmp_maze, maze_dir, color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == east){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x + 1, other_point_y, tmp_maze, maze_dir, color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == south){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x, other_point_y + 1, tmp_maze, maze_dir, color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == west){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x - 1, other_point_y, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
		}
		//east
		if(maze_exist[east] == other_point && other_point_std == 0){
			int other_real_maze;
			other_point_x = select_other_point(x + 1, y, tmp_maze, color) / 64;
			other_point_y = select_other_point(x + 1, y, tmp_maze, color) % 64;
			other_real_maze = tmp_maze[other_point_y][other_point_x];
			other_point_dir = find_maze_exist(tmp_maze, other_point_x, other_point_y, maze_dir,color);
			start_dir = where_start(tmp_maze, maze_dir, other_point_x, other_point_y,color);
			if(start_dir == north){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x, other_point_y - 1, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == east){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x + 1, other_point_y, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == south){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x, other_point_y + 1, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == west){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x - 1, other_point_y, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
		}
		//south
		if(maze_exist[south] == other_point && other_point_std == 0){
			int other_real_maze;
			other_point_x = select_other_point(x, y + 1, tmp_maze, color) / 64;
			other_point_y = select_other_point(x, y + 1, tmp_maze, color) % 64;
			other_real_maze = tmp_maze[other_point_y][other_point_x];
			other_point_dir = find_maze_exist(tmp_maze, other_point_x, other_point_y, maze_dir,color);
			start_dir = where_start(tmp_maze, maze_dir, other_point_x, other_point_y,color);
			if(start_dir == north){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x, other_point_y - 1, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == east){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x + 1, other_point_y, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == south){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x, other_point_y + 1, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == west){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x - 1, other_point_y, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
		}
		//west
		if(maze_exist[west] == other_point && other_point_std == 0){
			int other_real_maze;
			other_point_x = select_other_point(x - 1, y, tmp_maze, color) / 64;
			other_point_y = select_other_point(x - 1, y, tmp_maze, color) % 64;
			other_real_maze = tmp_maze[other_point_y][other_point_x];
			other_point_dir = find_maze_exist(tmp_maze, other_point_x, other_point_y, maze_dir,color);
			start_dir = where_start(tmp_maze, maze_dir, other_point_x, other_point_y,color);
			if(start_dir == north){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x, other_point_y - 1, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == east){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x + 1, other_point_y, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == south){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x, other_point_y + 1, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
			else if(start_dir == west){
				tmp_maze[other_point_y][other_point_x] = -2;
				cnt = check_all_route(other_point_x - 1, other_point_y, tmp_maze, maze_dir,color,refer_cnt + 1, AP);
				other_point_std = 1;
				if(min_cnt > cnt){
					std = 0;
					min_cnt = cnt;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				tmp_maze[other_point_y][other_point_x] = other_real_maze;
				in_cnt++;
			}
		}
		// empty------------------------------------------------
		
		if(other_point_std == 0){
			//north
			if(maze_exist[north] == 1){
				cnt = check_all_route(x,y-1,tmp_maze,maze_dir,color,refer_cnt + 1, AP);
				if(min_cnt > cnt){
					std = 1;
					min_cnt = cnt;
					if(refer_cnt == 0) third_dir = north;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				in_cnt++;
			}
			
			//east
			if(maze_exist[east] == 1){
				tmp_maze[y][x] = -2;
				cnt = check_all_route(x+1,y,tmp_maze,maze_dir,color,refer_cnt + 1, AP);
				if(min_cnt > cnt){
					std = 1;
					min_cnt = cnt;
					if(refer_cnt == 0) third_dir = east;
					
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				in_cnt++;
			}
			
			//south
			if(maze_exist[south] == 1){
				tmp_maze[y][x] = -2;
				cnt = check_all_route(x,y+1,tmp_maze,maze_dir,color,refer_cnt + 1, AP);
				if(min_cnt > cnt){
					std = 1;
					min_cnt = cnt;
					if(refer_cnt == 0) third_dir = south;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				in_cnt++;
			}
			
			//west
			if(maze_exist[west] == 1){
				tmp_maze[y][x] = -2;
				cnt = check_all_route(x-1,y,tmp_maze,maze_dir,color,refer_cnt + 1, AP);
				if(min_cnt > cnt){
					std = 1;
					min_cnt = cnt;
					if(refer_cnt == 0) third_dir = west;
				}
				//System.out.printf("cnt = %d min_cnt = %d, %d %d\n", cnt,min_cnt,y,x);
				tmp_maze[y][x] = real_maze;
				in_cnt++;
			}
			if(refer_cnt == 0 && third_dir != -1){
				AP.garbage_dir = third_dir;
			}
		}
		
		//exit
		if(in_cnt == 0){
			tmp_maze[y][x] = real_maze;
			return 8888;
		}
		if(min_cnt == 8888){
			tmp_maze[y][x] = real_maze;
			return 8888;
		}
		
		return min_cnt + std;
	}
	
	public int select_other_point(int x, int y, int [][] tmp_maze, int color){
		if((tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).S_point.x - min_x + 1) == x &&
				 (tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).S_point.y - min_y + 1) == y){
			return (tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).E_point.x - min_x + 1) * 64 + (tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).E_point.y - min_y + 1);
		}
		else if((tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).E_point.x - min_x + 1) == x &&
				(tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).E_point.y - min_y + 1) == y){
			return (tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).S_point.x - min_x + 1) * 64 + (tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).S_point.y - min_y + 1);
		}//tmp_maze[start.y - min_y + 1][start.x - min_x + 1] = start_std;
		System.out.printf("%d %d\n", tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).S_point.x, tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).S_point.y);
		System.out.printf("%d %d\n", tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).E_point.x, tmp_common_path.get(path_index.get(-(tmp_maze[y][x] + maze_std))).E_point.y);
		System.out.printf("Select Error\n");
		return -1;
	}
	
	public int where_start(int [][] tmp_maze, int [][][] maze_dir, int x, int y, int color){
		int [] po = find_maze_exist(tmp_maze,x,y,maze_dir,color);
		for(int i=0;i<4;i++){
			if(maze_dir[y][x][i] == color && po[i] == 1)return i;
		}
		return -1;
	}
	
	public int [] find_maze_exist(int [][] tmp_maze, int x, int y, int [][][] maze_dir, int color){
		int [] po = new int [4];
		
		//west
		if(x == 0 || tmp_maze[y][x-1] == -1 || tmp_maze[y][x-1] == -2 || tmp_maze[y][x-1] == 1 || (tmp_maze[y][x-1] == start_std && maze_dir[y][x-1][east] != color)){
			po[west] = -1;
		}
		else if(tmp_maze[y][x-1] == 0){
			po[west] = 1;
		}
		else if(tmp_maze[y][x-1] <= -30){
			if(maze_dir[y][x-1][east] == color) po[west] = other_point;
			else po[west] = -1;
		}
		else if(tmp_maze[y][x-1] == only_one){
			if(maze_dir[y][x-1][east] == color)	po[west] = only_one;
			else po[west] = -1;
		}
		else if(tmp_maze[y][x-1] == end_std || (tmp_maze[y][x-1] == start_std && maze_dir[y][x-1][east] == color)){
			if(maze_dir[y][x-1][east] == color)	po[west] = end_std;
			else po[west] = -1;
		}
		
		//north
		if(y == 0 || tmp_maze[y-1][x] == -1 || tmp_maze[y-1][x] == -2 || tmp_maze[y-1][x] == 1 || (tmp_maze[y-1][x] == start_std && maze_dir[y-1][x][south] != color)){
			po[north] = -1;
		}
		else if(tmp_maze[y-1][x] == 0){
			po[north] = 1;
		}
		else if(tmp_maze[y-1][x] <= -30){
			if(maze_dir[y-1][x][south] == color) po[north] = other_point;
			else po[north] = -1;
		}
		else if(tmp_maze[y-1][x] == only_one){
			if(maze_dir[y-1][x][south] == color) po[north] = only_one;
			else po[north] = -1;
		}
		else if(tmp_maze[y-1][x] == end_std || (tmp_maze[y-1][x] == start_std && maze_dir[y-1][x][south] == color)){
			if(maze_dir[y-1][x][south] == color) po[north] = end_std;
			else po[north] = -1;
		}
		
		//east
		if(x == width - 1 || tmp_maze[y][x+1] == -1 || tmp_maze[y][x+1] == -2 || tmp_maze[y][x+1] == 1 || (tmp_maze[y][x+1] == start_std && maze_dir[y][x+1][west] != color)){
			po[east] = -1;
		}
		else if(tmp_maze[y][x+1] == 0){
			po[east] = 1;
		}
		else if(tmp_maze[y][x+1] <= -30){
			if(maze_dir[y][x+1][west] == color) po[east] = other_point;
			else po[east] = -1;
		}
		else if(tmp_maze[y][x+1] == only_one){
			if(maze_dir[y][x+1][west] == color) po[east] = only_one;
			else po[east] = -1;
		}
		else if(tmp_maze[y][x+1] == end_std || (tmp_maze[y][x+1] == start_std && maze_dir[y][x+1][west] == color)){
			if(maze_dir[y][x+1][west] == color)	po[east] = end_std;
			else po[east] = -1;
		}
		
		//south
		if(y == height - 1 || tmp_maze[y+1][x] == -1 || tmp_maze[y+1][x] == -2 || tmp_maze[y+1][x] == 1 || (tmp_maze[y+1][x] == start_std && maze_dir[y+1][x][north] != color)){
			po[south] = -1;
		}
		else if(tmp_maze[y+1][x] == 0){
			po[south] = 1;
		}
		else if(tmp_maze[y+1][x] <= -30){
			if(maze_dir[y+1][x][north] == color) po[south] = other_point;
			else po[south] = -1;
		}
		else if(tmp_maze[y+1][x] == only_one){
			if(maze_dir[y+1][x][north] == color) po[south] = only_one;
			else po[south] = -1;
		}
		else if(tmp_maze[y+1][x] == end_std || (tmp_maze[y+1][x] == start_std && maze_dir[y+1][x][north] == color)){
			if(maze_dir[y+1][x][north] == color) po[south] = end_std;
			else po[south] = -1;
		}
		return po;
	}
}
