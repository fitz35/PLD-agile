package tsp;

public class RunTSP {
	public static void main(String[] args) {
		for (int nbVertices = 3; nbVertices <= 21; nbVertices += 2){
			TSP tsp = new TSP1();
			System.out.println("Graphs with "+nbVertices+" vertices:");
			Graph g = new CompleteGraph(nbVertices);
			long startTime = System.currentTimeMillis();
			tsp.searchSolution(200000000, g);
			System.out.print("Solution of cost "+tsp.getSolutionCost()+" found in "
					+(System.currentTimeMillis() - startTime)+"ms : ");
			for (int i=0; i<nbVertices; i++)
				System.out.print(tsp.getSolution(i)+" ");
			System.out.println("0");
		}
	}
}
