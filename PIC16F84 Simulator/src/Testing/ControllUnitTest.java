package Testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Controller.ControllUnit;
import Memory.DataMemory;
import Memory.SpecialRegister;

class ControllUnitTest {
	private DataMemory memoryTestee;
	private ControllUnit controlTestee;
	private int testRegister = 0b001_0000;
	private int valR = 8;
	private int valW = 3;

	@BeforeEach
	public void setUp() {
		memoryTestee = new DataMemory();
		controlTestee = new ControllUnit();
		controlTestee.attachNewStorage(memoryTestee);

		memoryTestee.writeByte(testRegister, valR);
		memoryTestee.writeW(valW);
	}

	@Test
	void testADDWF() {
		// save in register
		controlTestee.execute(0b111_1001_0000);
		assertEquals(valR + valW, memoryTestee.readByte(testRegister));
		assertEquals(valW, memoryTestee.readW());

		// save in w
		controlTestee.execute(0b111_0001_0000);
		assertEquals(valR + valW + valW, memoryTestee.readW());
		assertEquals(valR + valW, memoryTestee.readByte(testRegister));
	}

	@Test
	void testANDWF() {
		memoryTestee.writeByte(testRegister, 9);

		// save in register
		controlTestee.execute(0b101_1001_0000);
		assertEquals(9 & 3, memoryTestee.readByte(testRegister));
		assertEquals(3, memoryTestee.readW());

		// save in w
		controlTestee.execute(0b101_0001_0000);
		assertEquals(1 & 3, memoryTestee.readW());
		assertEquals(9 & 3, memoryTestee.readByte(testRegister));
	}

	@Test
	void testCLRF() {
		controlTestee.execute(0b001_1001_0000);
		assertEquals(0, memoryTestee.readByte(testRegister));
	}

	@Test
	void testCLRW() {
		controlTestee.execute(0b001_0001_0000);
		assertEquals(0, memoryTestee.readW());
	}

	// TODO COMF

	@Test
	void testDECF() {
		controlTestee.execute(0b11_1001_0000);
		assertEquals(valR - 1, memoryTestee.readByte(testRegister));

		controlTestee.execute(0b11_0001_0000);
		assertEquals(valR - 1 - 1, memoryTestee.readW());
	}

	// TODO DECFSZ

	@Test
	void testINCF() {
		// save in register
		controlTestee.execute(0b1010_1001_0000);
		assertEquals(valR + 1, memoryTestee.readByte(testRegister));

		// save in w
		controlTestee.execute(0b1010_0001_0000);
		assertEquals(valR + 1 + 1, memoryTestee.readW());
	}

	// TODO INCFSZ

	@Test
	void testIORWF() {
		// save in register
		controlTestee.execute(0b100_1001_0000);
		assertEquals(valR | valW, memoryTestee.readByte(testRegister));

		// save in w
		setUp();
		controlTestee.execute(0b100_0001_0000);
		assertEquals(valR | valW, memoryTestee.readW());
	}

	@Test
	void testMOVF() {
		// move to register
		controlTestee.execute(0b1000_1001_0000);
		assertEquals(valR, memoryTestee.readByte(testRegister));

		// move to w
		controlTestee.execute(0b1000_0001_0000);
		assertEquals(valR, memoryTestee.readW());
	}

	// TODO MOVWF, NOP

