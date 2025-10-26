
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
