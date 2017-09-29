/**
 * 
 */
package sp1;


import java.util.*;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
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
	
	/**
	 * Iterates over a HashMap and prints it out
	 * @param hmap - given HashMap
	 */
	
	public void mapIterator(HashMap<Integer,String> hmap) {
		Iterator<Map.Entry<Integer, String>> it = hmap.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry<Integer, String> pair = it.next();
			System.out.println(pair.getKey() + " " + pair.getValue());
		}
	}
	
	/**
	 * Creates a HashMap of vertices
	 * @return - The HashMap of vertices
	 * @throws IOException
	 */
	public HashMap<Integer, String> createVertexMap () throws IOException {
		try (Scanner scanner = new Scanner(new File ("dolphins.txt"))){
			Scanner myDelim = scanner.useDelimiter("label");
			HashMap<Integer, String> idMapper = new HashMap<Integer, String>();
			
			myDelim.next();
			
			for (int i = 0; i <= 61 && myDelim.hasNext(); i++) {
				idMapper.put(i, myDelim.findInLine("\\\"\\w*\\\""));
				myDelim.next();
			}
			
			return idMapper;
		}
	}
	
	/**
	 * Creates a HashMap of edges
	 * @return - The HashMap of edges
	 * @throws IOException
	 */
	public HashMap<Integer, ArrayList<Integer>> createEdgeMap () throws IOException {
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
			return edgeMapper;
		}
	}

	
	public static void main(String[] args) throws IOException {
		// Creates an instance of the class and a new SparseGraph
		GraphParserDrawer myData = new GraphParserDrawer();
		Graph<String, String> g = new SparseGraph<String,String>();
		
		// Instantiates an ArrayList of all the keys in the edge and vertex maps
		ArrayList<Integer> edgeKeySet = new ArrayList<Integer>();
		edgeKeySet.addAll(myData.createEdgeMap().keySet());
		
		ArrayList<Integer> vertexKeySet = new ArrayList<Integer>();
		vertexKeySet.addAll(myData.createVertexMap().keySet());
		
		// Instance of createEdgeMap() method for clarity
		HashMap<Integer, ArrayList<Integer>> edgeMap = myData.createEdgeMap();

		
		// For loop that add the vertices to the graph
		for (int i = 0; i <= myData.createVertexMap().size() - 1; i++) {
			g.addVertex(vertexKeySet.get(i).toString());
		}
		
		// For loop that adds the edges to the graph
		for (int i = 0, k = 0; i <= edgeMap.size() - 1; i++) {
			for (int j = 0; j <= edgeMap.get(edgeKeySet.get(i)).size() - 1; j++) {
				g.addEdge("e" + Integer.toString(k), 
						new Pair<String>(edgeKeySet.get(i).toString(), 
										edgeMap.get(edgeKeySet.get(i)).get(j).toString()));
				k++;
			}
		}

		System.out.println(g);


		FRLayout<String,String> l = new FRLayout<String,String>(g);
		Dimension dim = new Dimension(1000,1000);
		
		// Set the repulsion and attraction multiplier for FRLayout
		l.setAttractionMultiplier(0.1);
		l.setRepulsionMultiplier(0.6);
		
		VisualizationImageServer<String,String> vis = new VisualizationImageServer<String,String>(l, dim);

		BufferedImage im = (BufferedImage) vis.getImage(
				new Point2D.Double(dim.getWidth()/2, dim.getHeight()/2),
				dim);
		ImageIO.write((RenderedImage) im, "jpg", new File("dolphin.jpg"));
	}

}