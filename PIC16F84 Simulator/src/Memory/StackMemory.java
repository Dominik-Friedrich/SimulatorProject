package Memory;

public class StackMemory {
	private int[] stack = new int[8];
	private int stackCounter = 0;
	
	public StackMemory() {
		// TODO idky
	}
	
	public void callHappened (int returnAddress) {
		// TODO maybe mask address
		stack[stackCounter] = returnAddress;
		stackCounter++;
		
		// overflow check
		if (stackCounter > 7) {
			stackCounter = 0;
		}
	}
	
	public int returnHappened () {
		stackCounter--;
		
		// underflow check
		if (stackCounter < 0) {
			stackCounter = 7;
		}
		
		// TODO maybe mask address
		return stack[stackCounter];
	}
}
