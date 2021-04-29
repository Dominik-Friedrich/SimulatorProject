package Testing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Controller.ControllUnit;
import Memory.DataMemory;
import Memory.SpecialRegister;
import Memory.StackMemory;

public class LSTTest {
	private DataMemory memoryTestee;
	private ControllUnit controlTestee;
	private ArrayList<Integer> instructions;
	private StackMemory stackTestee;

	@BeforeEach
	public void setUp() {
		controlTestee = new ControllUnit(null);
		memoryTestee = controlTestee.getData();
		stackTestee = controlTestee.getStack();

		instructions = new ArrayList<Integer>();
	}
	
	@Test
	public void testLST1() {
		instructions.add(0x3011);
		instructions.add(0x3930);
		instructions.add(0x380D);
		instructions.add(0x3C3D);
		instructions.add(0x3A20);
		instructions.add(0x3E25);
		instructions.add(0x2806);

		controlTestee.newProgramm(instructions);

		controlTestee.run();
		assertEquals(0x11, memoryTestee.readW());

		controlTestee.run();
		assertEquals(0x10, memoryTestee.readW());

		controlTestee.run();
		assertEquals(0x1D, memoryTestee.readW());

		controlTestee.run();
		assertEquals(0x20, memoryTestee.readW());
		assertEquals(1, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit()));

