import javax.swing.text.Highlighter.Highlight;

//todo: M114020033 &李庭瑋
//todo: write code in the chop(int p, int q) to calculate what happens when a tree branch is chopped. 
//todo: modify union(int p, int q) to maintain add nodes to the tree. 
//DO NOT EDIT other functions NOR add global variables.

public class HW1 {
 
	// ChopTrees is modified from QuickUnionUF https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/QuickUnionUF.java.html
	// QuickUnionUF JavaDoc https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/QuickUnionUF.html
	public static class ChopTrees {
		private int[] parent;   // parent[i] = parent of i
		private int count;      // number of components
		private int N;
		private boolean collapsed;

		/**
		* Initializes an empty union-find data structure with
		* {@code n} elements {@code 0} through {@code n-1}.
		* Initially, each element is in its own set.
		*
		* @param  n the number of elements
		* @throws IllegalArgumentException if {@code n < 0}
		*/
		public ChopTrees(int n) {
			count = n;
			N = n;
			collapsed = false;
			parent = new int[n];
			for (int i = 0; i < n; i++) {
				parent[i] = 0;
			}
		}

		/**
		* Returns the number of sets.
		*
		* @return the number of sets (between {@code 1} and {@code n})
		*/
		public int count() {
			return count;
		}

		/**
		* Returns the canonical element of the set containing element {@code p}.
		*
		* @param  p an element
		* @return the canonical element of the set containing {@code p}
		* @throws IllegalArgumentException unless {@code 0 <= p < n}
		*/
		public int find(int p) {
			validate(p);
			while (parent[p] != 0 && parent[p] != -1)
				p = parent[p];
			return p;
		}

		/**
		* Returns true if the two elements are in the same set.
		* 
		* @param  p one element
		* @param  q the other element
		* @return {@code true} if {@code p} and {@code q} are in the same set;
		*         {@code false} otherwise
		* @throws IllegalArgumentException unless
		*         both {@code 0 <= p < n} and {@code 0 <= q < n}
		* @deprecated Replace with two calls to {@link #find(int)}.
		*/
		@Deprecated
		public boolean connected(int p, int q) {
			return find(p) == find(q);
		}

		// validate that p is a valid index
		private void validate(int p) {
			int n = parent.length;
			if (p < 0 || p >= n) {
				System.out.println("index " + p + " is not between 0 and " + (n-1));
			}
		}

		/**
		* Adds a branch to the tree, see assignment for details
		*
		* @param  p one element
		* @param  q the other element
		* @throws IllegalArgumentException unless
		*         both {@code 0 <= p < n} and {@code 0 <= q < n}
		*/
		public boolean union(int p, int q) {
			// modify code in this method
			if(collapsed == true) return false;

			if(parent[q] == -1) return false;
			int parentP = parent[p];
			int parentQ = parent[q];
			if (parentP == q || parentQ == p)  return true;
			parent[p] = q;
			count--;
			return true;
		}

		// For chopping tree branches, see assignment for details
		public int chop(int p, int q){
			// write your code here
			int[] tree_hights = new int[parent.length];
			int[] temp = new int[parent.length];
			int before_max = 0;
			int after_max = 0;
			int result = 0;



			validate(p);
			validate(q);

			// 判斷房子是否已經倒塌
			if(collapsed == true) return -1;

			// 將每個node的高度存進tree_hights陣列當中
			for(int i = 0; i<parent.length; i++){
				int count = 1;
				int x = i;
				while(parent[x] != 0 && parent[x] != -1){
					x = parent[x];
					count++;
				}
				tree_hights[i] = count;

			}
			
			// 從tree_hights中取得chop前最大高度
			for(int i = 0; i<parent.length; i++){
				if(before_max < tree_hights[i]) before_max = tree_hights[i];
			}



			// 從p和q中選出高度較高的作為剪枝的對象，並回傳剪下來的節點數量
			if(connected(p, q)){
				if(tree_hights[p]>tree_hights[q]){
					int child_amount =0;
					parent[p] = -1;
					int x = 1;
					for(int i = 0; i<parent.length; i++)
					{
						if(find(i) == p) 
						{
							child_amount++;
							temp[x] = i;
							x++;
							// parent[i] = -1;
						}
					}
					result = child_amount;
					// flag = 0;
				}else{
					int child_amount =0;
					parent[q] = -1;
					int x = 1;

					for(int i = 0; i<parent.length; i++)
					{
						if(find(i) == q)
						{
							child_amount++;
							temp[x] = i;
							x++;
							// parent[i] = -1;
						}
					}
					result = child_amount;
					// flag = 1;
				} 

			}else  return 0;


			// 再次將每個node的高度存進tree_hights陣列當中
			for(int i = 0; i<parent.length; i++){
				int count = 1;
				int x = i;
				while(parent[x] != 0 && parent[x] != -1){
					x = parent[x];
					count++;
				}
				tree_hights[i] = count;

			}
			
			// 從tree_hights中取得chop後最大高度
			for(int i = 0; i<parent.length; i++){
				if(after_max < tree_hights[i]) after_max = tree_hights[i];
			}

			// 把剩下的葉子刪掉
			for(int i = 1; i<parent.length; i++)
			{
				if(temp[i]!= 0){
					// System.out.print(" "+temp[i]);
					int x = temp[i];
					parent[x] = -1;
				}
			}

			

			// 判斷是否因為chop而造成最大高度降低，若降低則代表房子倒塌
			if(after_max < before_max){
				collapsed = true;
				return -1;
			} 


			return result;
		}

