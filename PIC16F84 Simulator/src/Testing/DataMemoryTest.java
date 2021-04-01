package Testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;

import Memory.DataMemory;
import Memory.MirroredRegister;

public class DataMemoryTest {
	private DataMemory memoryTestee;
	
	@Before
	public void setUp() {
		memoryTestee = new DataMemory();
	}
	
	@Test
	public void testBitOperations() {
		memoryTestee.setBit(10, 4);
		assertEquals(1, memoryTestee.readBit(10, 4));
		memoryTestee.clearBit(10, 4);
		assertEquals(0, memoryTestee.readBit(10, 4));
	}
	
	@Test
	public void testByteOperations() {
		memoryTestee.writeByte(0x50, 0xF9);
		assertEquals(0xF9, memoryTestee.readByte(0x50));
	}

	
	@Test
	public void testMirrored() {
		memoryTestee.writeByte(MirroredRegister.FSR.getAddress(), 0xFF);
		assertEquals(0, memoryTestee.readBit(MirroredRegister.STATUS.getAddress(), 5));
		memoryTestee.setBit(MirroredRegister.STATUS.getAddress(), 5);
		assertEquals(1, memoryTestee.readBit(MirroredRegister.STATUS.getAddress(), 5));
		assertEquals(0xFF, memoryTestee.readByte(MirroredRegister.FSR.getAddress()));
	}
}
