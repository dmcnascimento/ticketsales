package br.com.ufs.ds3.entity;

public enum TicketType {
	PAID_TICKET("Pago"), INVITE_TICKET("Convite");
	
	private String label;
	
	private TicketType(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return label;
	}
	
	public static TicketType fromClass(Class<? extends Ticket> ticketClass) {
		if (ticketClass == PaidTicket.class) {
			return PAID_TICKET;
		} else if (ticketClass == InviteTicket.class) {
			return INVITE_TICKET;
		}
		return null;
	}
}
