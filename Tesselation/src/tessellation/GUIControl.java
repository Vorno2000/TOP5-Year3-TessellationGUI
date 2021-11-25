/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tessellation;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 *
 * @author Vorno
 */

//this is the exact same as the methods found in Tesselation.java
//refer to those comments if needed
public class GUIControl {
    
    private ArrayList<Point> vertices;
    private ArrayList<String> interior_edges;
    private ArrayList<String> optimal_edges;
    private double optimal_length = 0;
    private ModList<Integer> search;
    private int total_edges;
    private int n;

    public GUIControl(ArrayList<Point> new_Vertices) {
        vertices = new ArrayList<>();
        for(Point A : new_Vertices)
            vertices.add(A);
        
        interior_edges = new ArrayList<>();
        optimal_edges = new ArrayList<>();
        
        ArrayList underLyingList = new ArrayList();
        search = new ModList<>(underLyingList);
        for(int i = 0; i < vertices.size(); i++)
            search.add(i);
        
        n = vertices.size();
        total_edges = n-3;
        
    }
    
    public void start_Bruteforce() {
        bruteForce(0);
    }
    
    public void start_Approximate() {
        approximate(0, vertices);
        while(interior_edges.size() < total_edges) {
            complete();
        }
    }
    
    public void approximate(int V_n, ArrayList<Point> vertices) {
        double smallest_length = 0.0;
        String smallest_edge = "";
        
        if(interior_edges.size() == total_edges) {
            for(String A : interior_edges) {
                optimal_length += edgeLength(A);
                optimal_edges.add(A);
            }
        }
        else if(interior_edges.isEmpty()) {
            smallest_length = edgeLength(search.get(V_n+1)+":"+search.get(V_n-1));
            smallest_edge = search.get(V_n+1)+":"+search.get(V_n-1);
            
            for(int i = 0; i < vertices.size()-1; i++) {
                if(i != search.get(V_n+1) && i != search.get(V_n-1)) {
                    if(i != V_n) {
                        double length_test = edgeLength(V_n+":"+i);
                        if(length_test < smallest_length) {
                            smallest_length = length_test;
                            smallest_edge = V_n+":"+i;
                        }
                    }
                }
            }
            interior_edges.add(smallest_edge);
            approximate(search.get(V_n+1), vertices);
        }
        else {
            String check = interior_edges.get(interior_edges.size()-1);
            String[] points = check.split(":");
            int vertex_one = Integer.parseInt(points[0]);
            int vertex_two = Integer.parseInt(points[1]);
            
            if(vertex_one == V_n) {
                if(!checkInteriorLine(vertex_two+":"+search.get(V_n+1))) {
                    if(checkInteriorLine(search.get(V_n)+":"+search.get(V_n+2))) {
                        smallest_length = edgeLength(vertex_two+":"+search.get(V_n+2));
                        smallest_edge = vertex_two+":"+search.get(V_n+2);
                    }
                    else {
                        smallest_length = edgeLength(vertex_two+":"+search.get(V_n+1));
                        smallest_edge = vertex_two+":"+search.get(V_n+1);
                    }
                }
            }
            else if(vertex_two == V_n) {
                if(!checkInteriorLine(vertex_one+":"+search.get(V_n+1))) {
                    if(checkInteriorLine(search.get(V_n)+":"+search.get(V_n+2))) {
                        smallest_length = edgeLength(vertex_one+":"+search.get(V_n+2));
                        smallest_edge = vertex_one+":"+search.get(V_n+2);
                    }
                    else {
                        smallest_length = edgeLength(vertex_one+":"+search.get(V_n+1));
                        smallest_edge = vertex_one+":"+search.get(V_n+1);
                    }
                }
            }
            
            if(vertex_one < vertex_two) {
                if(V_n < vertex_two) {
                    for(int i = vertex_two; i != search.get(vertex_one); i = search.get(i-1)) {
                        if(i != V_n) {
                            if(i != search.get(V_n-1) && i != search.get(V_n+1)) {
                                if(!checkInteriorLine(search.get(i-1)+":"+search.get(i+1))) {
                                    if(V_n == vertex_one) {
                                        if(i != vertex_two) {
                                            double length = edgeLength(V_n+":"+i);
                                            if(smallest_length == 0.0) {
                                                smallest_length = length;
                                                smallest_edge = V_n+":"+i;
                                            }
                                            else if(length < smallest_length) {
                                                smallest_length = length;
                                                smallest_edge = V_n+":"+i;
                                            }
                                        }
                                    }
                                    else {
                                        if(!checkInteriorLine(search.get(V_n)+":"+i)) {
                                            double length = edgeLength(V_n+":"+i);
                                            if(smallest_length == 0.0) {
                                                smallest_length = length;
                                                smallest_edge = V_n+":"+i;
                                            }
                                            else if(length < smallest_length) {
                                                smallest_length = length;
                                                smallest_edge = V_n+":"+i;
                                            }
                                        }
                                    }
                                }
                            }    
                        }
                    }
                    if(!smallest_edge.equals(""))
                        interior_edges.add(smallest_edge);
                    if(V_n == vertices.size()-1)
                        complete();
                    else
                        approximate(search.get(V_n+1), vertices);
                    //minus from vertex_two
                }
                else {
                    for(int i = vertex_two; i != search.get(vertex_one); i = search.get(i+1)) {
                        if(i != V_n) {
                            if(i != search.get(V_n-1) && i != search.get(V_n+1)) {
                                if(!checkInteriorLine(search.get(i-1)+":"+search.get(i+1))) {
                                    double length = edgeLength(V_n+":"+i);
                                    if(smallest_length == 0.0) {
                                        smallest_length = length;
                                        smallest_edge = V_n+":"+i;
                                    }
                                    else if(length < smallest_length) {
                                        smallest_length = length;
                                        smallest_edge = V_n+":"+i;
                                    }
                                }
                            }    
                        }
                    }
                    if(!smallest_edge.equals(""))
                        interior_edges.add(smallest_edge);
                    if(V_n == vertices.size()-1)
                        complete();
                    else
                        approximate(search.get(V_n+1), vertices);
                    //plus from vertex_two
                }
            }
            else {
                if(V_n < vertex_one) {
                    for(int i = vertex_one; i != search.get(vertex_two); i = search.get(i-1)) {
                        if(i != V_n) {
                            if(i != search.get(V_n-1) && i != search.get(V_n+1)) {
                                if(!checkInteriorLine(search.get(i-1)+":"+search.get(i+1))) {
                                    if(V_n == vertex_one) {
                                        if(i != vertex_two) {
                                            double length = edgeLength(V_n+":"+i);
                                            if(smallest_length == 0.0) {
                                                smallest_length = length;
                                                smallest_edge = V_n+":"+i;
                                            }
                                            else if(length < smallest_length) {
                                                smallest_length = length;
                                                smallest_edge = V_n+":"+i;
                                            }
                                        }
                                    }
                                    else {
                                        if(!checkInteriorLine(search.get(V_n)+":"+i)) {
                                            double length = edgeLength(V_n+":"+i);
                                            if(smallest_length == 0.0) {
                                                smallest_length = length;
                                                smallest_edge = V_n+":"+i;
                                            }
                                            else if(length < smallest_length) {
                                                smallest_length = length;
                                                smallest_edge = V_n+":"+i;
                                            }
                                        }
                                    }
                                }
                                
                            }    
                        }
                    }
                    if(!smallest_edge.equals(""))
                        interior_edges.add(smallest_edge);
                    if(V_n == vertices.size()-1)
                        complete();
                    else
                        approximate(search.get(V_n+1), vertices);
                    //minus from vertex_one
                }
                else {
                    for(int i = vertex_one; i != search.get(vertex_two); i = search.get(i+1)) {
                        if(i != V_n) {
                            if(i != search.get(V_n-1) && i != search.get(V_n+1)) {
                                if(!checkInteriorLine(search.get(i-1)+":"+search.get(i+1))) {
                                    double length = edgeLength(V_n+":"+i);
                                    if(smallest_length == 0.0) {
                                        smallest_length = length;
                                        smallest_edge = V_n+":"+i;
                                    }
                                    else if(length < smallest_length) {
                                        smallest_length = length;
                                        smallest_edge = V_n+":"+i;
                                    }
                                }
                            }    
                        }
                    }
                    if(!smallest_edge.equals(""))
                        interior_edges.add(smallest_edge);
                    if(V_n == vertices.size()-1)
                        complete();
                    else
                        approximate(search.get(V_n+1), vertices);
                    //plus from vertex_one
                }
            }
        }
    }
    public void complete() {
        ArrayList<Point> new_vertices = new ArrayList<>();;
        for(int i = 0; i < interior_edges.size(); i++) {
            
            String[] xy = interior_edges.get(i).split(":");
            if(!new_vertices.contains(vertices.get(Integer.parseInt(xy[0]))))
                new_vertices.add(vertices.get(Integer.parseInt(xy[0])));
            if(!new_vertices.contains(vertices.get(Integer.parseInt(xy[1]))))
                new_vertices.add(vertices.get(Integer.parseInt(xy[1])));
        }
        approximate(0, new_vertices);
    }
    
