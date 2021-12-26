package kitchenpos.order.domain;

import static kitchenpos.menu.MenuFixture.*;
import static kitchenpos.order.OrderLineItemFixture.*;
import static kitchenpos.ordertable.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.infra.repository.InMemoryMenuRepository;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.infra.MenusImpl;
import kitchenpos.order.infra.OrderTablesImpl;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.infra.repository.InMemoryOrderTableRepository;

@DisplayName("주문 검증자")
class OrderValidatorTest {
	private OrderTableRepository orderTableRepository;
	private MenuRepository menuRepository;
	private OrderValidator orderValidator;

	@BeforeEach
	void setUp() {
		orderTableRepository = new InMemoryOrderTableRepository();
		menuRepository = new InMemoryMenuRepository();
		orderValidator = new OrderValidatorImpl(
			new OrderTablesImpl(orderTableRepository),
			new MenusImpl(menuRepository));
	}

	@DisplayName("주문 테이블이 존재하고 비어있지 않은지 검증한다.")
	@Test
	void validateOrderTableExistAndNotEmpty() {
		// given
		orderTableRepository.save(비어있지않은_주문_테이블_1번());

		// when
		orderValidator.validateOrderTableExistAndNotEmpty(비어있지않은_주문_테이블_1번().getId());

		// then
	}

	@DisplayName("주문 테이블이 존재하고 비어있지 않은지 검증한다. (실패) - 존재하지 않는 주문 테이블")
	@Test
	void validateOrderTableExistAndNotEmptyFailOnNotExist() {
		// given

		// when
		ThrowingCallable throwingCallable = () -> orderValidator.validateOrderTableExistAndNotEmpty(
			비어있지않은_주문_테이블_1번().getId());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블이 존재하고 비어있지 않은지 검증한다. (실패) - 빈 테이블")
	@Test
	void validateOrderTableExistAndNotEmptyFailOnEmpty() {
		// given
		orderTableRepository.save(빈_주문_테이블_2번());

		// when
		ThrowingCallable throwingCallable = () -> orderValidator.validateOrderTableExistAndNotEmpty(
			빈_주문_테이블_2번().getId());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴가 존재하는지 검증한다.")
	@Test
	void validateMenusExist() {
		// given
		menuRepository.save(후라이드후라이드_메뉴());

		// when
		orderValidator.validateMenusExist(Collections.singletonList(OrderLineItemDto.from(후라이드후라이드_메뉴_주문_항목())));

		// then
	}

	@DisplayName("메뉴가 존재하는지 검증한다. (실패)")
	@Test
	void validateMenusExistFail() {
		// given

		// when
		ThrowingCallable throwingCallable = () -> orderValidator.validateMenusExist(
			Collections.singletonList(OrderLineItemDto.from(후라이드후라이드_메뉴_주문_항목())));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
