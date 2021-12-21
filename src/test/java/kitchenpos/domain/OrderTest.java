package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

	@Test
	@DisplayName("주문 생성 테스트")
	public void createOrderTest() {

		//when
		Order order = new Order();

		//then
		assertThat(order).isNotNull();
	}
}
