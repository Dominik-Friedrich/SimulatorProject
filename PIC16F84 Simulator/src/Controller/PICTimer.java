package Controller;

public class PICTimer {
	int preScaler = 2;
	int helpCounter = 0;
	int timer = 0;
	boolean overflow = false;

	int lastRA4Val = 0;
	
	public int incrementTimer() {
		helpCounter++;
		System.out.println("T: "+timer+"\tPS: "+preScaler+"\tHC: "+helpCounter);
		if (helpCounter >= preScaler) {
			timer++;
			
			// overflow check
			if (timer > 0xFF) {
				timer = 0;
				overflow = true;
			}
			
			helpCounter = 0;
		} 
		return timer;
	}
	
	public void clearOverflow() {
		overflow = false;
	}
	
	public boolean overflow() {
		return overflow;
	}

	public void registerUpdate(int newTimerVal) {
		timer = newTimerVal;
		helpCounter = 0;
	}

	public void setPreScaler(int optionRegister) {
		this.preScaler = (int) Math.pow(2, (optionRegister & 0b111) + 1);
	}

	public void externalTrigger(int newRA4Val, int sourceEdgeBit) {

		// check if rising or falling edge should trigger
		if (sourceEdgeBit == 1) {

			// falling edge
			if (lastRA4Val == 1 && newRA4Val == 0) {
				incrementTimer();
			}
		} else {

			// rising edge
			if (lastRA4Val == 0 && newRA4Val == 1) {
				incrementTimer();
			}
		}
		
		lastRA4Val = newRA4Val;
	}
}