    public void bruteForce(int V_n) {
        if(interior_edges.size() == total_edges) {
            double length = 0;
            for(String A : interior_edges) {
                length += edgeLength(A);
            }
            if(optimal_length == 0) {
                optimal_length = length;
                for(String i : interior_edges) {
                    optimal_edges.add(i);
                }
            }
            else if(length < optimal_length) {
                optimal_length = length;
                optimal_edges.removeAll(optimal_edges);
                for(String i : interior_edges) {
                    optimal_edges.add(i);
                }
            }
            return;
        }
        
        boolean noLine = true;
        int lineCounter = 0;
        
        for(int i = 0; i < vertices.size(); i++) {
            if(checkToConnect(search.get(V_n)+":"+i)) {
                noLine = false;
                lineCounter++;
                interior_edges.add(search.get(V_n)+":"+i);
                bruteForce(V_n+1);
                
                interior_edges.remove(search.get(V_n)+":"+i);
            }
        }
        if(noLine) {
            if(search.get(V_n) == vertices.size()-1)
                return;
            else
                bruteForce(V_n+1);
        }
        
        if(lineCounter > 1 && total_edges-interior_edges.size() >= lineCounter) {
            ArrayList<String> tempHolder = new ArrayList<>();
            for(int i = 0; i < vertices.size(); i++) {
                if(checkToConnect(search.get(V_n)+":"+i)) {
                    interior_edges.add(search.get(V_n)+":"+i);
                    tempHolder.add(search.get(V_n)+":"+i);
                }
            }
            bruteForce(V_n+1);
            
            for(String i : tempHolder) {
                interior_edges.remove(i);
            }
            
            if(search.get(V_n) != vertices.size()-1)
                bruteForce(V_n+1);
        }
    }
    
