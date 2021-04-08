package Memory;

public enum SpecialRegister {

	
	// Registers with the same value are either synonyms of each other or the counterpart on the other bank
	
	INDF(0x00, 0),
	
	// OPTION / TMR0
	RBPU(0x01, 7),	// inverted
	INTEDG(0x01, 6),
	T0CS(0x01, 5),
	T0SE(0x01, 4),
	PSA(0x01, 3),
	PS2(0x01, 2),
	PS1(0x01, 1),
	PS0(0x01, 0), OPTION(0x01, 0), TMR0(0x01, 0),
	
	
	PCL(0x02, 0),
	
	// STATUS
	IRP(0x03, 7),
	RP1(0x03, 6),
	RP0(0x03, 5),
	TO(0x03, 4), // inverted
	PD(0x03, 3), // inverted
	Z(0x03, 2),
	DC(0x03, 1),
	C(0x03, 0), STATUS(0x03, 0),
	
	FSR(0x04, 0),
	
	// PORTA / TRISA
	RA4(0x05, 4), T0CKI(0x05, 4),
	RA3(0x05, 3),
	RA2(0x05, 2),
	RA1(0x05, 1),
	RA0(0x05, 0), TRISA(0x5, 0),
	
	// PORTB / TRISB
	RB7(0x06, 7),
	RB6(0x06, 6),
	RB5(0x06, 5),
	RB4(0x06, 4), 
	RB3(0x06, 3),
	RB2(0x06, 2),
	RB1(0x06, 1),
	RB0(0x06, 0), INT(0x06, 0), TRISB(0x06, 0),
	
	// EEDATA / EECON1 
	EEIF(0x08, 4),
	WRERR(0x08, 3),
	WREN(0x08, 2),
	WR(0x08, 1),
	RD(0x08, 0), EEDATA(0x08, 0), EECON1(0x08, 0),

	// EEADR / EECON2
	EEADR(0x09, 0), EECON2(0x09, 0),
	
	// PCLATH
	PCLATH4(0x0A, 4),
	PCLATH3(0x0A, 3),
	PCLATH2(0x0A, 2),
	PCLATH1(0x0A, 1),
	PCLATH0(0x0A, 0),
	
	// INTCON
	GIE(0x0B, 7),
	EEIE(0x0B, 6),
	T0IE(0x0B, 5),
	INTE(0x0B, 4),
	RBIE(0x0B, 3),
	T0IF(0x0B, 2),
	INTF(0x0B, 1),
	RBIF(0x0B, 0), INTCON(0x0B, 0);

	private final int address;
	private final int bit;

	SpecialRegister(final int address, final int bit) {
		this.address = address;
		this.bit = bit;
	}

	public int getAddress() {
		return address;
	}
	
	public int getBit() {
		return bit;
	}
}
