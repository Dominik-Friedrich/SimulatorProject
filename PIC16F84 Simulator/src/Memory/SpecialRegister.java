package Memory;

public enum SpecialRegister {

	// STATUS
	IRP(0x03, 7),
	RP1(0x03, 6),
	RP0(0x03, 5),
	TO(0x03, 4), // inverted
	PD(0x03, 3), // inverted
	Z(0x03, 2),
	DC(0x03, 1),
	C(0x03, 0);
	
	// TODO other special registers

	private final int address;

	SpecialRegister(final int address, final int bit) {
		this.address = address;
	}

	public int getAddress() {
		return address;
	}
}
