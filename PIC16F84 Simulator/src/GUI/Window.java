package GUI;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuItem;
import javax.swing.border.TitledBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;

import org.junit.jupiter.params.provider.EmptySource;

import Controller.AutoRun;
import Controller.ControllUnit;
import Memory.DataMemory;
import Memory.ProgrammMemory;
import Memory.SpecialRegister;
import Memory.StackMemory;
import Read.ReadLST;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Window extends JFrame implements ActionListener {
	private ControllUnit controller;
	private ReadLST lstReader;
	private AutoRun autorunner;
	private boolean autorun = false;

	private JTable table;
	private JLabel sfrAndWVal[] = new JLabel[9];
	private JLabel sfrBit[] = new JLabel[24];
	private JLabel stack[] = new JLabel[8];
	private JCheckBox trisA[] = new JCheckBox[8];
	private JCheckBox trisB[] = new JCheckBox[8];
	private JCheckBox pinA[] = new JCheckBox[5];
	private JCheckBox pinB[] = new JCheckBox[8];
	private JPanel programmSourceCode;
	private JScrollPane scrollPane_1;
	private JList<Object> speicher;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window frame = new Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the frame.
	 */
	public Window() {
		controller = new ControllUnit(this);

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.out.println("Error setting the LAF..." + e);
		}

		setTitle("PIC18F84 Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1275, 726);
		setResizable(false);
		getContentPane().setLayout(null);
		getContentPane().setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1259, 22);
		getContentPane().add(menuBar);

		JButton file = new JButton("Datei"); // Action Listener Notwendig um File Explorer zu öffnen
		menuBar.add(file);

		JPanel specialFunctionRegisterAndW = new JPanel();
		specialFunctionRegisterAndW
				.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)), "SFR + W",
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
		specialFunctionRegisterAndW.setToolTipText("");
		specialFunctionRegisterAndW.setBounds(10, 33, 329, 160);
		getContentPane().add(specialFunctionRegisterAndW);
		specialFunctionRegisterAndW.setLayout(null);

		JLabel lblwRegister = new JLabel("W-Register:");
		lblwRegister.setBounds(10, 25, 64, 14);
		specialFunctionRegisterAndW.add(lblwRegister);

		JLabel lblPcl = new JLabel("PCL:");
		lblPcl.setBounds(10, 47, 64, 14);
		specialFunctionRegisterAndW.add(lblPcl);

		JLabel lblPclath = new JLabel("PCLATH:");
		lblPclath.setBounds(10, 69, 64, 14);
		specialFunctionRegisterAndW.add(lblPclath);

		JLabel lblPcIntern = new JLabel("PC intern:");
		lblPcIntern.setBounds(10, 91, 64, 14);
		specialFunctionRegisterAndW.add(lblPcIntern);

		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(10, 113, 64, 14);
		specialFunctionRegisterAndW.add(lblStatus);

		JLabel lblFsr = new JLabel("FSR:");
		lblFsr.setBounds(10, 135, 64, 14);
		specialFunctionRegisterAndW.add(lblFsr);

		sfrAndWVal[0] = new JLabel("00");
		sfrAndWVal[0].setBounds(90, 25, 25, 14);
		specialFunctionRegisterAndW.add(sfrAndWVal[0]);

		sfrAndWVal[1] = new JLabel("00");
		sfrAndWVal[1].setBounds(90, 47, 25, 14);
		specialFunctionRegisterAndW.add(sfrAndWVal[1]);

		sfrAndWVal[2] = new JLabel("00");
		sfrAndWVal[2].setBounds(90, 69, 25, 14);
		specialFunctionRegisterAndW.add(sfrAndWVal[2]);

		sfrAndWVal[3] = new JLabel("00");
		sfrAndWVal[3].setBounds(90, 91, 25, 14);
		specialFunctionRegisterAndW.add(sfrAndWVal[3]);

		sfrAndWVal[4] = new JLabel("00");
		sfrAndWVal[4].setBounds(90, 113, 25, 14);
		specialFunctionRegisterAndW.add(sfrAndWVal[4]);

		sfrAndWVal[5] = new JLabel("00");
		sfrAndWVal[5].setBounds(90, 135, 25, 14);
		specialFunctionRegisterAndW.add(sfrAndWVal[5]);

		JLabel lblOption = new JLabel("Option:");
		lblOption.setBounds(163, 25, 64, 14);
		specialFunctionRegisterAndW.add(lblOption);

		JLabel lblVorteiler = new JLabel("Vorteiler:");
		lblVorteiler.setBounds(163, 47, 64, 14);
		specialFunctionRegisterAndW.add(lblVorteiler);

		JLabel lblTimer = new JLabel("Timer0:");
		lblTimer.setBounds(163, 69, 64, 14);
		specialFunctionRegisterAndW.add(lblTimer);

		sfrAndWVal[6] = new JLabel("00");
		sfrAndWVal[6].setBounds(237, 25, 25, 14);
		specialFunctionRegisterAndW.add(sfrAndWVal[6]);

		sfrAndWVal[7] = new JLabel("00");
		sfrAndWVal[7].setBounds(237, 47, 25, 14);
		specialFunctionRegisterAndW.add(sfrAndWVal[7]);

		sfrAndWVal[8] = new JLabel("00");
		sfrAndWVal[8].setBounds(237, 69, 25, 14);
		specialFunctionRegisterAndW.add(sfrAndWVal[8]);

		JPanel specialFunctionRegisterInBits = new JPanel();
		specialFunctionRegisterInBits.setLayout(null);
		specialFunctionRegisterInBits.setToolTipText("");
		specialFunctionRegisterInBits
				.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)), "SFR (Bit)",
						TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		specialFunctionRegisterInBits.setBounds(349, 33, 310, 160);
		getContentPane().add(specialFunctionRegisterInBits);

		JLabel lblStatus_1 = new JLabel("Status:");
		lblStatus_1.setBounds(10, 25, 45, 14);
		specialFunctionRegisterInBits.add(lblStatus_1);

		JLabel lblOption_1 = new JLabel("Option:");
		lblOption_1.setBounds(10, 69, 45, 14);
		specialFunctionRegisterInBits.add(lblOption_1);

		JLabel lblOption_1_1 = new JLabel("Intcon:");
		lblOption_1_1.setBounds(10, 113, 45, 14);
		specialFunctionRegisterInBits.add(lblOption_1_1);

		JLabel lblIrp = new JLabel("IRP");
		lblIrp.setBounds(65, 25, 20, 14);
		specialFunctionRegisterInBits.add(lblIrp);

		JLabel lblRp1 = new JLabel("RP1");
		lblRp1.setBounds(95, 25, 20, 14);
		specialFunctionRegisterInBits.add(lblRp1);

		JLabel lblRp0 = new JLabel("RP0");
		lblRp0.setBounds(125, 25, 20, 14);
		specialFunctionRegisterInBits.add(lblRp0);

		JLabel lblTo = new JLabel("TO");
		lblTo.setBounds(155, 25, 20, 14);
		specialFunctionRegisterInBits.add(lblTo);

		JLabel lblPd = new JLabel("PD");
		lblPd.setBounds(185, 25, 20, 14);
		specialFunctionRegisterInBits.add(lblPd);

		JLabel lblZ = new JLabel("Z");
		lblZ.setBounds(215, 25, 20, 14);
		specialFunctionRegisterInBits.add(lblZ);

		JLabel lblDc = new JLabel("DC");
		lblDc.setBounds(245, 25, 20, 14);
		specialFunctionRegisterInBits.add(lblDc);

		JLabel lblC = new JLabel("C");
		lblC.setBounds(275, 25, 20, 14);
		specialFunctionRegisterInBits.add(lblC);

		sfrBit[0] = new JLabel("0");
		sfrBit[0].setBounds(65, 47, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[0]);

		sfrBit[1] = new JLabel("0");
		sfrBit[1].setBounds(95, 47, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[1]);

		sfrBit[2] = new JLabel("0");
		sfrBit[2].setBounds(125, 47, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[2]);

		sfrBit[3] = new JLabel("0");
		sfrBit[3].setBounds(155, 47, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[3]);

		sfrBit[4] = new JLabel("0");
		sfrBit[4].setBounds(185, 47, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[4]);

		sfrBit[5] = new JLabel("0");
		sfrBit[5].setBounds(215, 47, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[5]);

		sfrBit[6] = new JLabel("0");
		sfrBit[6].setBounds(245, 47, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[6]);

		sfrBit[7] = new JLabel("0");
		sfrBit[7].setBounds(275, 47, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[7]);

		JLabel lblRpu = new JLabel("RPu");
		lblRpu.setBounds(65, 69, 20, 14);
		specialFunctionRegisterInBits.add(lblRpu);

		JLabel lblIeg = new JLabel("IEg");
		lblIeg.setBounds(95, 69, 20, 14);
		specialFunctionRegisterInBits.add(lblIeg);

		JLabel lblRp_1_2 = new JLabel("TCs");
		lblRp_1_2.setBounds(125, 69, 20, 14);
		specialFunctionRegisterInBits.add(lblRp_1_2);

		JLabel lblTse = new JLabel("TSe");
		lblTse.setBounds(155, 69, 20, 14);
		specialFunctionRegisterInBits.add(lblTse);

		JLabel lblPsa = new JLabel("PSA");
		lblPsa.setBounds(185, 69, 20, 14);
		specialFunctionRegisterInBits.add(lblPsa);

		JLabel lblPs2 = new JLabel("PS2");
		lblPs2.setBounds(215, 69, 20, 14);
		specialFunctionRegisterInBits.add(lblPs2);

		JLabel lblPs1 = new JLabel("PS1");
		lblPs1.setBounds(245, 69, 20, 14);
		specialFunctionRegisterInBits.add(lblPs1);

		JLabel lblPs0 = new JLabel("PS0");
		lblPs0.setBounds(275, 69, 20, 14);
		specialFunctionRegisterInBits.add(lblPs0);

		sfrBit[8] = new JLabel("0");
		sfrBit[8].setBounds(65, 91, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[8]);

		sfrBit[9] = new JLabel("0");
		sfrBit[9].setBounds(95, 91, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[9]);

		sfrBit[10] = new JLabel("0");
		sfrBit[10].setBounds(125, 91, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[10]);

		sfrBit[11] = new JLabel("0");
		sfrBit[11].setBounds(155, 91, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[11]);

		sfrBit[12] = new JLabel("0");
		sfrBit[12].setBounds(185, 91, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[12]);

		sfrBit[13] = new JLabel("0");
		sfrBit[13].setBounds(215, 91, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[13]);

		sfrBit[14] = new JLabel("0");
		sfrBit[14].setBounds(245, 91, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[14]);

		sfrBit[15] = new JLabel("0");
		sfrBit[15].setBounds(275, 91, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[15]);

		JLabel lblGie = new JLabel("GIE");
		lblGie.setBounds(65, 113, 20, 14);
		specialFunctionRegisterInBits.add(lblGie);

		JLabel lblEie = new JLabel("EIE");
		lblEie.setBounds(95, 113, 20, 14);
		specialFunctionRegisterInBits.add(lblEie);

		JLabel lblTie = new JLabel("TIE");
		lblTie.setBounds(125, 113, 20, 14);
		specialFunctionRegisterInBits.add(lblTie);

		JLabel lblIe = new JLabel("IE");
		lblIe.setBounds(155, 113, 20, 14);
		specialFunctionRegisterInBits.add(lblIe);

		JLabel lblRie = new JLabel("RIE");
		lblRie.setBounds(185, 113, 20, 14);
		specialFunctionRegisterInBits.add(lblRie);

		JLabel lblTif = new JLabel("TIF");
		lblTif.setBounds(215, 113, 20, 14);
		specialFunctionRegisterInBits.add(lblTif);

		JLabel lblIf = new JLabel("IF");
		lblIf.setBounds(245, 113, 20, 14);
		specialFunctionRegisterInBits.add(lblIf);

		JLabel lblRif = new JLabel("RIF");
		lblRif.setBounds(275, 113, 20, 14);
		specialFunctionRegisterInBits.add(lblRif);

		sfrBit[16] = new JLabel("0");
		sfrBit[16].setBounds(65, 135, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[16]);

		sfrBit[17] = new JLabel("0");
		sfrBit[17].setBounds(95, 135, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[17]);

		sfrBit[18] = new JLabel("0");
		sfrBit[18].setBounds(125, 135, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[18]);

		sfrBit[19] = new JLabel("0");
		sfrBit[19].setBounds(155, 135, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[19]);

		sfrBit[20] = new JLabel("0");
		sfrBit[20].setBounds(185, 135, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[20]);

		sfrBit[21] = new JLabel("0");
		sfrBit[21].setBounds(215, 135, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[21]);

		sfrBit[22] = new JLabel("0");
		sfrBit[22].setBounds(245, 135, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[22]);

		sfrBit[23] = new JLabel("0");
		sfrBit[23].setBounds(275, 135, 20, 14);
		specialFunctionRegisterInBits.add(sfrBit[23]);

		JPanel stackRegister = new JPanel();
		stackRegister.setLayout(null);
		stackRegister.setToolTipText("");
		stackRegister.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)), "Stack",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		stackRegister.setBounds(669, 33, 100, 160);
		getContentPane().add(stackRegister);

		stack[0] = new JLabel("0000");
		stack[0].setBounds(33, 25, 25, 14);
		stackRegister.add(stack[0]);

		stack[1] = new JLabel("0000");
		stack[1].setBounds(33, 40, 25, 14);
		stackRegister.add(stack[1]);

		stack[2] = new JLabel("0000");
		stack[2].setBounds(33, 70, 25, 14);
		stackRegister.add(stack[2]);

		stack[3] = new JLabel("0000");
		stack[3].setBounds(33, 55, 25, 14);
		stackRegister.add(stack[3]);

		stack[4] = new JLabel("0000");
		stack[4].setBounds(33, 85, 25, 14);
		stackRegister.add(stack[4]);

		stack[5] = new JLabel("0000");
		stack[5].setBounds(33, 100, 25, 14);
		stackRegister.add(stack[5]);

		stack[6] = new JLabel("0000");
		stack[6].setBounds(33, 130, 25, 14);
		stackRegister.add(stack[6]);

		stack[7] = new JLabel("0000");
		stack[7].setBounds(33, 115, 25, 14);
		stackRegister.add(stack[7]);

		JPanel portARegister = new JPanel();
		portARegister.setLayout(null);
		portARegister.setToolTipText("");
		portARegister.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)),
				"Port A", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		portARegister.setBounds(779, 33, 470, 77);
		getContentPane().add(portARegister);

		JLabel lblStatus_1_1 = new JLabel("Tris");
		lblStatus_1_1.setBounds(10, 25, 45, 14);
		portARegister.add(lblStatus_1_1);

		trisA[0] = new JCheckBox("7");
		trisA[0].setEnabled(false);
		trisA[0].setBounds(60, 20, 38, 23);
		portARegister.add(trisA[0]);

		trisA[1] = new JCheckBox("6");
		trisA[1].setEnabled(false);
		trisA[1].setBounds(110, 20, 38, 23);
		portARegister.add(trisA[1]);

		trisA[2] = new JCheckBox("5");
		trisA[2].setEnabled(false);
		trisA[2].setBounds(160, 20, 38, 23);
		portARegister.add(trisA[2]);

		trisA[3] = new JCheckBox("4");
		trisA[3].setEnabled(false);
		trisA[3].setBounds(210, 20, 38, 23);
		portARegister.add(trisA[3]);

		trisA[4] = new JCheckBox("3");
		trisA[4].setEnabled(false);
		trisA[4].setBounds(260, 20, 38, 23);
		portARegister.add(trisA[4]);

		trisA[5] = new JCheckBox("2");
		trisA[5].setEnabled(false);
		trisA[5].setBounds(310, 20, 38, 23);
		portARegister.add(trisA[5]);

		trisA[6] = new JCheckBox("1");
		trisA[6].setEnabled(false);
		trisA[6].setBounds(360, 20, 38, 23);
		portARegister.add(trisA[6]);

		trisA[7] = new JCheckBox("0");
		trisA[7].setEnabled(false);
		trisA[7].setBounds(410, 20, 38, 23);
		portARegister.add(trisA[7]);

		JLabel lblStatus_1_1_1 = new JLabel("Pin");
		lblStatus_1_1_1.setBounds(10, 52, 45, 14);
		portARegister.add(lblStatus_1_1_1);

		pinA[0] = new JCheckBox("4");
		pinA[0].setBounds(210, 47, 38, 23);
		pinA[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinA[0].isSelected(), 4, 'A');
				controller.externalTimerTrigger();
			}
		});
		portARegister.add(pinA[0]);

		pinA[1] = new JCheckBox("3");
		pinA[1].setBounds(260, 47, 38, 23);
		pinA[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinA[1].isSelected(), 3, 'A');
			}
		});
		portARegister.add(pinA[1]);

		pinA[2] = new JCheckBox("2");
		pinA[2].setBounds(310, 47, 38, 23);
		pinA[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinA[2].isSelected(), 2, 'A');
			}
		});
		portARegister.add(pinA[2]);

		pinA[3] = new JCheckBox("1");
		pinA[3].setBounds(360, 47, 38, 23);
		pinA[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinA[3].isSelected(), 1, 'A');
			}
		});
		portARegister.add(pinA[3]);

		pinA[4] = new JCheckBox("0");
		pinA[4].setBounds(410, 47, 38, 23);
		pinA[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinA[4].isSelected(), 0, 'A');
			}
		});
		portARegister.add(pinA[4]);

		JPanel portBRegister = new JPanel();
		portBRegister.setLayout(null);
		portBRegister.setToolTipText("");
		portBRegister.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)),
				"Port B", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		portBRegister.setBounds(779, 116, 470, 77);
		getContentPane().add(portBRegister);

		JLabel lblStatus_1_1_2 = new JLabel("Tris");
		lblStatus_1_1_2.setBounds(10, 25, 45, 14);
		portBRegister.add(lblStatus_1_1_2);

		trisB[0] = new JCheckBox("7");
		trisB[0].setEnabled(false);
		trisB[0].setBounds(60, 20, 38, 23);
		portBRegister.add(trisB[0]);

		trisB[1] = new JCheckBox("6");
		trisB[1].setEnabled(false);
		trisB[1].setBounds(110, 20, 38, 23);
		portBRegister.add(trisB[1]);

		trisB[2] = new JCheckBox("5");
		trisB[2].setEnabled(false);
		trisB[2].setBounds(160, 20, 38, 23);
		portBRegister.add(trisB[2]);

		trisB[3] = new JCheckBox("4");
		trisB[3].setEnabled(false);
		trisB[3].setBounds(210, 20, 38, 23);
		portBRegister.add(trisB[3]);

		trisB[4] = new JCheckBox("3");
		trisB[4].setEnabled(false);
		trisB[4].setBounds(260, 20, 38, 23);
		portBRegister.add(trisB[4]);

		trisB[5] = new JCheckBox("2");
		trisB[5].setEnabled(false);
		trisB[5].setBounds(310, 20, 38, 23);
		portBRegister.add(trisB[5]);

		trisB[6] = new JCheckBox("1");
		trisB[6].setEnabled(false);
		trisB[6].setBounds(360, 20, 38, 23);
		portBRegister.add(trisB[6]);

		trisB[7] = new JCheckBox("0");
		trisB[7].setEnabled(false);
		trisB[7].setBounds(410, 20, 38, 23);
		portBRegister.add(trisB[7]);

		JLabel lblPin = new JLabel("Pin");
		lblPin.setBounds(10, 52, 45, 14);
		portBRegister.add(lblPin);

		pinB[0] = new JCheckBox("7");
		pinB[0].setBounds(60, 48, 38, 23);
		pinB[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinB[0].isSelected(), 7, 'B');
			}
		});
		portBRegister.add(pinB[0]);

		pinB[1] = new JCheckBox("6");
		pinB[1].setBounds(110, 48, 38, 23);
		pinB[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinB[1].isSelected(), 6, 'B');
			}
		});
		portBRegister.add(pinB[1]);

		pinB[2] = new JCheckBox("5");
		pinB[2].setBounds(160, 48, 38, 23);
		pinB[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinB[2].isSelected(), 5, 'B');
			}
		});
		portBRegister.add(pinB[2]);

		pinB[3] = new JCheckBox("4");
		pinB[3].setBounds(210, 47, 38, 23);
		pinB[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinB[3].isSelected(), 4, 'B');
			}
		});
		portBRegister.add(pinB[3]);

		pinB[4] = new JCheckBox("3");
		pinB[4].setBounds(260, 47, 38, 23);
		pinB[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinB[4].isSelected(), 3, 'B');
			}
		});
		portBRegister.add(pinB[4]);

		pinB[5] = new JCheckBox("2");
		pinB[5].setBounds(310, 47, 38, 23);
		pinB[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinB[5].isSelected(), 2, 'B');
			}
		});
		portBRegister.add(pinB[5]);

		pinB[6] = new JCheckBox("1");
		pinB[6].setBounds(360, 47, 38, 23);
		pinB[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinB[6].isSelected(), 1, 'B');
			}
		});
		portBRegister.add(pinB[6]);

		pinB[7] = new JCheckBox("0");
		pinB[7].setBounds(410, 47, 38, 23);
		pinB[7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.pinUpdate(pinB[7].isSelected(), 0, 'B');
			}
		});
		portBRegister.add(pinB[7]);

		programmSourceCode = new JPanel();
		programmSourceCode.setToolTipText("");
		programmSourceCode.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)),
				"Programm (LST-Datei)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		programmSourceCode.setBounds(10, 204, 759, 438);
		getContentPane().add(programmSourceCode);
		programmSourceCode.setLayout(new BorderLayout(0, 0));

		scrollPane_1 = new JScrollPane();
		programmSourceCode.add(scrollPane_1, BorderLayout.CENTER);

		JPanel fileregister = new JPanel();
		fileregister.setToolTipText("");
		fileregister.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)),
				"Fileregister (SFR + GPR)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		fileregister.setBounds(779, 204, 470, 288);
		getContentPane().add(fileregister);

		table = new JTable();
		table.setRowSelectionAllowed(false);
		table.setFillsViewportHeight(true);
		table.setCellSelectionEnabled(true);
		table.setModel(new DefaultTableModel(
				new String[][] { { " 00", null, null, null, null, null, null, null, null },
						{ " 08", null, null, null, null, null, null, null, null },
						{ " 10", null, null, null, null, null, null, null, null },
						{ " 18", null, null, null, null, null, null, null, null },
						{ " 20", null, null, null, null, null, null, null, null },
						{ " 28", null, null, null, null, null, null, null, null },
						{ " 30", null, null, null, null, null, null, null, null },
						{ " 38", null, null, null, null, null, null, null, null },
						{ " 40", null, null, null, null, null, null, null, null },
						{ " 48", null, null, null, null, null, null, null, null },
						{ " 50", null, null, null, null, null, null, null, null },
						{ " 58", null, null, null, null, null, null, null, null },
						{ " 60", null, null, null, null, null, null, null, null },
						{ " 68", null, null, null, null, null, null, null, null },
						{ " 70", null, null, null, null, null, null, null, null },
						{ " 78", null, null, null, null, null, null, null, null },
						{ " 80", null, null, null, null, null, null, null, null },
						{ " 88", null, null, null, null, null, null, null, null },
						{ " 90", null, null, null, null, null, null, null, null },
						{ " 98", null, null, null, null, null, null, null, null },
						{ " A0", null, null, null, null, null, null, null, null },
						{ " A8", null, null, null, null, null, null, null, null },
						{ " B0", null, null, null, null, null, null, null, null },
						{ " B8", null, null, null, null, null, null, null, null },
						{ " C0", null, null, null, null, null, null, null, null },
						{ " C8", null, null, null, null, null, null, null, null },
						{ " D0", null, null, null, null, null, null, null, null },
						{ " D8", null, null, null, null, null, null, null, null },
						{ " E0", null, null, null, null, null, null, null, null },
						{ " E8", null, null, null, null, null, null, null, null },
						{ " F0", null, null, null, null, null, null, null, null },
						{ "F8", null, null, null, null, null, null, null, null }, },
				new String[] { "0x", "+0", "+1", "+2", "+3", "+4", "+5", "+6", "+7" }) {
			Class[] columnTypes = new Class[] { Object.class, Integer.class, Integer.class, Integer.class,
					Integer.class, Integer.class, Integer.class, Integer.class, Integer.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.getColumnModel().getColumn(4).setResizable(false);
		table.getColumnModel().getColumn(5).setResizable(false);
		table.getColumnModel().getColumn(6).setResizable(false);
		table.getColumnModel().getColumn(7).setResizable(false);
		table.getColumnModel().getColumn(8).setResizable(false);
		fileregister.setLayout(new BorderLayout(0, 0));
		table.getTableHeader().setReorderingAllowed(false);
		// fileregister.add(table);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		fileregister.add(scrollPane);

		JPanel timing = new JPanel();
		timing.setLayout(null);
		timing.setToolTipText("");
		timing.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)), "Timing",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		timing.setBounds(779, 503, 167, 139);
		getContentPane().add(timing);

		JLabel lblNewLabel_5 = new JLabel("Laufzeit:");
		lblNewLabel_5.setBounds(10, 22, 46, 14);
		timing.add(lblNewLabel_5);

		JLabel lblNewLabel_5_1 = new JLabel("Quarz:");
		lblNewLabel_5_1.setBounds(10, 47, 46, 14);
		timing.add(lblNewLabel_5_1);

		JLabel lblNewLabel_5_1_1 = new JLabel("Watchdog:");
		lblNewLabel_5_1_1.setBounds(10, 102, 67, 14);
		timing.add(lblNewLabel_5_1_1);

		JCheckBox chckbxNewCheckBox_9 = new JCheckBox("Freigabe Watchdog");
		chckbxNewCheckBox_9.setBounds(6, 75, 151, 23);
		timing.add(chckbxNewCheckBox_9);

		JLabel lblNewLabel_6 = new JLabel("0 \u03BC");
		lblNewLabel_6.setBounds(83, 22, 74, 14);
		timing.add(lblNewLabel_6);

		JLabel lblNewLabel_6_1 = new JLabel("00");
		lblNewLabel_6_1.setBounds(83, 102, 37, 14);
		timing.add(lblNewLabel_6_1);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "32 kHz", "100 kHz", "500 kHz", "1 MHz", "2 MHz",
				"4 MHz", "8 MHz", "12 MHz", "20 MHz" }));
		comboBox.setBounds(83, 43, 74, 22);
		timing.add(comboBox);

		JPanel controls = new JPanel();
		controls.setToolTipText("");
		controls.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)),
				"Bedienelemente", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		controls.setBounds(1109, 503, 140, 139);
		getContentPane().add(controls);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO controller.reset();
			}
		});

		JButton btnSingleStep = new JButton("Einzelschritt");
		btnSingleStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.run();
			}
		});
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!autorun) {
					autorunner = new AutoRun(controller);
					autorunner.start();
					autorun = true;
				}
			}
		});

		JButton btnStop = new JButton("Stopp");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (autorun) {
					autorunner.stop();
					autorun = false;
				}
			}
		});

		controls.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		controls.add(btnReset);
		controls.add(btnSingleStep);
		controls.add(btnStart);
		controls.add(btnStop);

		file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputfile();
			}
		});
	}

	public void inputfile() {
		ArrayList<String> liste = new ArrayList<String>();
		int response;
		File fileOne;
		Scanner scan;
		JFileChooser chooser = new JFileChooser("");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		response = chooser.showOpenDialog(null);

		if (response == JFileChooser.APPROVE_OPTION) {
			fileOne = chooser.getSelectedFile();
			try {
				scan = new Scanner(fileOne, "ISO-8859-1");
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					liste.add(line);
				}

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			String listeZwei[] = new String[liste.size()];
			for (int i = 0; i < liste.size(); i++) {
				listeZwei[i] = liste.get(i);
			}

			speicher = new JList(listeZwei);
			programmSourceCode.add(speicher);
			programmSourceCode.setVisible(true);
			scrollPane_1.setViewportView(speicher);

			lstReader = new ReadLST(fileOne.getAbsolutePath());
			controller.newProgramm(lstReader.parseHex());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	public void updateGui(DataMemory dataStorage, StackMemory stack) {
		final int bank[][] = dataStorage.getBank();
		int programmCounter = controller.getProgrammCounter();

		// update table
		for (int i = 0; i < bank.length; i++) {
			for (int j = 0; j < bank[i].length; j++) {
				table.setValueAt(Integer.toHexString(bank[i][j] & 0xFF).toUpperCase(), (j / 8) + (i * 16), (j % 8) + 1);
			}
		}

		// update SFR labels ( "SFR + W" and "SFR (Bit)" tabs)

		// SFR + W
		sfrAndWVal[0].setText(Integer.toHexString(dataStorage.getwRegister()).toUpperCase());
		sfrAndWVal[1].setText(Integer.toHexString(bank[0][SpecialRegister.PCL.getAddress()]).toUpperCase());
		sfrAndWVal[2].setText(Integer.toHexString(bank[0][SpecialRegister.PCLATH0.getAddress()]).toUpperCase());
		sfrAndWVal[3].setText(Integer.toHexString(programmCounter).toUpperCase());
		sfrAndWVal[4].setText(Integer.toHexString(bank[0][SpecialRegister.STATUS.getAddress()]).toUpperCase());
		sfrAndWVal[5].setText(Integer.toHexString(bank[0][SpecialRegister.FSR.getAddress()]).toUpperCase());
		sfrAndWVal[6].setText(Integer.toHexString(bank[1][SpecialRegister.OPTION.getAddress()]).toUpperCase());
		// update vorteiler
		sfrAndWVal[8].setText(Integer.toHexString(bank[0][SpecialRegister.TMR0.getAddress()]).toUpperCase());

		// SFR (Bit)
		int bitmask = 0x80;

		// Status
		for (int i = 0; i < 8; i++) {
			if ((bank[0][SpecialRegister.STATUS.getAddress()] & bitmask) > 0) {
				sfrBit[i].setText("1");
			} else {
				sfrBit[i].setText("0");
			}
			bitmask = bitmask >> 1;
		}

		// Option
		bitmask = 0x80;
		for (int i = 0; i < 8; i++) {
			if ((bank[1][SpecialRegister.OPTION.getAddress()] & bitmask) > 0) {
				sfrBit[i + 8].setText("1");
			} else {
				sfrBit[i + 8].setText("0");
			}
			bitmask = bitmask >> 1;
		}

		// Intcon
		bitmask = 0x80;
		for (int i = 0; i < 8; i++) {
			if ((bank[0][SpecialRegister.INTCON.getAddress()] & bitmask) > 0) {
				sfrBit[i + 16].setText("1");
			} else {
				sfrBit[i + 16].setText("0");
			}
			bitmask = bitmask >> 1;
		}

		// Stack
		for (int i = 0; i < 8; i++) {
			this.stack[i].setText((Integer.toHexString(stack.get(i)).toUpperCase()));
		}

		// Ports A and B
		bitmask = 0;

		for (int i = 0; i < 8; i++) {
			bitmask = 0x80 >> i;

			// Tris A
			if ((bank[1][SpecialRegister.TRISA.getAddress()] & bitmask) > 0) {
				trisA[i].setSelected(true);
			} else {
				trisA[i].setSelected(false);
			}
			/*
			 * // Pin A, only 5 pins if (i > 2) { if
			 * ((bank[0][SpecialRegister.RA0.getAddress()] & bitmask) > 0) { pinA[i -
			 * 3].setSelected(true); } else { pinA[i - 3].setSelected(false); } }
			 */
			// Tris B
			if ((bank[1][SpecialRegister.TRISB.getAddress()] & bitmask) > 0) {
				trisB[i].setSelected(true);
			} else {
				trisB[i].setSelected(false);
			}
			/*
			 * // Pin B if ((bank[0][SpecialRegister.RB0.getAddress()] & bitmask) > 0) {
			 * pinB[i].setSelected(true); } else { pinB[i].setSelected(false); }
			 */
		}

		// Set selected row
		int currCount = 0;
		if (speicher != null) {
			for (int i = 0; i < speicher.getModel().getSize(); i++) {
				if (!(((String) (speicher.getModel().getElementAt(i))).startsWith(" "))) {
					if (currCount == programmCounter) {
						speicher.setSelectedIndex(i);
						speicher.ensureIndexIsVisible(i);
						break;
					}
					currCount++;
				}
			}
		}
	}
}