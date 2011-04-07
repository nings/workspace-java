package com.haggle.run;

public class Action {

	public long timestamp;
	public String cmd;

	public Action(long timestamp, String cmd) {
		this.timestamp = timestamp;
		this.cmd = cmd;
	}

}