package graph.draw;

import graph.components.Edge;
import graph.components.Node;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class GraphDraw extends JFrame {
    private Graphics graphics;
    private final int width;
    private final int height;
    private final ArrayList<Node> nodes;
    private final ArrayList<Edge> edges;

    public GraphDraw() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        width = 30;
        height = 30;
    }

    public void addNode(String name, int coordX, int coordY) { //add a node at pixel (x,y)
        nodes.add(new Node(name, coordX, coordY));
        this.repaint();
    }

    public void addEdge(int source, int destination) { //add an edge between nodes i and j
        edges.add(new Edge(source, destination));
        this.repaint();
    }

    @Override
    public void paint(Graphics graphics) { //draw the nodes and edges
        FontMetrics font = graphics.getFontMetrics();
        int nodeHeight = Math.max(height, font.getHeight());
        graphics.setColor(Color.black);

        for (Edge e : edges)
            graphics.drawLine(nodes.get(e.getSource()).getCoordX(), nodes.get(e.getSource()).getCoordY(), nodes.get(e.getDestination()).getCoordX(), nodes.get(e.getDestination()).getCoordY());

        for (Node n : nodes) {
            int nodeWidth = Math.max(width, font.stringWidth(n.getName()) + width / 2);
            graphics.setColor(Color.white);
            graphics.fillOval(n.getCoordX() - nodeWidth / 2, n.getCoordY() - nodeHeight / 2, nodeWidth, nodeHeight);
            graphics.setColor(Color.black);
            graphics.drawOval(n.getCoordX() - nodeWidth / 2, n.getCoordY() - nodeHeight / 2, nodeWidth, nodeHeight);
            graphics.drawString(n.getName(), n.getCoordX() - font.stringWidth(n.getName()) / 2, n.getCoordY() + font.getHeight() / 2);
        }
        this.graphics = graphics;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }
}