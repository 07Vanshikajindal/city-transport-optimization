package com.city.mst;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Union-Find (Disjoint Set Union) data structure with path compression and union by rank.
 * Used by Kruskal’s algorithm to avoid cycles when building the MST.
 */
public class UnionFind {
    private final Map<String, String> parent = new HashMap<>();
    private final Map<String, Integer> rank = new HashMap<>();

    public UnionFind(List<String> nodes) {
        // Initialize each node as its own parent
        for (String n : nodes) {
            parent.put(n, n);
            rank.put(n, 0);
        }
    }

    /**
     * Find the root (representative) of a node with path compression.
     */
    public String find(String x) {
        String p = parent.get(x);
        if (!p.equals(x)) {
            p = find(p);
            parent.put(x, p); // Path compression
        }
        return p;
    }

    /**
     * Union two sets. Returns true if union was successful (different sets), false if already in same set.
     */
    public boolean union(String a, String b) {
        String rootA = find(a);
        String rootB = find(b);

        // If they are already connected, no need to union
        if (rootA.equals(rootB)) return false;

        int rankA = rank.get(rootA);
        int rankB = rank.get(rootB);

        // Union by rank
        if (rankA < rankB) {
            parent.put(rootA, rootB);
        } else if (rankA > rankB) {
            parent.put(rootB, rootA);
        } else {
            parent.put(rootB, rootA);
            rank.put(rootA, rankA + 1);
        }

        return true;
    }
}
