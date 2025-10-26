package com.city.mst;

import com.google.gson.*;
import java.io.*;
import java.util.*;

/**
 * Automated testing suite for comparing Prim's and Kruskal's MST algorithms.
 * Generates a CSV summary file for performance comparison.
 */
public class AutomatedTests {

    public static void main(String[] args) {
        String inputFile = "assign_3_input.json";
        String outputCsv = "ComparisonSummary.csv";

        List<ResultRecord> results = new ArrayList<>();

        try (FileReader reader = new FileReader(inputFile)) {
            JsonObject inputJson = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray graphsArray = inputJson.getAsJsonArray("graphs");

            if (graphsArray == null || graphsArray.size() == 0) {
                System.out.println("❌ No graphs found for testing.");
                return;
            }

            for (JsonElement graphElem : graphsArray) {
                JsonObject graphObj = graphElem.getAsJsonObject();
                int id = graphObj.has("id") ? graphObj.get("id").getAsInt() : -1;
                JsonArray nodesArr = graphObj.getAsJsonArray("nodes");
                JsonArray edgesArr = graphObj.getAsJsonArray("edges");

                List<String> nodes = new ArrayList<>();
                for (JsonElement n : nodesArr) nodes.add(n.getAsString());

                List<Edge> edges = new ArrayList<>();
                for (JsonElement e : edgesArr) {
                    JsonObject eo = e.getAsJsonObject();
                    if (eo.has("from") && eo.has("to") && eo.has("weight")) {
                        edges.add(new Edge(eo.get("from").getAsString(),
                                eo.get("to").getAsString(),
                                eo.get("weight").getAsInt()));
                    }
                }

                Graph g = new Graph( nodes, edges);

                // --- Run Prim ---
                PrimAlgorithm.Result primRes = PrimAlgorithm.run(g, g.nodes.get(0));


                // --- Run Kruskal ---
                KruskalAlgorithm.Result kruskalRes = KruskalAlgorithm.run(g);

                // --- Check correctness ---
                boolean sameCost = primRes.totalCost == kruskalRes.totalCost;
                boolean validMST = primRes.isMST && kruskalRes.isMST;

                results.add(new ResultRecord(
                        id,
                        nodes.size(),
                        edges.size(),
                        primRes.totalCost,
                        kruskalRes.totalCost,
                        sameCost,
                        validMST,
                        primRes.executionTimeMs,
                        kruskalRes.executionTimeMs,
                        primRes.operations,
                        kruskalRes.operations
                ));

                System.out.printf("✅ Graph %d tested | Prim: %.2f ms | Kruskal: %.2f ms | Same Cost: %s%n",
                        id, primRes.executionTimeMs, kruskalRes.executionTimeMs, sameCost ? "✅" : "❌");
            }

            writeCsv(results, outputCsv);
            System.out.println("\n📊 Comparison summary saved to " + outputCsv);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Data structure for test results
    static class ResultRecord {
        int graphId, nodes, edges;
        long primCost, kruskalCost;
        boolean sameCost, validMST;
        double primTime, kruskalTime;
        long primOps, kruskalOps;

        public ResultRecord(int graphId, int nodes, int edges,
                            long primCost, long kruskalCost, boolean sameCost, boolean validMST,
                            double primTime, double kruskalTime, long primOps, long kruskalOps) {
            this.graphId = graphId;
            this.nodes = nodes;
            this.edges = edges;
            this.primCost = primCost;
            this.kruskalCost = kruskalCost;
            this.sameCost = sameCost;
            this.validMST = validMST;
            this.primTime = primTime;
            this.kruskalTime = kruskalTime;
            this.primOps = primOps;
            this.kruskalOps = kruskalOps;
        }
    }

    // CSV writer
    private static void writeCsv(List<ResultRecord> results, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("GraphID,Nodes,Edges,PrimCost,KruskalCost,SameCost,ValidMST,PrimTime(ms),KruskalTime(ms),PrimOps,KruskalOps");
            for (ResultRecord r : results) {
                pw.printf(Locale.US,
                        "%d,%d,%d,%d,%d,%s,%s,%.3f,%.3f,%d,%d%n",
                        r.graphId, r.nodes, r.edges,
                        r.primCost, r.kruskalCost,
                        r.sameCost, r.validMST,
                        r.primTime, r.kruskalTime,
                        r.primOps, r.kruskalOps);
            }
        } catch (IOException e) {
            System.err.println("❌ Error writing CSV: " + e.getMessage());
        }
    }
}
