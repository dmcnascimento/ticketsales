package br.com.ufs.ds3.entity;

public enum SessionType {
	NUMBERED_CHAIR("Cadeira Numerada"), FREE_CHAIR("Cadeira Livre");
	
	private String label;
	
	private SessionType(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return label;
	}
	
	public static SessionType fromClass(Class<? extends Session> sessionClass) {
		if (sessionClass == NumberedChairSession.class) {
			return NUMBERED_CHAIR;
		} else if (sessionClass == FreeChairSession.class) {
			return SessionType.FREE_CHAIR;
		}
		return null;
	}
}
