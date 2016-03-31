package br.com.ufs.ds3.gui.event.price;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import br.com.ufs.ds3.entity.Event;
import br.com.ufs.ds3.entity.Price;
import br.com.ufs.ds3.entity.WeekDay;
import br.com.ufs.ds3.exception.TicketSalesException;
import br.com.ufs.ds3.gui.main.ContentPanelInfo;
import br.com.ufs.ds3.gui.main.ContentPanelInfo.ContentPanel;
import br.com.ufs.ds3.gui.main.TicketSales;
import br.com.ufs.ds3.gui.util.SwingComponentUtil;
import br.com.ufs.ds3.service.PriceService;
import net.miginfocom.swing.MigLayout;

public class PricePanel {
	public static JPanel createPriceFormPanel() {
		JPanel pricePanel = new JPanel(new MigLayout());
		SwingComponentUtil swingComponentUtil = new SwingComponentUtil(pricePanel);
		
		JComboBox<Event> eventCombo = swingComponentUtil.createAndAddComboComponent("Evento", "growx, wrap");
		JComboBox<WeekDay> dayCombo = swingComponentUtil.createAndAddComboComponent("Dia", null);
		JSpinner spinnerStartHour = swingComponentUtil.createAndAddTimeComponent("Hora inicial", null);
		JSpinner spinnerEndHour = swingComponentUtil.createAndAddTimeComponent("Hora final", "wrap");
		JSpinner spinnerPrice = swingComponentUtil.createAndAddMonetaryComponent("Valor", "growx");
		
		swingComponentUtil.setComboModelValues(eventCombo, TicketSales.INSTANCE.getCurrentTheatre().getEvents());
		swingComponentUtil.setComboModelValues(dayCombo, WeekDay.values());
		
		JButton persistButton = new JButton("Gravar");
		pricePanel.add(persistButton, "x2 (container.w+pref)/2");
		persistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Price price = new Price();
				price.setEvent((Event) eventCombo.getSelectedItem());
				price.setWeekDay((WeekDay) dayCombo.getSelectedItem());
				price.setStartHour((Date) spinnerStartHour.getValue());
				price.setEndHour((Date) spinnerEndHour.getValue());
				price.setTicketPrice(new BigDecimal((Double) spinnerPrice.getValue()));
				
				try {
					new PriceService().persist(price);
					JOptionPane.showMessageDialog(null, "Registro gravado com sucesso");
					TicketSales.INSTANCE.changePanel(new ContentPanelInfo(ContentPanel.CREATE_SESSION));
				} catch (TicketSalesException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
			}
		});
		
		return pricePanel;
	}
}
