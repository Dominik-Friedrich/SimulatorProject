package Memory;

import Controller.ControllUnit;
import Controller.PICTimer;

public class DataMemory {
	private ControllUnit controller;
	private PICTimer timer;

	private int[][] bank = { new int[128], new int[128] };
	private int wRegister = 0;

	public DataMemory(ControllUnit controller, PICTimer timer) {
		this.controller = controller;
		this.timer = timer;
	}

	// USED ONLY FOR TESTING
	public DataMemory() {
	}

	/**
	 * 
	 * @return current Bank
	 */
	public int[][] getBank() {
		return bank;
	}

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

	/**
	 * 
	 * @return value of W-Register
	 */
	public int getwRegister() {
		return wRegister;
	}

	/**
	 * Writes the value to the specified address
	 * 
	 * @param address to write to
	 * @param value   to write
	 */
	public void writeByte(int address, int value) {
		value = value & 0xFF;

		if (address == 0) {
			address = readByte(SpecialRegister.FSR.getAddress());
		}
		if (address == SpecialRegister.TMR0.getAddress()) {
			if (readBit(SpecialRegister.RP0.getAddress(), SpecialRegister.RP0.getBit()) == 0) {
				// update on TMR0
				controller.incRuntimeCount();
				timer.registerUpdate(value);
			} else {
				// update on OPTION
				timer.setPreScaler(value);
			}
		}
		if (address == SpecialRegister.PCL.getAddress()) {
			controller.changePC();
		}

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

	/**
	 * Sets the value of the specified address to 0
	 * 
	 * @param address to set to 0
	 */
	public void clearByte(int address) {
		writeByte(address, 0);
	}

	/**
	 * Write to the PCL-Register
	 * 
	 * @param value to write
	 */
	public void writePCL(int value) {
		bank[0][SpecialRegister.PCL.getAddress()] = value & 0xFF;
		bank[1][SpecialRegister.PCL.getAddress()] = value & 0xFF;
	}

	/**
	 * Reads the value of the specified address
	 * 
	 * @param address to read
	 * @return value of read address
	 */
	public int readByte(int address) {
		if (address == 0) {
			address = readByte(SpecialRegister.FSR.getAddress());
		}

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

	/**
	 * Sets the bit of the specified address
	 * 
	 * @param address to write to
	 * @param bit     inside of the byte register to set
	 */
	public void setBit(int address, int bit) {
		int bitmask = 1 << bit;

		if (address == 0) {
			address = readByte(SpecialRegister.FSR.getAddress());
		}

		if (address == SpecialRegister.PCL.getAddress()) {
			controller.changePC();
		}

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

		if (address == SpecialRegister.TMR0.getAddress()) {
			controller.incRuntimeCount();
			timer.registerUpdate(bank[0][address] | bitmask);
			timer.setPreScaler(bank[1][address]);
		}
	}

	/**
	 * Clears the bit of the specified address
	 * 
	 * @param address to write to
	 * @param bit     inside of the byte register to clear
	 */
	public void clearBit(int address, int bit) {
		if (address == 0) {
			address = readByte(SpecialRegister.FSR.getAddress());
		}

		int bitmask = 1 << bit;
		bitmask = bitmask ^ 0b11111111;

		if (isMirrored(address)) {
			bank[0][address] = bank[0][address] & bitmask;
			bank[1][address] = bank[1][address] & bitmask;
		} else {
			if ((MirroredRegister.STATUS.getAddress() & 0b100000) == 0) {
				// RP0 is not set -> bank0 as target
				bank[0][address] = bank[0][address] & bitmask;
			} else {
				// RP0 is set -> bank1 as target
				bank[1][address] = bank[1][address] & bitmask;
			}
		}
	}

	/**
	 * Reads the bit of the specified address
	 * 
	 * @param address to write to
	 * @param bit     inside of the byte register to read
	 * @return value of bit
	 */
	public int readBit(int address, int bit) {
		if (address == 0) {
			address = readByte(SpecialRegister.FSR.getAddress());
		}

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

	/**
	 * Checks if a specified address is mirrored on both banks
	 * 
	 * @param address to check
	 * @return true if mirrored
	 */
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

	/**
	 * Sets the bit of the specified pin Register
	 * 
	 * @param bit inside of the byte register to set
	 * @param pin 'A' for port A, 'B' for Port B
	 */
	public void setPin(int bit, char pin) {
		int bitmask = 1 << bit;

		switch (pin) {
		case 'A':
			bank[0][SpecialRegister.RA0.getAddress()] = bank[0][SpecialRegister.RA0.getAddress()] | bitmask;
			break;

		case 'B':
			bank[0][SpecialRegister.RB0.getAddress()] = bank[0][SpecialRegister.RB0.getAddress()] | bitmask;
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + pin);
		}
	}

	/**
	 * Clears the bit of the specified pin Register
	 * 
	 * @param bit inside of the byte register to clear
	 * @param pin 'A' for port A, 'B' for Port B
	 */
	public void ClearPin(int bit, char pin) {
		int bitmask = 1 << bit;
		bitmask = (~bitmask) & 0xFF;

		switch (pin) {
		case 'A':
			bank[0][SpecialRegister.RA0.getAddress()] = bank[0][SpecialRegister.RA0.getAddress()] & bitmask;
			break;

		case 'B':
			bank[0][SpecialRegister.RB0.getAddress()] = bank[0][SpecialRegister.RB0.getAddress()] & bitmask;
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + pin);
		}
	}

	/**
	 * Writes a value to the W-Register
	 * 
	 * @param value to write
	 */
	public void writeW(int value) {
		wRegister = value & 0b11111111;
	}

	/**
	 * 
	 * @return value of W-Register
	 */
	public int readW() {
		return wRegister;
	}

	/**
	 * Clears the value of the W-Register
	 */
	public void clearW() {
		writeW(0);
	}

	/**
	 * Writes a value to the TMR0 Register
	 * 
	 * @param value to write
	 */
	public void setTMR0(int value) {
		bank[0][SpecialRegister.TMR0.getAddress()] = (value & 0xFF);
	}

	/**
	 * 
	 * @return value of T0CS Bit
	 */
	public int getT0CS() {
		return (bank[1][SpecialRegister.T0CS.getAddress()] & 0b10000);
	}

	/**
	 * 
	 * @return Value of RA4 Bit
	 */
	public int getRA4() {
		return (bank[0][SpecialRegister.RA4.getAddress()] & 0b10000);
	}

	/**
	 * 
	 * @return Value of INTEDG0 Bit
	 */
	public int getINTEDG0() {
		return (bank[1][SpecialRegister.INTEDG.getAddress()] & 0b1000000);
	}

	/**
	 * 
	 * @return Value of T0SE Bit
	 */
	public int getT0SE() {
		return (bank[1][SpecialRegister.T0SE.getAddress()] & 0b10000);
	}

	/**
	 * 
	 * @return Value of PSA Bit
	 */
	public int getPSA() {
		return (bank[1][SpecialRegister.PSA.getAddress()] & 0b1000);
	}

	/**
	 * Returns the Tris Register for the specified Port
	 * 
	 * @param port 'A' for port A, 'B' for Port B
	 * @return Value of Tris Register
	 */
	public int getTris(char port) {
		if (port == 'A') {
			return bank[1][SpecialRegister.TRISA.getAddress()];
		} else {
			return bank[1][SpecialRegister.TRISB.getAddress()];
		}
	}
}