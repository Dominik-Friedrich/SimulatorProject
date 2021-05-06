package Memory;

import java.util.ArrayList;

public class ProgrammMemory {
	private int[] programmMemory = new int[1024];

	public ProgrammMemory() {
	}

	/**
	 * Returns the value(OPCode) of the specified address
	 * 
	 * @param address to read
	 * @return value of address
	 */
	public int read(int address) {
		return (programmMemory[address] & 0b11111111111111);
	}

	/**
	 * Writes the value(OPCode) to the specified address
	 * 
	 * @param address to write to
	 * @param value   to write
	 */
	public void write(int address, int value) {
		value = value & 0b11111111111111;

		programmMemory[address] = value;
	}

	/**
	 * Discards the old Program for a new one
	 * 
	 * @param programmMemory new Program
	 */
	public void newProgramm(int[] programmMemory) {
		this.programmMemory = programmMemory;
	}

	/**
	 * Initializes a new Program with a list of instructions (OPCode)
	 * 
	 * @param instructions list that contains the OPCode for each instruction
	 */
	public void newProgramm(ArrayList<Integer> instructions) {
		for (int i = 0; i < instructions.size(); i++) {
			programmMemory[i] = instructions.get(i);
		}
	}
}
