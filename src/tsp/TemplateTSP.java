package tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public abstract class TemplateTSP implements TSP {
	private Integer[] bestSol;
	protected Graph g;
	private int bestSolCost;
	private int timeLimit;
	private long startTime;
	private Collection<Integer> unvisited;
	private Collection<Integer> unvisitable;
	private Collection<Integer> visited;

	public int searchSolution(int timeLimit, Graph g) {
		if (timeLimit <= 0)  return 1;
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
		return branchAndBound(0, unvisited, visited, unvisitable,0);
	}

	public Integer getSolution(int i) {
		if (g != null && i >= 0 && i < g.getNbVertices())
			return bestSol[i];
		return -1;
	}

	public int getSolutionCost() {
		if (g != null)
			return bestSolCost;
		return -1;
	}

	/**
	 * Method that must be defined in TemplateTSP subclasses
	 *
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract int bound(Integer currentVertex, Collection<Integer> unvisited);

	/**
	 * Method that must be defined in TemplateTSP subclasses
	 *
	 * @param currentVertex
	 * @param unvisited
	 * @param g
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g);

	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 *
	 * @param currentVertex the last visited vertex
	 * @param unvisited     the set of vertex that have not yet been visited
	 * @param visited       the sequence of vertices that have been already visited (including currentVertex)
	 * @param unvisitable   the ser of vertex that can't be visited at this time, it will evolve each time we visit an unvisitable
	 * @param currentCost   the cost of the path corresponding to <code>visited</code>
	 */
	private int branchAndBound(int currentVertex, Collection<Integer> unvisited,
								Collection<Integer> visited, Collection<Integer> unvisitable, int currentCost) {
		int error = 0;
		if (System.currentTimeMillis() - startTime > timeLimit && bestSol[g.getNbVertices()-1] != null) return 1;
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

	private void addToVisited(Collection<Integer> unvisited, Collection<Integer> visited, Collection<Integer> unvisitable, int nextVertex) {
		visited.add(nextVertex);
		unvisited.remove(nextVertex);
		if(nextVertex%2 == 1){
			unvisited.add((nextVertex + 1));
			unvisitable.remove((nextVertex + 1));
		}
		return;
	}

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
