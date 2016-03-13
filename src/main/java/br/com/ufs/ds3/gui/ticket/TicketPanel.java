package br.com.ufs.ds3.gui.ticket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import br.com.ufs.ds3.dao.ChairDao;
import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.dao.PriceDao;
import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.dao.TicketDao;
import br.com.ufs.ds3.entity.Chair;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.Ticket;
import br.com.ufs.ds3.gui.main.ContentPanelInfo;
import br.com.ufs.ds3.gui.main.ContentPanelInfo.ContentPanel;
import br.com.ufs.ds3.gui.main.TicketSales;
import net.miginfocom.swing.MigLayout;

public class TicketPanel {

	public static JPanel createTicketFormPanel() {
		JPanel ticketPanel = new JPanel(new MigLayout());
		
		JLabel eventLabel = new JLabel("Evento");
		JComboBox<Event> eventCombo = new JComboBox<>(new EventDao().listEventsFromTheatre(TicketSales.INSTANCE.getCurrentTheatre()).toArray(new Event[]{}));
		ticketPanel.add(eventLabel);
		ticketPanel.add(eventCombo, "growx");
		
		JLabel sessionLabel = new JLabel("Sessão");
		JComboBox<Session> sessionCombo = new JComboBox<>();
		ticketPanel.add(sessionLabel);
		ticketPanel.add(sessionCombo, "growx, wrap");
		
		eventCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					sessionCombo.removeAllItems();
					for (Session session : new SessionDao().listSessionsFromEvent((Event) eventCombo.getSelectedItem())) {
						sessionCombo.addItem(session);
					}
				}
			}
		});
		
		JLabel chairLabel = new JLabel("Cadeira");
		JComboBox<Chair> chairCombo = new JComboBox<>();
		ticketPanel.add(chairLabel);
		ticketPanel.add(chairCombo, "wrap");
		
		JLabel priceLabel = new JLabel("Preço");
		JTextField priceField = new JTextField();
		ticketPanel.add(priceLabel);
		ticketPanel.add(priceField, "wrap, width 75:75:");
		
		sessionCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Session session = (Session) sessionCombo.getSelectedItem();
					List<Chair> availableChairs = new ChairDao().listAvailableChairsForSession(session);
					chairCombo.removeAllItems();
					for (Chair chair : availableChairs) {
						chairCombo.addItem(chair);
					}
					
					priceField.setText(new PriceDao().getPriceForSession(session).getTicketPrice().toString());
				}
			}
		});
		
		JButton persistButton = new JButton("Gravar");
		ticketPanel.add(persistButton, "x2 (container.w+pref)/2");
		persistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Ticket ticket = new Ticket();
				ticket.setSession((Session) sessionCombo.getSelectedItem());
				
				JOptionPane.showMessageDialog(null, "Registro gravado com sucesso");
				TicketSales.INSTANCE.changePanel(new ContentPanelInfo(ContentPanel.CREATE_TICKET));
			}
		});
		
		return ticketPanel;
	}
	
	public static JPanel createListTicketPanel() {
		JPanel ticketPanel = new JPanel(new MigLayout());
		
		JLabel eventLabel = new JLabel("Evento");
		JComboBox<Event> eventCombo = new JComboBox<>(new EventDao().listEventsFromTheatre(TicketSales.INSTANCE.getCurrentTheatre()).toArray(new Event[]{}));
		ticketPanel.add(eventLabel);
		ticketPanel.add(eventCombo, "growx");
		
		JLabel sessionLabel = new JLabel("Sessão");
		JComboBox<Session> sessionCombo = new JComboBox<>();
		ticketPanel.add(sessionLabel);
		ticketPanel.add(sessionCombo, "growx");
		
		eventCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					sessionCombo.removeAllItems();
					for (Session session : new SessionDao().listSessionsFromEvent((Event) eventCombo.getSelectedItem())) {
						sessionCombo.addItem(session);
					}
				}
			}
		});
		
		JLabel numberLabel = new JLabel("Número");
		JTextField numberField = new JTextField();
		ticketPanel.add(numberLabel);
		ticketPanel.add(numberField, "growx, width 75:75:");
		
		TicketTableModel ticketTableModel = new TicketTableModel();
		JTable ticketTable = new JTable(ticketTableModel);
		ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(ticketTable);
		
		JButton searchButton = new JButton("Buscar");
		ticketPanel.add(searchButton, "wrap");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ticketTableModel.setEvent((Event) eventCombo.getSelectedItem());
				ticketTableModel.setSession((Session) sessionCombo.getSelectedItem());
				if (numberField.getText() != null && numberField.getText().trim().equals("")) {
					ticketTableModel.setNumber(null);
				} else {
					ticketTableModel.setNumber(Integer.valueOf(numberField.getText()));
				}
				ticketTableModel.refresh();
			}
		});
		
		ticketPanel.add(scrollPane, "span, grow, wrap, width 700:700:");
		
		JButton edit = new JButton("Visualizar");
		ticketPanel.add(edit);
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ContentPanelInfo contentPanelInfo = new ContentPanelInfo(ContentPanel.CREATE_TICKET);
				TicketSales.INSTANCE.changePanel(contentPanelInfo);
			}
		});
		
		return ticketPanel;
	}
}

class TicketTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final String[] COLUMNS = {"Evento", "Número", "Dia", "Hora Início", "Hora Fim"};
	
	private List<Ticket> tickets;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private TicketDao ticketDao;
	private Event event;
	private Session session;
	private Integer number;

	public TicketTableModel() {
		ticketDao = new TicketDao();
		this.tickets = ticketDao.listTickets(TicketSales.INSTANCE.getCurrentTheatre(), null, null, null);
	}
	
	public Ticket getSessionAt(int selectedRow) {
		return tickets.get(selectedRow);
	}

	@Override
	public int getRowCount() {
		return tickets.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Ticket ticket = tickets.get(rowIndex);
		if (columnIndex == 0) {
			return ticket.getSession().getEvent().getTitle();
		} else if (columnIndex == 1) {
			return ticket.getNumber();
		} else if (columnIndex == 2) {
			return dateFormat.format(ticket.getSession().getDay());
		} else if (columnIndex == 3) {
			return timeFormat.format(ticket.getSession().getStartHour());
		} else if (columnIndex == 4) {
			return timeFormat.format(ticket.getSession().getEndHour());
		}
		return null;
	}
	
	@Override
	public String getColumnName(int column) {
		return COLUMNS[column];
	}
	
	public void refresh() {
		tickets = ticketDao.listTickets(TicketSales.INSTANCE.getCurrentTheatre(), event, session, number);
	}
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	public Event getEvent() {
		return event;
	}
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
}