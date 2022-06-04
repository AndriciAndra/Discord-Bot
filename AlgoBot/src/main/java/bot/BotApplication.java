package bot;

import command.Command;
import command.CommandRepository;
import database.ManagerSingleton;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import graph.components.EdgeNode;
import graph.components.GraphCustom;
import graph.components.Node;
import graph.draw.GraphDraw;
import graph.draw.GraphDrawColor;
import graph.draw.Idraw;
import org.jgrapht.alg.color.GreedyColoring;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.traverse.BreadthFirstIterator;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Instant;
import java.util.Random;

public class BotApplication implements Idraw {
    private String BFSsearch;
    private String coloring;
    private String BFSshortestPath;

    private ByteArrayOutputStream exportImage(GraphDraw frame) {
        BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        frame.paintAll(g);
        g.dispose();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    private void createCommands() {
        Command command1 = new Command(1,
                "!connected_graph",
                "A connected graph is graph that is connected in the sense of a topological space, i.e., there is a path from any point to any other point in the graph. A graph that is not connected is said to be disconnected. This definition means that the null graph and singleton graph are considered connected, while empty graphs on.",
                "More info: https://mathworld.wolfram.com/ConnectedGraph.html");
        Command command2 = new Command(2,
                "!oriented_graph",
                "An oriented graph is a directed graph having no symmetric pair of directed edges. A complete oriented graph is called a tournament.",
                "More info: https://mathworld.wolfram.com/OrientedGraph.html");
        Command command3 = new Command(3,
                "!graph",
                "The word \"graph\" has (at least) two meanings in mathematics.\n" +
                        "\n" +
                        "In elementary mathematics, \"graph\" refers to a function graph or \"graph of a function,\" i.e., a plot.\n" +
                        "\n" +
                        "In a mathematician's terminology, a graph is a collection of points and lines connecting some (possibly empty) subset of them. The points of a graph are most commonly known as graph vertices, but may also be called \"nodes\" or simply \"points.\" Similarly, the lines connecting the vertices of a graph are most commonly known as graph edges, but may also be called \"arcs\" or \"lines.\"",
                "More info  at : https://mathworld.wolfram.com/Graph.html");
        Command command4 = new Command(4,
                "!isomorphic_graph",
                "Let V(G) be the vertex set of a simple graph and E(G) its edge set. Then a graph isomorphism from a simple graph G to a simple graph H is a bijection f:V(G)->V(H) such that uv in E(G) iff f(u)f(v) in E(H) (West 2000, p. 7).\n" +
                        "\n" +
                        "If there is a graph isomorphism for G to H, then G is said to be isomorphic to H, written G=H.",
                "There exists no known P algorithm for graph isomorphism testing, although the problem has also not been shown to be NP-complete. As a result, the special complexity class graph isomorphism complete is sometimes used to refer to the problem of graph isomorphism testing.");
        Command command5 = new Command(5,
                "!perfect_matching",
                "A perfect matching of a graph is a matching (i.e., an independent edge set) in which every vertex of the graph is incident to exactly one edge of the matching. A perfect matching is therefore a matching containing n/2 edges (the largest possible), meaning perfect matchings are only possible on graphs with an even number of vertices. A perfect matching is sometimes called a complete matching or 1-factor.",
                "More info at: https://mathworld.wolfram.com/PerfectMatching.html");
        Command command6 = new Command(6,
                "!multigraph",
                "The term multigraph refers to a graph in which multiple edges between nodes are either permitted (Harary 1994, p. 10; Gross and Yellen 1999, p. 4) or required (Skiena 1990, p. 89, Pemmaraju and Skiena 2003, p. 198; Zwillinger 2003, p. 220). West (2000, p. xiv) recommends avoiding the term altogether on the grounds of this ambiguity.",
                "More info at: https://mathworld.wolfram.com/Multigraph.html");
        Command command7 = new Command(7,
                "!independent_set",
                "Two sets A and B are said to be independent if their intersection A intersection B=emptyset, where emptyset is the empty set. For example, {A,B,C} and {D,E} are independent, but {A,B,C} and {C,D,E} are not. Independent sets are also called disjoint or mutually exclusive. ",
                "More info at : https://mathworld.wolfram.com/IndependentSet.html");
        Command command8 = new Command(8,
                "!graph_matching",
                "A matching, also called an independent edge set, on a graph G is a set of edges of G such that no two sets share a vertex in common.\n"
                        + "\n"
                        + "It is not possible for a matching on a graph with n nodes to exceed n/2 edges. When a matching with n/2 edges exists, it is called a perfect matching. When a matching exists that leaves a single vertex unmatched, it is called a near-perfect matching.\n"
                        + "\n"
                        + "While not all graphs have perfect matchings, a largest matching (commonly known as a maximum matching or maximum independent edge set) exists for every graph. The size of this maximum matching is called the matching number of G and is denoted nu(G).",
                "More info at: https://mathworld.wolfram.com/Matching.html");
        addCommand(command1);
        addCommand(command2);
        addCommand(command3);
        addCommand(command4);
        addCommand(command5);
        addCommand(command6);
        addCommand(command7);
        addCommand(command8);
    }

    private void addCommand(Command command) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("testProject");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(command);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public GraphDraw draw(String command1, String command2) {
        GraphDraw frame = new GraphDraw();
        frame.setSize(800, 800);
        frame.setVisible(true);

        int countNodes = Integer.parseInt(command1);
        String[] input = command2.split("-");

        int index = 1;
        while (countNodes != 0) {
            Random random = new Random();
            //(index * 50)
            int randomCoordsY = random.nextInt(50);
            int yCoords = (2 * randomCoordsY * index + randomCoordsY) % 800;
            int yCoords2 = (2 * randomCoordsY * index - randomCoordsY) % 800;
            int randomCoordsX = random.nextInt(100);

            if ((index - 1) % 2 == 0)
                frame.addNode(String.valueOf(index - 1), (randomCoordsX * index) % 800, yCoords > 50 ? yCoords : yCoords + 50);
            else
                frame.addNode(String.valueOf(index - 1), (randomCoordsX * index) % 800, yCoords2 > 50 ? yCoords2 : yCoords2 + 50);
            index++;
            countNodes--;
        }
        for (int iterator = 0; iterator < input.length; iterator++) {
            String edge = input[iterator];
            frame.addEdge(Character.getNumericValue(edge.charAt(0)), Character.getNumericValue(edge.charAt(1)));
        }
        return frame;
    }

    public GraphDraw drawBFS(String command1, String command2, String command3) {
        GraphDraw frame = new GraphDraw();
        GraphCustom<Node, EdgeNode> graphCustom = new GraphCustom<>();
        frame.setSize(800, 800);
        frame.setVisible(true);

        int countNodes = Integer.parseInt(command1);
        String[] input = command2.split("-");
        Random random = new Random();
        int index = 1;
        while (countNodes != 0) {
            //(index * 50)
            int randomCoordsY = random.nextInt(50);
            int yCoords = (2 * randomCoordsY * index + randomCoordsY) % 800;
            int yCoords2 = (2 * randomCoordsY * index - randomCoordsY) % 800;
            int randomCoordsX = random.nextInt(100);
            int coordY;
            int coordX = (randomCoordsX * index) % 800;
            if ((index - 1) % 2 == 0)
                coordY = yCoords > 50 ? yCoords : yCoords + 50;
            else
                coordY = yCoords2 > 50 ? yCoords2 : yCoords2 + 50;
            frame.addNode(String.valueOf(index - 1), coordX, coordY);
            graphCustom.addVertex(new Node(String.valueOf(index - 1), coordX, coordY));
            index++;
            countNodes--;
        }

        for (int iterator = 0; iterator < input.length; iterator++) {
            String edge = input[iterator];
            frame.addEdge(Character.getNumericValue(edge.charAt(0)), Character.getNumericValue(edge.charAt(1)));
            String firstNodeName = String.valueOf(Character.getNumericValue(edge.charAt(0)));
            String secondNodeName = String.valueOf(Character.getNumericValue(edge.charAt(1)));
            Node tempNode1 = graphCustom.getNode(firstNodeName);
            Node tempNode2 = graphCustom.getNode(secondNodeName);
            graphCustom.addEdge(tempNode1, tempNode2);
        }
        BFSsearch = "";
        System.out.println(graphCustom);
        BreadthFirstIterator<Node, EdgeNode> breadthFirstIterator = new BreadthFirstIterator<>(graphCustom, graphCustom.getNode(command3));
        while (breadthFirstIterator.hasNext()) {
            BFSsearch = BFSsearch + breadthFirstIterator.next().getName() + " ";
        }
        return frame;
    }

    public GraphDraw drawBFSshortestPath(String command1, String command2, String command3, String command4) {
        GraphDraw frame = new GraphDraw();
        GraphCustom<Node, EdgeNode> graphCustom = new GraphCustom<>();
        frame.setSize(800, 800);
        frame.setVisible(true);

        int countNodes = Integer.parseInt(command1);
        String[] input = command4.split("-");
        Random random = new Random();
        int index = 1;
        while (countNodes != 0) {
            //(index * 50)
            int randomCoordsY = random.nextInt(50);
            int yCoords = (2 * randomCoordsY * index + randomCoordsY) % 800;
            int yCoords2 = (2 * randomCoordsY * index - randomCoordsY) % 800;
            int randomCoordsX = random.nextInt(100);
            int coordY;
            int coordX = (randomCoordsX * index) % 800;
            if ((index - 1) % 2 == 0)
                coordY = yCoords > 50 ? yCoords : yCoords + 50;
            else
                coordY = yCoords2 > 50 ? yCoords2 : yCoords2 + 50;
            frame.addNode(String.valueOf(index - 1), coordX, coordY);
            graphCustom.addVertex(new Node(String.valueOf(index - 1), coordX, coordY));
            index++;
            countNodes--;
        }

        for (int iterator = 0; iterator < input.length; iterator++) {
            String edge = input[iterator];
            frame.addEdge(Character.getNumericValue(edge.charAt(0)), Character.getNumericValue(edge.charAt(1)));
            String firstNodeName = String.valueOf(Character.getNumericValue(edge.charAt(0)));
            String secondNodeName = String.valueOf(Character.getNumericValue(edge.charAt(1)));
            Node tempNode1 = graphCustom.getNode(firstNodeName);
            Node tempNode2 = graphCustom.getNode(secondNodeName);
            graphCustom.addEdge(tempNode1, tempNode2);
        }
        BFSshortestPath = "";
        BFSShortestPath<Node, EdgeNode> bfsShortestPath = new BFSShortestPath<>(graphCustom);
        BFSshortestPath = bfsShortestPath.getPath(graphCustom.getNode(command2), graphCustom.getNode(command3)).toString();
        return frame;
    }

    public GraphDrawColor drawColoring(String command1, String command2) {
        GraphDrawColor frame = new GraphDrawColor();
        GraphCustom<Node, EdgeNode> graphCustom = new GraphCustom<>();
        frame.setSize(800, 800);
        frame.setVisible(true);

        int countNodes = Integer.parseInt(command1);
        String[] input = command2.split("-");
        Random random = new Random();
        int index = 1;
        while (countNodes != 0) {
            //(index * 50)
            int randomCoordsY = random.nextInt(50);
            int yCoords = (2 * randomCoordsY * index + randomCoordsY) % 800;
            int yCoords2 = (2 * randomCoordsY * index - randomCoordsY) % 800;
            int randomCoordsX = random.nextInt(100);
            int coordY;
            int coordX = (randomCoordsX * index) % 800;
            if ((index - 1) % 2 == 0)
                coordY = yCoords > 50 ? yCoords : yCoords + 50;
            else
                coordY = yCoords2 > 50 ? yCoords2 : yCoords2 + 50;
            frame.addNode(String.valueOf(index - 1), coordX, coordY);
            graphCustom.addVertex(new Node(String.valueOf(index - 1), coordX, coordY));
            index++;
            countNodes--;
        }
        for (int iterator = 0; iterator < input.length; iterator++) {
            String edge = input[iterator];
            frame.addEdge(Character.getNumericValue(edge.charAt(0)), Character.getNumericValue(edge.charAt(1)));
            String firstNodeName = String.valueOf(Character.getNumericValue(edge.charAt(0)));
            String secondNodeName = String.valueOf(Character.getNumericValue(edge.charAt(1)));
            Node tempNode1 = graphCustom.getNode(firstNodeName);
            Node tempNode2 = graphCustom.getNode(secondNodeName);
            graphCustom.addEdge(tempNode1, tempNode2);
        }
        coloring = "";
        GreedyColoring<Node, EdgeNode> greedyColoring = new GreedyColoring<>(graphCustom);
        coloring = String.valueOf(greedyColoring.getColoring());
        frame.setGraphCustomColoring(graphCustom);
        return frame;
    }

    public BotApplication() {
        DiscordClient client = DiscordClient.create("token");

        //createCommands(); //only for the first run of the project

        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) -> {
            // ReadyEvent example
            Mono<Void> printOnLogin = gateway.on(ReadyEvent.class, event -> Mono.fromRunnable(() -> {
                final User self = event.getSelf();
                System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
            })).then();

            // MessageCreateEvent example
            Mono<Void> handleCommands = gateway.on(MessageCreateEvent.class, event -> {
                ManagerSingleton managerSingleton = ManagerSingleton.getInstance();
                EntityManager entityManager = managerSingleton.getEntityManagerFactory().createEntityManager();
                entityManager.getTransaction().begin();
                CommandRepository commandRepository = new CommandRepository();

                Message message = event.getMessage();
                String[] command = message.getContent().split(" ");
                switch (command[0]) {
                    case "!" -> {
                        String commands = commandRepository.findAllNames();
                        entityManager.close();
                        managerSingleton.closeEntityManagerFactory();
                        String output = commands + "!draw_graph countNodes edge-edge..\n!bfs countNodes startNode edge-edge..\n!coloring countNodes edge-edge..\n!bfs_shortest_path countNodes startNode finalNode edge-edge..";
                        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                                .title("Hello! :)")
                                .description("I'm AlgoBot!")
                                .thumbnail("https://banner2.cleanpng.com/20180411/lew/kisspng-graph-theory-girth-adjacency-matrix-petersen-graph-mathematics-5acddbac7dbca0.970463721523440556515.jpg")
                                .addField("The commands available are:", output, false)
                                .timestamp(Instant.now())
                                .build();
                        return message.getChannel().flatMap(channel -> channel.createMessage(embed));
                    }
                    case "!connected_graph", "!oriented_graph", "!graph",
                            "!isomorphic_graph", "!graph_matching", "!independent_set",
                            "!perfect_matching", "!multigraph" -> {
                        Command function = commandRepository.findByName(command[0]);
                        entityManager.close();
                        managerSingleton.closeEntityManagerFactory();
                        return message.getChannel().flatMap(channel ->
                                channel.createMessage(new StringBuilder().append(function.getInformation())
                                        .append("\n")
                                        .append(function.getLink())
                                        .toString()));
                    }
                    case "!draw_graph" -> {
                        GraphDraw frame = draw(command[1], command[2]);
                        InputStream inputStream = new ByteArrayInputStream(exportImage(frame).toByteArray());
                        event.getMessage().getChannel().block().createMessage(messageCreateSpec ->
                        {
                            messageCreateSpec.addFile("graph.png", inputStream);
                        }).block();
                        return message.getChannel().flatMap(channel -> channel.createMessage("Done!"));
                    }
                    case "!bfs" -> {
                        GraphDraw frame = drawBFS(command[1], command[3], command[2]);
                        InputStream inputStream = new ByteArrayInputStream(exportImage(frame).toByteArray());
                        event.getMessage().getChannel().block().createMessage(messageCreateSpec ->
                        {
                            messageCreateSpec.addFile("graph.png", inputStream);
                        }).block();
                        return message.getChannel().flatMap(channel -> channel.createMessage("An example of BFS starting from node " + command[2] + ": " + BFSsearch));
                    }
                    case "!coloring" -> {
                        GraphDrawColor frame = drawColoring(command[1], command[2]);
                        BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
                        Graphics g = image.getGraphics();
                        frame.paintAll(g);
                        g.dispose();
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        try {
                            ImageIO.write(image, "png", outputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                        event.getMessage().getChannel().block().createMessage(messageCreateSpec ->
                        {
                            messageCreateSpec.addFile("graph.png", inputStream);
                        }).block();
                        return message.getChannel().flatMap(channel -> channel.createMessage("An example of greedy coloring is: \n" + coloring));
                    }
                    case "!bfs_shortest_path" -> {
                        GraphDraw frame = drawBFSshortestPath(command[1], command[2], command[3], command[4]);
                        InputStream inputStream = new ByteArrayInputStream(exportImage(frame).toByteArray());
                        event.getMessage().getChannel().block().createMessage(messageCreateSpec ->
                        {
                            messageCreateSpec.addFile("graph.png", inputStream);
                        }).block();
                        return message.getChannel().flatMap(channel -> channel.createMessage("The shortest path from " + command[2] + " to " + command[3] + " is: " + BFSshortestPath));
                    }
                    default -> {
                        return Mono.empty();
                    }
                }
            }).then();
            return printOnLogin.and(handleCommands);
        });
        login.block();
    }
}
