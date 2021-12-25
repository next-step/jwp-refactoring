package kitchenpos.orders.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.orders.domain.Order;

class OrderTest {

	@Test
	@DisplayName("주문 생성 테스트")
	public void createOrderTest() {
		//when
		Order order = new Order();

		//then
		assertThat(order).isNotNull();
	}

	@ParameterizedTest
	@CsvSource(value = {"MEAL:false", "COOKING:false", "COMPLETION:true"}, delimiter = ':')
	@DisplayName("주문이 계산완료된 상태인지 확인")
	public void isCompletion(String status, boolean expected) {
		//when
		Order order = new Order(1L,null, OrderStatus.valueOf(status), Lists.emptyList());

		//then
		assertThat(order.isCompletion()).isEqualTo(expected);
	}

	@ParameterizedTest
	@ValueSource(strings = {"MEAL", "COOKING", "COMPLETION"})
	@DisplayName("주문 상태 변경 확인")
	public void changeOrderStatus(String status) {
		//when
		Order order = new Order();
		order.changeOrderStatus(status);
		//then
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.valueOf(status));
	}
}
