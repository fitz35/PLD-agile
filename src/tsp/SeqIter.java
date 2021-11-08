package tsp;

import java.util.Collection;
import java.util.Iterator;

public class SeqIter implements Iterator<Integer> {
	private Integer[] candidates;
	private int nbCandidates;

	/**
	 * Create an iterator to traverse the set of vertices in <code>unvisited</code> 
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param unvisited
	 * @param currentVertex
	 * @param g
	 */
	public SeqIter(Collection<Integer> unvisited, int currentVertex, Graph g){
		this.candidates = new Integer[unvisited.size()];
		for (Integer s : unvisited){
			if (g.isArc(currentVertex, s))
				candidates[nbCandidates++] = s;
		}
		sortHeuristic(currentVertex, g);
	}

	private void sortHeuristic(int currentVertex, Graph g){
		boolean success = false;
		while(!success){
			success = true;
			for(int i = 1; i<nbCandidates; i++){
				if( g.getCost(currentVertex, candidates[i-1]) < g.getCost(currentVertex, candidates[i])){
					swap(i-1, i);
					//printArray(nonVus, nbNonVus);
					success = false;
				}
			}
		}
	}

	private void swap(int i, int j){
		int temp = candidates[i];
		candidates[i] = candidates[j];
		candidates[j] = temp;
	}
	
	@Override
	public boolean hasNext() {
		return nbCandidates > 0;
	}

	@Override
	public Integer next() {
		nbCandidates--;
		return candidates[nbCandidates];
	}

	@Override
	public void remove() {}

}
