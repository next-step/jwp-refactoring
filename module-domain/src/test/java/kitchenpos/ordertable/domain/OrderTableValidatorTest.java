package kitchenpos.ordertable.domain;

import static kitchenpos.order.OrderFixture.*;
import static kitchenpos.ordertable.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infra.repository.InMemoryOrderRepository;
import kitchenpos.ordertable.infra.OrdersImpl;

@DisplayName("주문 테이블 검증자")
public class OrderTableValidatorTest {
	private OrderRepository orderRepository;
	private OrderTableValidator orderTableValidator;

	@BeforeEach
	void setUp() {
		orderRepository = new InMemoryOrderRepository();
		orderTableValidator = new OrderTableValidatorImpl(new OrdersImpl(orderRepository));
	}

	@DisplayName("완료되지 않은 주문이 없는지 검증한다.")
	@Test
	void validateNotCompletedOrderNotExist() {
		// given
		Order order = orderRepository.save(후라이드후라이드_메뉴_주문());
		order.changeOrderStatus(OrderStatus.COMPLETION);

		// when
		orderTableValidator.validateNotCompletedOrderNotExist(비어있지않은_주문_테이블_1번().getId());

		// then
	}

	@DisplayName("완료되지 않은 주문이 없는지 검증한다. (실패)")
	@Test
	void validateNotCompletedOrderNotExistFail() {
		// given
		orderRepository.save(후라이드후라이드_메뉴_주문());

		// when
		ThrowingCallable throwingCallable = () -> orderTableValidator.validateNotCompletedOrderNotExist(
			비어있지않은_주문_테이블_1번().getId());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}
}
