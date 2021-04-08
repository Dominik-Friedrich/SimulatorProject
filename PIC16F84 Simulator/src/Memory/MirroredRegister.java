package Memory;

public enum MirroredRegister {
	INTCON(0x0B), 
	PCLATH(0x0A), 
	FSR(0x04),
	STATUS(0x03),
	PCL(0x02),
	INDF(0x00);
	
	private final int address;
	MirroredRegister(final int address) {
		this.address = address;
	}
	
	public int getAddress() {
		return address;
	}
}