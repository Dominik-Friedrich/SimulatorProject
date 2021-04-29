package Controller;

import java.util.ArrayList;

import GUI.Window;
import Memory.DataMemory;
import Memory.ProgrammMemory;
import Memory.SpecialRegister;
import Memory.StackMemory;
import Read.ReadLST;

public class ControllUnit {
	private Window gui;
	private ProgrammMemory programmStorage;
	private StackMemory stack;
	private DataMemory dataStorage;
	private PICTimer timer;

	private int programmCounter = 0;

	// runtimeCount
	private int lastRuntimeCount = 0;
	private int runtimeCount = 0;
	private int currFrequency = 1; // in kHz

	public ControllUnit() {
	}

	public ControllUnit(Window gui) {
		this.gui = gui;
		this.stack = new StackMemory();
		this.timer = new PICTimer();
		this.dataStorage = new DataMemory(this, timer);
		this.programmStorage = new ProgrammMemory();
	}

	public DataMemory getData() {
		return dataStorage;
	}

	public StackMemory getStack() {
		return stack;
	}

	// GUI uses this
	public int getRuntime() {
		double runtime = 0;

		// runtime in micro seconds
		runtime = (double) (4000 * runtimeCount) / (double) (currFrequency);

		return (int) runtime;
	}

	public void run() {
		if (programmStorage != null) {
			lastRuntimeCount = runtimeCount;

			if (interruptHappened()) {
				dataStorage.clearBit(SpecialRegister.GIE.getAddress(), SpecialRegister.GIE.getBit());
				// CALL to address 0x04
				execute(0x2004);
			}
			execute(programmStorage.read(programmCounter));

			// Timer
			if (dataStorage.getT0CS() == 0) {
				// Update on cycle clock
				// update for how many cycles happened
				for (int i = 0; i < (runtimeCount - lastRuntimeCount); i++) {
					dataStorage.setTMR0(timer.incrementTimer());
				}
			}

			// check for overflow
			if (timer.overflow()) {
				// timer overflow, set T0IF
				dataStorage.setBit(SpecialRegister.T0IF.getAddress(), SpecialRegister.T0IF.getBit());
				updateZeroFlag(0);
				timer.clearOverflow();
			}

			gui.updateGui(dataStorage, stack);
		}
	}

	public void newProgramm(ArrayList<Integer> instructions) {
		programmStorage.newProgramm(instructions);
		dataStorage.powerOnReset();
		runtimeCount = 0;
		programmCounter = 0;
		gui.updateGui(dataStorage, stack);
	}

	public void timerUpdate(int newTimerValue) {
		timer.registerUpdate(newTimerValue);
	}

	private void incrementPC() {
		if ((programmCounter & 0xFF) == 255 && programmCounter > 0) {
			programmCounter -= 0xFF;
		} else {
			programmCounter++;
		}

		dataStorage.writePCL(programmCounter);
	}

	public void changePC() {
		// PC is incremented after
		programmCounter = dataStorage.readByte(SpecialRegister.PCL.getAddress());
		int tempPCLATH = dataStorage.readByte(SpecialRegister.PCL.getAddress()) & 0b11111;
		tempPCLATH = tempPCLATH << 8;

		programmCounter += tempPCLATH;
	}

	public int getProgrammCounter() {
		return programmCounter;
	}

	public void incRuntimeCount() {
		runtimeCount++;
	}

	private boolean interruptHappened() {
		boolean bRet = false;

		// global interrupt enable
		if (dataStorage.readBit(SpecialRegister.GIE.getAddress(), SpecialRegister.GIE.getBit()) == 1) {

			// Timer interrupt
			if (dataStorage.readBit(SpecialRegister.T0IE.getAddress(), SpecialRegister.T0IE.getBit()) == 1) {
				if (dataStorage.readBit(SpecialRegister.T0IF.getAddress(), SpecialRegister.T0IF.getBit()) == 1) {
					bRet = true;
				}
			}

			// RB0/INT interrupt
			if (dataStorage.readBit(SpecialRegister.INTE.getAddress(), SpecialRegister.INTE.getBit()) == 1) {
				if (dataStorage.readBit(SpecialRegister.INTF.getAddress(), SpecialRegister.INTF.getBit()) == 1) {
					bRet = true;
				}
			}

			// RB7:RB4 interrupt
			if (dataStorage.readBit(SpecialRegister.RBIE.getAddress(), SpecialRegister.RBIE.getBit()) == 1) {
				if (dataStorage.readBit(SpecialRegister.RBIF.getAddress(), SpecialRegister.RBIF.getBit()) == 1) {
					bRet = true;
				}
			}
		}

		return bRet;
	}

	public void pinUpdate(boolean value, int bit, char pin) {
		// Port B Interrupts
		if (pin == 'B') {
			switch (bit) {
			case 0:
				if ((dataStorage.getTris('B') & 0x1) > 0) {
					
					dataStorage.setBit(SpecialRegister.INTF.getAddress(), SpecialRegister.INTF.getBit());
				}
				break;

			case 4:
			case 5:
			case 6:
			case 7:
				if ((dataStorage.getTris('B') & (0x1 << bit)) > 0) {
					dataStorage.setBit(SpecialRegister.RBIF.getAddress(), SpecialRegister.RBIF.getBit());
				}
				break;

			default:
				//System.out.println(bit + "  " + pin + "   " + value);
				break;
			}
		}

		if (value) {
			dataStorage.setPin(bit, pin);
		} else {
			dataStorage.ClearPin(bit, pin);
		}

		gui.updateGui(dataStorage, stack);
	}

	public void externalTimerTrigger() {
		if (dataStorage.getT0CS() > 0) {
			dataStorage.setTMR0(timer.externalTrigger(dataStorage.getRA4(), dataStorage.getT0SE()));
		}
	}

	public int getPSA() {
		return dataStorage.getPSA();
	}

	public void setFrequency(int frequency) {
		this.currFrequency = frequency;
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
		dataStorage.writeW(k);
		programmCounter = stack.pop();

		runtimeCount++;
		runtimeCount++;
	}

	private void retfie() {
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
		int tempVal = 0;
		tempVal = dataStorage.readByte(SpecialRegister.PCLATH0.getAddress());
		tempVal = tempVal & 0b0001_1000;
		tempVal = tempVal << 8;

		// PCL and PCLATH are updated in incrementPC() afterwards
		programmCounter = k + tempVal - 1;

		if (programmCounter < 0) {
			programmCounter = -1;
		}

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
		tempVal = tempVal << 8;

		// PCL is updated in incrementPC afterwards
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
		if (dataStorage.readBit(fileAdress, bitAdress) == 1) {
			nop();
			incrementPC();
		}

		runtimeCount++;
	}

	private void btfsc(int fileAdress, int bitAdress) {
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
		int tempVal = 0;
		tempVal = dataStorage.readByte(fileAdress) + 1;

		if (destination == 0) {
			dataStorage.writeW(tempVal);
			if (dataStorage.readW() == 0) {
				execute(0);
			}
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
			if (dataStorage.readByte(fileAdress) == 0) {
				execute(0);
			}
		}

		runtimeCount++;
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
		int tempVal = 0;
		tempVal = dataStorage.readByte(fileAdress) - 1;

		if (destination == 0) {
			dataStorage.writeW(tempVal);
			if (dataStorage.readW() == 0) {
				execute(0);
			}
		} else {
			dataStorage.writeByte(fileAdress, tempVal);
			if (dataStorage.readByte(fileAdress) == 0) {
				execute(0);
			}
		}

		runtimeCount++;
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
