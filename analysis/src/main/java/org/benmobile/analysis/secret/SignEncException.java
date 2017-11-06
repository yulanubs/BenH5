package org.benmobile.analysis.secret;

public class SignEncException extends Exception {
	
	private static final long serialVersionUID = 8564389106566648956L;

	public SignEncException(){
    }

    /**
     *
     * @param s
     */
    public SignEncException(String s){
        super(s);
    }

    /**
     *
     * @param p0
     */
    public SignEncException(Throwable p0){
    }
}
