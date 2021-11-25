/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tessellation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author Vorno
 */
public class TessellationGUI extends JFrame implements ActionListener {
    private JLabel title;
    private JLabel entry_title;
    private JTextField vertex_entry;
    private JButton entry_btn;
    private JButton start_btn;
    private JComboBox methods;
    private JList<String> vertex_list;
    private JLabel error_msg;
    private Drawer canvas;
    private JButton example_btn;
    private JButton example2_btn;
    private JLabel example_label;
    private JButton reset_btn;
    private JLabel optimal_lines;
    private DefaultListModel<String> model;
    Border blackline;
    
    //initialises and sets the bounds for all components
    public TessellationGUI() {
        setLayout(null);
        setSize(350, 350);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        title = new JLabel("Tessellation GUI:");
        entry_title = new JLabel("Enter Vertex: \"x,y\"");
        optimal_lines = new JLabel("");
        vertex_entry = new JTextField();
        entry_btn = new JButton("Enter Vertex:");
        start_btn = new JButton("Calculate:");
        methods = new JComboBox();
        model = new DefaultListModel<>();
        reset_btn = new JButton("Reset:");
        vertex_list = new JList<>(model);
        error_msg = new JLabel("");
        example_label = new JLabel("<html>Uses Coordinates from Assignment example</html>");
        
        example_btn = new JButton("Example");
        example2_btn = new JButton("Example 2");
        
        blackline = BorderFactory.createLineBorder(Color.black);
        
        title.setBounds(super.getWidth()/2, 10, 130, 30);
        title.setSize(new Dimension(200, 50));
        title.setFont(new Font("Calibri", 1, 20));
        
        reset_btn.setBounds(super.getWidth(), 10, 110, 40);
        reset_btn.addActionListener(this);
        
        entry_btn.setBounds(50, super.getHeight()+60, 110, 40);
        entry_btn.addActionListener(this);
        
        vertex_entry.setBounds(50, super.getHeight()+10, 110, 40);
        entry_title.setBounds(50, super.getHeight()-25, 110, 40);
        
        optimal_lines.setBounds(40, super.getHeight()-80, 140, 100);
        optimal_lines.setBackground(Color.red);
        
        start_btn.setBounds(super.getWidth(), super.getHeight()+60, 110, 40);
        start_btn.addActionListener(this);
        
        methods.setBounds(super.getWidth(), super.getHeight()+10, 110, 40);
        methods.addItem("");
        methods.addItem("Approximate:");
        methods.addItem("Bruteforce:");
        
        example_btn.setBounds(super.getWidth()/2+30, super.getHeight()+10, 110, 40);
        example_btn.addActionListener(this);
        example_label.setBounds(super.getWidth()/2+30, super.getHeight()-70, 120, 100);
        
        example2_btn.setBounds(super.getWidth()/2+30, super.getHeight()+60, 110, 40);
        example2_btn.addActionListener(this);
        
        vertex_list.setBounds(10, 50, 200, 250);
        vertex_list.setBorder(blackline);
        
        error_msg.setBounds(super.getWidth(), super.getHeight()-70, 110, 100);
        error_msg.setForeground(Color.red);
        
        add(title);
        add(optimal_lines);
        add(reset_btn);
        add(example_label);
        add(example_btn);
        add(example2_btn);
        add(vertex_entry);
        add(entry_title);
        add(entry_btn);
        add(start_btn);
        add(methods);
        add(vertex_list);
        add(error_msg);
    }
    
