package Memory;

public class StackMemory {
	private int[] stack = new int[8];
	private int stackCounter = 0;

	/**
	 * Returns the value on the stack at a given Index
	 * 
	 * @param i Index to return
	 * @return Value of Stack on Index
	 */
	public int get(int i) {
		return stack[i];
	}

	/**
	 * Pushes a value (returnAddress) on TOS
	 * 
	 * @param returnAddress Value to push
	 */
	public void push(int returnAddress) {
		stack[stackCounter] = returnAddress;
		stackCounter++;

		// overflow check
		if (stackCounter > 7) {
			stackCounter = 0;
		}
	}

	/**
	 * 
	 * @return current TOS value
	 */
	public int pop() {
		stackCounter--;

		// underflow check
		if (stackCounter < 0) {
			stackCounter = 7;
		}

		return stack[stackCounter];
	}

	@Override
	public String toString() {
		String tempString = "";

		for (int i = 0; i < stack.length; i++) {
			tempString += "[" + stack[i] + "] ";
		}

		return tempString;
	}
}
