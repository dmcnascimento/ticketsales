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
		
		JMenu teatro = new JMenu("Teatro");
		menuBar.add(teatro);
		teatro.add(new CrudMenuBarAction("Atualizar Mapa Físico", ContentPanel.INITIAL));
		
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
		ingresso.add(new CrudMenuBarAction("Vender", ContentPanel.CREATE_TICKET));
		ingresso.add(new CrudMenuBarAction("Cancelar", ContentPanel.LIST_TICKET));
		
		JMenu convidados = new JMenu("Convidados");
		menuBar.add(convidados);
		convidados.add(new CrudMenuBarAction("Reservar Cadeiras", ContentPanel.INITIAL));
		convidados.add(new CrudMenuBarAction("Emitir Ingressos", ContentPanel.INITIAL));
		convidados.add(new CrudMenuBarAction("Cancelar Ingresso", ContentPanel.INITIAL));
		
		JMenu consultas = new JMenu("Consultas");
		menuBar.add(consultas);
		consultas.add(new CrudMenuBarAction("Mapa de Ocupação da Sessão", ContentPanel.INITIAL));
		consultas.add(new CrudMenuBarAction("Ingressos Vendidos", ContentPanel.INITIAL));
		consultas.add(new CrudMenuBarAction("Receita", ContentPanel.INITIAL));
		
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