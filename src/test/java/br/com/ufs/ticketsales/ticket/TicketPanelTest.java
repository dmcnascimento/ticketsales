package br.com.ufs.ticketsales.ticket;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.launcher.ApplicationLauncher;
import org.assertj.swing.timing.Timeout;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.ufs.ds3.Main;
import br.com.ufs.ds3.db.DB;
import br.com.ufs.ticketsales.fixtures.TicketSaleFixture;

public class TicketPanelTest extends AssertJSwingJUnitTestCase {

	private FrameFixture frameFixture;
	
	@Test
	public void sellTicket() {
		frameFixture.menuItemWithPath("Ingresso", "Vender").click();
		frameFixture.comboBox("Evento").selectItem(1);
		frameFixture.comboBox("Sessão").selectItem(1);
		JPanel chairPanel = frameFixture.panel("chairPanel").target();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				int totalCadeirasSelecionadas = 0;
				for (Component component : chairPanel.getComponents()) {
					if (component instanceof JCheckBox) {
						JCheckBox checkBox = (JCheckBox) component;
						if (checkBox.isEnabled() && !checkBox.isSelected()) {
							checkBox.setSelected(true);
							totalCadeirasSelecionadas++;
						}
						if (totalCadeirasSelecionadas == 3) {
							break;
						}
					}
				}
			}
		});
		
		frameFixture.spinner("Quantidade (inteira)").requireValue(3);
		frameFixture.spinner("Quantidade (meia)").requireValue(0);
		frameFixture.spinner("Quantidade (meia)").enterTextAndCommit("1");
		frameFixture.spinner("Quantidade (inteira)").requireValue(2);
		frameFixture.textBox("Total").requireText("18,75");
		frameFixture.button("buttonGravar").click();
		frameFixture.optionPane(Timeout.timeout()).requireMessage("Registro gravado com sucesso");
	}
	
	@Test
	public void validateZeroTicket() {
		frameFixture.menuItemWithPath("Ingresso", "Vender").click();
		frameFixture.comboBox("Evento").selectItem(1);
		frameFixture.comboBox("Sessão").selectItem(1);
		
		frameFixture.spinner("Quantidade (inteira)").requireValue(0);
		frameFixture.spinner("Quantidade (meia)").requireValue(0);
		frameFixture.textBox("Total").requireText("");
		frameFixture.button("buttonGravar").click();
		frameFixture.optionPane(Timeout.timeout()).requireMessage("A quantidade de ingressos deve ser maior do que 0");
	}
	
	@Override
	protected void onSetUp() {
		ApplicationLauncher.application(Main.class).start();
		frameFixture = WindowFinder.findFrame(JFrame.class).using(robot());
	}

	@BeforeClass
	public static void setupTestPersistenceUnit() {
		DB.setPersistenceUnit("ticketsalesTestPU");
		new TicketSaleFixture().create();
	}
}
