package study;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class BigDecimalTest {
	@Test
	void bigDecimal() {
		BigDecimal bigDecimal1 = new BigDecimal(100);
		BigDecimal bigDecimal2 = new BigDecimal(50);
		if(bigDecimal1.compareTo(bigDecimal2) > 0) {
			System.out.println("0보다 큼");
		} else if (bigDecimal1.compareTo(bigDecimal2) < 0) {
			System.out.println("0보다 작음");
		}
	}
}
