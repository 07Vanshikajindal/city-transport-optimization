#  Assignment 3 — Optimization of a City Transportation Network
(Minimum Spanning Tree using Prim’s and Kruskal’s Algorithms)

## 🎯 Objective
The goal of this project is to optimize a city’s transportation network by finding the Minimum Spanning Tree (MST) that connects all city districts with the lowest total road construction cost.  
Both Prim’s Algorithm and Kruskal’s Algorithm were implemented, tested, and compared in terms of execution time, operation count, and MST cost.

---

## 📂 Project Structure

| File | Description |
|------|-------------|
| Edge.java | Represents a road (edge) with a start node, end node, and weight (cost). |
| Graph.java | Stores all city districts (nodes), roads (edges), and adjacency lists. |
| PrimAlgorithm.java | Implements Prim’s MST algorithm with operation count and timing. |
| KruskalAlgorithm.java | Implements Kruskal’s MST algorithm using Union-Find. |
| UnionFind.java | Helper class for cycle detection in Kruskal’s algorithm. |
| Main.java | Loads input graphs from JSON, runs both algorithms, writes results. |
| assign_3_input.json | Input graphs (small, medium, large). |
| assign_3_output.json | Auto-generated MST results for each graph. |
| AutomatedTests.java | Runs both algorithms automatically and generates comparison CSV. |
| ComparisonSummary.csv | Summarizes MST cost, execution time, and operation count. |
| Report.md | Analytical report including results and comparison. |

---

## ⚙️ How It Works

1. Input Loading:  
   The program reads a JSON file containing several city network graphs.

2. Algorithm Execution:  
   For each graph, both Prim’s and Kruskal’s algorithms are run to compute the MST.

3. Performance Metrics:
    - MST total cost
    - Execution time (in milliseconds)
    - Operation count
    - Validity of MST

4. Automated Testing:  
   Automated tests compare both algorithms and store their results in ComparisonSummary.csv.

---

## 📊 Results Summary

| GraphID | Nodes  | Edges  | PrimCost  | KruskalCost  | SameCost  | PrimTime (ms)  | KruskalTime (ms) | PrimOps | KruskalOps |
|---------|--------|--------|-----------|--------------|-----------|----------------|------------------|---------|------------|
| 1       |   4    |    5   |    6      | 6            |     yes   |       7.483    | 3.281            | 19      | 13         |
| 2       | 6      | 9      | 13        | 13           |     yes   |       0.137    | 0.105            | 37      | 27         |
| 3       | 10     | 14     | 25        | 25           |     yes   |       0.218    | 0.151            | 60      |  50        |

---

## 📈 Visualization

Three plots were created
1. Execution Time Comparison — Kruskal runs slightly faster.
2. Operation Count Comparison — Prim performs more operations as graphs grow.
3. MST Cost Comparison — Both algorithms produce the same MST cost.


## 🧠 Analysis

- Correctness:  
  Both algorithms produced identical MST costs, confirming correctness.

- Performance:  
  Kruskal’s algorithm was slightly faster for medium and large graphs due to efficient edge sorting and Union-Find optimization.

- Complexity:
    - Prim’s: O(E log V) using a priority queue
    - Kruskal’s: O(E log E) using Union-Find

- Best Use Cases:
    - Prim’s → dense graphs (many edges).
    - Kruskal’s → sparse graphs (fewer edges).



## 🧪 Testing

Automated tests verified:  
Same MST cost for both algorithms  
Correct number of MST edges (V - 1)  
Graphs remain connected  
No cycles in MST  
Proper handling of disconnected graphs  
Consistent timing and operation counts



## 🏁 Conclusion

Both Prim’s and Kruskal’s algorithms successfully optimized the transportation network.  
While both produced identical MST costs, Kruskal’s algorithm was slightly faster and more efficient overall.  
The project demonstrates the power of algorithmic optimization in real-world city planning.

---

## 🏅 Bonus

Implemented custom Graph and Edge classes in Java (OOP Design)  
Automated tests and CSV comparison  
Visualization of results  
