# COE865-Project
Design and Implementation of a Multicast Overlay Network

A multicast overlay network is a connected graph of nodes and links. Nodes are hosts that are connected to the network,
and links are transport-level connections (TCP or UDP) between two nodes.

There are 3 different types of nodes:
1. Source
2. Forwarder
3. Receiver

Source nodes are multicast source nodes which will broadcast packets which are destined for the receivers.
Forwarder nodes are nodes which will forward the packet along the path toward the destination (a receiver node).
Receiver nodes are nodes which are waiting to receive the broadcasted multicast packets from the source node.

Links are connections between two nodes and are 1-directional. Every link has a cost which is specified in the configuration file.

The program will determine the shortest path to the receivers from the source node using Djikstra's shortest path algorithm. The program 
will route the packets through the determined routes.

The program requires a configuration file (config.txt) which describes the node configuration and the link configuration. An example
config is listed below:

1 1 4444
2 0 4445
3 0 4446
4 2 4447
5 2 4448
6 0 4449
7 2 5000
END
1 2 5
1 3 2
2 3 3
2 4 7
2 5 1
3 5 4
3 6 2
6 7 5

The first 7 lines describe each node with an ID, type of node, and port number. The program will stop adding nodes once the line
END is detected.
FORMAT: ID #, TYPE, PORT #
TYPE Options:
0 = Forwarder
1 = Source
2 = Receiver

The next 8 lines describe the links between the nodes. The first 2 values are the starting and ending nodes ID's, and the 3rd value
is the cost of the link.
FORMAT: START NODE ID #, END NODE ID #, COST 
