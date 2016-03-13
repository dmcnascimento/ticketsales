package br.com.ufs.ds3.gui.session;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.DateFormatter;

import org.jdatepicker.DateModel;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.gui.main.ContentPanelInfo;
import br.com.ufs.ds3.gui.main.ContentPanelInfo.ContentPanel;
import br.com.ufs.ds3.gui.main.TicketSales;
import net.miginfocom.swing.MigLayout;

public class SessionPanel {
	public static final String EDIT_SESSION = "editSession";

	public static JPanel createSessionFormPanel(Session baseSession) {
		JPanel sessionPanel = new JPanel(new MigLayout());
		
		JLabel eventLabel = new JLabel("Evento");
		JComboBox<Event> eventCombo = new JComboBox<>(new EventDao().listEventsFromTheatre(TicketSales.INSTANCE.getCurrentTheatre()).toArray(new Event[]{}));
		sessionPanel.add(eventLabel);
		sessionPanel.add(eventCombo, "growx, wrap");
		
		JLabel dayLabel = new JLabel("Dia");
		DateModel<Date> dateModel = new UtilDateModel();
		Properties datei18n = new Properties();
		try {
			datei18n.load(SessionPanel.class.getResourceAsStream("/org/jdatepicker/i18n/Text_pt.properties"));
		} catch (IOException e) {
			throw new TicketSalesException(e);
		}
		JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, datei18n);
		JDatePickerImpl dayField = new JDatePickerImpl(datePanel, new DateComponentFormatter());
		sessionPanel.add(dayLabel);
		sessionPanel.add(dayField);
		
		Calendar initialHour = Calendar.getInstance();
		initialHour.set(1970, 0, 1, 0, 0, 0);
		initialHour.set(Calendar.MILLISECOND, 0);
		JLabel labelStartHour = new JLabel("Hora Início");
		sessionPanel.add(labelStartHour);
		SpinnerDateModel spinnerStartHourModel = new SpinnerDateModel();
		spinnerStartHourModel.setValue(initialHour.getTime());
		JSpinner spinnerStartHour = new JSpinner(spinnerStartHourModel);
		JSpinner.DateEditor editorStartHour = new JSpinner.DateEditor(spinnerStartHour, "HH:mm");
		DateFormatter formatter = (DateFormatter) editorStartHour.getTextField().getFormatter();
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
		spinnerStartHour.setEditor(editorStartHour);
		sessionPanel.add(spinnerStartHour);
		
		JLabel labelEndHour = new JLabel("Hora Fim");
		sessionPanel.add(labelEndHour);
		SpinnerDateModel spinnerEndHourModel = new SpinnerDateModel();
		spinnerEndHourModel.setValue(initialHour.getTime());
		JSpinner spinnerEndHour = new JSpinner(spinnerEndHourModel);
		JSpinner.DateEditor editorEndHour = new JSpinner.DateEditor(spinnerEndHour, "HH:mm");
		formatter = (DateFormatter) editorEndHour.getTextField().getFormatter();
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
		spinnerEndHour.setEditor(editorEndHour);
		sessionPanel.add(spinnerEndHour, "wrap");
		
		JButton persistButton = new JButton("Gravar");
		sessionPanel.add(persistButton, "x2 (container.w+pref)/2");
		persistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Session session = baseSession == null ? new Session() : baseSession;
				session.setDay(dateModel.getValue());
				session.setStartHour(spinnerStartHourModel.getDate());
				session.setEndHour(spinnerEndHourModel.getDate());
				session.setEvent((Event) eventCombo.getSelectedItem());
				
				if (session.getId() == null) {
					new SessionDao().persist(session);
				} else {
					new SessionDao().update(session);
				}
				
				JOptionPane.showMessageDialog(null, "Registro gravado com sucesso");
				TicketSales.INSTANCE.changePanel(new ContentPanelInfo(ContentPanel.CREATE_SESSION));
			}
		});
		
		if (baseSession != null) {
			dateModel.setValue(baseSession.getDay());
			eventCombo.setSelectedItem(baseSession.getEvent());
			spinnerStartHourModel.setValue(baseSession.getStartHour());
			spinnerEndHourModel.setValue(baseSession.getEndHour());
			
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