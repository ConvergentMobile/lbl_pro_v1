package com.business.common.util;

import java.util.concurrent.atomic.AtomicLong;

public class LBLUtil {
	
	private static final AtomicLong sequence = new AtomicLong(
			System.currentTimeMillis() / 1000);

	public static long getNextLBLStoreId() {
		return sequence.incrementAndGet();
	}

}
