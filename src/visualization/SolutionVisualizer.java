package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;

import instance.Ambulance;
import instance.Call;
import instance.Instance;
import instance.Site;

public class SolutionVisualizer {
	private static int cwidth, cheight;
	
	public static void setWindowSize(int width, int height){
		if(cwidth==width && cheight==height)
			return;
		cwidth=width;
		cheight=height;
		StdDraw.setCanvasSize(width, height);
	}
	
	public static void visualize(Instance inst, String title){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setWindowSize(screenSize.height-100, screenSize.height-100);
		
		// Clear the canvas
		int width = new Double(inst.getMaxX()-inst.getMinX()).intValue();
		int height = new Double(inst.getMaxY()-inst.getMinY()).intValue();
		int padding = 20;
		
		double scale = 0.25;
		
		System.out.println("Visualizing solution: "+width+"x"+height+" (This might take a bit of time)");

		StdDraw.clear();
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 16));
		StdDraw.text(0, inst.getMaxY()+20, title);
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 10));
		StdDraw.setXscale(inst.getMinX()-padding, inst.getMaxX()+padding);
		StdDraw.setYscale(inst.getMinY()-padding, inst.getMaxY()+padding);
		
//		ArrayList<Color> colors = StdDraw.getColorPallette();
	
		for(Site z : inst.getSites()){
			StdDraw.square(z.getX(), z.getY(), 3*scale);
		}
		
		for(Call c : inst.getCalls()){
			if(c.getPrio()==1)
				StdDraw.circle(c.getX(), c.getY(), 3*scale);
			else if(c.getPrio()==2)
				StdDraw.circle(c.getX(), c.getY(), 2*scale);
			else if(c.getPrio()==3)
				StdDraw.circle(c.getX(), c.getY(), 1*scale);
			
		}
	}
	
	public static void visualize(Instance in, ArrayList<Ambulance> ams, String title){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setWindowSize(screenSize.height-100, screenSize.height-100);
		
		// Clear the canvas
		int width = new Double(in.getMaxX()-in.getMinX()).intValue();
		int height = new Double(in.getMaxY()-in.getMinY()).intValue();
		int padding = 20;
		
		double scale = 0.25;
		
		System.out.println("Visualizing solution: "+width+"x"+height+" (This might take a bit of time)");

		StdDraw.clear();
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 16));
		StdDraw.text(0, in.getMaxY()+20, title);
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 10));
		StdDraw.setXscale(in.getMinX()-padding, in.getMaxX()+padding);
		StdDraw.setYscale(in.getMinY()-padding, in.getMaxY()+padding);
		
		ArrayList<Color> colors = StdDraw.getColorPallette();

		for(Site z : in.getSites()){
			int val = 240;
			StdDraw.setPenColor( new Color(val, val, val));
			StdDraw.filledCircle(z.getX(), z.getY(), 80*(10.0/60.0));
		}

		// here rings are drown...
		for(Site z : in.getSites()){
			StdDraw.setPenColor(colors.get(z.getId()));
			StdDraw.filledSquare(z.getX(), z.getY(), 3*scale);
			StdDraw.circle(z.getX(), z.getY(), z.getStdDev());
			StdDraw.circle(z.getX(), z.getY(), 2*z.getStdDev());
			StdDraw.circle(z.getX(), z.getY(), 3*z.getStdDev());
		}
		
		for(Ambulance a : ams){
			for(int i=0; i<a.getCalls().size(); i++){
				Call c = a.getCalls().get(i);
				Site s = a.getSites().get(i);
				
				StdDraw.setPenColor(colors.get(s.getId()));
				
				if(c.getPrio()==1)
					StdDraw.circle(c.getX(), c.getY(), 3*scale);
				else if(c.getPrio()==2)
					StdDraw.circle(c.getX(), c.getY(), 2*scale);
				else if(c.getPrio()==3)
					StdDraw.circle(c.getX(), c.getY(), 1*scale);
			}		
		}
	}	

}
