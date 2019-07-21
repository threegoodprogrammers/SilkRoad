# Silk Road Graph Analyzer

### Introduction:

​	**Silk Road Graph Analyzer** is an application in which you can draw you desired graph with arbitrary IDs and numbers, specify **source** and **destination** node and do the following:

- Find the shortest path available between the source and destination node using **Dijkstra** algorithm
- Solve the TSP (Traveling Sales Person) problem using **Dynamic Programming** algorithm
- Solve the TSP (Traveling Sales Person) problem using **Ant Colony** algorithm.



### Libraries Used:

- [FontAwesomeFX](https://bitbucket.org/Jerady/fontawesomefx)
- [JFoenix](https://github.com/jfoenixadmin/JFoenix)



### Authors:

- [Aram Zahedi](https://github.com/AramZahedi) --> User interface and graph management
- [Ali Najafi](https://github.com/AliNajafi1998) --> Dynamic Programming and Ant Colony
- [Omid Rezaei](https://github.com/OmidRezaei) --> Dijkstra



### Manual:

​	The application has 4 different modes:

1. **Node Mode:** Add new edges to the graph with automatically generated numeric ID.

   ![Node mode]((https://user-images.githubusercontent.com/40715409/61590735-b0d8f080-abd2-11e9-912e-1ad87e761bf8.gif)

2. **Directional Edge Mode:** Draw directional edge between two desired nodes with user-input edge weight.

   ![Directional edge mode]((https://user-images.githubusercontent.com/40715409/61590751-d36b0980-abd2-11e9-985a-7320dfe36834.gif)

3. **Non-directional Edge Mode:** Draw non-directional edge between two desired nodes with user-input edge weight. Non-directional edge acts as if two directional edges have been drawn between the two nodes in opposite direction.

   ```diff
      - Note: This mode is disabled if the selected problem is Traveling Sales Person
   ```

   ![Non-directional edge mode]((https://user-images.githubusercontent.com/40715409/61590759-ec73ba80-abd2-11e9-8875-47b87e4a3125.gif)

4. **Select Mode:** Select graph elements and move the nodes in the canvas.

   ![Select mode]((https://user-images.githubusercontent.com/40715409/61590916-96077b80-abd4-11e9-8056-4affa14619e1.gif)