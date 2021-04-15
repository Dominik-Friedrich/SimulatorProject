package Controller;

import java.util.ArrayList;

import Memory.DataMemory;
import Memory.ProgrammMemory;
import Memory.SpecialRegister;
import Memory.StackMemory;

public class ControllUnit extends Thread {
	private StackMemory stack = new StackMemory();
	private DataMemory dataStorage = new DataMemory();
	// private Parser lstParser = new Parser();
	private ProgrammMemory programmStorage = new ProgrammMemory();

	private int programmCounter = 0;

	private int runtimeCount = 0;
	private int currFrequency = 1; // in kHz
	private boolean running = false;

	public ControllUnit() {
	}
	
	public ControllUnit(DataMemory dataStorage, StackMemory stack) {
		this.dataStorage = dataStorage;
		this.stack = stack;
	}
	
	public ControllUnit(DataMemory dataStorage) {
		this.dataStorage = dataStorage;
	}

	// TODO implement reset here
	public int getRuntime() {
		int runtime = 0;

		// runtime in ms
		runtime = runtimeCount * 1 / (currFrequency * 1000) * 4;
		return runtime;
	}

	// TODO interrupt checks
	public void run() {
		int lastRunTime = 0;

		lastRunTime = getRuntime();
		execute(programmStorage.read(programmCounter));
		
		// TODO sleep(getRuntime() - lastRunTime);
		// updateGUI
	}

	public void newProgramm(ArrayList<Integer> instructions) {
		programmStorage.newProgramm(instructions);
		dataStorage.powerOnReset();
		runtimeCount = 0;
	}

	private void incrementPC() {
		programmCounter++;
		if (programmCounter > 8191) {
			programmCounter = 0;
		}

		int PCLATH = programmCounter & 0x1F0;
		PCLATH = PCLATH >> 8;

		dataStorage.writeByte(SpecialRegister.PCL.getAddress(), programmCounter);
		dataStorage.writeByte(SpecialRegister.PCLATH0.getAddress(), PCLATH);
	}
	
	public int getProgrammCounter() {
		return programmCounter;
	}

	// decoding the OPC and executing it
	public void execute(final int operationCode) {
		int fileAdress = operationCode & 0b00_0000_0111_1111;
		int destination = operationCode & 0b00_0000_1000_0000;
		destination = destination >> 7;
		int bitAdress = operationCode & 0b00_0011_1000_0000;
		bitAdress = bitAdress >> 7;

		// k is usually 8 Bit long
		// in case of goto and call it is 11 Bits long
		// so it gets re-masked later in case of a goto or call command
		int k = operationCode & 0b00_0000_1111_1111;

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
				if (destination > 0) {
					// MOVWF f
					movwf(fileAdress);
				} else {
					switch (operationCode) {
					case 0x64:
						// CLRWDT
						clrwdt();
						break;

					case 0x63:
						// SLEEP
						_sleep();
						break;

					case 0x9:
						// RETFIE
						retfie();
						break;

					case 0x8:
						// RETURN
						_return();
						break;

					case 0:
						// NOP
						nop();
						break;

					default:
						throw new IllegalArgumentException("Unexpected OPC: " + operationCode);
					}

				}
				// TODO multiple operations

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
		
		incrementPC();
	}

	private void xorlw(int k) {
		int tempVal = 0;
		tempVal = dataStorage.readW() ^ k;

		updateZeroFlag(tempVal);

		dataStorage.writeW(tempVal);

		runtimeCount++;
	}

	private void sublw(int k) {
		int tempVal = 0;
		tempVal = dataStorage.readW();
		tempVal = ~tempVal;
		tempVal += 1;

		dataStorage.writeW(tempVal);
		addlw(k);
	}

	private void _sleep() {
		// TODO Auto-generated method stub

		// dataStorage.reset();

		runtimeCount++;
	}

	private void _return() {
		programmCounter = stack.pop();

		runtimeCount++;
		runtimeCount++;
	}

	private void retlw(int k) {
		// TODO test
		dataStorage.writeW(k);
		programmCounter = stack.pop();

		runtimeCount++;
		runtimeCount++;
	}

	private void retfie() {
		// TODO test
		programmCounter = stack.pop();
		dataStorage.setBit(SpecialRegister.GIE.getAddress(), SpecialRegister.GIE.getBit());

		runtimeCount++;
		runtimeCount++;
	}

	private void movlw(int k) {
		dataStorage.writeW(k);

		runtimeCount++;
	}

	private void iorlw(int k) {
		int tempVal = 0;
		tempVal = dataStorage.readW() | k;

		updateZeroFlag(tempVal);

		dataStorage.writeW(tempVal);

		runtimeCount++;
	}

	private void goto_(int k) {
		// TODO test

		int tempVal = 0;
		tempVal = dataStorage.readByte(SpecialRegister.PCLATH0.getAddress());
		tempVal = tempVal & 0b0001_1000;
		tempVal = tempVal << 11;

		// PCL and PCLATH are updated in incrementPC() afterwards
		programmCounter = k + tempVal;

		runtimeCount++;
		runtimeCount++;
	}

	private void clrwdt() {
		// TODO Auto-generated method stub

		runtimeCount++;
	}

	private void call(int k) {
		stack.push(programmCounter);
		
		int tempVal = 0;
		tempVal = dataStorage.readByte(SpecialRegister.PCLATH0.getAddress());
		tempVal = tempVal & 0b0001_1000;
		tempVal = tempVal << 11;

		// PCL and PCLATH are updated in incrementPC() afterwards
		programmCounter = k + tempVal - 1;

		runtimeCount++;
		runtimeCount++;
	}

