package Controller;

import java.util.ArrayList;

import GUI.Window;

public class AutoRun extends Thread {
	private ControllUnit controller;
	private static Window gui;
	
	private static ArrayList<Integer> breakpoints = new ArrayList<Integer>();
	private static ArrayList<Integer> indexOfBP = new ArrayList<Integer>();
	
	private static boolean stopped = true;
	
	public AutoRun(ControllUnit controller) {
		this.controller = controller;
	}
	
	@Override
	public void run() {
		while(true) {
			if (breakpoints.contains(controller.getProgrammCounter())) {
				gui.removeBreakpointVisual(indexOfBP.get(breakpoints.indexOf(controller.getProgrammCounter())));
				stopped = true;
				break;
			}
			
			try {
				controller.run();
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void attachGUI(Window tempgui) {
		gui = tempgui;
	}
	
	public static void addBreakpoint(int breakpoint, int index) {
		if (!breakpoints.contains(breakpoint)) {
			breakpoints.add(breakpoint);
			indexOfBP.add(index);
		}
	}
	
	public static void removeBreakpoint(int breakpoint) {
		int tempIndex = breakpoints.indexOf(breakpoint);
		if (tempIndex >= 0) {
			breakpoints.remove(tempIndex);
			indexOfBP.remove(tempIndex);
		}
	}
	
	public static boolean isStopped() {
		return stopped;
	}
	
	public static void setStopped(boolean stopped) {
		AutoRun.stopped = stopped;
	}
}
