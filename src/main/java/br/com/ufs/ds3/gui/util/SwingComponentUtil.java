package br.com.ufs.ds3.gui.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Properties;

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
		TEXT, DATE, TIME, INTEGER, COMBO, MONETARY;
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
	
	public JSpinner createAndAddMonetaryComponent(String label, String constraints) {
		return (JSpinner) createAndAddComponent(label, ComponentType.MONETARY, constraints);
	}
	
	public JComponent createAndAddComponent(String label, ComponentType componentType, String constraints) {
		return createAndAddComponent(label, componentType, this.parent, constraints);
	}
	
	public <T> JComponent createAndAddComponent(String label, ComponentType componentType, JComponent parent, String constraints) {
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
			JComboBox<T> combo = new JComboBox<T>(new CustomComboBoxModel<T>());
			combo.setRenderer(new CustomListCellRenderer());
			addNoSelectionOption(combo);
			component = combo;
			break;
		case MONETARY:
	        SpinnerNumberModel model = new SpinnerNumberModel(0d, 0d, null, 1d);
	        JSpinner spinner = new JSpinner(model);
	        JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor(spinner);
	        DecimalFormat format = numberEditor.getFormat();
	        format.setMinimumFractionDigits(2);
	        format.setMaximumFractionDigits(2);
	        format.setCurrency(Currency.getInstance("BRL"));
	        spinner.setEditor(numberEditor);
	        component = spinner;
	        break;
		default:
			throw new TicketSalesException("Componente desconhecido: " + componentType);
		}
		
		component.setName(label);
		parent.add(labelComponent);
		parent.add(component, constraints);
		return component;
	}
	
	public <T> void setComboModelValues(JComboBox<T> combo, Collection<T> values) {
		combo.removeAllItems();
		addNoSelectionOption(combo);
		for (T value : values) {
			combo.addItem(value);
		}
	}
	
	public <T> void setComboModelValues(JComboBox<T> combo, T[] values) {
		combo.removeAllItems();
		addNoSelectionOption(combo);
		for (T value : values) {
			combo.addItem(value);
		}
	}

	public void clearCombo(JComboBox<?> combo) {
		combo.removeAllItems();
		addNoSelectionOption(combo);
	}
	
	private void addNoSelectionOption(JComboBox<?> combo) {
		combo.addItem(null);
	}
}
