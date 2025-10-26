//package com.city.mst;
//
//import java.util.*;
//
//// Implements Prim's algorithm
//public class PrimAlgorithm {
//    public static List<Edge> findMST(Graph graph) {
//        List<Edge> mst = new ArrayList<>();
//        Set<String> visited = new HashSet<>();
//        PriorityQueue<Edge> pq = new PriorityQueue<>();
//
//        // Start from first city
//        String startCity = graph.cities.get(0);
//        visited.add(startCity);
//
//        // Add all edges connected to start city
//        for (Edge e : graph.edges) {
//            if (e.source.equals(startCity) || e.destination.equals(startCity))
//                pq.add(e);
//        }
//
//        while (!pq.isEmpty() && mst.size() < graph.cities.size() - 1) {
//            Edge edge = pq.poll();
//
//            // Find unvisited city
//            String nextCity = null;
//            if (visited.contains(edge.source) && !visited.contains(edge.destination))
//                nextCity = edge.destination;
//            else if (visited.contains(edge.destination) && !visited.contains(edge.source))
//                nextCity = edge.source;
//
//            if (nextCity != null) {
//                visited.add(nextCity);
//                mst.add(edge);
//
//                // Add new edges from that city
//                for (Edge e : graph.edges) {
//                    if ((e.source.equals(nextCity) && !visited.contains(e.destination)) ||
//                            (e.destination.equals(nextCity) && !visited.contains(e.source))) {
//                        pq.add(e);
//                    }
//                }
//            }
//        }
//
//        return mst;
//    }
//}

package com.city.mst;

import java.util.*;

/**
 * Prim's algorithm that returns a Result object with fields used in Main.
 */
public class PrimAlgorithm {

    public static class Result {
        public List<Edge> mstEdges = new ArrayList<>();
        public long totalCost = 0;
        public long operations = 0;
        public double executionTimeMs = 0.0;
        public boolean isMST = true;
    }

    public static Result run(Graph g, String startNode) {
        Result res = new Result();
        long ops = 0;
        long t0 = System.nanoTime();

        if (g.nodes.isEmpty()) {
            res.isMST = true;
            res.executionTimeMs = 0.0;
            return res;
        }

        if (!g.isConnected()) {
            res.isMST = false;
            res.executionTimeMs = 0.0;
            res.operations = 0;
            return res;
        }

        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        visited.add(startNode);
        for (Edge e : g.adj.get(startNode)) { pq.add(e); ops++; }

        while (!pq.isEmpty() && visited.size() < g.nodes.size()) {
            Edge e = pq.poll(); ops++;
            if (visited.contains(e.to)) { ops++; continue; }
            res.mstEdges.add(e);
            res.totalCost += e.weight;
            visited.add(e.to);
            ops++;
            for (Edge out : g.adj.get(e.to)) {
                ops++;
                if (!visited.contains(out.to)) { pq.add(out); ops++; }
            }
        }

        long t1 = System.nanoTime();
        res.executionTimeMs = (t1 - t0) / 1_000_000.0;
        res.operations = ops;
        res.isMST = (res.mstEdges.size() == g.nodes.size() - 1);
        return res;
    }
}
