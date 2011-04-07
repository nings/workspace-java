package com.haggle.run;

import java.util.Comparator;
import com.haggle.run.Action;

public class ActionComparator implements Comparator<Action> {
	public final int compare(Action a, Action b) {
		return (int) (a.timestamp - b.timestamp);
	}
}