package graph.components;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Node {
    int coordX;
    int coordY;
    private  String name;

    public Node(String name, int x, int y) {
        this.coordX = x;
        this.coordY = y;
        this.name = name;
    }

    @Override
    public String toString() {
        return
                "name='" + name + '\''
                ;
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public String getName() {
        return name;
    }
}
