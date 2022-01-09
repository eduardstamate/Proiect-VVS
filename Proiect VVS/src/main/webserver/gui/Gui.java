package main.webserver.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import main.webserver.MainThread;

public class Gui {

	private JFrame frmJavaWebServer;
	private JTextField normalPath;
	private JTextField maintenanceFilePath;
	private JTextField port;

	private MainThread runningServer;

	private boolean serverRunning;
	public static final int defaultPort = 10008;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frmJavaWebServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJavaWebServer = new JFrame();
		frmJavaWebServer.setTitle("Web server");
		frmJavaWebServer.setBounds(100, 100, 357, 187);
		frmJavaWebServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJavaWebServer.getContentPane().setLayout(null);

		JButton startButton = new JButton("Start");

		startButton.setBounds(157, 16, 67, 26);
		frmJavaWebServer.getContentPane().add(startButton);

		JCheckBox checkbox = new JCheckBox("Maintenance");
		checkbox.setEnabled(false);
		checkbox.setBounds(206, 16, 147, 26);
		checkbox.setHorizontalAlignment(SwingConstants.CENTER);
		frmJavaWebServer.getContentPane().add(checkbox);

		normalPath = new JTextField();
		normalPath.setBounds(0, 91, 170, 49);
		normalPath.setHorizontalAlignment(SwingConstants.CENTER);
		normalPath.setToolTipText("Normal path");
		frmJavaWebServer.getContentPane().add(normalPath);
		normalPath.setColumns(10);

		maintenanceFilePath = new JTextField();
		maintenanceFilePath.setBounds(167, 91, 170, 49);
		maintenanceFilePath.setHorizontalAlignment(SwingConstants.CENTER);
		maintenanceFilePath.setToolTipText("Maintenance path");
		maintenanceFilePath.setColumns(10);
		frmJavaWebServer.getContentPane().add(maintenanceFilePath);

		port = new JTextField();
		port.setText("10008");
		port.setBounds(83, 20, 50, 20);
		frmJavaWebServer.getContentPane().add(port);
		port.setColumns(10);

		JLabel lblNewLabel_1_1 = new JLabel("port");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1.setBounds(23, 21, 50, 14);
		frmJavaWebServer.getContentPane().add(lblNewLabel_1_1);

		JLabel lblServerPath = new JLabel("Web server path");
		lblServerPath.setBounds(52, 75, 105, 20);
		frmJavaWebServer.getContentPane().add(lblServerPath);

		JLabel lblMaintenancePage = new JLabel("Maintenance page");
		lblMaintenancePage.setBounds(222, 78, 115, 14);
		frmJavaWebServer.getContentPane().add(lblMaintenancePage);

		if (normalPath.getText().length() == 0) {
			normalPath.setText("TestSite/");
		}

		if (maintenanceFilePath.getText().length() == 0) {
			maintenanceFilePath.setText("maintenance.html");
		}

		if (port.getText().length() == 0) {
			port.setText("10008");
		}

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!serverRunning) {
					try {
						if (!port.getText().isBlank())
							MainThread.port = Integer.valueOf(port.getText());
						else
							MainThread.port = defaultPort;

						serverRunning = true;
						runningServer = new MainThread();
						runningServer.start();

						checkbox.setEnabled(true);
						port.setEditable(false);
						startButton.setText("Stop");
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					}
				} else {
					try {
						runningServer.kill();
						serverRunning = false;
						checkbox.setEnabled(false);
						port.setEditable(true);
						startButton.setText("Start");
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
		checkbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.webserver.WebServer.toggleMaintenance();
			}
		});

	}
}