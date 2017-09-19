/**
 * 
 */
package sp1;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.VisualizationImageServer;

/**
 * @author Juan Zamudio
 * @version September 8th, 2017
 *
 */
public class GraphParserDrawer {
	
	public void createVertexMap () throws IOException {
		try (Scanner scanner = new Scanner(new File ("dolphins.txt"))){
			Scanner myDelim = scanner.useDelimiter("label");
			HashMap<Integer, String> idMapper = new HashMap<Integer, String>();
			
			myDelim.next();
			
			for (int i = 0; i <= 61 && myDelim.hasNext(); i++) {
				idMapper.put(i, myDelim.findInLine("\\\"\\w*\\\""));
				System.out.println(idMapper.get(i));
				myDelim.next();
			}
			
			Iterator<Map.Entry<Integer, String>> it = idMapper.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, String> pair = it.next();
				System.out.println(pair.getKey() + " " + pair.getValue());
			}
			
		};
	}
	
	public void createEdgeMap () throws IOException {
		try (Scanner scanner = new Scanner(new File ("dolphins.txt"))){
			Scanner myDelim = scanner.useDelimiter("source");
			
			HashMap<Integer, ArrayList<Integer>> edgeMapper = new HashMap<Integer, ArrayList<Integer>>();
			
			myDelim.next();
			
			for (int i = 0; i <= 158 && myDelim.hasNext(); i++) {
				myDelim = scanner.useDelimiter("\\p{javaWhitespace}+");
				String vertex = myDelim.findInLine("\\d+");
				myDelim.next();
				
				if (edgeMapper.containsKey((Integer)Integer.parseInt(vertex))) {
					edgeMapper.get((Integer)Integer.parseInt(vertex)).add(Integer.parseInt(myDelim.next()));
				} else {
					edgeMapper.put((Integer)Integer.parseInt(vertex), new ArrayList<Integer>());
					edgeMapper.get((Integer)Integer.parseInt(vertex)).add(Integer.parseInt(myDelim.next()));
				}
				
				myDelim = scanner.useDelimiter("source");
				myDelim.next();
			}
			System.out.println(edgeMapper);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		GraphParserDrawer myData = new GraphParserDrawer();
		//myData.createVertexMap();
		//myData.createEdgeMap();
		Graph<String, String> g = new SparseGraph<String,String>();

		g.addVertex("000");
		g.addVertex("001");
		g.addVertex("010");
		g.addVertex("100");
		g.addVertex("011");
		g.addVertex("101");
		g.addVertex("110");
		g.addVertex("111");
		g.addEdge("e0", new Pair<String>("000","001"));
		g.addEdge("e1", new Pair<String>("000","010"));
		g.addEdge("e2", new Pair<String>("000","100"));
		g.addEdge("e3", new Pair<String>("001","011"));
		g.addEdge("e4", new Pair<String>("001","101"));
		g.addEdge("e5", new Pair<String>("010","011"));
		g.addEdge("e6", new Pair<String>("010","110"));
		g.addEdge("e7", new Pair<String>("100","101"));
		g.addEdge("e8", new Pair<String>("100","110"));
		g.addEdge("e9", new Pair<String>("011","111"));
		g.addEdge("e10", new Pair<String>("101","111"));
		g.addEdge("e11", new Pair<String>("110","111"));

		//System.out.println(g);


		Layout<String,String> l = new FRLayout<String,String>(g);
		Dimension dim = new Dimension(750,750);
		VisualizationImageServer<String,String> vis = new VisualizationImageServer<String,String>(l, dim);

		BufferedImage im = (BufferedImage) vis.getImage(
				new Point2D.Double(dim.getWidth()/2, dim.getHeight()/2),
				dim);
		ImageIO.write((RenderedImage) im, "jpg", new File("out.jpg"));
	}

}