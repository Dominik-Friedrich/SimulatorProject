package Controller;
import Memory.DataMemory;
import Memory.ProgrammMemory;
import Memory.SpecialRegister;
import Memory.StackMemory;

public class ControllUnit {
	private StackMemory stack = new StackMemory();
	private DataMemory dataStorage = new DataMemory();
	// private Parser lstParser = new Parser();
	private ProgrammMemory programmStorage = new ProgrammMemory();
	// TODO write tests
	// TODO replace subtraction updatecarry and halfcarry with correct one

	public ControllUnit() {
		// TODO Auto-generated constructor stub
	}
	
	// Used for testing
	public void attachNewStorage(DataMemory dataStorage) {
		if (dataStorage != null) {
			this.dataStorage = dataStorage;
		}
	}

	public void newProgramm(final String filepath) {
		// TODO parser that takes a filepath (LST file) as input and generates the
		// programmmemory from it
		// TODO initialise the programmStorage using ^ as input parameter
		// parser is its own class
	}

	// Everything below this line is for decoding the OPC and executing it
	public void execute(final int operationCode) {
		int fileAdress = operationCode & 0b00_0000_0111_1111;
		int destination = operationCode & 0b00_0000_1000_0000;
		destination = destination >> 7;
		int bitAdress = operationCode & 0b00_0011_1000_0000;
		bitAdress = bitAdress >> 7;
		
		// k is usually 8 Bit long
		// in case of goto and call it is 11 Bits long
		// so it gets re-masked later in case of a goto or call command
		int k = operationCode & 0b00000011111111;

		// top 2 bits to identify operation class
		switch (operationCode & 0b11000000000000) {
		case 0:
			// byte-oriented file register operations
			// and some literal and control operations

			// TODO replace exceptions with GUI output of invalid code

			// top 6 bits to identify operation
			switch (operationCode & 0b11111100000000) {
			case 0x700:
				// ADDWF f,d
				addwf(fileAdress, destination);
				break;

			case 0x500:
				// ANDWF f,d
				andwf(fileAdress, destination);
				break;

			case 0x100:
				// 2 commands fill this case
				// use "destination" to differentiate
				// d = 1; CLRF f
				// d = 0; CLRW
				if (destination == 1) {
					clrf(fileAdress);
				} else {
					clrw();
				}
				break;

			case 0x900:
				// COMF f,d
				comf(fileAdress, destination);
				break;

			case 0x300:
				// DECF f,d
				decf(fileAdress, destination);
				break;

			case 0xB00:
				// DECFSZ f,d
				decfsz(fileAdress, destination);
				break;

			case 0xA00:
				// INCF f, d
				incf(fileAdress, destination);
				break;

			case 0xF00:
				// INCFSZ f,d
				incfsz(fileAdress, destination);
				break;

			case 0x400:
				// IORWF f,d
				iorwf(fileAdress, destination);
				break;

			case 0x800:
				// MOVF f,d
				movf(fileAdress, destination);
				break;

			case 0x000:
				// TODO multiple operations
				// MOVWF f
				// NOP
				// CLRWDT
				// RETFIE
				// RETURN
				// SLEEP
				break;

			case 0xD00:
				// RLF f,d
				rlf(fileAdress, destination);
				break;

			case 0xC00:
				// RRF f,d
				rrf(fileAdress, destination);
				break;

			case 0x200:
				// SUBWF f,d
				subwf(fileAdress, destination);
				break;

			case 0xE00:
				// SWAPF f,d
				swapf(fileAdress, destination);
				break;

			case 0x600:
				// XORWF
				xorwf(fileAdress, destination);
				break;

			default:
				throw new IllegalArgumentException("Unexpected OPC: " + operationCode);
			}
			break;

		case 0x1000:

			// bit-oriented file register operations
			// bits 11 and 12 to identify which operation
			switch (operationCode & 0b00110000000000) {
			case 0:
				// BCF f,b
				bcf(fileAdress, bitAdress);
				break;

			case 0x400:
				// BSF f,b
				bsf(fileAdress, bitAdress);
				break;

			case 0x800:
				// BTFSC f,b
				btfsc(fileAdress, bitAdress);
				break;

			case 0xC00:
				// BTFSS f,b
				btfss(fileAdress, bitAdress);
				break;

			default:
				throw new IllegalArgumentException("Unexpected OPC: " + operationCode);
			}
			break;

		// literial and control operations

		case 0x2000:
			// TODO goto and call
			k = operationCode & 0b00011111111111;
			if ((operationCode & 0b100000000000) == 0x800) {
				// GOTO k
				goto_(k);
			} else {
				// CALL
				call(k);
			}
			break;

		case 0x3000:
			// bit 9 to 12 to identify operation
			switch (operationCode & 0b00111100000000) {

			// fall through is intentional
			case 0xE00:
			case 0xF00:
				// ADDLW k
				addlw(k);
				break;

			case 0x900:
				// ANDLW k
				andlw(k);
				break;

			case 0x800:
				// IORLW k
				iorlw(k);
				break;

			// fall through is intentional
			case 0:
			case 0x100:
			case 0x200:
			case 0x300:
				// MOVLW k
				movlw(k);
				break;

			// fall through is intentional
			case 0x400:
			case 0x500:
			case 0x600:
			case 0x700:
				// RETLW k
				retlw(k);
				break;

			// fall through is intentional
			case 0xC00:
			case 0xD00:
				// SUBLW k
				sublw(k);
				break;

			case 0xA00:
				// XORLW k
				xorlw(k);
				break;

			default:
				throw new IllegalArgumentException("Unexpected OPC: " + operationCode);
			}
			break;

		default:
			throw new IllegalArgumentException("Unexpected OPC: " + operationCode);
		}
	}

