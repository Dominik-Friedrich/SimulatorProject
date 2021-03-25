import Memory.DataMemory;
import Memory.ProgrammMemory;
import Memory.StackMemory;

public class ControllUnit {
	private StackMemory stack = new StackMemory();
	private DataMemory dataStorage = new DataMemory();
	// private Parser lstParser = new Parser();
	private ProgrammMemory programmStorage = new ProgrammMemory();

	public static void main(String[] args) {
		// TODO remove after testing is done, programm starts in GUI
		ControllUnit controller = new ControllUnit();
	}

	public ControllUnit() {
		// TODO Auto-generated constructor stub
		execute(0b00011111111111);
	}

	public void newProgramm(final String filepath) {
		// TODO parser that takes a filepath (LST file) as input and generates the
		// programmmemory from it
		// TODO initialise the programmStorage using ^ as input parameter
	}

	// Everything below this line is for decoding the OPC and executing it
	private void execute(final int operationCode) {
		int fileAdress = operationCode & 0b00000001111111;
		int destination = operationCode & 0b00000010000000;
		int bitAdress = operationCode & 00001110000000;
		
		// k is usually 8 Bit long 
		// in case of goto and call it is 11 Bits long 
		// so it gets remasked in case of a goto or call command
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
				decdsz(fileAdress, destination);
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
			k = operationCode & 0b00011111111111;
			// TODO either CALL or GOTO
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
				//SUBLW k
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
		// TODO Auto-generated method stub
		
	}

	private void sublw(int k) {
		// TODO Auto-generated method stub
		
	}

	private void retlw(int k) {
		// TODO Auto-generated method stub
		
	}

	private void movlw(int k) {
		// TODO Auto-generated method stub
		
	}

	private void iorlw(int k) {
		// TODO Auto-generated method stub
		
	}

	private void andlw(int k) {
		// TODO Auto-generated method stub
		
	}

	private void addlw(int k) {
		// TODO Auto-generated method stub
		
	}

	private void btfss(int fileAdress, int bitAdress) {
		// TODO Auto-generated method stub
		
	}

	private void btfsc(int fileAdress, int bitAdress) {
		// TODO Auto-generated method stub
		
	}

	private void bsf(int fileAdress, int bitAdress) {
		// TODO Auto-generated method stub
		
	}

	private void bcf(int fileAdress, int bitAdress) {
		// TODO Auto-generated method stub
		
	}

	private void xorwf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void swapf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void subwf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void rrf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void rlf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void movf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void iorwf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void incfsz(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void incf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void decdsz(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void decf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void comf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void andwf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}

	private void addwf(int fileAdress, int destination) {
		// TODO Auto-generated method stub
		
	}
}
