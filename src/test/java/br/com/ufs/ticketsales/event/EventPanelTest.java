package br.com.ufs.ticketsales.event;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.launcher.ApplicationLauncher;
import org.assertj.swing.timing.Timeout;
import org.jdatepicker.DateModel;
import org.jdatepicker.impl.JDatePickerImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.ufs.ds3.Main;
import br.com.ufs.ds3.db.DB;

public class EventPanelTest extends AssertJSwingJUnitTestCase {
	
	private FrameFixture frameFixture;
	
	@SuppressWarnings("unchecked")
	@Test
	public void createEvent() {
		frameFixture.menuItemWithPath("Evento", "Criar").click();
		frameFixture.textBox("Título").enterText("Teste");
		frameFixture.textBox("Descrição").enterText("Teste Descrição");
		frameFixture.comboBox("Classificação").selectItem(1);
		frameFixture.spinner("Duração (minutos)").enterTextAndCommit("120");
		frameFixture.spinner("Duração do intervalo (minutos)").enterTextAndCommit("30");
		
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				Calendar calendar = Calendar.getInstance();
				DateModel<Date> model = (DateModel<Date>) frameFixture.panel("Data inicial").targetCastedTo(JDatePickerImpl.class).getModel();
				model.setValue(calendar.getTime());
				calendar.add(Calendar.MONTH, 2);
				model = (DateModel<Date>) frameFixture.panel("Data final").targetCastedTo(JDatePickerImpl.class).getModel();
				model.setValue(calendar.getTime());
			}
		});
		
		frameFixture.button("buttonGravar").click();
		frameFixture.optionPane(Timeout.timeout()).requireMessage("Registro gravado com sucesso");
	}
	
	@Test
	public void errorOnCreateEvent() {
		frameFixture.menuItemWithPath("Evento", "Criar").click();
		frameFixture.textBox("Título").enterText("Teste");
		frameFixture.textBox("Descrição").enterText("Teste Descrição");
		frameFixture.comboBox("Classificação").selectItem(1);
		frameFixture.spinner("Duração (minutos)").enterTextAndCommit("120");
		frameFixture.spinner("Duração do intervalo (minutos)").enterTextAndCommit("30");
		
		frameFixture.button("buttonGravar").click();
		frameFixture.optionPane(Timeout.timeout()).requireMessage("A data inicial do evento deve ser informada");
	}

	@Override
	protected void onSetUp() {
		ApplicationLauncher.application(Main.class).start();
		frameFixture = WindowFinder.findFrame(JFrame.class).using(robot());
	}
	
	@BeforeClass
	public static void setupTestPersistenceUnit() {
		DB.setPersistenceUnit("ticketsalesTestPU");
	}
}
