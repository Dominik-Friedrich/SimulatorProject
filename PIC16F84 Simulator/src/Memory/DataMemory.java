package Memory;

public class DataMemory {
	private int[][] bank = {new int[128], new int[128]};
	// currentBank is set in RP0
	
	// TODO implement bit addressable option
	// every register is bit addressable
	// switch case to get bitmask
	
	public DataMemory() {
		// TODO set values to "values on power on reset"
		// reset() which does the above in a method ^
	}
	
	public void write(int address, int value) {
		boolean isMirrored = false;
		
		// part of the special registers that are mirrored on both banks
		for(final MirroredRegister element : MirroredRegister.values()) {
			if (address == element.getAddress()) {
				isMirrored = true;
			}
		}
		
		// part of userarea which is also mirrored on both banks
		if (address > 0x0B && address < 0x30) {
			isMirrored = true;
		}
		
		
		if (isMirrored) {
			bank[0][address] = value;
			bank[1][address] = value; 
		} else {
			// TODO remove mask, add bit addressable option
			if ((MirroredRegister.STATUS.getAddress() & 0b100000) == 0) {
				// RP0 is not set -> bank0 as target
				bank[0][address] = value;
			} else {
				// RP0 is set -> bank1 as target
				bank[1][address] = value;
			}
		}
	}
}