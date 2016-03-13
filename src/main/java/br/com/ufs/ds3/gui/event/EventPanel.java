package br.com.ufs.ds3.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;

import br.com.ufs.ds3.dao.EventDao;
import br.com.ufs.ds3.dao.SessionDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Rating;
import br.com.ufs.ds3.gui.main.ContentPanelInfo;
import br.com.ufs.ds3.gui.main.ContentPanelInfo.ContentPanel;
import br.com.ufs.ds3.gui.main.TicketSales;
import net.miginfocom.swing.MigLayout;

public class EventPanel {
	public static final String EDIT_EVENT = "editEvent";

	public static JPanel createEventFormPanel(Event baseEvent) {
		JPanel eventPanel = new JPanel(new MigLayout());
		JLabel titleLabel = new JLabel("Título");
		JTextField titleField = new JTextField();
		eventPanel.add(titleLabel);
		eventPanel.add(titleField, "span, growx, wrap, width 50:600:");
		
		JLabel descriptionLabel = new JLabel("Descrição");
		JTextArea descriptionField = new JTextArea();
		eventPanel.add(descriptionLabel);
		eventPanel.add(descriptionField, "span, growx, wrap, width 50:600:, height 100:100:");
		
		JLabel ratingLabel = new JLabel("Classificação");
		JComboBox<Rating> ratingCombo = new JComboBox<>(Rating.values());
		eventPanel.add(ratingLabel);
		eventPanel.add(ratingCombo);
		
		JLabel durationLabel = new JLabel("Duração");
		SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0, 0, null, 10);
		JSpinner durationSpinner = new JSpinner(spinnerNumberModel);
		eventPanel.add(durationLabel);
		eventPanel.add(durationSpinner, "width 100:100:, wrap");
		
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
				
				if (event.getId() == null) {
					new EventDao().persist(event);
				} else {
					new EventDao().update(event);
				}
				
				JOptionPane.showMessageDialog(null, "Registro gravado com sucesso");
				TicketSales.INSTANCE.changePanel(new ContentPanelInfo(ContentPanel.CREATE_EVENT));
			}
		});
		
		if (baseEvent != null) {
			titleField.setText(baseEvent.getTitle());
			descriptionField.setText(baseEvent.getDescription());
			ratingCombo.setSelectedItem(baseEvent.getRating());
			durationSpinner.setValue(baseEvent.getDuration());
			
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
		EventTableModel eventTableModel = new EventTableModel(new EventDao().listEventsFromTheatre(TicketSales.INSTANCE.getCurrentTheatre()));
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
	private static final String[] COLUMNS = {"Nome"};
	
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