    //actionlistener to see which buttons are pressed and to do the according action
    @Override
    public void actionPerformed(ActionEvent e) {
        error_msg.setText("");
        error_msg.setForeground(Color.red);
        if(e.getSource() == entry_btn) {
            String input = vertex_entry.getText();
            try {
                input = input.replace(" ","");
                String[] entered_vertices = input.split(",");
                int vertex_x = Integer.parseInt(entered_vertices[0]);
                int vertex_y = Integer.parseInt(entered_vertices[1]);

                model.addElement(vertex_x+","+vertex_y);
            } catch(Exception ex) {
                error_msg.setText("<html>Enter with format \"x,y\"</html>");
            }
            vertex_entry.setText("");
        }
        if(e.getSource() == example_btn) {
            model.removeAllElements();
            model.addElement("0,6");
            model.addElement("2,8");
            model.addElement("5,8");
            model.addElement("10,6");
            model.addElement("10,2");
            model.addElement("8,0");
            model.addElement("2,2");
        }
        if(e.getSource() == example2_btn) {
            model.removeAllElements();
            model.addElement("1,6");
            model.addElement("3,9");
            model.addElement("5,10");
            model.addElement("9,10");
            model.addElement("10,7");
            model.addElement("9,4");
            model.addElement("7,2");
            model.addElement("4,1");
            model.addElement("2,2");
            /*model.addElement("3,9");
            model.addElement("7,10");
            model.addElement("8,5");
            model.addElement("5,2");
            model.addElement("2,4");*/
        }
        
        //gets all calculations for the canvas to draw onto the jpanel Drawer
        if(e.getSource() == start_btn) {
            if(canvas != null)
                remove(canvas);
            if(model.size() < 4)
                error_msg.setText("<html>Tessellation not possible or already complete:</html>");
            else {
                if(methods.getSelectedIndex() == 0)
                    error_msg.setText("<html>Choose a method to begin computing with:</html>");
                else {
                    ArrayList<Point> vertices = new ArrayList<>();
                    int[] temp_x = new int[100];
                    int[] temp_y = new int[100];
                    for(int i = 0; i < model.size(); i++) {
                        String vertex = model.get(i);
                        String xy[] = vertex.split(",");

                        int vertex_x = Integer.parseInt(xy[0]);
                        int vertex_y = Integer.parseInt(xy[1]);
                        
                        temp_x[i] = vertex_x*20+30;
                        temp_y[i] = vertex_y*20+30;

                        Point new_Point = new Point(vertex_x, vertex_y);

                        vertices.add(new_Point);
                    }
                    int counter = 0;
                    for(int index : temp_x) {
                        if(index != 0)
                            counter++;
                        else
                            break;
                    }
                    int[] x = new int[counter];
                    int[] y = new int[counter];
                    
                    for(int i = 0; i < counter; i++) {
                        if(temp_x[counter-i-1] != 0) {
                            x[i] = temp_x[counter-i-1];
                            y[i] = temp_y[counter-i-1];
                        }
                    }
                    GUIControl control = new GUIControl(vertices);
                    long totalTime = 0;
                    if(methods.getSelectedIndex() == 1) {
                        long startTime = System.nanoTime();
                        control.start_Approximate();
                        long endTime = System.nanoTime();
                        totalTime = endTime-startTime;
                    }
                    else {
                        long startTime = System.nanoTime();
                        control.start_Bruteforce();
                        long endTime = System.nanoTime();
                        totalTime = endTime-startTime;
                    }
                    
                    int[] temp_x_line = new int[100];
                    int[] temp_y_line = new int[100];
                    
                    ArrayList<String> optimal_edges = control.getOptimalEdges();
                    double optimal_length = control.getOptimalLength();
                    
                    for(int i = 0; i < optimal_edges.size(); i++) {
                        String[] vertices_AB = optimal_edges.get(i).split(":");
                        String[] A_xy = vertices_AB[0].split(",");
                        String[] B_xy = vertices_AB[1].split(",");
                        
                        temp_x_line[i*2] = Integer.parseInt(A_xy[0]);
                        temp_x_line[i*2+1] = Integer.parseInt(B_xy[0]);

                        temp_y_line[i*2] = Integer.parseInt(A_xy[1]);
                        temp_y_line[i*2+1] = Integer.parseInt(B_xy[1]);
                    }
                    int[] x_line = new int[optimal_edges.size()*2];
                    int[] y_line = new int[optimal_edges.size()*2];
                    
                    for(int i = 0; i < optimal_edges.size()*2; i++) {
                        x_line[i] = temp_x_line[i];
                        y_line[i] = temp_y_line[i];
                    }
                    
                    canvas = new Drawer(x,y,x_line,y_line);
                    canvas.setBounds(super.getWidth()/2-30, 50, 265, 250);
                    canvas.setBorder(blackline);
                    add(canvas);
                    repaint();
                    
                    error_msg.setForeground(Color.blue);
                    String temp = "Optimal Length: "+String.format("%.2f", optimal_length);
                    String temp2 = "Total Time: "+totalTime;
                    error_msg.setText("<html>"+temp+"<br>"+temp2+"</html>");
                    
                    String optimal = optimal_edges.toString();
                    optimal = optimal.replace(", ", " ; ");
                    optimal_lines.setText("<html>"+optimal+"</html>");
                }
            }
        }
        
        if(e.getSource() == reset_btn) {
            model.removeAllElements();
            vertex_entry.setText("");
            optimal_lines.setText("");
            methods.setSelectedIndex(0);
            if(canvas != null)
                remove(canvas);
            repaint();
        }
    }
    
    public static void main(String[] args) {
        TessellationGUI jf = new TessellationGUI();
        jf.setSize(500,500);
        jf.setTitle("Tessellation GUI");
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