		// ---Functions------

		// public int find_hight(int p){
		// 	validate(p);
		// 	int count = 1;
		// 	while (parent[p] != 0 && parent[p] != -1){
		// 		p = parent[p];
		// 		count++;
		// 	}
		// 	return count;
		// }
		// // 計算node p有幾個子節點
		// public int count_childs(int p){
		// 	count =0;
		// 	for(int i = 0; i<parent.length; i++){
		// 		if(parent[i] == p) count++;
		// 	}
		// 	return count;
		// }
		
		// // 把切下來的樹枝剩下所有葉子剪掉（設其parent為 -1）
		// public void prune_childs(int p){
		// 	validate(p);
		// 	for(int i = 0; i<parent.length; i++){
		// 		// if(parent[i] == p) parent[i] = -1;
		// 		while (parent[p] != 0 && parent[p] != -1)
		// 			p = parent[p];
		// 	}
		// }

		// // 印出樹的內容
		// public void tree(){
		// 	System.out.println("parents:");
		// 	for(int i = 0; i<parent.length; i++){
		// 		System.out.print(parent[i]+" ");
		// 	}
			
		// 	System.out.println("");
			
		// 	System.out.println("roots:");
		// 	for(int i = 0; i<parent.length; i++){
		// 		System.out.print(find(i)+" ");
		// 	}

		// 	System.out.println("");
		// 	System.out.println("hight of 7 is: " + find_hight(7));
		// 	System.out.println("child node amount of 7 is: "+count_childs(7));
		// 	System.out.println("");


			
		// }
		
	}

	public static void main(String[] args) {
		ChopTrees ct = new ChopTrees(25);
		ct.union(1, 2);
		ct.union(3, 4);
		ct.union(1, 3);
		ct.union(7, 2);
		ct.union(7, 3);
		ct.union(1, 6);
		ct.union(10, 11);
		ct.union(15, 12);
		ct.union(2, 17);
		ct.union(3, 15);
		ct.union(4, 11);
		ct.union(1, 3);
		ct.union(6, 8);
		ct.union(8, 19);
		ct.union(11, 17);
		ct.union(12, 15);
		ct.union(11, 18);
		ct.union(5, 14);

		// ct.tree();
		

		System.out.println("After Chop 8, 6 => " + ct.chop(8, 6)); // expected output: After Chop 8, 6 => 1
		System.out.println("After Chop 11, 18 => " + ct.chop(11, 18));// expected output: After Chop 11, 18 => 3
		System.out.println("After Chop 1, 3 => " + ct.chop(1, 3));//  expected output: After Chop 1, 3 => 1

		System.out.println("Union of 20, 9 => " + ct.union(20,9)); // expected output: Union of 20, 9 => true

		System.out.println("After Chop 15, 3 => " + ct.chop(15, 3));//  expected output: After Chop 15, 3 => -1
		System.out.println("After Chop 14, 9 => " + ct.chop(14, 9));//  expected output: After Chop 14, 9 => -1

		System.out.println("Union of 20, 9 => " + ct.union(20,9)); // expected output: Union of 20, 9 => false
		// System.out.println("After Chop 7, 3 => " + ct.chop(7, 3));//  expected output: After Chop 7, 3 => -1
		// System.out.println("After Chop 15, 12 => " + ct.chop(15, 12));//  expected output: After Chop 15, 12 => -1
		// System.out.println("After Chop 20, 9 => " + ct.chop(20, 9));//  expected output: After Chop 20, 9 => 1



		// System.out.println("");
		// ct.tree();

		
	}
}