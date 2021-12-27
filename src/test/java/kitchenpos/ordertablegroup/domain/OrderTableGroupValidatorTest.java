package kitchenpos.ordertablegroup.domain;

import static kitchenpos.order.OrderFixture.*;
import static kitchenpos.ordertable.OrderTableFixture.*;
import static kitchenpos.ordertablegroup.OrderTableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infra.repository.InMemoryOrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.ValidOrderTableValidator;
import kitchenpos.ordertable.infra.repository.InMemoryOrderTableRepository;
import kitchenpos.ordertablegroup.infra.OrderTablesImpl;
import kitchenpos.ordertablegroup.infra.OrdersImpl;

@DisplayName("주문 테이블 그룹 검증자")
public class OrderTableGroupValidatorTest {
	private OrderTableRepository orderTableRepository;
	private OrderRepository orderRepository;
	private OrderTableGroupValidator orderTableGroupValidator;

	@BeforeEach
	void setUp() {
		orderTableRepository = new InMemoryOrderTableRepository();
		orderRepository = new InMemoryOrderRepository();
		orderTableGroupValidator = new OrderTableGroupValidatorImpl(
			new OrderTablesImpl(orderTableRepository),
			new OrdersImpl(orderRepository));
	}

	@DisplayName("주문 테이블이 2개 이상인지 검증한다.")
	@Test
	void validateOrderTablesAreGreaterThanOrEqualToTwo() {
		// given
		orderTableRepository.save(빈_주문_테이블_3번());
		orderTableRepository.save(빈_주문_테이블_4번());

		// when
		orderTableGroupValidator.validateOrderTablesAreGreaterThanOrEqualToTwo(
			Arrays.asList(빈_주문_테이블_3번().getId(), 빈_주문_테이블_4번().getId()));

		// then
	}

	@DisplayName("주문 테이블이 2개 이상인지 검증한다. (실패)")
	@Test
	void validateOrderTablesAreGreaterThanOrEqualToTwoFail() {
		// given
		orderTableRepository.save(빈_주문_테이블_3번());

		// when
		ThrowingCallable throwingCallable = () -> orderTableGroupValidator.validateOrderTablesAreGreaterThanOrEqualToTwo(
			Collections.singletonList(빈_주문_테이블_3번().getId()));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블이 그룹핑되어 있지 않은지 검증한다.")
	@Test
	void validateNotGrouped() {
		// given
		orderTableRepository.save(빈_주문_테이블_3번());
		orderTableRepository.save(빈_주문_테이블_4번());

		// when
		orderTableGroupValidator.validateNotGrouped(
			Arrays.asList(빈_주문_테이블_3번().getId(), 빈_주문_테이블_4번().getId()));

		// then
	}

	@DisplayName("주문 테이블이 그룹핑되어 있지 않은지 검증한다. (실패)")
	@Test
	void validateNotGroupedFail() {
		// given
		orderTableRepository.save(빈_주문_테이블_3번()).groupedBy(1L);
		orderTableRepository.save(빈_주문_테이블_4번()).groupedBy(1L);

		// when
		ThrowingCallable throwingCallable = () -> orderTableGroupValidator.validateNotGrouped(
			Arrays.asList(빈_주문_테이블_3번().getId(), 빈_주문_테이블_4번().getId()));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블이 비어있는지 검증한다.")
	@Test
	void validateOrderTableIsNotEmpty() {
		// given
		orderTableRepository.save(빈_주문_테이블_3번());
		orderTableRepository.save(빈_주문_테이블_4번());

		// when
		orderTableGroupValidator.validateOrderTableIsEmpty(
			Arrays.asList(빈_주문_테이블_3번().getId(), 빈_주문_테이블_4번().getId()));

		// then
	}

	@DisplayName("주문 테이블이 비어있는지 검증한다. (실패)")
	@Test
	void validateOrderTableIsNotEmptyFail() {
		// given
		orderTableRepository.save(빈_주문_테이블_3번()).changeEmpty(false, new ValidOrderTableValidator());
		orderTableRepository.save(빈_주문_테이블_4번());

		// when
		ThrowingCallable throwingCallable = () -> orderTableGroupValidator.validateOrderTableIsEmpty(
			Arrays.asList(빈_주문_테이블_3번().getId(), 빈_주문_테이블_4번().getId()));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("완료되지 않은 주문이 없는지 검증한다.")
	@Test
	void validateNotCompletedOrderNotExist() {
		// given
		OrderTable orderTable1 = orderTableRepository.save(빈_주문_테이블_3번());
		OrderTable orderTable2 = orderTableRepository.save(빈_주문_테이블_4번());
		OrderTableGroup orderTableGroup = 주문_테이블_그룹(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
		orderTable1.groupedBy(orderTableGroup.getId());
		orderTable2.groupedBy(orderTableGroup.getId());
		Order order = orderRepository.save(후라이드후라이드_메뉴_주문(orderTable1));
		order.changeOrderStatus(OrderStatus.COMPLETION);

		// when
		orderTableGroupValidator.validateNotCompletedOrderNotExist(orderTableGroup.getId());

		// then
	}

	@DisplayName("완료되지 않은 주문이 없는지 검증한다. (실패)")
	@Test
	void validateNotCompletedOrderNotExistFail() {
		// given
		OrderTable orderTable1 = orderTableRepository.save(빈_주문_테이블_3번());
		OrderTable orderTable2 = orderTableRepository.save(빈_주문_테이블_4번());
		OrderTableGroup orderTableGroup = 주문_테이블_그룹(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
		orderTable1.groupedBy(orderTableGroup.getId());
		orderTable2.groupedBy(orderTableGroup.getId());
		Order order = orderRepository.save(후라이드후라이드_메뉴_주문(orderTable1));

		// when
		ThrowingCallable throwingCallable = () -> orderTableGroupValidator.validateNotCompletedOrderNotExist(
			orderTableGroup.getId());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}
}
