package tsp;
/**
 * @author AGILE team and H4124
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * In our representation for solving this given problem, we assume that we need always to visit the vertex p= 2n+1
 * before visiting the vertex q = 2n+2. This is why we have a unvisited and unvisitable list
 */
public abstract class TemplateTSP implements TSP {
	/**
	 * Ordered list of vertex to visit, best solution found yet
	 */
	private Integer[] bestSol;
	/**
	 * Graph in which we are computing the TSP
	 */
	protected Graph g;
	/**
	 * Cost of the best solution found yet
	 */
	private int bestSolCost;
	/**
	 * Time allotted for the computation task
	 */
	private int timeLimit;
	/**
	 * Time at which we start the computations
	 */
	private long startTime;
	/**
	 * Collection of vertex not yet visited, but we are able to visit them
	 */
	private Collection<Integer> unvisited;
	/**
	 * Collection of vertex not yet visited, but we are unable to visit them at this time.
	 * This collection is updated each time we add a new vertex to visited
	 */
	private Collection<Integer> unvisitable;
	/**
	 * Collection of visited vertices
	 */
	private Collection<Integer> visited;
	/**
	 * Last visited vertex before stopping the TSP, if this is the first time computing it it's default is 0;
	 */
	private Integer lastVisitedVertex = 0;
	/**
	 * Last current cost, before stopping the TSP
	 */
	private int lasCurrentCost = 0;

	/**
	 * Finds the shortest Hamiltonian path in a given graph
	 * @param timeLimit Time allotted to search a solution, if it is exceeded the class will save it's current state
	 *                  and return the shortest path computed so far
	 * @param g Graph in which we will look for the solution
	 * @return 1 if the time limit was exceeded, 0 else
	 */
	public int searchSolution(int timeLimit, Graph g) {
		if (timeLimit < 0)  return 1;
		startTime = System.currentTimeMillis();
		this.timeLimit = timeLimit;
		if(bestSol == null) {
			this.g = g;
			bestSol = new Integer[g.getNbVertices()];
			this.unvisited = new ArrayList<Integer>(g.getNbVertices() - 1);
			this.unvisitable = new ArrayList<>(g.getNbVertices() - 1);
			for (int i = 1; i < g.getNbVertices(); i += 2) unvisited.add(i);
			for (int i = 2; i < g.getNbVertices(); i += 2) unvisitable.add(i);
			this.visited = new ArrayList<Integer>(g.getNbVertices());
			visited.add(0); // The first visited vertex is 0
			bestSolCost = Integer.MAX_VALUE;
		}
		return branchAndBound(lastVisitedVertex, unvisited, visited, unvisitable,lasCurrentCost);
	}

	/**
	 * Returns the number of the i th vertex to be visited
	 * @param i the i th vertex to visit
	 * @return the
	 */
	public Integer getSolution(int i) {
		if (g != null && i >= 0 && i < g.getNbVertices())
			return bestSol[i];
		return -1;
	}

	/**
	 * Gets the cost of the solution retained
	 * @return the cost of the current solution
	 */
	public int getSolutionCost() {
		if (g != null)
			return bestSolCost;
		return -1;
	}

	/**
	 * Heuristic to bound the branches of the branch and bound solution
	 *
	 * @param currentVertex
	 * @param unvisited collection of unvisited vertices
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract int bound(Integer currentVertex, Collection<Integer> unvisited);

	/**
	 * Creates an iterator of the unvisited vertices collection
	 *
	 * @param currentVertex
	 * @param unvisited collection of unvisited vertices
	 * @param g graph in which we are computing the TSP
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g);

	/**
	 * Branch and bound algorithm for solving the TSP in <code>g</code>.
	 *
	 * @param currentVertex the last visited vertex
	 * @param unvisited     the set of vertex that have not yet been visited
	 * @param visited       the sequence of vertices that have been already visited (including currentVertex)
	 * @param unvisitable   the set of vertex that can't be visited at this time, it will evolve each time we visit an unvisitable
	 * @param currentCost   the cost of the path corresponding to <code>visited</code>
	 */
	private int branchAndBound(int currentVertex, Collection<Integer> unvisited,
								Collection<Integer> visited, Collection<Integer> unvisitable, int currentCost) {
		int error = 0;
		if (System.currentTimeMillis() - startTime > timeLimit && bestSol[g.getNbVertices()-1] != null){
			lastVisitedVertex = currentVertex;
			lasCurrentCost = currentCost;
			return 1;
		}
		if (unvisited.size() == 0) {
			if (g.isArc(currentVertex, 0)) {
				if (currentCost + g.getCost(currentVertex, 0) < bestSolCost) {
					visited.toArray(bestSol);
					bestSolCost = currentCost + g.getCost(currentVertex, 0);
				}
			}
		} else if (currentCost + bound(currentVertex, unvisited) < bestSolCost) {
			Iterator<Integer> it = iterator(currentVertex, unvisited, g);
			while (it.hasNext()) {
				Integer nextVertex = it.next();
				addToVisited(unvisited, visited, unvisitable, nextVertex);
				error = branchAndBound(nextVertex, unvisited, visited, unvisitable,
						currentCost + g.getCost(currentVertex, nextVertex));
				removeFromVisited(unvisited, visited, unvisitable, nextVertex);
			}
		}
		return error;
	}

	/**
	 * Adds to the visited collection a given vertex, updating the unvisited and unvisitable collections
	 * @param unvisited     the set of vertex that have not yet been visited
	 * @param visited       the sequence of vertices that have been already visited (including currentVertex)
	 * @param unvisitable   the set of vertex that can't be visited at this time, it will evolve each time we visit an unvisitable
 	 * @param nextVertex    Vertex to be added to visited, removed from unvisited and evaluated to update the unvisitable and unvisited collections
	 */
	private void addToVisited(Collection<Integer> unvisited, Collection<Integer> visited, Collection<Integer> unvisitable, int nextVertex) {
		visited.add(nextVertex);
		unvisited.remove(nextVertex);
		if(nextVertex%2 == 1){
			unvisited.add((nextVertex + 1));
			unvisitable.remove((nextVertex + 1));
		}
		return;
	}
	/**
	 * Removes from the visited collection a given vertex, updating the unvisited and unvisitable collections
	 * @param unvisited     the set of vertex that have not yet been visited
	 * @param visited       the sequence of vertices that have been already visited (including currentVertex)
	 * @param unvisitable   the set of vertex that can't be visited at this time, it will evolve each time we visit an unvisitable
	 * @param nextVertex    Vertex to be removed from visited, added to unvisited and evaluated to update the unvisitable and unvisited collections
	 */
	private void removeFromVisited(Collection<Integer> unvisited, Collection<Integer> visited, Collection<Integer> unvisitable, int nextVertex) {
		visited.remove(nextVertex);
		unvisited.add(nextVertex);
		if(nextVertex%2 == 1) {
			unvisited.remove((nextVertex + 1));
			unvisitable.add((nextVertex + 1));
		}
		return;
	}
}
