package org.jdevelopment.jscreensaver;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Locale;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.Timer;

public class JScreenSaver extends JWindow {

	private static boolean isEnabled = true;		// marca se está em execução
	private static boolean isFirstStart = true;		// marca se é a primeira vez que está rodando
	private static int delay = 30;					// tempo de espera após o mouse parar
	private static int coffeCups = 10;				// numero de xícaras
	private static int currentCoffeCups = 1;		// numero atualmente rodando

	public static Image img;						// representa a imagem

	public JScreenSaver(final int wd, final int ht) {

		if (img == null) {
			img = new ImageIcon((URL) getClass().getResource("/img/foot.png"))
					.getImage();
		}

		setSize(70, 70);
		add(new JLabel(new ImageIcon((URL) getClass().getResource(
				"/img/java.png"))));
		setAlwaysOnTop(true);	// deixa sempre na frente das janelas

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();

		final int width = screenSize.width;
		final int heigth = screenSize.height;

		// LINHA SUPER IMPORTANTE, SÓ FUNCIONA NO JAVA 7, DEIXA O JFRAME INVISÍVEL
		setBackground(new Color(0f, 0f, 0f, 0f));
		//-----------------------------------------------------------------------

		// este timer representa o timer que muda o frame de lugar
		// os milissegundos do timer representam a velocidade do frame
		new Timer(1, new ActionListener() {

			int x = wd;
			int ax = 1;
			int y = ht;
			int ay = 1;

			@Override
			public void actionPerformed(ActionEvent e) {

				// se o mouse foi mexido então ela tem que se mandar
				if (!isEnabled && isVisible()) {

					currentCoffeCups--; // decrementa a quantidade atual de xicaras
					if (currentCoffeCups < 0) {
						currentCoffeCups = 0;
					}
					dispose(); // tchau tchau

				}

				JScreenSaver.this.setLocation(x, y);

				// se bateu no lado direito ou esquerdo da tela reverte o contador
				if (x + 70 >= width || x <= 0) {
					ax *= -1;
				}
				
				//se bateu em baixo ou em cima da tela reverte o contador
				if (y + 70 >= heigth || y <= 0) {
					ay *= -1;
				}

				x += ax;
				y += ay;

			}
		}).start();
		//-----------------------------------------------------

		// timer que representa a vida útil do frame. 20 segundos
		new Timer(20000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (isVisible()) {

					currentCoffeCups--; // decrementa a quantidade atual de xicaras
					if (currentCoffeCups < 0) {
						currentCoffeCups = 0;
					}

				}

				dispose(); // vaza!!!!!!

			}

		}).start();

		setVisible(true);

	}

	private static void start() {

		isEnabled = true;

	}

	private static void stop() {

		isEnabled = false;
		currentCoffeCups = 0;// zera as xicaras

	}

	public static void main(String[] args) {

		final Random r = new Random();

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();

		final int width = screenSize.width;
		final int heigth = screenSize.height;

		try {
			Thread.sleep(5000); // ao iniciar pela primeira vez, espera 5 segundos para começar
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		new JScreenSaver(r.nextInt(width - 70), r.nextInt(heigth - 70));

		isFirstStart = false;

		// timer que verifica se o mouse está parado
		new Timer(500, new ActionListener() {

			// pega as posições do mouse
			int x = MouseInfo.getPointerInfo().getLocation().x;
			int y = MouseInfo.getPointerInfo().getLocation().y;
			int count;

			@Override
			public void actionPerformed(ActionEvent e) {

				// pega de novo
				int auxX = MouseInfo.getPointerInfo().getLocation().x;
				int auxY = MouseInfo.getPointerInfo().getLocation().y;

				if (auxX == x && auxY == y) {

					count++; // se estiver igual vai incrementando

				} else {

					count = 0; // se mexeu o mouse zera tudo
					stop();

				}

				if (count == delay) { // se chegar ao delay inicia
					start();
				}

				// pega as pos de novo
				x = MouseInfo.getPointerInfo().getLocation().x;
				y = MouseInfo.getPointerInfo().getLocation().y;

				// System.out.println(count);

			}

		}).start();

		// timer que representa de quanto em quanto tempo as xicaras sao criadas
		new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (isEnabled) { // se true significa que o mouse está parado
					if (currentCoffeCups < coffeCups) {
						new JScreenSaver(r.nextInt(width - 70),
								r.nextInt(heigth - 70));
						currentCoffeCups++;
					}
				}

			}

		}).start();
		//---------------------------------------------------------------------

		// tudo isto até embaixo representa só o menu
		ActionListener exitAct = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};

		ActionListener setDelayAct = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JOptionPane.setDefaultLocale(Locale.ENGLISH);

				try {
					String aux = JOptionPane.showInputDialog(null,
							"Enter the new delay(seconds):", "New delay",
							JOptionPane.QUESTION_MESSAGE);

					if (aux != null) {

						delay = Integer.parseInt(aux);

					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Invalid value!",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};

		ActionListener aboutAct = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "" + "Name: JScreenSaver\n"
						+ "Version: 1.0\n"
						+ "Developed by: Jonathan Sansalone\n"
						+ "Language: Java(TM) SE 7\n" + "Date: March, 2012",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}
		};
		
		ActionListener setCoffeAct = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JOptionPane.setDefaultLocale(Locale.ENGLISH);

				try {
					String aux = JOptionPane.showInputDialog(null,
							"Enter the number of coffe cups:", "Coffe cups",
							JOptionPane.QUESTION_MESSAGE);

					if (aux != null) {

						coffeCups = Integer.parseInt(aux);
						stop();

					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Invalid value!",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};

		// create a popup menu
		PopupMenu popup = new PopupMenu();
		// create menu item for the default action
		MenuItem setDelayItem = new MenuItem("Set delay");
		MenuItem setCoffeCups = new MenuItem("Set coffe cups");
		MenuItem aboutItem = new MenuItem("About...");
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(exitAct);
		setDelayItem.addActionListener(setDelayAct);
		setCoffeCups.addActionListener(setCoffeAct);
		aboutItem.addActionListener(aboutAct);
		popup.add(setDelayItem);
		popup.add(setCoffeCups);
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(exitItem);

		// parte legaw nde vc pega a barra de status
		SystemTray tray = SystemTray.getSystemTray();
		TrayIcon trayIcon = new TrayIcon(img, "Exemplo", popup);
		trayIcon.setToolTip("JScreenSaver");

		if (SystemTray.isSupported()) {

			try {
				tray.add(trayIcon);
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		//-----------------------------------------------------------------------

	}

}