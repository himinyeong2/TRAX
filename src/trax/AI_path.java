package trax;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class AI_path {
	List<Point> path;
	List<Integer> possible_tile;
	public Point S_point, E_point;
	public int item_cnt = 0;
	public int first_dir;
	public int second_dir;
	public int min_cnt;
	public int next_x,next_y;
	public int third_x, third_y;
	public int interrupted_tile;
	public int []line_attack_po;
	
	
	public int third_dir;
	public boolean third_attack_valid;
	public int garbage_dir;
	
	
	public boolean line_attack_valid;
	public int which_line_tile;
	public int which_dir;
	public int x_or_y;
	public int max;
	public AI_path(Point point){
		path = new ArrayList<Point>();
		possible_tile = new ArrayList<Integer>();
		path.add(point);
		S_point = point;
		line_attack_po = new int[4];
		line_attack_valid = false;
		third_attack_valid = false;
	}
	public AI_path(){
		line_attack_po = new int[4];
		line_attack_valid = false;
		third_attack_valid = false;
	}
}