	private void andlw(int k) {
		int tempVal = 0;
		tempVal = dataStorage.readW() & k;

		updateZeroFlag(tempVal);

		dataStorage.writeW(tempVal);

		runtimeCount++;
	}

	private void addlw(int k) {
		int tempVal = 0;
		tempVal = dataStorage.readW() + k;

		updateCarryFlag(tempVal);
		updateHalfCarryFlag(dataStorage.readW(), k);
		updateZeroFlag(tempVal);

		dataStorage.writeW(tempVal);

		runtimeCount++;
	}

	private void btfss(int fileAdress, int bitAdress) {
		// TODO test
		if (dataStorage.readBit(fileAdress, bitAdress) == 1) {
			nop();
			incrementPC();
		}

		runtimeCount++;
	}

	private void btfsc(int fileAdress, int bitAdress) {
		// TODO test
		if (dataStorage.readBit(fileAdress, bitAdress) == 0) {
			nop();
			incrementPC();
		}

		runtimeCount++;
	}

	private void bsf(int fileAdress, int bitAdress) {
		dataStorage.setBit(fileAdress, bitAdress);

		runtimeCount++;
	}

	private void bcf(int fileAdress, int bitAdress) {
		dataStorage.clearBit(fileAdress, bitAdress);

		runtimeCount++;
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

		runtimeCount++;
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

		runtimeCount++;
	}

	private void subwf(int fileAdress, int destination) {
		int originalVal = dataStorage.readW();
		int tempVal = originalVal;
		
		tempVal = ~tempVal;
		tempVal += 1;
		
		// write 2s complement to w so addwf() can work with it
		dataStorage.writeW(tempVal);

		addwf(fileAdress, destination);
		
		
		// restore original value of w in case the destination was the fileregister
		if (destination > 0) {
			dataStorage.writeW(originalVal);
		}
		// Special case when subtracting 0
		if (originalVal == 0) {
			updateCarryFlag(0xFFF);
			updateHalfCarryFlag(0xF, 0xF);
		}
	}

	private void rrf(int fileAdress, int destination) {
		int tempVal = 0;
		int tempBit = 0;
		tempVal = dataStorage.readByte(fileAdress);

		// save carry
		tempBit = dataStorage.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());

		// move the least significant bit into carry
		if ((tempVal & 0b1) > 0) {
			dataStorage.setBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());
		} else {
			dataStorage.clearBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());
		}

		// add tempBit as the most significant bit (8th bit in our case)
		tempVal = tempVal >> 1;
		tempBit = tempBit << 7;

		tempVal = tempVal | tempBit;

		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}

		runtimeCount++;
	}

	private void rlf(int fileAdress, int destination) {
		// TODO test
		int tempVal = 0;
		int tempBit = 0;
		tempVal = dataStorage.readByte(fileAdress);

		// save carry
		tempBit = dataStorage.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());

		// move the most significant bit into carry
		if ((tempVal & 0b1000_0000) > 0) {
			dataStorage.setBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());
		} else {
			dataStorage.clearBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());
		}

		// add tempBit as the least significant bit (8th bit in our case)
		tempVal = tempVal << 1;

		tempVal = tempVal | tempBit;

		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}

		runtimeCount++;
	}

	private void nop() {
		runtimeCount++;
	}

	private void movwf(int fileAdress) {
		// TODO test
		dataStorage.writeByte(fileAdress, dataStorage.readW());

		runtimeCount++;
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

		runtimeCount++;
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

		runtimeCount++;
	}

	private void incfsz(int fileAdress, int destination) {
		// TODO test
		incf(fileAdress, destination);

		if (dataStorage.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()) > 0) {
			nop();
			incrementPC();
		}
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

		runtimeCount++;
	}

	private void decfsz(int fileAdress, int destination) {
		// TODO test
		decf(fileAdress, destination);

		if (dataStorage.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()) > 0) {
			nop();
		}
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

		runtimeCount++;
	}

	private void comf(int fileAdress, int destination) {
		// TODO test
		int tempVal = 0;
		tempVal = dataStorage.readByte(fileAdress);
		tempVal = ~tempVal;

		updateZeroFlag(tempVal);

		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}

		runtimeCount++;
	}

	private void clrw() {
		dataStorage.clearW();
		updateZeroFlag(0);

		runtimeCount++;
	}

	private void clrf(int fileAdress) {
		dataStorage.clearByte(fileAdress);
		updateZeroFlag(0);

		runtimeCount++;
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

		runtimeCount++;
	}

	private void addwf(int fileAdress, int destination) {
		int tempVal = 0;
		tempVal = dataStorage.readByte(fileAdress) + dataStorage.readW();

		updateCarryFlag(tempVal);
		updateHalfCarryFlag(dataStorage.readByte(fileAdress), dataStorage.readW());
		updateZeroFlag(tempVal);

		if (destination == 0) {
			dataStorage.writeW(tempVal);
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
		}

		runtimeCount++;
	}

	public void updateCarryFlag(int tempVal) {
		if (tempVal > 0xFF) {
			dataStorage.setBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());
		} else {
			dataStorage.clearBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());
		}
	}

	public void updateHalfCarryFlag(int tempVal1, int tempVal2) {
		// TODO test
		tempVal1 = tempVal1 & 0xF;
		tempVal2 = tempVal2 & 0xF;
		int tempVal = tempVal1 + tempVal2;

		if (tempVal > 0xF) {
			dataStorage.setBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit());
		} else {
			dataStorage.clearBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit());
		}
	}

	public void updateZeroFlag(int value) {
		value = value & 0xFF;

		if (value == 0) {
			dataStorage.setBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit());
		} else {
			dataStorage.clearBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit());
		}
	}
}
