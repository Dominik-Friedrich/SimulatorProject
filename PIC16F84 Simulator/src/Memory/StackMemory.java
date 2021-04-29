package Memory;

public class StackMemory {
	private int[] stack = new int[8];
	private int stackCounter = 0;
	
	public int get(int i) {
		return stack[i];
	}
	
	public void push (int returnAddress) {
		stack[stackCounter] = returnAddress;
		stackCounter++;
		
		// overflow check
		if (stackCounter > 7) {
			stackCounter = 0;
		}
	}
	
	public int pop () {
		stackCounter--;
		
		// underflow check
		if (stackCounter < 0) {
			stackCounter = 7;
		}
		
		return stack[stackCounter];
	}
	
	public String toString() {
		String tempString = "";
		
		for (int i = 0; i < stack.length; i++) {
			tempString += "["+stack[i]+"] ";
		}
		
		return tempString;
	}
}
