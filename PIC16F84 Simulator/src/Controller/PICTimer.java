package Controller;

public class PICTimer {
	int preScaler = 2;
	int helpCounter = 0;
	int timer = 0;
	boolean overflow = false;

	int lastRA4Val = 0;

	/**
	 * increments timer
	 * 
	 * @return current timer value
	 */
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

	/**
	 * clears the overflow flag
	 */
	public void clearOverflow() {
		overflow = false;
	}

	/**
	 * 
	 * @return true if overflow flag is set
	 */
	public boolean overflow() {
		return overflow;
	}

	/**
	 * Sets the timer to a specified value
	 * 
	 * @param newTimerVal new value of the timer
	 */
	public void registerUpdate(int newTimerVal) {
		timer = newTimerVal;
		helpCounter = 0;
	}

	/**
	 * Sets the prescaler of the timer to a specified value
	 * 
	 * @param optionRegister new value of the prescaler
	 */
	public void setPreScaler(int optionRegister) {
		if ((optionRegister & 0b1000) > 0) {
			this.preScaler = 1;
		} else {
			this.preScaler = (int) Math.pow(2, (optionRegister & 0b111) + 1);
		}
	}

	/**
	 * Used when timer should act on an external signal
	 * 
	 * @param newRA4Val     Value of the current input. > 0 for high or 0 for low
	 *                      signal
	 * @param sourceEdgeBit Specifies on which edge the timer should count. Either >
	 *                      0 for rising or <= 0 for falling edge
	 * @return value of timer
	 */
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
