package graph.components;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EdgeNode {
    Node source;
    Node destination;

    @Override
    public String toString() {
        return "(" + source + "," + destination + ")";
    }
}
