package br.com.ufs.ds3.gui.ticket;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import br.com.ufs.ds3.bean.ChairAvailability;
import br.com.ufs.ds3.bean.ChairAvailability.ChairStatus;
import br.com.ufs.ds3.bean.OccupationMap;
import br.com.ufs.ds3.bean.TicketSaleBean;
import br.com.ufs.ds3.bean.TicketSaleBean.TicketInfo;
import br.com.ufs.ds3.dao.TicketDao;
import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.NumberedChairSession;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.Session;
import br.com.ufs.ds3.entity.SessionType;
import br.com.ufs.ds3.entity.Ticket;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.gui.main.ContentPanelInfo;
import br.com.ufs.ds3.gui.main.ContentPanelInfo.ContentPanel;
import br.com.ufs.ds3.gui.main.TicketSales;
import br.com.ufs.ds3.gui.util.SwingComponentUtil;
import br.com.ufs.ds3.service.SessionService;
import br.com.ufs.ds3.service.TicketService;
import net.miginfocom.swing.MigLayout;

public class TicketPanel {

	public static JPanel createTicketFormPanel() {
		JPanel ticketPanel = new JPanel(new MigLayout());
		SwingComponentUtil swingComponentUtil = new SwingComponentUtil(ticketPanel);
		SessionService sessionService = new SessionService();
		TicketService ticketService = new TicketService();
		
		JComboBox<Event> eventCombo = swingComponentUtil.createAndAddComboComponent("Evento", "growx, wrap");
		JComboBox<Session> sessionCombo = swingComponentUtil.createAndAddComboComponent("Sessão", "growx, wrap");
		
		JLabel chairLabel = new JLabel("Cadeiras");
		ticketPanel.add(chairLabel);
		JScrollPane chairScrollPane = new JScrollPane();
		ticketPanel.add(chairScrollPane, "wrap, width 300:500:, height 150:150:");
		
		JSpinner spinnerInteira = swingComponentUtil.createAndAddIntegerComponent("Quantidade (inteira)", null);
		JSpinner spinnerMeia = swingComponentUtil.createAndAddIntegerComponent("Quantidade (meia)", "wrap");
		SpinnerNumberModel modelInteira = (SpinnerNumberModel) spinnerInteira.getModel();
		SpinnerNumberModel modelMeia = (SpinnerNumberModel) spinnerMeia.getModel();
		modelInteira.setStepSize(1);
		modelMeia.setStepSize(1);
		modelInteira.setMaximum(0);
		modelMeia.setMaximum(0);
		JTextField priceField = swingComponentUtil.createAndAddTextComponent("Total", "wrap, width 75:75:");
		priceField.setEditable(false);
		
		ChangeListener spinnerChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Session session = (Session) sessionCombo.getSelectedItem();
				if (SessionType.fromClass(session.getClass()) == SessionType.NUMBERED_CHAIR) {
					if (e.getSource() == spinnerMeia) {
						int max = (int) modelMeia.getMaximum();
						int valorMeia = (int) modelMeia.getValue();
						int valorInteira = (int) modelInteira.getValue();
						if (valorInteira + valorMeia < max) {
							modelInteira.setValue(valorInteira + 1);
						} else if (valorInteira + valorMeia > max) {
							modelInteira.setValue(valorInteira - 1);
						}
					}
				}
				
				int totalInteiras = (int) spinnerInteira.getValue();
				int totalMeias = (int) spinnerMeia.getValue();
				Price price = sessionService.getPriceForSession((Session) sessionCombo.getSelectedItem());

				NumberFormat numberFormat = NumberFormat.getNumberInstance();
				numberFormat.setMaximumFractionDigits(2);
				numberFormat.setMinimumFractionDigits(2);
				priceField.setText(numberFormat.format(ticketService.calculatePrice(price.getTicketPrice(), totalInteiras, totalMeias).doubleValue()));
			}
		};
		
		spinnerInteira.addChangeListener(spinnerChangeListener);
		spinnerMeia.addChangeListener(spinnerChangeListener);
		
		swingComponentUtil.setComboModelValues(eventCombo, TicketSales.INSTANCE.getCurrentTheatre().getEvents());
		
		eventCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (eventCombo.getSelectedItem() != null) {
						Event event = (Event) eventCombo.getSelectedItem();
						swingComponentUtil.setComboModelValues(sessionCombo, event.getSessions());
					} else {
						swingComponentUtil.clearCombo(sessionCombo);
					}
				}
			}
		});

		List<ChairAvailability> selectedChairs = new ArrayList<>();
		sessionCombo.addItemListener(new ItemListener() {
			private JPanel chairPanel;
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (sessionCombo.getSelectedItem() != null) {
						Session session = (Session) sessionCombo.getSelectedItem();
						try {
							sessionService.getPriceForSession(session);
						} catch (TicketSalesException ex) {
							JOptionPane.showMessageDialog(null, ex.getMessage());
						}
						if (SessionType.fromClass(session.getClass()) == SessionType.NUMBERED_CHAIR) {
							chairPanel = createChairPanel((NumberedChairSession) session);
							chairScrollPane.setViewportView(chairPanel);
							modelInteira.setMaximum(0);
							modelMeia.setMaximum(0);
							spinnerInteira.setEnabled(false);
						} else {
							modelInteira.setMaximum(null);
							modelMeia.setMaximum(null);
							spinnerInteira.setEnabled(true);
							chairScrollPane.setViewportView(null);
						}
					} else {
						modelInteira.setMaximum(null);
						modelMeia.setMaximum(null);
						spinnerInteira.setEnabled(true);
						chairScrollPane.setViewportView(null);
						priceField.setText("");
					}
					modelInteira.setValue(0);
					modelMeia.setValue(0);
				}
			}

			private JPanel createChairPanel(NumberedChairSession session) {
				OccupationMap occupationMap = new SessionService().generateOccupationMap(session);
				JPanel panel = new JPanel(new GridLayout(occupationMap.getRows(), occupationMap.getCols()));
				
				for (ChairAvailability chairAvailability : occupationMap.getChairAvailabilities()) {
					JCheckBox checkBox = new JCheckBox();
					panel.add(checkBox);
					if (chairAvailability.getChairStatus() != ChairStatus.FREE) {
						checkBox.setEnabled(false);
					} else {
						checkBox.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (checkBox.isSelected()) {
									spinnerInteira.setValue(((Integer) spinnerInteira.getValue()) + 1);
									selectedChairs.add(chairAvailability);
								} else {
									if (((Integer) spinnerInteira.getValue()) > 0) {
										spinnerInteira.setValue(((Integer) spinnerInteira.getValue()) - 1);
									} else {
										spinnerMeia.setValue(((Integer) spinnerMeia.getValue()) - 1);
									}
									selectedChairs.remove(chairAvailability);
								}
								
								int max = ((Integer) modelInteira.getValue()) + ((Integer) modelMeia.getValue());
								modelInteira.setMaximum(max);
								modelMeia.setMaximum(max);
							}
						});
					}
				}
				
				return panel;
			}
		});
		
		JButton persistButton = new JButton("Gravar");
		ticketPanel.add(persistButton, "x2 (container.w+pref)/2");
		persistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					TicketSaleBean ticketSaleBean = new TicketSaleBean();
					Session session = (Session) sessionCombo.getSelectedItem();
					ticketSaleBean.setBasePrice(sessionService.getPriceForSession(session).getTicketPrice());
					ticketSaleBean.setSession(session);
					for (int i = 0; i < (int) modelInteira.getValue(); i++) {
						TicketInfo ticketInfo = new TicketInfo();
						ticketSaleBean.getFullPriceTickets().add(ticketInfo);
					}
					for (int i = 0; i < (int) modelMeia.getValue(); i++) {
						TicketInfo ticketInfo = new TicketInfo();
						ticketSaleBean.getHalfPriceTickets().add(ticketInfo);
					}
					
					if (SessionType.fromClass(session.getClass()) == SessionType.NUMBERED_CHAIR) {
						Iterator<ChairAvailability> it = selectedChairs.iterator();
						for (TicketInfo ticketInfo : ticketSaleBean.getFullPriceTickets()) {
							ChairAvailability chairAvailability = it.next();
							ticketInfo.setChair(chairAvailability.getChair());
						}
						for (TicketInfo ticketInfo : ticketSaleBean.getHalfPriceTickets()) {
							ChairAvailability chairAvailability = it.next();
							ticketInfo.setChair(chairAvailability.getChair());
						}
					}
					
					ticketService.sellTickets(ticketSaleBean);
					JOptionPane.showMessageDialog(null, "Registro gravado com sucesso");
					TicketSales.INSTANCE.changePanel(new ContentPanelInfo(ContentPanel.CREATE_TICKET));
				} catch (TicketSalesException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		
		return ticketPanel;
	}
	
	public static JPanel createListTicketPanel() {
		JPanel ticketPanel = new JPanel(new MigLayout());
		SwingComponentUtil swingComponentUtil = new SwingComponentUtil(ticketPanel);
		
		JLabel eventLabel = new JLabel("Evento");
		JComboBox<Event> eventCombo = new JComboBox<>(TicketSales.INSTANCE.getCurrentTheatre().getEvents().toArray(new Event[] {}));
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
					if (eventCombo.getSelectedItem() != null) {
						Event event = (Event) eventCombo.getSelectedItem();
						swingComponentUtil.setComboModelValues(sessionCombo, event.getSessions());
					} else {
						swingComponentUtil.clearCombo(sessionCombo);
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
	private static final String[] COLUMNS = {"Evento", "Número", "Dia", "Hora Início"};
	
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