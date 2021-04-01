package GUI;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class Window extends JFrame {
	private JTable table;

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

		JButton file = new JButton("Datei");
		menuBar.add(file);

		File failOne;
		Scanner failOneIn;
		final int response;
		JFileChooser chooser = new JFileChooser(".");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		

		

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

		JLabel lblwRegisterValue = new JLabel("00");
		lblwRegisterValue.setBounds(90, 25, 25, 14);
		specialFunctionRegisterAndW.add(lblwRegisterValue);

		JLabel lblPclValue = new JLabel("00");
		lblPclValue.setBounds(90, 47, 25, 14);
		specialFunctionRegisterAndW.add(lblPclValue);

		JLabel lblPclathValue = new JLabel("00");
		lblPclathValue.setBounds(90, 69, 25, 14);
		specialFunctionRegisterAndW.add(lblPclathValue);

		JLabel lblPcinternValue = new JLabel("00");
		lblPcinternValue.setBounds(90, 91, 25, 14);
		specialFunctionRegisterAndW.add(lblPcinternValue);

		JLabel lblStatusValue = new JLabel("00");
		lblStatusValue.setBounds(90, 113, 25, 14);
		specialFunctionRegisterAndW.add(lblStatusValue);

		JLabel lblFsrValue = new JLabel("00");
		lblFsrValue.setBounds(90, 135, 25, 14);
		specialFunctionRegisterAndW.add(lblFsrValue);

		JLabel lblOption = new JLabel("Option:");
		lblOption.setBounds(163, 25, 64, 14);
		specialFunctionRegisterAndW.add(lblOption);

		JLabel lblVorteiler = new JLabel("Vorteiler:");
		lblVorteiler.setBounds(163, 47, 64, 14);
		specialFunctionRegisterAndW.add(lblVorteiler);

		JLabel lblTimer = new JLabel("Timer0:");
		lblTimer.setBounds(163, 69, 64, 14);
		specialFunctionRegisterAndW.add(lblTimer);

		JLabel lblwOptionValue = new JLabel("00");
		lblwOptionValue.setBounds(237, 25, 25, 14);
		specialFunctionRegisterAndW.add(lblwOptionValue);

		JLabel lblwVorteilerValue = new JLabel("00");
		lblwVorteilerValue.setBounds(237, 47, 25, 14);
		specialFunctionRegisterAndW.add(lblwVorteilerValue);

		JLabel lblTimerValue = new JLabel("00");
		lblTimerValue.setBounds(237, 69, 25, 14);
		specialFunctionRegisterAndW.add(lblTimerValue);

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

		JLabel lblIrp_1 = new JLabel("0");
		lblIrp_1.setBounds(65, 47, 20, 14);
		specialFunctionRegisterInBits.add(lblIrp_1);

		JLabel lblRp1_1 = new JLabel("0");
		lblRp1_1.setBounds(95, 47, 20, 14);
		specialFunctionRegisterInBits.add(lblRp1_1);

		JLabel lblRp0_1 = new JLabel("0");
		lblRp0_1.setBounds(125, 47, 20, 14);
		specialFunctionRegisterInBits.add(lblRp0_1);

		JLabel lblTo_1 = new JLabel("0");
		lblTo_1.setBounds(155, 47, 20, 14);
		specialFunctionRegisterInBits.add(lblTo_1);

		JLabel lblPd_1 = new JLabel("0");
		lblPd_1.setBounds(185, 47, 20, 14);
		specialFunctionRegisterInBits.add(lblPd_1);

		JLabel lblZ_1 = new JLabel("0");
		lblZ_1.setBounds(215, 47, 20, 14);
		specialFunctionRegisterInBits.add(lblZ_1);

		JLabel lblDc_1 = new JLabel("0");
		lblDc_1.setBounds(245, 47, 20, 14);
		specialFunctionRegisterInBits.add(lblDc_1);

		JLabel lblC_1 = new JLabel("0");
		lblC_1.setBounds(275, 47, 20, 14);
		specialFunctionRegisterInBits.add(lblC_1);

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

		JLabel lblRpu_1 = new JLabel("0");
		lblRpu_1.setBounds(65, 91, 20, 14);
		specialFunctionRegisterInBits.add(lblRpu_1);

		JLabel lblIeg_1 = new JLabel("0");
		lblIeg_1.setBounds(95, 91, 20, 14);
		specialFunctionRegisterInBits.add(lblIeg_1);

		JLabel lblRp_1_2_1 = new JLabel("0");
		lblRp_1_2_1.setBounds(125, 91, 20, 14);
		specialFunctionRegisterInBits.add(lblRp_1_2_1);

		JLabel lblTse_1 = new JLabel("0");
		lblTse_1.setBounds(155, 91, 20, 14);
		specialFunctionRegisterInBits.add(lblTse_1);

		JLabel lblPsa_1 = new JLabel("0");
		lblPsa_1.setBounds(185, 91, 20, 14);
		specialFunctionRegisterInBits.add(lblPsa_1);

		JLabel lblPs2_1 = new JLabel("0");
		lblPs2_1.setBounds(215, 91, 20, 14);
		specialFunctionRegisterInBits.add(lblPs2_1);

		JLabel lblPs1_1 = new JLabel("0");
		lblPs1_1.setBounds(245, 91, 20, 14);
		specialFunctionRegisterInBits.add(lblPs1_1);

		JLabel lblPs0_1 = new JLabel("0");
		lblPs0_1.setBounds(275, 91, 20, 14);
		specialFunctionRegisterInBits.add(lblPs0_1);

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

		JLabel lblGie_1 = new JLabel("0");
		lblGie_1.setBounds(65, 135, 20, 14);
		specialFunctionRegisterInBits.add(lblGie_1);

		JLabel lblEie_1 = new JLabel("0");
		lblEie_1.setBounds(95, 135, 20, 14);
		specialFunctionRegisterInBits.add(lblEie_1);

		JLabel lblTie_1 = new JLabel("0");
		lblTie_1.setBounds(125, 135, 20, 14);
		specialFunctionRegisterInBits.add(lblTie_1);

		JLabel lblIe_1 = new JLabel("0");
		lblIe_1.setBounds(155, 135, 20, 14);
		specialFunctionRegisterInBits.add(lblIe_1);

		JLabel lblRie_1 = new JLabel("0");
		lblRie_1.setBounds(185, 135, 20, 14);
		specialFunctionRegisterInBits.add(lblRie_1);

		JLabel lblTif_1 = new JLabel("0");
		lblTif_1.setBounds(215, 135, 20, 14);
		specialFunctionRegisterInBits.add(lblTif_1);

		JLabel lblIf_1 = new JLabel("0");
		lblIf_1.setBounds(245, 135, 20, 14);
		specialFunctionRegisterInBits.add(lblIf_1);

		JLabel lblRif_1 = new JLabel("0");
		lblRif_1.setBounds(275, 135, 20, 14);
		specialFunctionRegisterInBits.add(lblRif_1);

		JPanel stackRegister = new JPanel();
		stackRegister.setLayout(null);
		stackRegister.setToolTipText("");
		stackRegister.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)), "Stack",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		stackRegister.setBounds(669, 33, 100, 160);
		getContentPane().add(stackRegister);

		JPanel portARegister = new JPanel();
		portARegister.setLayout(null);
		portARegister.setToolTipText("");
		portARegister.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)),
				"Port A", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		portARegister.setBounds(779, 33, 470, 77);
		getContentPane().add(portARegister);

		JPanel portBRegister = new JPanel();
		portBRegister.setLayout(null);
		portBRegister.setToolTipText("");
		portBRegister.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)),
				"Port B", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		portBRegister.setBounds(779, 116, 470, 77);
		getContentPane().add(portBRegister);

		JPanel programmSourceCode = new JPanel();
		programmSourceCode.setToolTipText("");
		programmSourceCode.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)),
				"Programm (LST-Datei)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		programmSourceCode.setBounds(10, 204, 759, 438);
		getContentPane().add(programmSourceCode);
		programmSourceCode.setLayout(new BorderLayout(0, 0));

		JTextPane textPane = new JTextPane();
		programmSourceCode.add(textPane, BorderLayout.CENTER);

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
			new Object[][] {
				{" 00", null, null, null, null, null, null, null, null},
				{" 08", null, null, null, null, null, null, null, null},
				{" 10", null, null, null, null, null, null, null, null},
				{" 18", null, null, null, null, null, null, null, null},
				{" 20", null, null, null, null, null, null, null, null},
				{" 28", null, null, null, null, null, null, null, null},
				{" 30", null, null, null, null, null, null, null, null},
				{" 38", null, null, null, null, null, null, null, null},
				{" 40", null, null, null, null, null, null, null, null},
				{" 48", null, null, null, null, null, null, null, null},
				{" 50", null, null, null, null, null, null, null, null},
				{" 58", null, null, null, null, null, null, null, null},
				{" 60", null, null, null, null, null, null, null, null},
				{" 68", null, null, null, null, null, null, null, null},
				{" 70", null, null, null, null, null, null, null, null},
				{" 78", null, null, null, null, null, null, null, null},
				{" 80", null, null, null, null, null, null, null, null},
				{" 88", null, null, null, null, null, null, null, null},
				{" 90", null, null, null, null, null, null, null, null},
				{" 98", null, null, null, null, null, null, null, null},
				{" A0", null, null, null, null, null, null, null, null},
				{" A8", null, null, null, null, null, null, null, null},
				{" B0", null, null, null, null, null, null, null, null},
				{" B8", null, null, null, null, null, null, null, null},
				{" C0", null, null, null, null, null, null, null, null},
				{" C8", null, null, null, null, null, null, null, null},
				{" D0", null, null, null, null, null, null, null, null},
				{" D8", null, null, null, null, null, null, null, null},
				{" E0", null, null, null, null, null, null, null, null},
				{" E8", null, null, null, null, null, null, null, null},
				{" F0", null, null, null, null, null, null, null, null},
				{"F8", null, null, null, null, null, null, null, null},
			},
			new String[] {
				"0x", "+0", "+1", "+2", "+3", "+4", "+5", "+6", "+7"
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false
			};
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
		//fileregister.add(table);

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

		JPanel controls = new JPanel();
		controls.setToolTipText("");
		controls.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(192, 192, 192)),
				"Bedienelemente", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		controls.setBounds(1109, 503, 140, 139);
		getContentPane().add(controls);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		JButton btnSingleStep = new JButton("Einzelschritt");

		JButton btnStart = new JButton("Start");

		JButton btnStop = new JButton("Stopp");
		controls.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		controls.add(btnReset);
		controls.add(btnSingleStep);
		controls.add(btnStart);
		controls.add(btnStop);
	}
}
