package Memory;

public class DataMemory {
	private int[][] bank = { new int[128], new int[128] };
	private int wRegister = 0;

	public DataMemory() {
		// TODO set values to "values on power on reset" and normal reset
		// reset() which does the above in a method
	}

	// TODO indirect adressing
	// TODO if target address == 0 -> use value in FSR register as target adress
	public void powerOnReset() {
		// Bank 0
		writeByte(SpecialRegister.PCL.getAddress(), 0);
		writeByte(SpecialRegister.STATUS.getAddress(), 24);
		writeByte(SpecialRegister.PCLATH0.getAddress(), 0);
		writeByte(SpecialRegister.INTCON.getAddress(), 0);

		// Bank 1
		setBit(SpecialRegister.RP0.getAddress(), SpecialRegister.RP0.getBit());

		writeByte(SpecialRegister.OPTION.getAddress(), 0xFF);
		writeByte(SpecialRegister.TRISA.getAddress(), 0xFF);
		writeByte(SpecialRegister.TRISB.getAddress(), 0xFF);
		writeByte(SpecialRegister.EECON1.getAddress(), 0);

		clearBit(SpecialRegister.RP0.getAddress(), SpecialRegister.RP0.getBit());
	}

	// TODO see how q works
	public void reset() {
		// Bank 0
		writeByte(SpecialRegister.PCL.getAddress(), 0);
		// writeByte(SpecialRegister.STATUS.getAddress(), 24);
		writeByte(SpecialRegister.PCLATH0.getAddress(), 0);
		writeByte(SpecialRegister.INTCON.getAddress(), readByte(SpecialRegister.INTCON.getAddress()) & 1);

		// Bank 1
		setBit(SpecialRegister.RP0.getAddress(), SpecialRegister.RP0.getBit());

		writeByte(SpecialRegister.OPTION.getAddress(), 0xFF);
		writeByte(SpecialRegister.TRISA.getAddress(), 0xFF);
		writeByte(SpecialRegister.TRISB.getAddress(), 0xFF);
		// writeByte(SpecialRegister.EECON1.getAddress(), 0);

		clearBit(SpecialRegister.RP0.getAddress(), SpecialRegister.RP0.getBit());
	}

	public void writeByte(int address, int value) {
		value = value & 0b11111111;

		if (isMirrored(address)) {
			bank[0][address] = value;
			bank[1][address] = value;
		} else {
			if (readBit(SpecialRegister.RP0.getAddress(), SpecialRegister.RP0.getBit()) == 0) {
				// RP0 is not set -> bank0 as target
				bank[0][address] = value;
			} else {
				// RP0 is set -> bank1 as target
				bank[1][address] = value;
			}
		}
	}

	public void clearByte(int address) {
		writeByte(address, 0);
	}

	public int readByte(int address) {
		int retByte = 0;

		if (readBit(SpecialRegister.RP0.getAddress(), SpecialRegister.RP0.getBit()) == 0) {
			// RP0 is not set -> bank0 as target
			retByte = bank[0][address];
		} else {
			// RP0 is set -> bank1 as target
			retByte = bank[1][address];
		}

		return retByte;
	}

	public void setBit(int address, int bit) {
		int bitmask = 1 << bit;

		if (isMirrored(address)) {
			bank[0][address] = bank[0][address] | bitmask;
			bank[1][address] = bank[1][address] | bitmask;
		} else {
			if ((MirroredRegister.STATUS.getAddress() & 0b100000) == 0) {
				// RP0 is not set -> bank0 as target
				bank[0][address] = bank[0][address] | bitmask;
			} else {
				// RP0 is set -> bank1 as target
				bank[1][address] = bank[1][address] | bitmask;
			}
		}
	}

	public void clearBit(int address, int bit) {
		int bitmask = 1 << bit;
		bitmask = bitmask ^ 0b11111111;

		if ((MirroredRegister.STATUS.getAddress() & 0b100000) == 0) {
			// RP0 is not set -> bank0 as target
			bank[0][address] = bank[0][address] & bitmask;
		} else {
			// RP0 is set -> bank1 as target
			bank[1][address] = bank[1][address] & bitmask;
		}
	}

	public int readBit(int address, int bit) {
		int retBit = 0;
		int bitmask = 1 << bit;

		if ((MirroredRegister.STATUS.getAddress() & 0b100000) == 0) {
			// RP0 is not set -> bank0 as target
			retBit = bank[0][address] & bitmask;
		} else {
			// RP0 is set -> bank1 as target
			retBit = bank[1][address] & bitmask;
		}

		retBit = retBit >> bit;

		return retBit;
	}

	private boolean isMirrored(int address) {
		boolean isMirrored = false;

		// part of the special registers that are mirrored on both banks
		for (final MirroredRegister element : MirroredRegister.values()) {
			if (address == element.getAddress()) {
				isMirrored = true;
			}
		}

		// part of userarea which is also mirrored on both banks
		if (address > 0x0B && address < 0x30) {
			isMirrored = true;
		}

		return isMirrored;
	}

	public void writeW(int value) {
		wRegister = value & 0b11111111;
	}

	public int readW() {
		return wRegister;
	}

	public void clearW() {
		writeW(0);
	}
}