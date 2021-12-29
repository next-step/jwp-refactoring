package kitchenpos.orders.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	@Test
	@DisplayName("주문 저장 테스트")
	public void saveOrderTest() {
		//given
		Order order = new Order(null, 1L, OrderStatus.COOKING);

		//when
		Order save = orderRepository.save(order);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(8L);
		assertThat(save.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
	}

	@Test
	@DisplayName("주문 목록 조회 테스트")
	public void findAllOrderTest() {
		//given
		//when
		List<Order> orders = orderRepository.findAll();

		//then
		assertThat(orders).hasSizeGreaterThanOrEqualTo(7);
	}

	@Test
	@DisplayName("주문 id로 조회 테스트")
	public void findByIdOrderTest() {
		//given
		//when
		Order order = orderRepository.findById(1L).orElse(new Order());

		//then
		assertThat(order.getId()).isEqualTo(1L);
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
	}
}
