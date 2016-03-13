package br.com.ufs.ds3.gui.main;

import java.util.HashMap;
import java.util.Map;

public class ContentPanelInfo {
	public enum ContentPanel {
		INITIAL, CREATE_EVENT, LIST_EVENT, CREATE_SESSION, LIST_SESSION, CREATE_TICKET, LIST_TICKET,
		UPDATE_PHYSICAL_MAP;
	}
	
	private ContentPanel contentPanel;
	private Map<String, Object> info = new HashMap<>();
	
	public ContentPanelInfo(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
	}
	
	public Object getInfo(String key) {
		return info.get(key);
	}
	
	public void addInfo(String key, Object information) {
		info.put(key, information);
	}
	
	public ContentPanel getContentPanel() {
		return contentPanel;
	}
}
