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
	/**
	 * Constantly runs the next command until it hits a breakpoint
	 */
	public void run() {
		while (true) {
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

	/**
	 * Adds a breakpoint
	 * 
	 * @param breakpoint to add
	 * @param index      of the breakpoint in the JList
	 */
	public static void addBreakpoint(int breakpoint, int index) {
		if (!breakpoints.contains(breakpoint)) {
			breakpoints.add(breakpoint);
			indexOfBP.add(index);
		}
	}

	/**
	 * removes the breakpoint
	 * 
	 * @param breakpoint to remove
	 */
	public static void removeBreakpoint(int breakpoint) {
		int tempIndex = breakpoints.indexOf(breakpoint);
		if (tempIndex >= 0) {
			breakpoints.remove(tempIndex);
			indexOfBP.remove(tempIndex);
		}
	}

	/**
	 * 
	 * @return true if not autorunning
	 */
	public static boolean isStopped() {
		return stopped;
	}

	/**
	 * Sets current status true if autorun should stop
	 * 
	 * @param stopped
	 */
	public static void setStopped(boolean stopped) {
		AutoRun.stopped = stopped;
	}
}
