package tsp;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {
	@Override
	protected int bound(Integer currentVertex, Collection<Integer> unvisited) {

		//Calcul de l------------------------------------
		int l = -1;
		for(Integer unvisitedVertex : unvisited) {
			if (g.getCost(currentVertex, unvisitedVertex) < l || l < 0) {
				l = g.getCost(currentVertex, unvisitedVertex);
			}
		}
		//return l;
		//______________________________________________
		//Calcul de li ---------------------------------

		int li;
		int lisum = 0;
		for(Integer unvisitedVertex1 : unvisited){
			li = g.getCost(unvisitedVertex1, 0);
			for(Integer otherUnvisitedVertex : unvisited){
				int possibleNewli = g.getCost(unvisitedVertex1, otherUnvisitedVertex);
				if( possibleNewli < li && possibleNewli >= 0){
					li = possibleNewli;
				}
			}
			lisum = lisum + li;
		}
		return l+lisum;

	}

	@Override
	protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
		return new SeqIter(unvisited, currentVertex, g);
	}

}
