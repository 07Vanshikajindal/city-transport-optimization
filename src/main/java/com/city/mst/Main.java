package com.city.mst;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (FileReader reader = new FileReader("assign_3_input.json")) {
            JsonObject inputJson = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray graphsArray = inputJson.getAsJsonArray("graphs");

            if (graphsArray == null || graphsArray.size() == 0) {
                System.out.println("❌ No graphs found in assign_3_input.json");
                return;
            }

            JsonArray resultsArray = new JsonArray();

            for (JsonElement graphElem : graphsArray) {
                JsonObject graphObj = graphElem.getAsJsonObject();

                // ✅ Safe ID extraction
                int graphId = graphObj.has("id") && !graphObj.get("id").isJsonNull()
                        ? graphObj.get("id").getAsInt()
                        : -1;
                if (graphId == -1)
                    System.out.println("⚠️ Graph without ID found. Using default ID -1.");

                // ✅ Validate presence of "nodes" and "edges"
                JsonArray nodesArr = graphObj.getAsJsonArray("nodes");
                JsonArray edgesArr = graphObj.getAsJsonArray("edges");

                if (nodesArr == null || edgesArr == null) {
                    System.out.println("⚠️ Graph " + graphId + " missing nodes or edges.");
                    continue;
                }

                // ✅ Extract nodes
                List<String> nodes = new ArrayList<>();
                for (JsonElement e : nodesArr) nodes.add(e.getAsString());

                // ✅ Extract edges safely
                List<Edge> edges = new ArrayList<>();
                for (JsonElement e : edgesArr) {
                    JsonObject edgeObj = e.getAsJsonObject();
                    if (edgeObj.has("from") && edgeObj.has("to") && edgeObj.has("weight")) {
                        edges.add(new Edge(
                                edgeObj.get("from").getAsString(),
                                edgeObj.get("to").getAsString(),
                                edgeObj.get("weight").getAsInt()
                        ));
                    } else {
                        System.out.println("⚠️ Skipping incomplete edge in graph " + graphId);
                    }
                }

                System.out.println("\nProcessing Graph ID: " + graphId);
                System.out.println("Nodes: " + nodes.size() + ", Edges: " + edges.size());

                JsonObject resultObj = new JsonObject();
                resultObj.addProperty("graph_id", graphId);

                // Input stats
                JsonObject inputStats = new JsonObject();
                inputStats.addProperty("vertices", nodes.size());
                inputStats.addProperty("edges", edges.size());
                resultObj.add("input_stats", inputStats);

                // Create graph
                Graph g = new Graph(nodes, edges);

                // Run Prim’s Algorithm
                PrimAlgorithm.Result primRes = PrimAlgorithm.run(g, nodes.get(0));
                resultObj.add("prim", generateMSTStats(primRes));

                // Run Kruskal’s Algorithm
                KruskalAlgorithm.Result kruskalRes = KruskalAlgorithm.run(g);
                resultObj.add("kruskal", generateMSTStats(kruskalRes));

                resultsArray.add(resultObj);
            }

            // ✅ Write final results
            JsonObject outputJson = new JsonObject();
            outputJson.add("results", resultsArray);

            try (FileWriter writer = new FileWriter("assign_3_output.json")) {
                new GsonBuilder().setPrettyPrinting().create().toJson(outputJson, writer);
                System.out.println("\n✅ Output written to assign_3_output.json");
            }

        } catch (FileNotFoundException e) {
            System.out.println("❌ assign_3_input.json not found. Please make sure it exists.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ---------------- Output Formatter ----------------
    private static JsonObject generateMSTStats(Object resultObj) {
        JsonObject mstObj = new JsonObject();
        JsonArray mstEdges = new JsonArray();

        List<Edge> mstEdgesList;
        long totalCost, operations;
        double execTime;

        if (resultObj instanceof PrimAlgorithm.Result) {
            PrimAlgorithm.Result r = (PrimAlgorithm.Result) resultObj;
            mstEdgesList = r.mstEdges;
            totalCost = r.totalCost;
            operations = r.operations;
            execTime = r.executionTimeMs;
        } else {
            KruskalAlgorithm.Result r = (KruskalAlgorithm.Result) resultObj;
            mstEdgesList = r.mstEdges;
            totalCost = r.totalCost;
            operations = r.operations;
            execTime = r.executionTimeMs;
        }

        for (Edge e : mstEdgesList) {
            JsonObject edgeObj = new JsonObject();
            edgeObj.addProperty("from", e.from);
            edgeObj.addProperty("to", e.to);
            edgeObj.addProperty("weight", e.weight);
            mstEdges.add(edgeObj);
        }

        mstObj.add("mst_edges", mstEdges);
        mstObj.addProperty("total_cost", totalCost);
        mstObj.addProperty("operations_count", operations);
        mstObj.addProperty("execution_time_ms", execTime);

        return mstObj;
    }
}