	private void xorlw(int k) {
		int tempVal = 0;
		tempVal = dataStorage.readW() ^ k;

		updateZeroFlag(tempVal);
		
		dataStorage.writeW(tempVal);
	}

	private void sublw(int k) {
		int tempVal = 0;
		tempVal = k - dataStorage.readW();

		updateCarryFlag(tempVal, dataStorage.readW());
		updateHalfCarryFlag(tempVal, dataStorage.readW());
		updateZeroFlag(tempVal);
		
		dataStorage.writeW(tempVal);
	}

	private void sleep() {
		// TODO Auto-generated method stub

	}
	
	private void return_() {
		// TODO Auto-generated method stub

	}
	
	private void retlw(int k) {
		// TODO Auto-generated method stub

	}
	
	private void retfie() {
		// TODO Auto-generated method stub

	}

	private void movlw(int k) {
		dataStorage.writeW(k);
	}

	private void iorlw(int k) {
		int tempVal = 0;
		tempVal = dataStorage.readW() | k;

		updateZeroFlag(tempVal);
		
		dataStorage.writeW(tempVal);
	}

	private void goto_(int k) {
		// TODO Auto-generated method stub
		
	}
	
	private void clrwdt() {
		// TODO Auto-generated method stub

	}
	
	private void call(int k) {
		// TODO Auto-generated method stub

	}
	
	private void andlw(int k) {
		int tempVal = 0;
		tempVal = dataStorage.readW() & k;

		updateZeroFlag(tempVal);
		
		dataStorage.writeW(tempVal);
	}

	private void addlw(int k) {
		int tempVal = 0;
		tempVal = dataStorage.readW() + k;
		
		updateCarryFlag(tempVal);
		updateHalfCarryFlag(tempVal);
		updateZeroFlag(tempVal);
		
		dataStorage.writeW(tempVal);
	}

	private void btfss(int fileAdress, int bitAdress) {
		// TODO Auto-generated method stub

	}

	private void btfsc(int fileAdress, int bitAdress) {
		// TODO Auto-generated method stub

	}

	private void bsf(int fileAdress, int bitAdress) {
		dataStorage.setBit(fileAdress, bitAdress);
	}

	private void bcf(int fileAdress, int bitAdress) {
		dataStorage.clearBit(fileAdress, bitAdress);
	}

