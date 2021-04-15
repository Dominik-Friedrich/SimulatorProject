package Controller;

public class PICTimer {
	int preScaler = 2;
	int timer = 0;
	
	int extTriggerCounter = 0;
	int lastRA4Val = 0;
	
	public int updateTimer(int cycleCount) {
		timer = cycleCount / preScaler;
		return timer;
	}

	public void setPreScaler(int optionRegister) {
		this.preScaler = (int) Math.pow(2, (optionRegister & 0x3) + 1);
	}

	public int externalTrigger(int newRA4Val, int sourceEdgeBit) {
		if (sourceEdgeBit == 1) {
			if (lastRA4Val == 1 && newRA4Val == 0) {
				extTriggerCounter++;
			}
		} else {
			if (lastRA4Val == 0 && newRA4Val == 1) {
				extTriggerCounter++;
			}
		}	
		return updateTimer(extTriggerCounter);
	}
}
