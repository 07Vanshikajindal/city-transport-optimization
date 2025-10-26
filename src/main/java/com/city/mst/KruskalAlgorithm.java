//package com.city.mst;
//
//import java.util.*;
//
// Implements Kruskal's algorithm
// public class KruskalAlgorithm {
//    public static List<Edge> findMST(Graph graph) {
//        List<Edge> mst = new ArrayList<>();
//        Collections.sort(graph.edges); // sort by cost
//        Map<String, String> parent = new HashMap<>();
//
//        for (String city : graph.cities)
//            parent.put(city, city);
//
//        for (Edge edge : graph.edges) {
//            String root1 = find(parent, edge.source);
//            String root2 = find(parent, edge.destination);
//
//            if (!root1.equals(root2)) {
//                mst.add(edge);
//                parent.put(root1, root2);
//            }
//        }
//
//        return mst;
//    }
//
//    private static String find(Map<String, String> parent, String city) {
//        if (parent.get(city).equals(city))
//            return city;
//        return find(parent, parent.get(city));
//    }
//}


package com.city.mst;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Kruskal's algorithm that returns a Result object with fields used in Main.
 */
public class KruskalAlgorithm {

    public static class Result {
        public List<Edge> mstEdges = new ArrayList<>();
        public long totalCost = 0;
        public long operations = 0;
        public double executionTimeMs = 0.0;
        public boolean isMST = true;
    }

    public static Result run(Graph g) {
        Result res = new Result();
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

        List<Edge> edges = new ArrayList<>(g.edges);
        AtomicLong compareCount = new AtomicLong(0);
        edges.sort((a,b) -> { compareCount.incrementAndGet(); return Integer.compare(a.weight, b.weight); });
        long ops = compareCount.get();

        UnionFind uf = new UnionFind(g.nodes);
        for (Edge e : edges) {
            ops++;
            if (uf.union(e.from, e.to)) {
                res.mstEdges.add(e);
                res.totalCost += e.weight;
            }
        }

        long t1 = System.nanoTime();
        res.executionTimeMs = (t1 - t0) / 1_000_000.0;
        res.operations = ops;
        res.isMST = (res.mstEdges.size() == g.nodes.size() - 1);
        return res;
    }
}