	private void xorwf(int fileAdress, int destination) {
		int tempVal = 0;
		tempVal = dataStorage.readW() ^ dataStorage.readByte(fileAdress);

		updateZeroFlag(tempVal);

		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}
	}

	private void swapf(int fileAdress, int destination) {
		int tempVal = 0;
		int tempVal2 = 0;

		// put lower Nibble to position of higher Nibble
		tempVal = dataStorage.readByte(fileAdress) & 0b1111;
		tempVal = tempVal << 4;

		// put higher Nibble to position of lower Nibble
		tempVal2 = dataStorage.readByte(fileAdress) & 0b11110000;
		tempVal2 = tempVal2 >> 4;

		tempVal = tempVal + tempVal2;

		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}

	}

	private void subwf(int fileAdress, int destination) {
		int tempVal = 0;
		tempVal = dataStorage.readByte(fileAdress) - dataStorage.readW();

		updateCarryFlag(tempVal, dataStorage.readW());
		updateHalfCarryFlag(tempVal, dataStorage.readW());
		updateZeroFlag(tempVal);
		
		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}
	}

	private void rrf(int fileAdress, int destination) {
		// TODO Auto-generated method stub

	}

	private void rlf(int fileAdress, int destination) {
		// TODO Auto-generated method stub

	}
	
	private void nop() {
		// TODO Auto-generated method stub
		// probably dont even need a method for NOP
	}
	
	private void movwf(int fileAdress) {
		// TODO Auto-generated method stub

	}

	private void movf(int fileAdress, int destination) {
		int tempVal = 0;
		tempVal = dataStorage.readByte(fileAdress);

		updateZeroFlag(tempVal);
		
		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}
	}

	private void iorwf(int fileAdress, int destination) {
		int tempVal = 0;
		tempVal = dataStorage.readW() | dataStorage.readByte(fileAdress);

		updateZeroFlag(tempVal);

		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}
	}

	private void incfsz(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		// ask prof
	}

	private void incf(int fileAdress, int destination) {
		int tempVal = 0;
		tempVal = dataStorage.readByte(fileAdress) + 1;

		updateZeroFlag(tempVal);

		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}
	}

	private void decfsz(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		// ask prof
	}

	private void decf(int fileAdress, int destination) {
		int tempVal = 0;
		tempVal = dataStorage.readByte(fileAdress) - 1;

		updateZeroFlag(tempVal);

		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}
	}

	private void comf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		// ask prof

	}

	private void clrw() {
		dataStorage.clearW();
		updateZeroFlag(0);
	}

	private void clrf(int fileAdress) {
		dataStorage.clearByte(fileAdress);
		updateZeroFlag(0);
	}

	private void andwf(int fileAdress, int destination) {
		int tempVal = 0;
		tempVal = dataStorage.readW() & dataStorage.readByte(fileAdress);

		updateZeroFlag(tempVal);

		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}
	}

	private void addwf(int fileAdress, int destination) {
		int tempVal = 0;
		tempVal = dataStorage.readByte(fileAdress) + dataStorage.readW();

		updateCarryFlag(tempVal);
		updateHalfCarryFlag(tempVal);
		updateZeroFlag(tempVal);
		
		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}
	}

	
	// TODO methods should be private, public only for testing purposes
	public void updateCarryFlag(int tempVal, int subtractionVal) {
		// Special case subtraction
		boolean setBit = false;
		
			
		if (tempVal >= 0 || subtractionVal == 0) {
			setBit = true;
		}
		
		if (setBit) {
			dataStorage.setBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());
		} else {
			dataStorage.clearBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());
		}
	}
	
	public void updateCarryFlag(int tempVal) {
		// Case addition
		boolean setBit = false;
		
		if (tempVal > 0xFF) {
			setBit = true;
		}
		
		if (setBit) {
			dataStorage.setBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());
		} else {
			dataStorage.clearBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());
		}
	}
	
	public void updateHalfCarryFlag(int tempVal, int subtractionVal) {
		// TODO ask prof how halfcarry works not done yet implement half carry 
		// Special case subtraction
		boolean setBit = false;
		
		// wrong
		/*if (tempVal >= 0 || subtractionVal == 0) {
			setBit = true;
		}
		*/
		if (setBit) {
			dataStorage.setBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit());
		} else {
			dataStorage.clearBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit());
		}
	}
	
	public void updateHalfCarryFlag(int tempVal) {
		// TODO Auto-generated method stub
	}
	
	public void updateZeroFlag(int value) { 
		if (value == 0) {
			dataStorage.setBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit());
		} else {
			dataStorage.clearBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit());
		}
	}
}
