package kitchenpos.orders.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.common.domain.Quantity;

@DataJpaTest
class OrderLineItemRepositoryTest {

	@Autowired
	private OrderLineItemRepository orderLineItemRepository;


	@Test
	@DisplayName("주문메뉴 저장 테스트")
	public void saveOrderLineItemTest() {
		//given
		//when
		OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, Quantity.valueOf(2L));

		//when
		OrderLineItem save = orderLineItemRepository.save(orderLineItem);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getSeq()).isEqualTo(4L);
	}

	@Test
	@DisplayName("주문메뉴 목록 조회 테스트")
	public void findAllOrderLineItemTest() {
		//given

		//when
		List<OrderLineItem> orderLineItems = orderLineItemRepository.findAll();

		//then
		assertThat(orderLineItems).hasSizeGreaterThanOrEqualTo(3);
	}

	@Test
	@DisplayName("주문메뉴 id로 조회 테스트")
	public void findByIdOrderLineItemTest() {
		//given

		//when
		OrderLineItem orderLineItem = orderLineItemRepository.findById(2L).orElse(new OrderLineItem());

		//then
		assertThat(orderLineItem.getSeq()).isEqualTo(2L);
	}

	@Test
	@DisplayName("없는 주문메뉴 id로 조회 테스트")
	public void findByIdOrderLineItemFailTest() {
		//given

		//when
		OrderLineItem orderLineItem = orderLineItemRepository.findById(99L).orElse(new OrderLineItem());

		//then
		assertThat(orderLineItem.getSeq()).isNull();
	}

	@Test
	@DisplayName("메뉴 id로 조회 테스트")
	public void findOrderLineItemsByMenuId() {
		//given

		//when
		List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(1L);

		//then
		assertThat(orderLineItems).hasSize(1);
	}
}
