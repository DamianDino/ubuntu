import java.nio.channels.NetworkChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
	
	static class Pair{
		int onSale;
		int afterSale;
		
		public Pair() {
			this(0,0);
		}
		
		public Pair(int onSale, int afterSale) {
			// TODO Auto-generated constructor stub
			this.onSale = onSale;
			this.afterSale = afterSale;
		}
	}
	
	static class Solution{
		public int n;
		public int k; //we have to buy at least k items on sales
		
		public Pair[] objs;
		
		public Solution() {
			// TODO Auto-generated constructor stub
		}
		
		public void setValues() {
			Scanner sc = new Scanner(System.in);
			n = sc.nextInt();
			k = sc.nextInt();
			
			objs = new Pair[n];

			
			int[] onSale = new int[n];
			int[] after = new int[n];
			
			for(int i = 0; i < n; i++) {
				onSale[i] = sc.nextInt();
			}
			
			for(int i = 0; i < n; i++) {
				after[i] = sc.nextInt();
			}
			
			for(int i = 0; i < n; i++) {
				objs[i] = new Pair();
				objs[i].onSale = onSale[i];
				objs[i].afterSale = after[i];
				
			}
			
		}

		public static int getFactor(Pair p) {
			return p.onSale - p.afterSale;
		}
		
		
		public int getSolution() {
			
			int money = 0;
			
			Arrays.sort(objs, new Comparator<Pair>() {

				@Override
				public int compare(Pair o1, Pair o2) {
					// TODO Auto-generated method stub
					double diff = getFactor(o1) - getFactor(o2);
					
					if(diff == 0) {
						return 0;
					}
					
					if(diff < 0) {
						return -1;
					}
					return 1;
				}
			});

			
			int i;
			for(i = 0; i < k; i++) {
				money+= objs[i].onSale;
			}
			
			while((i<objs.length)&&(getFactor(objs[i])<	0)) {
				money+= objs[i].onSale;
				i++;
			}
			
			while(i<objs.length) {
				money+= objs[i].afterSale;
				i++;
			}
			
			return money;
		}
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
		Solution s = new Solution();
		s.setValues();
		System.out.println(s.getSolution());
		
	}
}
