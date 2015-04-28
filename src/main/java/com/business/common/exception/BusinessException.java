package com.business.common.exception;

/**
 * 
 * @author Vasanth
 * 
 *         It Throws CustomException
 * 
 */

public class BusinessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException() {

	}

	public BusinessException(String message) {
		super(message);
	}
}
