package com.haggle.run;

import java.util.ArrayList;

public class LinkTuple {

	public int firstNode;
	public int secondNode;
	public int startTime;
	public int stopTime;

	public LinkTuple(int _firstNode, int _secondNode, int _startTime,
			int _stopTime) {
		firstNode = _firstNode;
		secondNode = _secondNode;
		startTime = _startTime;
		stopTime = _stopTime;
	}

	static LinkTuple[] parseLinkTuples(String[] trace) {

		LinkTuple[] tmp, retval;
		int i, j;

		tmp = new LinkTuple[trace.length];
		j = 0;
		for (i = 0; i < trace.length; i++) {
			String[] brokenDown = trace[i].split("\\s+");
			if (brokenDown.length >= 4) {
				tmp[j] = new LinkTuple(Integer.parseInt(brokenDown[0]),
						Integer.parseInt(brokenDown[1]),
						Integer.parseInt(brokenDown[2]),
						Integer.parseInt(brokenDown[3]));
				j++;
			}
		}

		if (j != trace.length) {
			retval = new LinkTuple[j];
			for (i = 0; i < j; i++)
				retval[i] = tmp[i];
		} else
			retval = tmp;
		return retval;
	}

	static LinkTuple[] preprocess(LinkTuple[] tuple) {

		if (tuple.length == 0)
			return tuple;

		int i, j, a, b, minNode, maxNode;
		int minTime, maxTime, startTime, stopTime;
		int[] connectivityMap;
		ArrayList<LinkTuple> retval = new ArrayList<LinkTuple>();

		minTime = tuple[0].startTime;
		maxTime = tuple[0].startTime;
		minNode = tuple[0].firstNode;
		maxNode = tuple[0].firstNode;

		for (i = 0; i < tuple.length; i++) {
			if (minTime > tuple[i].startTime)
				minTime = tuple[i].startTime;
			if (minTime > tuple[i].stopTime)
				minTime = tuple[i].stopTime;
			if (maxTime < tuple[i].startTime)
				maxTime = tuple[i].startTime;
			if (maxTime < tuple[i].stopTime)
				maxTime = tuple[i].stopTime;

			if (minNode > tuple[i].firstNode)
				minNode = tuple[i].firstNode;
			if (minNode > tuple[i].secondNode)
				minNode = tuple[i].secondNode;
			if (maxNode < tuple[i].firstNode)
				maxNode = tuple[i].firstNode;
			if (maxNode < tuple[i].secondNode)
				maxNode = tuple[i].secondNode;
		}

		connectivityMap = new int[maxTime - minTime + 1];

		for (a = minNode; a <= maxNode; a++)
			for (b = a + 1; b <= maxNode; b++) {

				for (i = 0; i < connectivityMap.length; i++)
					connectivityMap[i] = 0;

				for (j = 0; j < tuple.length; j++) {
					if (tuple[j].firstNode == a && tuple[j].secondNode == b) {
						for (i = tuple[j].startTime; i < tuple[j].stopTime; i++)
							connectivityMap[i - minTime] = connectivityMap[i
									- minTime] | 1;
					}
					if (tuple[j].firstNode == b && tuple[j].secondNode == a) {
						for (i = tuple[j].startTime; i < tuple[j].stopTime; i++)
							connectivityMap[i - minTime] = connectivityMap[i
									- minTime] | 2;
					}
				}

				// FIXME: call other function to do different things here...
				// j is the value of the previous element in the connectivity
				// map.

				j = 0;
				startTime = 0;
				stopTime = 0;
				for (i = 0; i < connectivityMap.length; i++) {
					if (j == 0 && connectivityMap[i] != 0) {
						startTime = i + minTime;
						j = connectivityMap[i];
					}
					if (j != 0 && connectivityMap[i] == 0) {
						stopTime = i + minTime;
						j = connectivityMap[i];
						retval.add(new LinkTuple(a, b, startTime, stopTime));
					}
				}
				if (j != 0) {
					stopTime = connectivityMap.length + minTime;
					retval.add(new LinkTuple(a, b, startTime, stopTime));
				}
			}

		return retval.toArray(new LinkTuple[0]);
	}
}