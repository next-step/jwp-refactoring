package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Price;

class PriceTest {

	@Test
	@DisplayName("Price 생성")
	public void createPrice() {
		//given
		//when
		Price price = Price.valueOf(BigDecimal.valueOf(1000));
		//then
		assertThat(price).isNotNull();
		assertThat(price.value()).isEqualTo(new BigDecimal(1000));
		assertThat(price).isEqualTo(Price.valueOf(BigDecimal.valueOf(1000)));
	}

	@Test
	@DisplayName("Price 가격이 0보다 작아서 생성실패")
	public void createPriceFail() {
		assertThatThrownBy(() -> Price.valueOf(BigDecimal.valueOf(-1)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("가격은 0보다 작을 수 없습니다");
	}

	@Test
	@DisplayName("더 큰지 여부 확인 테스트")
	public void greaterThanTest() {
		//given
		//when
		Price price = Price.valueOf(BigDecimal.valueOf(1000));
		boolean result = price.greaterThan(Price.valueOf(BigDecimal.valueOf(999)));
		//then
		assertThat(result).isTrue();
	}


}


