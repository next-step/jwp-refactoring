package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

	private Price 이만원;
	private Price 삼만원;

	@BeforeEach
	void setUp() {
		이만원 = new Price(BigDecimal.valueOf(20000));
		삼만원 = new Price(BigDecimal.valueOf(30000));
	}

	@DisplayName("가격은 0보다 작을 수 없다.")
	@Test
	void testCreatePriceError() {
		assertThatThrownBy(() -> {
			new Price(BigDecimal.valueOf(-1));
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품 가격은 0보다 작을 수 없습니다.");
	}

	@DisplayName("가격은 null이 될 수 없다.")
	@Test
	void testCreatePriceErrorNull() {
		assertThatThrownBy(() -> {
			new Price(null);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품 가격은 0보다 작을 수 없습니다.");
	}

	@DisplayName("0이상 금액에 대해 가격 정상 생성")
	@Test
	void testCreatePrice() {
		assertThat(이만원).isEqualTo(new Price(BigDecimal.valueOf(20000)));
	}

	@DisplayName("가격 비교 로직 테스트")
	@Test
	void testComparePrice() {

		int smaller = 이만원.compareTo(삼만원);
		int bigger = 삼만원.compareTo(이만원);
		int equals = 이만원.compareTo(이만원);

		assertThat(smaller).isEqualTo(-1);
		assertThat(bigger).isEqualTo(1);
		assertThat(equals).isZero();
	}

	@DisplayName("가격 추가 테스트")
	@Test
	void testAddPrice() {
		Price 이만원 = new Price(BigDecimal.valueOf(20000));
		이만원.addPrice(new Price(BigDecimal.valueOf(1000)));

		Price 이만천원 = new Price(BigDecimal.valueOf(21000));
		assertThat(이만원).isEqualTo(이만천원);
	}

}