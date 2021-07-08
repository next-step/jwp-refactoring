package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.exception.PriceException;

@DisplayName("메뉴 가격 도메인 테스트")
public class PriceTest {

	@DisplayName("가격 생성 테스트")
	@Test
	void 가격_생성() {
		Price price = new Price(new BigDecimal(1000));
		assertThat(price).isNotNull();
	}

	@DisplayName("가격 생성 - null일 경우 에러")
	@Test
	void 가격_생성_null일_경우_에러() {
		assertThatThrownBy(() ->
			new Price(null)
		).isInstanceOf(PriceException.class);

	}

	@DisplayName("가격 생성 - 0보다 작을 경우 에러")
	@Test
	void 가격_생성_0보다_작을_경우_에러() {
		assertThatThrownBy(() ->
			new Price(new BigDecimal(-5))
		).isInstanceOf(PriceException.class);

	}
}