		controlTestee.run();
		assertEquals(0x00, memoryTestee.readW());
		assertEquals(1, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit()));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()));

		controlTestee.run();
		assertEquals(0x25, memoryTestee.readW());
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit()));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()));
	}

	@Test
	public void testLST2() {
		instructions.add(0x3011);
		instructions.add(0x2006);
		instructions.add(0x0000);
		instructions.add(0x2008);
		instructions.add(0x0000);
		instructions.add(0x2800);
		instructions.add(0x3E25);
		instructions.add(0x0008);
		instructions.add(0x3477);
		instructions.add(0x2809);

		controlTestee.newProgramm(instructions);

		controlTestee.run();
		assertEquals(0x11, memoryTestee.readW());

		controlTestee.run();
		assertEquals(0x1, stackTestee.get(0));
		assertEquals(0x6, controlTestee.getProgrammCounter());

		controlTestee.run();
		assertEquals(0x36, memoryTestee.readW());

		controlTestee.run();
		assertEquals(0x2, controlTestee.getProgrammCounter());

		controlTestee.run();
		controlTestee.run();
		assertEquals(0x8, controlTestee.getProgrammCounter());

		controlTestee.run();
		controlTestee.run();
		assertEquals(0x77, memoryTestee.readW());
	}

	@Test
	public void testLST3() {
		int wert1 = 0x0C;
		int wert2 = 0x0D;
		
		instructions.add(0x3011);
		instructions.add(0x008c);
		instructions.add(0x3014);
		instructions.add(0x070C);
		instructions.add(0x078C);
		instructions.add(0x050C);
		instructions.add(0x008D);
		instructions.add(0x018C);
		instructions.add(0x090D);
		instructions.add(0x030C);
		instructions.add(0x0A8D);
		instructions.add(0x088C);
		instructions.add(0x048C);
		instructions.add(0x020D);
		instructions.add(0x0E8D);
		instructions.add(0x068C);
		instructions.add(0x0100);
		instructions.add(0x020C);
		instructions.add(0x020D);
		instructions.add(0x028D);
		instructions.add(0x028D);
		
		controlTestee.newProgramm(instructions);
		
		controlTestee.run();
		assertEquals(0x11, memoryTestee.readW());
		
		controlTestee.run();
		assertEquals(0x11, memoryTestee.readByte(wert1));
		
		controlTestee.run();
		assertEquals(0x14, memoryTestee.readW());
		
		controlTestee.run();
		assertEquals(0x25, memoryTestee.readW());
		
		controlTestee.run();
		assertEquals(0x25, memoryTestee.readW());
		assertEquals(0x36, memoryTestee.readByte(wert1));
		assertEquals(0x0, memoryTestee.readByte(wert2));
		
		controlTestee.run();
		assertEquals(0x24, memoryTestee.readW());
		assertEquals(0x36, memoryTestee.readByte(wert1));
		assertEquals(0x0, memoryTestee.readByte(wert2));
		
		controlTestee.run();
		assertEquals(0x24, memoryTestee.readW());
		assertEquals(0x36, memoryTestee.readByte(wert1));
		assertEquals(0x24, memoryTestee.readByte(wert2));
		
		controlTestee.run();
		assertEquals(0x24, memoryTestee.readW());
		assertEquals(0x0, memoryTestee.readByte(wert1));
		assertEquals(0x24, memoryTestee.readByte(wert2));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()));
		
		controlTestee.run();
		assertEquals(0xDB, memoryTestee.readW());
		assertEquals(0x0, memoryTestee.readByte(wert1));
		assertEquals(0x24, memoryTestee.readByte(wert2));
		
		controlTestee.run();
		assertEquals(0xFF, memoryTestee.readW());
		assertEquals(0x0, memoryTestee.readByte(wert1));
		assertEquals(0x24, memoryTestee.readByte(wert2));
		
		controlTestee.run();
		assertEquals(0xFF, memoryTestee.readW());
		assertEquals(0x0, memoryTestee.readByte(wert1));
		assertEquals(0x25, memoryTestee.readByte(wert2));
		
		controlTestee.run();
		assertEquals(0xFF, memoryTestee.readW());
		assertEquals(0x0, memoryTestee.readByte(wert1));
		assertEquals(0x25, memoryTestee.readByte(wert2));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()));
		
		controlTestee.run();
		assertEquals(0xFF, memoryTestee.readW());
		assertEquals(0xFF, memoryTestee.readByte(wert1));
		assertEquals(0x25, memoryTestee.readByte(wert2));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()));
		
		controlTestee.run();
		assertEquals(0x26, memoryTestee.readW());
		assertEquals(0xFF, memoryTestee.readByte(wert1));
		assertEquals(0x25, memoryTestee.readByte(wert2));
		
		controlTestee.run();
		assertEquals(0x26, memoryTestee.readW());
		assertEquals(0xFF, memoryTestee.readByte(wert1));
		assertEquals(0x52, memoryTestee.readByte(wert2));
		
		controlTestee.run();
		assertEquals(0x26, memoryTestee.readW());
		assertEquals(0xD9, memoryTestee.readByte(wert1));
		assertEquals(0x52, memoryTestee.readByte(wert2));
		
		controlTestee.run();
		assertEquals(0x0, memoryTestee.readW());
		assertEquals(0xD9, memoryTestee.readByte(wert1));
		assertEquals(0x52, memoryTestee.readByte(wert2));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.Z.getAddress(), SpecialRegister.Z.getBit()));
		
		controlTestee.run();
		assertEquals(0xD9, memoryTestee.readW());
		assertEquals(0xD9, memoryTestee.readByte(wert1));
		assertEquals(0x52, memoryTestee.readByte(wert2));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit()));
		
		controlTestee.run();
		assertEquals(0x79, memoryTestee.readW());
		assertEquals(0xD9, memoryTestee.readByte(wert1));
		assertEquals(0x52, memoryTestee.readByte(wert2));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit()));
		
		controlTestee.run();
		assertEquals(0x79, memoryTestee.readW());
		assertEquals(0xD9, memoryTestee.readByte(wert1));
		assertEquals(0xD9, memoryTestee.readByte(wert2));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
		assertEquals(0, memoryTestee.readBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit()));
		
		controlTestee.run();
		assertEquals(0x79, memoryTestee.readW());
		assertEquals(0xD9, memoryTestee.readByte(wert1));
		assertEquals(0x60, memoryTestee.readByte(wert2));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.C.getAddress(), SpecialRegister.C.getBit()));
		assertEquals(1, memoryTestee.readBit(SpecialRegister.DC.getAddress(), SpecialRegister.DC.getBit()));
	}

	@Test
	public void testLST4() {
	}

	@Test
	public void testLST5() {
	}

	@Test
	public void testLST6() {
	}

	@Test
	public void testLST7() {
	}

	@Test
	public void testLST8() {
	}

	@Test
	public void testLST9() {
	}

}
