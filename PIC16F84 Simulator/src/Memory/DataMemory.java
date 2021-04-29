package Memory;

import Controller.ControllUnit;
import Controller.PICTimer;

public class DataMemory {
	private ControllUnit controller;
	private PICTimer timer;

	private int[][] bank = { new int[128], new int[128] };
	private int wRegister = 0;

	// TODO remove other constructors
	public DataMemory(ControllUnit controller, PICTimer timer) {
		this.controller = controller;
		this.timer = timer;
	}

	// USED ONLY FOR TESTING
	public DataMemory() {
	}

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

	public int getwRegister() {
		return wRegister;
	}

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

	public void clearByte(int address) {
		writeByte(address, 0);
	}

	public void writePCL(int value) {
		bank[0][SpecialRegister.PCL.getAddress()] = value & 0xFF;
		bank[1][SpecialRegister.PCL.getAddress()] = value & 0xFF;
	}

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

	public void writeW(int value) {
		wRegister = value & 0b11111111;
	}

	public int readW() {
		return wRegister;
	}

	public void clearW() {
		writeW(0);
	}
	
	public void setTMR0(int value) {
		bank[0][SpecialRegister.TMR0.getAddress()] = (value & 0xFF);
	}
	
	public int getT0CS() {
		return (bank[1][SpecialRegister.T0CS.getAddress()] & 0b10000);
	}
	
	public int getRA4() {
		return (bank[0][SpecialRegister.RA4.getAddress()] & 0b10000);
	}
	
	public int getINTEDG0() {
		return (bank[1][SpecialRegister.INTEDG.getAddress()] & 0b1000000);
	}
	
	public int getT0SE() {
		return (bank[1][SpecialRegister.T0SE.getAddress()] & 0b10000);
	}
	
	public int getPSA() {
		return (bank[1][SpecialRegister.PSA.getAddress()] & 0b1000);
	}
	
	public int getTris(char port) {
		if (port == 'A') {
			return bank[1][SpecialRegister.TRISA.getAddress()];
		} else {
			return bank[1][SpecialRegister.TRISB.getAddress()];
		}
	}
}