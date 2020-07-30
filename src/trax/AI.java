package trax;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class AI {
	public int [][] exist;
	private int [][] check_travel;
	public List<Point> datapo_white;
	public List<Point> datapo_black;
	public List<AI_path> AI_white_path;
	public List<AI_path> AI_black_path;
	InGame IG;
	AI_win_check ai_win_white_check;
	AI_win_check ai_win_black_check;
	final int x_len = 64;
	final int y_len = 64;
	final int north = 0;
	final int east = 1;
	final int south = 2;
	final int west = 3;
	final int white = 10;
	final int black = 11;
	public AI(InGame IG) {
		datapo_white = new ArrayList<Point>();
		datapo_black = new ArrayList<Point>();
		AI_white_path = new ArrayList<AI_path>();
		AI_black_path = new ArrayList<AI_path>();
		this.IG = IG;
	}
	
	public void AI_win_check(int last_x, int last_y){
		if(IG.All_click_cnt > 1){
			ai_win_white_check.common_path_Init();
			ai_win_black_check.common_path_Init();
			AI_white_path.clear();
			datapo_white.clear();
			AI_black_path.clear();
			datapo_black.clear();
		}
		exist = IG.exist;
		check_travel = new int[y_len][x_len];
		list_append(last_x,last_y);
		/*
		for(int i=0;i<datapo_white.size();i++){
			System.out.printf("W F enemy %d %d\n", datapo_white.get(i).y,datapo_white.get(i).x);
		}
		for(int i=0;i<datapo_black.size();i++){
			System.out.printf("B F enemy %d %d\n", datapo_black.get(i).y,datapo_black.get(i).x);
		}
		print();
		*/
		real_point_list(white);
		/*
		for(int i=0;i<datapo_white.size();i++){
			System.out.printf("W B enemy %d %d\n", datapo_white.get(i).y,datapo_white.get(i).x);
		}
		*/
		real_point_list(black);
		/*
		for(int i=0;i<datapo_black.size();i++){
			System.out.printf("B B enemy %d %d\n", datapo_black.get(i).y,datapo_black.get(i).x);
		}
		*/
		white_path();
		black_path();
		/*
		for(int i=0;i<AI_white_path.size();i++){
			for(int j=0;j<AI_white_path.get(i).path.size();j++){
				System.out.printf("%d AI W Path %d %d\n", i, AI_white_path.get(i).path.get(j).y,AI_white_path.get(i).path.get(j).x);
			}
			System.out.printf("%d AI W Path %d %d\n", i, AI_white_path.get(i).E_point.y,AI_white_path.get(i).E_point.x);
		}//
		for(int i=0;i<AI_black_path.size();i++){
			for(int j=0;j<AI_black_path.get(i).path.size();j++){
				System.out.printf("%d AI B Path %d %d\n", i, AI_black_path.get(i).path.get(j).y,AI_black_path.get(i).path.get(j).x);
			}
			System.out.printf("%d AI B Path %d %d\n", i, AI_black_path.get(i).E_point.y,AI_black_path.get(i).E_point.x);
		}
		*/

		// ai path test
		ai_win_white_check = new AI_win_check(IG.max_x,IG.min_x,IG.max_y,IG.min_y,IG.exist,IG.tile_dir, IG);
		ai_win_black_check = new AI_win_check(IG.max_x,IG.min_x,IG.max_y,IG.min_y,IG.exist,IG.tile_dir, IG);
		//System.out.printf("x %d %d y %d %d\n", IG.max_x,IG.min_x,IG.max_y,IG.min_y);
		System.out.printf("WHITE+_\n");
		ai_win_white_check.set_maze(AI_white_path,white);
		System.out.printf("BLACK+_\n");
		ai_win_black_check.set_maze(AI_black_path,black);
		
		for(int i=0;i<AI_white_path.size();i++){
			AI_white_path.get(i).path.clear();
		}
		for(int i=0;i<AI_black_path.size();i++){
			AI_black_path.get(i).path.clear();
		}
	}
	
	public int find_index(Point point, int color){
		int i;
		if(color == white){
			for(i=0;i<datapo_white.size();i++){
				if(datapo_white.get(i).x == point.x && datapo_white.get(i).y == point.y){
					return i;
				}
			}
		}
		else if(color == black){
			for(i=0;i<datapo_black.size();i++){
				if(datapo_black.get(i).x == point.x && datapo_black.get(i).y == point.y){
					return i;
				}
			}
		}
		return -1;
	}
	
	public void white_path(){
		Point point;
		AI_path tmp;
		while(datapo_white.size() != 0){
			point = datapo_white.get(0);
			tmp = new AI_path(point);
			//System.out.printf("\nexternal white_path %d %d\n\n", point.x,point.y);
			make_path(tmp,white);
			AI_white_path.add(tmp);
		}
	}
	public void black_path(){
		Point point;
		AI_path tmp;
		while(datapo_black.size() != 0){
			point = datapo_black.get(0);
			tmp = new AI_path(point);
			//System.out.printf("\nexternal white_path %d %d\n\n", point.x,point.y);
			make_path(tmp,black);
			AI_black_path.add(tmp);
		}
	}
	public void print(){
		for(int i=0;i<y_len;i++){
			for(int j=0;j<x_len;j++){
				if(exist[i][j] == 1){
					System.out.printf("EP %d %d ", i,j);
				}
			}
		}
	}
	public void make_path(AI_path ap, int color){
		int [] po;
		int s_x,s_y,l_x,l_y,std_north,std_east,std_south,std_west,cnt;
		int delete_i,delete_s_i;
		l_x = s_x = ap.S_point.x;
		l_y = s_y = ap.S_point.y;
		//System.out.printf("\ninternal white_path %d %d\n\n", l_x,l_y);
		if(color == white){
			if(datapo_white.contains(ap.S_point)){
				delete_s_i = find_index(ap.S_point,color);
				if(delete_s_i == -1)System.out.printf("error make_path %d %d\n", s_y,s_x);
				else datapo_white.remove(delete_s_i);
			}
		}
		else if(color == black){
			if(datapo_black.contains(ap.S_point)){
				delete_s_i = find_index(ap.S_point,color);
				if(delete_s_i == -1)System.out.printf("error make_path %d %d\n", s_y,s_x);
				else datapo_black.remove(delete_s_i);
			}
		}
		while(true){
			std_north = 0;
			std_east = 0;
			std_south = 0;
			std_west = 0;
			cnt = 0;
			po = where_tile(s_x,s_y);
			if(po[north] == 1 && IG.tile_dir[s_y][s_x][north] == color && IG.tile_dir[s_y-1][s_x][south] == color){
				cnt++;
				std_north = 1;
			}
			if(po[east] == 1 && IG.tile_dir[s_y][s_x][east] == color && IG.tile_dir[s_y][s_x+1][west] == color){
				cnt++;
				std_east = 1;
			}
			if(po[south] == 1 && IG.tile_dir[s_y][s_x][south] == color && IG.tile_dir[s_y+1][s_x][north] == color){
				cnt++;
				std_south = 1;
			}
			if(po[west] == 1 && IG.tile_dir[s_y][s_x][west] == color && IG.tile_dir[s_y][s_x-1][east] == color){
				cnt++;
				std_west = 1;
			}
			if(s_x == l_x && s_y == l_y){
				if(cnt == 0){
					ap.E_point = ap.S_point;
					if(color == white){
						if(datapo_white.contains(ap.E_point)){
							ap.path.add(new Point(s_x,s_y));
							delete_i = find_index(ap.E_point,color);
							if(delete_i == -1)System.out.printf("error make_path %d %d\n", s_y,s_x);
							else datapo_white.remove(delete_i);
						}
					}
					else if(color == black){
						if(datapo_black.contains(ap.E_point)){
							ap.path.add(new Point(s_x,s_y));
							delete_i = find_index(ap.E_point,color);
							if(delete_i == -1)System.out.printf("error make_path %d %d\n", s_y,s_x);
							else datapo_black.remove(delete_i);
						}
					}
					return;
				}
				else if(cnt == 1){
					if(std_north == 1){
						s_y = s_y - 1;
					}
					if(std_east == 1){
						s_x = s_x + 1;
					}
					if(std_south == 1){
						s_y = s_y + 1;
					}
					if(std_west == 1){
						s_x = s_x - 1;
					}
				}
				else{
					System.out.printf("cnt == 1 else\n");
					return;
				}
				ap.item_cnt++;
			}
			else{
				if(cnt == 0){
					return;
				}
				if(!ap.path.contains(new Point(s_x,s_y))){
					if(cnt == 1){
						ap.E_point = new Point(s_x,s_y);
						if(color == white){
							if(datapo_white.contains(ap.E_point)){
								ap.path.add(new Point (s_x,s_y));
								delete_i = find_index(ap.E_point,color);
								if(delete_i == -1)System.out.printf("error make_path %d %d %d/ %d %d\n", s_y,s_x,delete_i,ap.E_point.x,ap.E_point.y);
								else datapo_white.remove(delete_i);
							}
						}
						else if(color == black){
							if(datapo_black.contains(ap.E_point)){
								ap.path.add(new Point (s_x,s_y));
								delete_i = find_index(ap.E_point,color);
								if(delete_i == -1)System.out.printf("error make_path %d %d %d/ %d %d\n", s_y,s_x,delete_i,ap.E_point.x,ap.E_point.y);
								else datapo_black.remove(delete_i);
							}
						}
						ap.item_cnt++;
						return;
					}
					else if(cnt == 2){
						int std = 0;
						ap.path.add(new Point (s_x,s_y));
						ap.item_cnt++;
						if(std_north == 1 && l_y != s_y - 1){
							//System.out.printf(" A %d %d %d %d north in\n", s_y,s_x,l_y,l_x);
							l_x = s_x;
							l_y = s_y;
							s_y = s_y - 1;
							std = 1;
						}
						else if(std_east == 1 && l_x != s_x + 1){
							//System.out.printf(" A %d %d %d %d east in\n", s_y,s_x,l_y,l_x);
							l_x = s_x;
							l_y = s_y;
							s_x = s_x + 1;
							std = 1;
						}
						else if(std_south == 1 && l_y != s_y + 1){
							//System.out.printf(" A %d %d %d %d south in\n", s_y,s_x,l_y,l_x);
							l_x = s_x;
							l_y = s_y;
							s_y = s_y + 1;
							std = 1;
						}
						else if(std_west == 1 && l_x != s_x - 1){
							//System.out.printf(" A %d %d %d %d west in\n", s_y,s_x,l_y,l_x);
							l_x = s_x;
							l_y = s_y;
							s_x = s_x - 1;
							std = 1;
						}
						if(std == 0){
							System.out.printf("std == 0 Error cnt = 2 %d %d\n",s_x,s_y);
							return;
						}
					}
					else{
						System.out.printf("std == 0 elseU\n");
						return;
					}
				}
			}
		}
	}
	
	public void real_point_list(int color){
		int [] po;
		int cnt = 0,len = 0;
		List<Integer> delete_list = new ArrayList<Integer>();
		if(color == white){
			len = datapo_white.size();
		}
		else if(color == black){
			len = datapo_black.size();
		}
		for(int i=0;i<len;i++){
			cnt = 0;
			if(color == white){
				po = where_tile(datapo_white.get(i).x,datapo_white.get(i).y);
				if(po[north] == 1 && IG.tile_dir[datapo_white.get(i).y][datapo_white.get(i).x][north] == white && IG.tile_dir[datapo_white.get(i).y - 1][datapo_white.get(i).x][south] == white){
					cnt++;
				}
				if(po[east] == 1 && IG.tile_dir[datapo_white.get(i).y][datapo_white.get(i).x][east] == white && IG.tile_dir[datapo_white.get(i).y][datapo_white.get(i).x + 1][west] == white){
					cnt++;		
				}
				if(po[south] == 1 && IG.tile_dir[datapo_white.get(i).y][datapo_white.get(i).x][south] == white && IG.tile_dir[datapo_white.get(i).y + 1][datapo_white.get(i).x][north] == white){
					cnt++;
				}
				if(po[west] == 1 && IG.tile_dir[datapo_white.get(i).y][datapo_white.get(i).x][west] == white && IG.tile_dir[datapo_white.get(i).y][datapo_white.get(i).x - 1][east] == white){
					cnt++;
				}
			}
			else if (color == black){
				po = where_tile(datapo_black.get(i).x,datapo_black.get(i).y);
				if(po[north] == 1 && IG.tile_dir[datapo_black.get(i).y][datapo_black.get(i).x][north] == black && IG.tile_dir[datapo_black.get(i).y - 1][datapo_black.get(i).x][south] == black){
					cnt++;
				}
				if(po[east] == 1 && IG.tile_dir[datapo_black.get(i).y][datapo_black.get(i).x][east] == black && IG.tile_dir[datapo_black.get(i).y][datapo_black.get(i).x + 1][west] == black){
					cnt++;		
				}
				if(po[south] == 1 && IG.tile_dir[datapo_black.get(i).y][datapo_black.get(i).x][south] == black && IG.tile_dir[datapo_black.get(i).y + 1][datapo_black.get(i).x][north] == black){
					cnt++;
				}
				if(po[west] == 1 && IG.tile_dir[datapo_black.get(i).y][datapo_black.get(i).x][west] == black && IG.tile_dir[datapo_black.get(i).y][datapo_black.get(i).x - 1][east] == black){
					cnt++;
				}
			}
			if(cnt == 2){
				delete_list.add(i);
			}
			else if(cnt>2){
				System.out.printf("\nerror error\n\n");
			}
		}
		for(int i=0;i<delete_list.size();i++){
			//System.out.printf("DELETE %d\n", delete_list.get(i)-cnt);
			if(color == white){
				datapo_white.remove(delete_list.get(i) - i);
			}
			else if(color == black){
				datapo_black.remove(delete_list.get(i) - i);
			}
			else{
				System.out.printf("ERror\n");
			}
		}
		delete_list.clear();
	}
	
	public void list_append(int x, int y){
		int [] po;
		int cnt = 0;
		Point point = null;
		po = where_tile(x,y);
		check_travel[y][x] = 1;
		if(exist[y][x] == 1){
			for(int i=0;i<4;i++){
				if(po[i] == 1){
					cnt++;
				}
			}
			if(cnt == 0 && IG.All_click_cnt == 1){
				point = new Point(x,y);
				datapo_white.add(point);
				datapo_black.add(point);
				return;
			}
			if(cnt < 4 && cnt > 0){
				point = new Point(x,y);
				//System.out.printf("DDD %b %d %d\n", datapo_white.contains(point),x,y);
				if(!datapo_white.contains(point)){
					datapo_white.add(point);
				}
				if(!datapo_black.contains(point)){
					datapo_black.add(point);
				}
			}
			if(check_travel[y-1][x] == 0 && po[0] == 1){
				//System.out.printf("po[0] = %d, %d %d %d\n", po[0], y, x, exist[y-1][x]);
				list_append(x,y-1);
			}
			if(check_travel[y][x+1] == 0 && po[1] == 1){
				//System.out.printf("po[1] = %d, %d %d %d\n", po[1], y, x, exist[y][x+1]);
				list_append(x+1,y);
			}
			if(check_travel[y+1][x] == 0 && po[2] == 1){
				//System.out.printf("po[2] = %d, %d %d %d\n", po[2], y, x, exist[y+1][x]);
				list_append(x,y+1);
			}
			if(check_travel[y][x-1] == 0 && po[3] == 1){
				//System.out.printf("po[3] = %d, %d %d %d\n", po[3], y, x, exist[y][x-1]);
				list_append(x-1,y);
			}
		}
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
}
