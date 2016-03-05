package br.com.ufs.ds3.exception;

public class TicketSalesException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TicketSalesException() {
    }

	public TicketSalesException(String message) {
        super(message);
    }

	public TicketSalesException(String message, Throwable cause) {
        super(message, cause);
    }

	public TicketSalesException(Throwable cause) {
        super(cause);
    }
}
