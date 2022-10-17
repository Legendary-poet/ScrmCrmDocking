package com.nox.kol.exception;

public class KOLException extends RuntimeException{

	private static final long serialVersionUID = 4549161463974673813L;

	// 错误码
	private Integer errCode;
	// 显示的错误信息
	private String errMsg;
	// 堆栈内的详细错误信息
	private String traceMsg = "";

	public KOLException() {
		super();
	}

	public KOLException(String message, Throwable cause) {
		super(message, cause);
	}

	public KOLException(String message) {
		super(message);
	}

	public KOLException(Throwable cause) {
		super(cause);
	}

	public KOLException(Integer errCode, String errMsg) {
		super(errCode + ":" + errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public KOLException(Integer errCode, String errMsg, String traceMsg) {
		super(errCode + ":" + errMsg + ":" + traceMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
		this.traceMsg = traceMsg;
	}

	public Integer getErrCode() {
		return this.errCode;
	}

	public String getErrMsg() {
		return this.errMsg;
	}

	public String getTraceMsg() {
		return this.traceMsg;
	}	
	
	public String getErrorStr() {
		return this.errCode+" "+this.errMsg;
	}	

}
