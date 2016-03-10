package br.com.ufs.ds3.entity;

import br.com.ufs.ds3.exception.TicketSalesException;

public enum Rating {
	CL, C14, C16, C18;
	
	public String toString() {
		switch (this) {
		case CL:
			return "Livre";
		case C14:
			return "Maiores de 14 anos";
		case C16:
			return "Maiores de 16 anos";
		case C18:
			return "Maiores de 18 anos";
		default:
			throw new TicketSalesException("No label for enum constant " + name());
		}
	};
}
