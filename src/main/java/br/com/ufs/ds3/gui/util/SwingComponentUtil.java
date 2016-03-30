package br.com.ufs.ds3.gui.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.DateFormatter;

import org.jdatepicker.DateModel;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.gui.session.SessionPanel;

public class SwingComponentUtil {
	public static enum ComponentType {
		TEXT, DATE, TIME, INTEGER, COMBO;
	}

	private JComponent parent;
	
	public SwingComponentUtil() {
	}
	
	public SwingComponentUtil(JComponent parent) {
		this.parent = parent;
	}
	
	public JTextField createAndAddTextComponent(String label, String constraints) {
		return (JTextField) createAndAddComponent(label, ComponentType.TEXT, constraints);
	}
	
	public JDatePickerImpl createAndAddDateComponent(String label, String constraints) {
		return (JDatePickerImpl) createAndAddComponent(label, ComponentType.DATE, constraints);
	}
	
	public JSpinner createAndAddTimeComponent(String label, String constraints) {
		return (JSpinner) createAndAddComponent(label, ComponentType.TIME, constraints);
	}
	
	public JSpinner createAndAddIntegerComponent(String label, String constraints) {
		return (JSpinner) createAndAddComponent(label, ComponentType.INTEGER, constraints);
	}
	
	@SuppressWarnings("unchecked")
	public <T> JComboBox<T> createAndAddComboComponent(String label, String constraints) {
		return (JComboBox<T>) createAndAddComponent(label, ComponentType.COMBO, constraints);
	}
	
	public JComponent createAndAddComponent(String label, ComponentType componentType, String constraints) {
		return createAndAddComponent(label, componentType, this.parent, constraints);
	}
	
	public JComponent createAndAddComponent(String label, ComponentType componentType, JComponent parent, String constraints) {
		JLabel labelComponent = new JLabel(label);
		JComponent component;
		switch (componentType) {
		case TEXT:
			component = new JTextField();
			break;
		case DATE:
			DateModel<Date> dateModel = new UtilDateModel();
			Properties datei18n = new Properties();
			try {
				datei18n.load(SessionPanel.class.getResourceAsStream("/org/jdatepicker/i18n/Text_pt.properties"));
			} catch (IOException e) {
				throw new TicketSalesException(e);
			}
			JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, datei18n);
			component = new JDatePickerImpl(datePanel, new DateComponentFormatter());
			break;
		case TIME:
			Calendar initialHour = Calendar.getInstance();
			initialHour.set(1970, 0, 1, 0, 0, 0);
			initialHour.set(Calendar.MILLISECOND, 0);
			SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
			spinnerDateModel.setValue(initialHour.getTime());
			component = new JSpinner(spinnerDateModel);
			JSpinner.DateEditor editor = new JSpinner.DateEditor((JSpinner) component, "HH:mm");
			DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();
			formatter.setAllowsInvalid(false);
			formatter.setOverwriteMode(true);
			((JSpinner) component).setEditor(editor);
			break;
		case INTEGER:
			SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(0, 0, null, 10);
			component = new JSpinner(spinnerNumberModel);
			break;
		case COMBO:
			component = new JComboBox<>(new DefaultComboBoxModel<>());
			break;
		default:
			throw new TicketSalesException("Componente desconhecido: " + componentType);
		}
		
		parent.add(labelComponent);
		parent.add(component, constraints);
		
		return component;
	}
}