    public boolean checkToConnect(String possibleInteriorEdge) {
        boolean canConnect = false;
        String[] edges = possibleInteriorEdge.split(":");
        
        int edge_one = Integer.parseInt(edges[0]);
        int edge_two = Integer.parseInt(edges[1]);
        
        if(checkInteriorLine(possibleInteriorEdge) || edge_one == edge_two)
            return canConnect;
        else if(edge_two == search.get(edge_one-1) || edge_two == search.get(edge_one+1)) {
            return canConnect;
        }
        else {
            for(int i = search.get(edge_one-1); i != edge_two; i = search.get(i-1)) {
                for(int j = search.get(edge_two-1); j != edge_one; j = search.get(j-1)) {
                    String edge_check = i+":"+j;
                    
                    if(checkInteriorLine(edge_check))
                        return canConnect;
                }
            }
            canConnect = true;
            return canConnect;
        }
    }
    
    public boolean checkInteriorLine(String interiorLine) {
        boolean exists = false;
        
        String[] line_vertices = interiorLine.split(":");
        String vertex_one = line_vertices[0];
        String vertex_two = line_vertices[1];
        
        if(interior_edges.contains(vertex_one+":"+vertex_two) || interior_edges.contains(vertex_two+":"+vertex_one))
            exists = true;
        return exists;
    }
    
    public double edgeLength(String interior_edge) {
        String[] edge = interior_edge.split(":");
        int edge_one = Integer.parseInt(edge[0]);
        int edge_two = Integer.parseInt(edge[1]);
        
        Point A = vertices.get(edge_one);
        Point B = vertices.get(edge_two);
        
        double length = sqrt((pow(A.x-B.x, 2) + pow(A.y-B.y, 2)));
        
        return length;
    }
    
    public double getOptimalLength() {
        return optimal_length;
    }
    
    public ArrayList<String> getOptimalEdges() {
        ArrayList<String> optimal_vertices = new ArrayList<>();
        
        for(String A : optimal_edges) {
            String[] index = A.split(":");
            int Point_x = Integer.parseInt(index[0]);
            int Point_y = Integer.parseInt(index[1]);
            
            optimal_vertices.add(vertices.get(Point_x).x+","+vertices.get(Point_x).y+":"+vertices.get(Point_y).x+","+vertices.get(Point_y).y);
        }
        return optimal_vertices;
    }
}
