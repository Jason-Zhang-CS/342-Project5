import java.util.ArrayList;

public class FindNextMove {
	String delim = "[ ]+";
	ArrayList<Node> node = new ArrayList<Node>();
	int rando ;
	int count = 0;
	FindNextMove(){
	}
	public String[] find(String[] listOfPlays,String difficulty) {
		MinMax minmax = new MinMax(listOfPlays);
		node = minmax.findMoves();
		if(difficulty.equals ("expert")) {
			System.out.println(node.size());
			System.out.println(node.size());
			System.out.println(node.size());
			System.out.println(node.size());
			System.out.println(node.size());
			System.out.println(node.size());
			System.out.println(node.size());
			for(int i = 0; i < node.size();i++) {
				Node temp = node.get(i);
				if(temp.getMinMax() == 10 ) {
					return temp.getInitStateString();
				}
				if(temp.getMinMax() == 0)
					count++;
			}
			System.out.println("count: "+ count);
			if(count == node.size()) {
				count = 0;
				for (int i = 0; i < 10; i++) 
		             rando = (int)(Math.random() * node.size());
				System.out.println("randome is: "+rando);
				return node.get(rando).getInitStateString();
			}
			for(int i = 0; i < node.size();i++) {
				Node temp = node.get(i);
				if(temp.getMinMax() == 0 ) {
					return temp.getInitStateString();
				}
			}
			for(int i = 0; i < node.size();i++) {
				Node temp = node.get(i);
				if(temp.getMinMax() == -1 ) {
					return temp.getInitStateString();
				}
			}
		}
		else if(difficulty.equals ("medium")) {
			System.out.println("medium server");
			int rand = (int) Math.random();
			if(rand % 2 == 0) {
				for(int i = 0; i < node.size();i++) {
					Node temp = node.get(i);
					if(temp.getMinMax() == 10) {
						return temp.getInitStateString();
					}
				}
				for(int i = 0; i < node.size();i++) {
					Node temp = node.get(i);
					if(temp.getMinMax() == 0 ) {
						return temp.getInitStateString();
					}
				}
				for(int i = 0; i < node.size();i++) {
					Node temp = node.get(i);
					if(temp.getMinMax() == -1 ) {
						return temp.getInitStateString();
					}
				}
			}
			else {
				for (int i = 0; i < 10; i++) 
		             rando = (int)(Math.random() * node.size());
				return node.get(rando).getInitStateString();		
			}
		}
		else if (difficulty.equals ("easy")) {
			for (int i = 0; i < 10; i++) 
	             rando = (int)(Math.random() * node.size()); 
			return node.get(rando).getInitStateString();
		}
		return listOfPlays;
		
	}
	public boolean enddraw(String[] list) {
		for(int i =0; i<list.length;i++) {
			if(list[i].equals("b"))
				return false;
		}
		return true;
	}
	public boolean checkO(String[] state) {
		if(state[0].equals("O") && state[1].equals("O") && state[2].equals("O")) //horizontal top
		{
			return true;
		}
		
		if(state[3].equals("O") && state[4].equals("O") && state[5].equals("O"))//horizontal middle
		{
			return true;
		}

		if(state[6].equals("O") && state[7].equals("O") && state[8].equals("O"))//horizontal bottom
		{
			return true;
		}

		if(state[0].equals("O") && state[3].equals("O") && state[6].equals("O"))//vert right
		{
			return true;
		}

		if(state[1].equals("O") && state[4].equals("O") && state[7].equals("O"))//vert middle
		{
			return true;
		}

		if(state[2].equals("O") && state[5].equals("O") && state[8].equals("O"))//vert left
		{
			return true;
		}

		if(state[0].equals("O") && state[4].equals("O") && state[8].equals("O"))// diag from top left
		{
			return true;
		}

		if(state[2].equals("O") && state[4].equals("O") && state[6].equals("O"))// diag from top right
		{
			return true;
		}
		return false;
	}
	public boolean checkX(String[] state) {
		if(state[0].equals("X") && state[1].equals("X") && state[2].equals("X")) //horizontal top
		{
			return true;
		}
		
		if(state[3].equals("X") && state[4].equals("X") && state[5].equals("X"))//horizontal middle
		{
			return true;
		}

		if(state[6].equals("X") && state[7].equals("X") && state[8].equals("X"))//horizontal bottom
		{
			return true;
		}

		if(state[0].equals("X") && state[3].equals("X") && state[6].equals("X"))//vert right
		{
			return true;
		}

		if(state[1].equals("X") && state[4].equals("X") && state[7].equals("X"))//vert middle
		{
			return true;
		}

		if(state[2].equals("X") && state[5].equals("X") && state[8].equals("X"))//vert left
		{
			return true;
		}

		if(state[0].equals("X") && state[4].equals("X") && state[8].equals("X"))// diag from top left
		{
			return true;
		}

		if(state[2].equals("X") && state[4].equals("X") && state[6].equals("X"))// diag from top right
		{
			return true;
		}
		return false;
	}
	private void printBestMoves()
	{
		System.out.print("\n\nThe moves list is: < ");
		
		for(int x = 0; x < node.size(); x++)
		{
			Node temp = node.get(x);
			
			if(temp.getMinMax() == 10 || temp.getMinMax() == 0)
			{
				System.out.print(temp.getMovedTo() + " ");
			}
		}
		
		System.out.print(">");
	}
//	for (int i = 0; i < 10; i++) { 
//        int rand = (int)(Math.random() * range) + min; 
}
