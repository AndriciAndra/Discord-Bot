package graph.components;

import org.jgrapht.Graph;
import org.jgrapht.GraphType;

import java.util.*;
import java.util.function.Supplier;

public class GraphCustom<V, E> implements Graph<V, E> {
    List<EdgeNode> edges = new LinkedList<>();
    Set<Node> nodes = new HashSet<>();

    @Override
    public String toString() {
        return "GraphCustom{" +
                "edges=" + edges +
                ", nodes=" + nodes +
                '}';
    }

    public Node getNode(String name) {
        for (Node node : nodes) {
            if (node.getName().equals(name))
                return node;
        }
        return null;
    }

    @Override
    public Set<E> getAllEdges(V v, V v1) {
        for (EdgeNode edge : edges) {
            if (v.equals(edge.getDestination()) && v1.equals(edge.getSource()) ||
                    v.equals(edge.getSource()) && v1.equals(edge.getDestination()))
                return (Set<E>) Set.of(edge);
        }
        return null;
    }

    @Override
    public E getEdge(V v, V v1) {
        for (EdgeNode edge : edges) {
            if (v.equals(edge.getDestination()) && v1.equals(edge.getSource()) ||
                    v.equals(edge.getSource()) && v1.equals(edge.getDestination()))
                return (E) edge;
        }
        return null;
    }

    @Override
    public Supplier<V> getVertexSupplier() {
        return null;
    }

    @Override
    public Supplier<E> getEdgeSupplier() {
        return null;
    }

    @Override
    public E addEdge(V v, V v1) {
        EdgeNode tempEdge = new EdgeNode();
        for (Node node : nodes) {
            if (v.equals(node)) tempEdge.setSource(node);
            if (v1.equals(node))
                tempEdge.setDestination(node);
        }
        edges.add(tempEdge);
        return null;
    }

    @Override
    public boolean addEdge(V v, V v1, E e) {
        for (Node node : nodes) {
            if (v.equals(node)) ((EdgeNode) e).setSource(node);
            if (v1.equals(node)) ((EdgeNode) e).setDestination(node);
        }
        this.edges.add((EdgeNode) e);
        return false;
    }

    @Override
    public V addVertex() {
        Node node = new Node();
        this.nodes.add(node);
        return (V) node;
    }

    @Override
    public boolean addVertex(V v) {
        nodes.add((Node) v);
        return true;
    }

    @Override
    public boolean containsEdge(V v, V v1) {
        for (EdgeNode edge : edges) {
            if (edge.getSource().equals(v) && edge.getDestination().equals(v1) ||
                    edge.getSource().equals(v1) && edge.getDestination().equals(v))
                return true;
        }
        return false;
    }

    @Override
    public boolean containsEdge(E e) {
        for (EdgeNode edgeNode : edges) {
            if (edgeNode.equals(e))
                return true;
        }
        return false;
    }

    @Override
    public boolean containsVertex(V v) {
        for (Node node : nodes) {
            if (node.equals(v))
                return true;
        }
        return false;
    }

    @Override
    public Set<E> edgeSet() {
        Set<EdgeNode> temp = new HashSet<>();
        temp.addAll(edges);
        return (Set<E>) temp;
    }

    @Override
    public int degreeOf(V v) {
        int deg = 0;
        for (EdgeNode edgeNode : edges) {
            if (edgeNode.getDestination().equals(v) || edgeNode.getSource().equals(v))
                deg++;
        }

        return deg;
    }

    @Override
    public Set<E> edgesOf(V v) {
        Set<E> allEdges = new HashSet<>();
        for (EdgeNode edgeNode : edges) {
            if (edgeNode.getDestination().equals(v) || edgeNode.getSource().equals(v))
                allEdges.add((E) edgeNode);
        }

        return allEdges;
    }

    @Override
    public int inDegreeOf(V v) {
        return degreeOf(v);
    }

    @Override
    public Set<E> incomingEdgesOf(V v) {
        return edgesOf(v);
    }

    @Override
    public int outDegreeOf(V v) {
        return degreeOf(v);
    }

    @Override
    public Set<E> outgoingEdgesOf(V v) {
        return edgesOf(v);
    }

    @Override
    public boolean removeAllEdges(Collection<? extends E> collection) {
        nodes.removeAll(collection);
        return true;
    }

    @Override
    public Set<E> removeAllEdges(V v, V v1) {
        Set<E> toRemove = new HashSet<>();
        for (EdgeNode edgeNode : edges) {
            if (edgeNode.getSource().equals(v) && edgeNode.getDestination().equals(v1) ||
                    edgeNode.getSource().equals(v1) && edgeNode.getDestination().equals(v))
                toRemove.add((E) edgeNode);
        }
        edges.removeAll(toRemove);
        return null;
    }

    @Override
    public boolean removeAllVertices(Collection<? extends V> collection) {
        nodes.removeAll(collection);
        return false;
    }

    @Override
    public E removeEdge(V v, V v1) {
        EdgeNode toRemove = new EdgeNode();
        for (EdgeNode edgeNode : edges) {
            if (edgeNode.getSource().equals(v) && edgeNode.getDestination().equals(v1) ||
                    edgeNode.getSource().equals(v1) && edgeNode.getDestination().equals(v)) {
                toRemove = edgeNode;
                break;
            }
        }
        edges.remove(toRemove);
        return (E) toRemove;
    }

    @Override
    public boolean removeEdge(E e) {
        if (edges.contains(e)) {
            edges.remove(e);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeVertex(V v) {
        if (nodes.contains(v)) {
            nodes.remove(v);
            return true;
        }
        return false;
    }

    @Override
    public Set<V> vertexSet() {
        return (Set<V>) nodes;
    }

    @Override
    public V getEdgeSource(E e) {
        if (!edges.contains(e))
            return null;
        int index = edges.indexOf(e);
        return (V) edges.get(index).getSource();
    }

    @Override
    public V getEdgeTarget(E e) {
        if (!edges.contains(e))
            return null;
        int index = edges.indexOf(e);
        return (V) edges.get(index).getDestination();
    }

    @Override
    public GraphType getType() {
        return null;
    }

    @Override
    public double getEdgeWeight(E e) {
        return 0;
    }

    @Override
    public void setEdgeWeight(E e, double v) {

    }
}
