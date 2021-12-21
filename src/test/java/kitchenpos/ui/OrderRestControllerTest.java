package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

@Transactional
@SpringBootTest
class OrderRestControllerTest {

	@Autowired
	private OrderRestController orderRestController;

	@Test
	@DisplayName("주문 생성 테스트")
	public void createOrderTest() {
		//given
		OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1L);
		Order order = new Order(null, 1L, null, null, Lists.newArrayList(orderLineItem));
		//when
		ResponseEntity<Order> responseEntity = orderRestController.create(order);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getHeaders().getLocation().toString()).isEqualTo("/api/orders/4");
	}

	@Test
	@DisplayName("주문 목록 조회 테스트")
	public void findAllOrderTest() {
		//given
		//when
		ResponseEntity<List<Order>> responseEntity = orderRestController.list();

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).hasSize(3);
	}


	@Test
	@DisplayName("주문 상태 변경 테스트")
	public void changeOrderStatusTest() {
		//given
		Order changeOrderStatus = new Order(null, null, OrderStatus.MEAL.name(), null, null);
		//when
		ResponseEntity<Order> responseEntity = orderRestController.changeOrderStatus(1L, changeOrderStatus);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody().getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}
}