	@Test
	void testRLF() {
		controlTestee.execute(0b1101_1001_0000);
		assertEquals(16, memoryTestee.readByte(testRegister));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));

		controlTestee.execute(0b1101_1001_0000);
		assertEquals(32, memoryTestee.readByte(testRegister));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));

		memoryTestee.setBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());

		controlTestee.execute(0b1101_1001_0000);
		assertEquals(64 + 1, memoryTestee.readByte(testRegister));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));

		controlTestee.execute(0b1101_1001_0000);
		assertEquals(128 + 2, memoryTestee.readByte(testRegister));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));

		controlTestee.execute(0b1101_1001_0000);
		assertEquals(4, memoryTestee.readByte(testRegister));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
	}

	@Test
	void testRRF() {
		controlTestee.execute(0b1100_1001_0000);
		assertEquals(4, memoryTestee.readByte(testRegister));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));

		memoryTestee.setBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit());

		controlTestee.execute(0b1100_1001_0000);
		assertEquals(2 + 128, memoryTestee.readByte(testRegister));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));

		controlTestee.execute(0b1100_1001_0000);
		assertEquals(1 + 64, memoryTestee.readByte(testRegister));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));

		controlTestee.execute(0b1100_1001_0000);
		assertEquals(32, memoryTestee.readByte(testRegister));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
	}

	@Test
	void testSUBWF() {
		// save in register
		controlTestee.execute(0b10_1001_0000);
		assertEquals(valR - valW, memoryTestee.readByte(testRegister));

		// save in w
		setUp();
		controlTestee.execute(0b10_0001_0000);
		assertEquals(valR - valW, memoryTestee.readW());
	}

	@Test
	void testSWAPF() {
		// save in register
		controlTestee.execute(0b1110_1001_0000);
		assertEquals(128, memoryTestee.readByte(testRegister));

		// save in w
		controlTestee.execute(0b1110_0001_0000);
		assertEquals(8, memoryTestee.readW());
	}

	@Test
	void testXORWF() {
		// save in register
		controlTestee.execute(0b110_1001_0000);
		assertEquals(11, memoryTestee.readByte(testRegister));

		// save in w
		controlTestee.execute(0b110_0001_0000);
		assertEquals(8, memoryTestee.readW());
	}

	@Test
	void testBCF() {
		controlTestee.execute(0b1_0001_1001_0000);
		assertEquals(0, memoryTestee.readBit(testRegister, 3));
		assertEquals(0, memoryTestee.readByte(testRegister));
	}

	@Test
	void testBSF() {
		controlTestee.execute(0b1_0100_0001_0000);
		assertEquals(1, memoryTestee.readBit(testRegister, 0));
		assertEquals(9, memoryTestee.readByte(testRegister));
	}

	// TODO BTFSC, BTFSS

	@Test
	void testADDLW() {
		controlTestee.execute(0b11_1110_0000_0000 + valR);
		assertEquals(valW + valR, memoryTestee.readW());

		controlTestee.execute(0b11_1111_0000_0000 + valR);
		assertEquals(valW + valR + valR, memoryTestee.readW());
	}

	@Test
	void testANDLW() {
		controlTestee.execute(0b11_1001_0000_0000 + valR);
		assertEquals(valW & valR, memoryTestee.readW());
	}

	// TODO CALL, CLRWDT, GOTO

	@Test
	void testIORLW() {
		controlTestee.execute(0b11_1000_0000_0000 + valR);
		assertEquals(valW | valR, memoryTestee.readW());
	}

	@Test
	void testMOVLW() {
		controlTestee.execute(0b11_0000_0000_0000 + 1);
		assertEquals(1, memoryTestee.readW());

		controlTestee.execute(0b11_0001_0000_0000 + 2);
		assertEquals(2, memoryTestee.readW());

		controlTestee.execute(0b11_0010_0000_0000 + 3);
		assertEquals(3, memoryTestee.readW());

		controlTestee.execute(0b11_0011_0000_0000 + 4);
		assertEquals(4, memoryTestee.readW());
	}

	// TODO RETFIE, RETLW, RETURN, SLEEP

	@Test
	void testSUBLW() {
		controlTestee.execute(0b11_1100_0000_0000 + valR);
		assertEquals(valR - valW, memoryTestee.readW());

		setUp();
		controlTestee.execute(0b11_1100_0000_0000);
		memoryTestee.writeW(-1);
		assertEquals(255, memoryTestee.readW());
	}

	@Test
	void testXORLW() {
		controlTestee.execute(0b11_1010_0000_0000 + valR);
		assertEquals(valR ^ valW, memoryTestee.readW());
	}

	// Tests with C, DC and Z flag will later fail because of method visibility
	@Test
	void testZeroFlag() {
		assertEquals(0, memoryTestee.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()));
		controlTestee.updateZeroFlag(0);
		assertEquals(1, memoryTestee.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()));
		controlTestee.updateZeroFlag(5);
		assertEquals(0, memoryTestee.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()));
		controlTestee.updateZeroFlag(0);
		assertEquals(1, memoryTestee.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()));
	}

	@Test
	void testCarryFlag() {
		// add
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
		controlTestee.updateCarryFlag(256);
		assertEquals(1, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
		controlTestee.updateCarryFlag(200);
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
	}
}
