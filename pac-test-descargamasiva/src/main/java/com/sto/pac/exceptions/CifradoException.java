package com.sto.pac.exceptions;

public class CifradoException extends Exception {
	//private static final long serialVersionUID = 6955821626015527918L;
	
	public CifradoException() {
		super();
	}
	
	public CifradoException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public CifradoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CifradoException(String arg0) {
		super(arg0);
	}

	public CifradoException(Throwable arg0) {
		super(arg0);
	}
}