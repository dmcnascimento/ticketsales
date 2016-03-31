package br.com.ufs.ds3.gui.session;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import br.com.ufs.ds3.bean.SessionModelBean;
import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.SessionType;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.gui.main.ContentPanelInfo;
import br.com.ufs.ds3.gui.main.ContentPanelInfo.ContentPanel;
import br.com.ufs.ds3.gui.main.TicketSales;
import br.com.ufs.ds3.gui.util.SwingComponentUtil;
import br.com.ufs.ds3.service.SessionService;
import net.miginfocom.swing.MigLayout;

public class SessionPanel {
	public static final String EDIT_SESSION = "editSession";

	public static JPanel createSessionFormPanel(Session baseSession) {
		JPanel sessionPanel = new JPanel(new MigLayout());
		SwingComponentUtil swingComponentUtil = new SwingComponentUtil(sessionPanel);

		JComboBox<Event> eventCombo = swingComponentUtil.createAndAddComboComponent("Evento", "growx, wrap");
		JComboBox<WeekDay> dayCombo = swingComponentUtil.createAndAddComboComponent("Dia", null);
		JSpinner spinnerStartHour = swingComponentUtil.createAndAddTimeComponent("Hora inicial", null);
		JComboBox<SessionType> sessionTypeCombo = swingComponentUtil.createAndAddComboComponent("Tipo de Sessão", "wrap");
		
		swingComponentUtil.setComboModelValues(eventCombo, TicketSales.INSTANCE.getCurrentTheatre().getEvents());
		swingComponentUtil.setComboModelValues(dayCombo, WeekDay.values());
		swingComponentUtil.setComboModelValues(sessionTypeCombo, SessionType.values());
		
		JButton persistButton = new JButton("Gravar");
		sessionPanel.add(persistButton, "x2 (container.w+pref)/2");
		persistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Event event = (Event) eventCombo.getSelectedItem();
				WeekDay day = (WeekDay) dayCombo.getSelectedItem();
				Date startHour = (Date) spinnerStartHour.getValue();
				SessionType sessionType = (SessionType) sessionTypeCombo.getSelectedItem();
				SessionModelBean sessionModelBean = new SessionModelBean(event, day, startHour, sessionType);
				
				try {
					new SessionService().createSessions(sessionModelBean);
					JOptionPane.showMessageDialog(null, "Registro gravado com sucesso");
					TicketSales.INSTANCE.changePanel(new ContentPanelInfo(ContentPanel.CREATE_SESSION));
				} catch (TicketSalesException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		
		if (baseSession != null) {
			eventCombo.setSelectedItem(baseSession.getEvent());
			dayCombo.setSelectedItem(WeekDay.fromDate(baseSession.getDay()));
			spinnerStartHour.setValue(baseSession.getStartHour());
			sessionTypeCombo.setSelectedItem(SessionType.fromClass(baseSession.getClass()));
			
			JButton removeButton = new JButton("Remover");
			removeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new SessionDao().remove(baseSession);
					JOptionPane.showMessageDialog(null, "Registro removido com sucesso");
					TicketSales.INSTANCE.changePanel(new ContentPanelInfo(ContentPanel.CREATE_SESSION));
				}
			});
		}
		
		return sessionPanel;
	}
	
	public static JPanel createListSessionPanel() {
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
				ContentPanelInfo contentPanelInfo = new ContentPanelInfo(ContentPanel.CREATE_SESSION);
				if (sessionTable.getSelectedRow() > -1) {
					contentPanelInfo.addInfo(EDIT_SESSION, sessionTableModel.getSessionAt(sessionTable.getSelectedRow()));
				}
				TicketSales.INSTANCE.changePanel(contentPanelInfo);
			}
		});
		
		return sessionPanel;
	}
}

class SessionTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final String[] COLUMNS = {"Evento", "Dia", "Hora Início"};
	
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
		}
		return null;
	}
	
	@Override
	public String getColumnName(int column) {
		return COLUMNS[column];
	}
}