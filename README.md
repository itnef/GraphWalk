# GraphWalk
Graph Experiments in Scala

Read a large directed graph as a list of node pairs and query it via akka.

Run:
* `sbt "runMain graphServer"`
* when it's done loading (without terminating the server): `sbt "runMain clientWalk"`

Interesting examples:
* http://konect.uni-koblenz.de/networks/dblp_coauthor
* http://konect.uni-koblenz.de/networks/wikipedia_link_de
