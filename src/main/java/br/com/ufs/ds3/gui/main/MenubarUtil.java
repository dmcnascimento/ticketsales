package br.com.ufs.ds3.gui.main;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import br.com.ufs.ds3.gui.main.ContentPanelInfo.ContentPanel;

class MenubarUtil {
	static JMenuBar createMenubar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu evento = new JMenu("Evento");
		menuBar.add(evento);
		evento.add(new CrudMenuBarAction("Criar", ContentPanel.CREATE_EVENT));
		evento.add(new CrudMenuBarAction("Listar", ContentPanel.LIST_EVENT));
		
		JMenu sessao = new JMenu("Sessão");
		menuBar.add(sessao);
		sessao.add(new CrudMenuBarAction("Criar", ContentPanel.CREATE_SESSION));
		sessao.add(new CrudMenuBarAction("Listar", ContentPanel.LIST_SESSION));
		
		JMenu ingresso = new JMenu("Ingresso");
		menuBar.add(ingresso);
		ingresso.add(new CrudMenuBarAction("Comprar", ContentPanel.CREATE_TICKET));
		ingresso.add(new CrudMenuBarAction("Listar", ContentPanel.LIST_TICKET));
		
		return menuBar;
	}
}

class CrudMenuBarAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private ContentPanel contentPanel;
	
	public CrudMenuBarAction(String label, ContentPanel contentPanel) {
		putValue(Action.NAME, label);
		this.contentPanel = contentPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TicketSales.INSTANCE.changePanel(new ContentPanelInfo(contentPanel));
	}
}