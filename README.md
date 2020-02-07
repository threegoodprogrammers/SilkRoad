# Silk Road Graph Analyzer

### Introduction:

​	**Silk Road Graph Analyzer** is an application in which you can draw you desired graph with arbitrary IDs and numbers, specify **source** and **destination** node and do the following:

- Find the shortest path available between the source and destination node using **Dijkstra** algorithm
- Solve the TSP (Travelling Salesman Problem) problem using **Dynamic Programming** algorithm
- Solve the TSP (Travelling Salesman Problem) problem using **Ant Colony** algorithm.



### Libraries Used:

- [FontAwesomeFX](https://bitbucket.org/Jerady/fontawesomefx)
- [JFoenix](https://github.com/jfoenixadmin/JFoenix)



### Authors:

- [Aram Zahedi](https://github.com/AramZahedi) --> User interface and graph management
- [Ali Najafi](https://github.com/AliNajafi1998) --> Dynamic Programming and Ant Colony
- [Omid Rezaei](https://github.com/OmidRezaei) --> Dijkstra



### Manual:

#### Modes:

The application has 4 different modes:

1. **Node Mode:** Add new edges to the graph with automatically generated numeric ID.

   ![Node mode](https://user-images.githubusercontent.com/40715409/61590735-b0d8f080-abd2-11e9-912e-1ad87e761bf8.gif)

2. **Directional Edge Mode:** Draw directional edge between two desired nodes with user-input edge weight.

   ![Directional edge mode](https://user-images.githubusercontent.com/40715409/61590751-d36b0980-abd2-11e9-985a-7320dfe36834.gif)

3. **Non-directional Edge Mode:** Draw non-directional edge between two desired nodes with user-input edge weight. Non-directional edge acts as if two directional edges have been drawn between the two nodes in opposite directions.

   - **<u>Note:</u>** This mode is disabled if the selected problem is Traveling Sales Person

   ![Non-directional edge mode](https://user-images.githubusercontent.com/40715409/61590759-ec73ba80-abd2-11e9-8875-47b87e4a3125.gif)

4. **Select Mode:** Select graph elements and move the nodes in the canvas. You must left-click the element for selection or drag the mouse to move it.

   * **<u>Note:</u>** You are only able to move graph **nodes**.

   ![Select mode](https://user-images.githubusercontent.com/40715409/61590916-96077b80-abd4-11e9-8056-4affa14619e1.gif)



#### Selection:

You can select **single** or **multiple** elements in the canvas. If you **hold the shift key**, you can add other elements to the selection with **mouse click** retaining the previous selections.

You can delete the selected items using the **Delete** button in the left menu or pressing the **Delete** key on the keyboard.

![Multiple selection and delete](https://user-images.githubusercontent.com/40715409/61591120-bedd4000-abd7-11e9-9d81-9356dd9d10d7.gif)

#### Deletion:

You can delete selected items like we said above, or delete everything by clicking on **Delete** button or pressing **delete key** on the keyboard when **<u>nothing is selected</u>**.

![Delete all](https://user-images.githubusercontent.com/40715409/61591606-d5869580-abdd-11e9-8652-84930977f169.gif)

#### Node Tools:

- **Edit node ID:** You can select a node and press **Change ID** button in the tools menu on the left side, then enter the desired ID when prompted.

  ![Edit node ID](https://user-images.githubusercontent.com/40715409/61591643-49c13900-abde-11e9-86b1-b8ab2a42fe5a.gif)



- **Set source node:** You can set a node as **source** by selecting it and pressing **Set as Source** button in the tools menu on the left side. This will unset the previously selected source button if there is any.

  ![Set source node](https://user-images.githubusercontent.com/40715409/61591789-ef28dc80-abdf-11e9-9313-82d3915bd0dc.gif)

- **Set destination node:** You can set a node as **destination** by selecting it and pressing **Set as Target** button in the tools menu on the left side. This will **unset the previously selected** destination node if there is any.

  ![Set target node](https://user-images.githubusercontent.com/40715409/61591805-2ac3a680-abe0-11e9-96ae-be41e3694cad.gif)

#### Edge tools:

- **Edit edge weight:** You can change the weight for a **single** or **multiple** selected edges by pressing the **Change Edge Weight** button in the tools menu on the left side, then entering the desired value when prompted.

  ![Change edge weight](https://user-images.githubusercontent.com/40715409/61591855-c35a2680-abe0-11e9-9e9a-157c04e859dc.gif)

- **Change edge direction:** You can change the direction of a single selected edge by pressing the **Change Edge Direction** button in the tools menu on the left side. This only applies to **directional edges**, when **no edge** exists between the two nodes in the **opposite direction**.

  ![Change edge direction](https://user-images.githubusercontent.com/40715409/61591899-3e234180-abe1-11e9-9a28-1394cc79495c.gif)



#### Problems:

**<u>Shortest Path</u>:** In this problem we need to find the shortest path between two specified nodes in a graph. To solve Shortest Path problem using this algorithm, do as follows:

- Add nodes and draw edges between them as you desire

- Specify a **source** and **destination** node in the canvas 

- Press the **Shortest Path** problem button in problems menu on the top left side

  ![Shortest path problem](https://user-images.githubusercontent.com/40715409/61615254-c99ce100-ac7a-11e9-8fa0-b41f41978236.gif)



**<u>Traveling Sales Person</u>:** In this problem we need to find the shortest cycle in the graph which starts from and ends in the specified source node, traversing every node in the graph without visiting any node or edge more than once. We have implemented two algorithms to solve this problem:

- **Dynamic Programming algorithm:**

  - Add nodes and draw edges between them as you desire until the graph is complete

  - Specify a **source** node in the canvas 

  - Press the **Traveling Sales Person** problem button in problems menu on the top left side, so a mini-menu pops up.

  - Press the Dynamic Programming button in the pop-up menu.

    (**Note:** Graph must be complete for this problem!)

    ![Dynamic programming](https://user-images.githubusercontent.com/40715409/61616336-63658d80-ac7d-11e9-8418-3cf531fa45e5.gif)

  

- **Ant Colony algorithm:**

  - Add nodes and draw edges between them as you desire until the graph is complete

  - Specify a **source** node in the canvas 

  - Press the **Traveling Sales Person** problem button in problems menu on the top left side, so a mini-menu pops up.

  - Press the Ant Colony button in the pop-up menu so another menu appears in the right side containing ant colony settings.

  - Set the desired settings in ant colony settings menu

    (**Note:** Graph must be complete for this problem!)

     ![Ant colony](https://user-images.githubusercontent.com/40715409/61616478-c1927080-ac7d-11e9-891f-a95f1d9a6011.gif)

    

    **Ant Colony settings:**

    ![Ant colony](https://user-images.githubusercontent.com/40715409/61616534-ee468800-ac7d-11e9-85ba-8e624f25b371.gif)



#### Batch adding edges:

Drawing every possible edge in order to make the graph complete can be exhausting especially when our graph grows larger. So we have implemented a feature named **Batch Add Edges** that draws edges from a specified node to every other possible nodes.

With this feature you must just choose the desired node and enter a **Minimum** and **Maximum** value for weight, so the application chooses a random weight for each new edge between the two values you enter.

To use this feature:

- Go into **Directional Edge** or **Non-directional Edge** mode in the mode menu on the bottom left side
- Hold **Shift** key on the keyboard and press any desired node on the canvas, so a a prompt dialog appears
- Enter minimum and maximum value for the weight value in the dialog. Enter the **same value** in both text fields if you need a **specific weight** set for every edge, then press OK.

![Batch add edges](https://user-images.githubusercontent.com/40715409/61617484-04edde80-ac80-11e9-8049-e952483f10d8.gif)



#### Pan and Zoom:

You can zoom-in, zoom-out or move the canvas if you need to.

- **Move canvas:** Just hold the **Control** key on the keyboard, then press and hold left mouse click and drag   where you want.
- **Zoom:** Zoom in the canvas using **mouse-wheel up** and zoom out using **mouse-wheel down**.

![Pan and Zoom](https://user-images.githubusercontent.com/40715409/61618074-4c289f00-ac81-11e9-918b-c2a20f7e21d2.gif)
