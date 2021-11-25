/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tessellation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.JPanel;

/**
 *
 * @author Vorno
 */
//Modified JPanel to work accordingly for the GUI
public class Drawer extends JPanel {
    public Polygon p;
    public int[] x_line, y_line;
    
    public Drawer(int[] x, int[] y, int[] x_line, int[] y_line) {
        this.p = new Polygon(x,y,x.length);
        this.x_line = x_line;
        this.y_line = y_line;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawPolygon(this.p);
        
        g.setColor(Color.red);
        for(int i = 0; i < x_line.length; i += 2) {
            g.drawLine(((x_line[i]*20)+30), ((y_line[i]*20)+30), ((x_line[i+1]*20)+30), ((y_line[i+1]*20)+30));
        }
    }
}
