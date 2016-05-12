package cmu.tecnico.ubibikeserver.domain.exceptions;

public class NotFoundException extends Exception {

	public NotFoundException(String resource) {
		super("No such resource " + resource);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 542607738787116168L;

}
