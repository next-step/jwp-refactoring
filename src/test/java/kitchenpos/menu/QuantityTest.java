package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Quantity;
import kitchenpos.menu.exception.QuantityException;

@DisplayName("수량 도메인 테스트")
public class QuantityTest {
	
	@DisplayName("수량 생성 테스트")
	@Test
	void 수량_생성() {
		Quantity quantity = new Quantity(10L);
		assertThat(quantity).isNotNull();
		assertThat(quantity.value()).isEqualTo(10);
	}
	
	@DisplayName("수량 생성 시 0개 이하 에러 발생 테스트")
	@Test
	void 수량_생성_시_0개_이하_에러_테스트() {
		assertThatThrownBy(
			() -> new Quantity(0)
		).isInstanceOf(QuantityException.class);
	}
	
}
