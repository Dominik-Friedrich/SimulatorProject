package Memory;

public enum MirroredRegister {
	//INTCON(0x0B), 
	PCLATH(0x0A), 
	//PORTB(0x06),
	//PORTA(0x05),
	FSR(0x04),
	STATUS(0x03),
	PCL(0x02),
	//TMR0(0x01),
	IND(0x00);
	
	private final int address;
	MirroredRegister(final int address) {
		this.address = address;
	}
	
	public int getAddress() {
		return address;
	}
}