package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

	@Test
	@DisplayName("주문테이블 생성 테스트")
	public void createOrderTableTest() {

		//when
		OrderTable orderTable = new OrderTable();

		//then
		assertThat(orderTable).isNotNull();
	}
}
