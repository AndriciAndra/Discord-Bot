package graph.draw;

public interface Idraw {
    GraphDraw draw(String command1, String command2);

    GraphDraw drawBFS(String command1, String command2, String command3);

    GraphDraw drawBFSshortestPath(String command1, String command2, String command3, String command4);

    GraphDrawColor drawColoring(String command1, String command2);
}
