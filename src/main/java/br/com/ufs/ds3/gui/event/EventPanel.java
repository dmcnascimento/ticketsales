package br.com.ufs.ds3.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.jdatepicker.DateModel;
import org.jdatepicker.impl.JDatePickerImpl;

import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.dao.TheatreDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Rating;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.gui.main.ContentPanelInfo;
import br.com.ufs.ds3.gui.main.ContentPanelInfo.ContentPanel;
import br.com.ufs.ds3.gui.main.TicketSales;
import br.com.ufs.ds3.gui.util.SwingComponentUtil;
import br.com.ufs.ds3.service.EventService;
import net.miginfocom.swing.MigLayout;

public class EventPanel {
	public static final String EDIT_EVENT = "editEvent";

	@SuppressWarnings("unchecked")
	public static JPanel createEventFormPanel(Event baseEvent) {
		EventService eventService = new EventService();
		
		JPanel eventPanel = new JPanel(new MigLayout());
		SwingComponentUtil swingComponentUtil = new SwingComponentUtil(eventPanel);
		JTextField titleField = swingComponentUtil.createAndAddTextComponent("Título", "span, growx, wrap, width 50:600:");
		JTextField descriptionField = swingComponentUtil.createAndAddTextComponent("Descrição", "span, growx, wrap, width 50:600:, height 100:100:");
		JComboBox<Rating> ratingCombo = swingComponentUtil.createAndAddComboComponent("Classificação", null);
		JSpinner durationSpinner = swingComponentUtil.createAndAddIntegerComponent("Duração (minutos)", "width 100:100:");
		JSpinner intervalDurationSpinner = swingComponentUtil.createAndAddIntegerComponent("Duração do intervalo (minutos)", "width 100:100:, wrap");
		JDatePickerImpl initialDateField = swingComponentUtil.createAndAddDateComponent("Data inicial", null);
		JDatePickerImpl endDateField = swingComponentUtil.createAndAddDateComponent("Data final", "wrap");

		swingComponentUtil.setComboModelValues(ratingCombo, Rating.values());
		
		JButton persistButton = new JButton("Gravar");
		eventPanel.add(persistButton, "x2 (container.w+pref)/2");
		persistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Event event = baseEvent == null ? new Event() : baseEvent;
				event.setTitle(titleField.getText());
				event.setDescription(descriptionField.getText());
				event.setRating((Rating) ratingCombo.getSelectedItem());
				event.setTheatre(TicketSales.INSTANCE.getCurrentTheatre());
				event.setDuration((Integer) durationSpinner.getValue());
				event.setIntervalDuration((Integer) intervalDurationSpinner.getValue());
				event.setStartDate((Date) initialDateField.getModel().getValue());
				event.setEndDate((Date) endDateField.getModel().getValue());
				
				try {
					if (event.getId() == null) {
						eventService.persist(event);
					} else {
						eventService.update(event);
					}
					JOptionPane.showMessageDialog(null, "Registro gravado com sucesso");
					TicketSales.INSTANCE.changePanel(new ContentPanelInfo(ContentPanel.CREATE_EVENT));
				} catch (TicketSalesException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		
		if (baseEvent != null) {
			titleField.setText(baseEvent.getTitle());
			descriptionField.setText(baseEvent.getDescription());
			ratingCombo.setSelectedItem(baseEvent.getRating());
			durationSpinner.setValue(baseEvent.getDuration());
			intervalDurationSpinner.setValue(baseEvent.getIntervalDuration());
			DateModel<Date> dateModel = (DateModel<Date>) initialDateField.getModel();
			dateModel.setValue(baseEvent.getStartDate());
			DateModel<Date> endDateModel = (DateModel<Date>) endDateField.getModel();
			endDateModel.setValue(baseEvent.getEndDate());
			
			JButton removeButton = new JButton("Remover");
			removeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new EventDao().remove(baseEvent);
					JOptionPane.showMessageDialog(null, "Registro removido com sucesso");
					TicketSales.INSTANCE.changePanel(new ContentPanelInfo(ContentPanel.CREATE_EVENT));
				}
			});
		}
		
		return eventPanel;
	}
	
	public static JPanel createListEventPanel() {
		JPanel eventPanel = new JPanel(new MigLayout());
		TheatreDao theatreDao = new TheatreDao();
		EventTableModel eventTableModel = new EventTableModel(theatreDao.listEventsFromTheatre(TicketSales.INSTANCE.getCurrentTheatre()));
		JTable eventTable = new JTable(eventTableModel);
		eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(eventTable);
		eventPanel.add(scrollPane, "span, grow, wrap, width 700:700:");
		
		JButton edit = new JButton("Editar");
		eventPanel.add(edit);
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ContentPanelInfo contentPanelInfo = new ContentPanelInfo(ContentPanel.CREATE_EVENT);
				if (eventTable.getSelectedRow() > -1) {
					contentPanelInfo.addInfo(EDIT_EVENT, eventTableModel.getEventAt(eventTable.getSelectedRow()));
				}
				TicketSales.INSTANCE.changePanel(contentPanelInfo);
			}
		});
		
		return eventPanel;
	}
}

class EventTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private static final String[] COLUMNS = {"Título"};
	
	private List<Event> events;

	public EventTableModel(List<Event> events) {
		this.events = events;
	}
	
	public Event getEventAt(int selectedRow) {
		return events.get(selectedRow);
	}

	@Override
	public int getRowCount() {
		return events.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Event event = events.get(rowIndex);
		if (columnIndex == 0) {
			return event.getTitle();
		}
		return null;
	}
	
	@Override
	public String getColumnName(int column) {
		return COLUMNS[column];
	}
}