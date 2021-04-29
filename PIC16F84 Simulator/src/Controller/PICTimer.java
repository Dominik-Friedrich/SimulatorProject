package Controller;

public class PICTimer {
	int preScaler = 2;
	int helpCounter = 0;
	int timer = 0;
	boolean overflow = false;

	int lastRA4Val = 0;

	public int incrementTimer() {
		helpCounter++;

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
		if ((optionRegister & 0b1000) > 0) {
			this.preScaler = 1;
		} else {
			this.preScaler = (int) Math.pow(2, (optionRegister & 0b111) + 1);
		}
	}

	public int externalTrigger(int newRA4Val, int sourceEdgeBit) {
		// check if rising or falling edge should trigger
		if (sourceEdgeBit > 0) {
			// falling edge
			if (lastRA4Val > 0 && newRA4Val == 0) {
				incrementTimer();
			}
		} else {
			// rising edge
			if (lastRA4Val == 0 && newRA4Val > 0) {
				incrementTimer();
			}
		}

		lastRA4Val = newRA4Val;
		return timer;
	}
}
