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
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.dao.SessionDao;
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
		JComboBox<Session> sessionCombo = new JComboBox<>(new SessionDao().listSessionsFromEvent((Event) eventCombo.getSelectedItem()).toArray(new Session[]{}));
		ticketPanel.add(sessionLabel);
		ticketPanel.add(sessionCombo, "growx, wrap");
		
		eventCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				sessionCombo.removeAllItems();
				for (Session session : new SessionDao().listSessionsFromEvent((Event) eventCombo.getSelectedItem())) {
					sessionCombo.addItem(session);
				}
			}
		});
		
		JButton persistButton = new JButton("Gravar");
		ticketPanel.add(persistButton, "x2 (container.w+pref)/2");
		persistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Ticket ticket = new Ticket();
				
				JOptionPane.showMessageDialog(null, "Registro gravado com sucesso");
				TicketSales.INSTANCE.changePanel(new ContentPanelInfo(ContentPanel.CREATE_TICKET));
			}
		});
		
		return ticketPanel;
	}
	
	public static JPanel createListTicketPanel() {
		JPanel sessionPanel = new JPanel(new MigLayout());
		SessionTableModel sessionTableModel = new SessionTableModel(new SessionDao().listSessions());
		JTable sessionTable = new JTable(sessionTableModel);
		sessionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(sessionTable);
		sessionPanel.add(scrollPane, "span, grow, wrap, width 700:700:");
		
		JButton edit = new JButton("Editar");
		sessionPanel.add(edit);
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ContentPanelInfo contentPanelInfo = new ContentPanelInfo(ContentPanel.CREATE_TICKET);
				TicketSales.INSTANCE.changePanel(contentPanelInfo);
			}
		});
		
		return sessionPanel;
	}
}

class SessionTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final String[] COLUMNS = {"Evento", "Dia", "Hora Início", "Hora Fim"};
	
	private List<Session> sessions;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

	public SessionTableModel(List<Session> sessions) {
		this.sessions = sessions;
	}
	
	public Session getSessionAt(int selectedRow) {
		return sessions.get(selectedRow);
	}

	@Override
	public int getRowCount() {
		return sessions.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Session session = sessions.get(rowIndex);
		if (columnIndex == 0) {
			return session.getEvent().getTitle();
		} else if (columnIndex == 1) {
			return dateFormat.format(session.getDay());
		} else if (columnIndex == 2) {
			return timeFormat.format(session.getStartHour());
		} else if (columnIndex == 3) {
			return timeFormat.format(session.getEndHour());
		}
		return null;
	}
	
	@Override
	public String getColumnName(int column) {
		return COLUMNS[column];
	}
}