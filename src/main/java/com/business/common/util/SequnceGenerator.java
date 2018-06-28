package com.business.common.util;

import java.util.concurrent.atomic.AtomicLong;

public class SequnceGenerator {

	private static final AtomicLong sequence = new AtomicLong(
			System.currentTimeMillis() / 1000);

	public static long getNext() {
		return sequence.incrementAndGet();
	}

}
