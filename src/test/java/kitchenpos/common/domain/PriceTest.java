package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PriceTest {

	@Test
	@DisplayName("자신의 가격이 전달된 가격보다 비싸면 true 를 반환해야한다.")
	void isExpensiveThan() {
		//given
		Price basePrice = new Price(BigDecimal.TEN);
		Price comparePrice = new Price(BigDecimal.ONE);

		//when-then
		assertThat(basePrice.isExpensiveThan(comparePrice)).isTrue();
	}

	@Test
	@DisplayName("자신의 가격이 전달된 가격과 같으면 false 를 반환해야한다.")
	void isSameWith() {
		//given
		Price basePrice = new Price(BigDecimal.TEN);
		Price comparePrice = new Price(BigDecimal.TEN);

		//when-then
		assertThat(basePrice.isExpensiveThan(comparePrice)).isFalse();
	}

	@Test
	@DisplayName("자신의 가격이 전달된 가격보다 작으면 false 를 반환해야한다.")
	void isCheaperThan() {
		//given
		Price basePrice = new Price(BigDecimal.ONE);
		Price comparePrice = new Price(BigDecimal.TEN);

		//when-then
		assertThat(basePrice.isExpensiveThan(comparePrice)).isFalse();
	}

	@Test
	@DisplayName("가격을 null 로 생성하려고 하면 IllegalArgumentException 을 throw 해야한다.")
	void nullPrice() {
		//when-then
		assertThatThrownBy(() -> new Price(null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("가격을 음수로 생성하려고 하면 IllegalArgumentException 을 throw 해야한다.")
	void negativePrice() {
		//when-then
		assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-200)))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
