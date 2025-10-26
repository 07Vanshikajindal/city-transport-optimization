package com.city.mst;

import java.util.*;

/**
 * Graph holds node list, edge list and adjacency map.
 * Field names match what Main.java expects: nodes, edges.
 */
public class Graph {
    public final List<String> nodes;
    public final List<Edge> edges;
    public final Map<String, List<Edge>> adj = new HashMap<>();

    public Graph(List<String> nodes, List<Edge> edges) {
        this.nodes = new ArrayList<>(nodes);
        this.edges = new ArrayList<>(edges);
        for (String n : nodes) adj.put(n, new ArrayList<>());
        for (Edge e : edges) {
            // add both directions for undirected graph
            adj.get(e.from).add(new Edge(e.from, e.to, e.weight));
            adj.get(e.to).add(new Edge(e.to, e.from, e.weight));
        }
    }

    // quick connectivity check
    public boolean isConnected() {
        if (nodes.isEmpty()) return true;
        Set<String> seen = new HashSet<>();
        Deque<String> dq = new ArrayDeque<>();
        dq.add(nodes.get(0));
        seen.add(nodes.get(0));
        while (!dq.isEmpty()) {
            String u = dq.poll();
            for (Edge e : adj.getOrDefault(u, Collections.emptyList())) {
                if (!seen.contains(e.to)) {
                    seen.add(e.to);
                    dq.add(e.to);
                }
            }
        }
        return seen.size() == nodes.size();
    }
}
