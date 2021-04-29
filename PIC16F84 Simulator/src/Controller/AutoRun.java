package Controller;

public class AutoRun extends Thread {
	private ControllUnit controller;
	
	public AutoRun(ControllUnit controller) {
		this.controller = controller;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				controller.run();
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
