package br.com.ufs.ds3.gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.ufs.ds3.dao.TheatreDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.Theatre;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.gui.event.EventPanel;
import br.com.ufs.ds3.gui.main.ContentPanelInfo.ContentPanel;
import br.com.ufs.ds3.gui.session.SessionPanel;
import br.com.ufs.ds3.gui.ticket.TicketPanel;
import net.miginfocom.swing.MigLayout;

public class TicketSales {
	public static final TicketSales INSTANCE = new TicketSales();
	private JFrame mainWindow;
	private JPanel currentPanel;
	private Theatre currentTheatre;
	private JLabel currentTheatreLabel;
	private ContentPanel currentContentPanel;
	private JPanel currentHeaderPanel;
	
	private TicketSales() {
	}
	
	public void start() {
		try {
			SwingUtilities.invokeAndWait(() -> loadMainWindow());
		} catch (InvocationTargetException | InterruptedException e) {
			throw new TicketSalesException(e);
		}
	}
	
	private void loadMainWindow() {
		currentContentPanel = ContentPanel.INITIAL;
		mainWindow = new JFrame("Ticket Sales");
		mainWindow.setLayout(new BorderLayout());
		mainWindow.setSize(800, 600);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainWindow.setJMenuBar(MenubarUtil.createMenubar());
		currentPanel = createInitialPanel();
		currentPanel.setPreferredSize(new Dimension(800, 600));
		mainWindow.add(currentPanel, BorderLayout.CENTER);
		currentHeaderPanel = createHeaderPanel();
		mainWindow.add(currentHeaderPanel, BorderLayout.NORTH);
		mainWindow.pack();
		mainWindow.setVisible(true);
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel(new MigLayout());
		headerPanel.setBackground(new Color(171, 170, 171));
		headerPanel.setPreferredSize(new Dimension(800, 45));
		currentTheatreLabel = new JLabel("Teatro selecionado: " + currentTheatre.getName());
		headerPanel.add(currentTheatreLabel);
		
		if (currentContentPanel != ContentPanel.INITIAL) {
			JButton buttonSelectTheatre = new JButton("Alterar");
			headerPanel.add(buttonSelectTheatre);
			buttonSelectTheatre.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					changePanel(new ContentPanelInfo(ContentPanel.INITIAL));
				}
			});
		}
		return headerPanel;
	}

	private JPanel createInitialPanel() {
		JPanel panel = new JPanel(new MigLayout());
		
		JLabel theatreLabel = new JLabel("Teatro");
		JComboBox<Theatre> theatreCombo = new JComboBox<>(new TheatreDao().listTheatres().toArray(new Theatre[]{}));
		panel.add(theatreLabel);
		panel.add(theatreCombo, "growx, wrap");
		
		theatreCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				currentTheatre = (Theatre) theatreCombo.getSelectedItem();
				currentTheatreLabel.setText("Teatro selecionado: " + currentTheatre.getName());
			}
		});
		
		if (currentTheatre == null) {
			currentTheatre = theatreCombo.getItemAt(0);
		} else {
			theatreCombo.setSelectedItem(currentTheatre);
		}
		return panel;
	}

	public void tearDown() {
		if (mainWindow != null) {
			mainWindow.dispose();
			mainWindow = null;
		}
	}

	public void changePanel(ContentPanelInfo contentPanelInfo) {
		mainWindow.remove(currentPanel);
		currentPanel = null;
		currentContentPanel = contentPanelInfo.getContentPanel();
		switch (contentPanelInfo.getContentPanel()) {
		case CREATE_EVENT:
			Event editEvent = (Event) contentPanelInfo.getInfo(EventPanel.EDIT_EVENT);
			if (editEvent != null) {
				mainWindow.setTitle("Ticket Sales - Editar Evento");
			} else {
				mainWindow.setTitle("Ticket Sales - Criar Evento");
			}
			currentPanel = EventPanel.createEventFormPanel(editEvent);
			break;
		case CREATE_SESSION:
			Session editSession = (Session) contentPanelInfo.getInfo(SessionPanel.EDIT_SESSION);
			if (editSession != null) {
				mainWindow.setTitle("Ticket Sales - Editar Sessão");
			} else {
				mainWindow.setTitle("Ticket Sales - Criar Sessão");
			}
			currentPanel = SessionPanel.createSessionFormPanel(editSession);
			break;
		case LIST_EVENT:
			mainWindow.setTitle("Ticket Sales - Listar Eventos");
			currentPanel = EventPanel.createListEventPanel();
			break;
		case LIST_SESSION:
			mainWindow.setTitle("Ticket Sales - Listar Sessões");
			currentPanel = SessionPanel.createListSessionPanel();
			break;
		case CREATE_TICKET:
			mainWindow.setTitle("Ticket Sales - Comprar Ingresso");
			currentPanel = TicketPanel.createTicketFormPanel();
			break;
		case LIST_TICKET:
			mainWindow.setTitle("Ticket Sales - Listar Ingressos");
			currentPanel = TicketPanel.createListTicketPanel();
			break;
		case INITIAL:
		default:
			mainWindow.setTitle("Ticket Sales");
			currentPanel = createInitialPanel();
			break;
		}
		
		currentPanel.setPreferredSize(new Dimension(800, 600));
		mainWindow.add(currentPanel, BorderLayout.CENTER);
		mainWindow.remove(currentHeaderPanel);
		currentHeaderPanel = createHeaderPanel();
		mainWindow.add(currentHeaderPanel, BorderLayout.NORTH);
		mainWindow.pack();
	}
	
	public Theatre getCurrentTheatre() {
		return currentTheatre;
	}
}
