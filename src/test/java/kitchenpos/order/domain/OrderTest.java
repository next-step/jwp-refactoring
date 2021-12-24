package kitchenpos.order.domain;

import static kitchenpos.common.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.exception.CanNotEditOrderStatusException;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;

class OrderTest {

	private final Product 초밥 = product(1L, "초밥", 3_000);
	private final MenuProduct 메뉴초밥 = menuProduct(1L, null, 초밥, 10);
	private final MenuGroup 메인메뉴그룹 = menuGroup(1L, "메인");
	private final Menu 일식메뉴 = menu(1L, "일식", 30_000, 메인메뉴그룹, Arrays.asList(메뉴초밥));
	private final List<OrderLineItem> 주문항목_목록 = Arrays.asList(OrderLineItem.of(일식메뉴, 2L));

	@DisplayName("주문 테이블이 없거나 비어있으면 예외발생")
	@Test
	void of_invalid_order_table() {
		assertThatExceptionOfType(InvalidOrderException.class)
			.isThrownBy(() -> Order.of(null, OrderStatus.COOKING, 주문항목_목록));

		final OrderTable 빈_주문테이블 = orderTable(1L, null, 4, true);
		assertThatExceptionOfType(InvalidOrderException.class)
			.isThrownBy(() -> Order.of(빈_주문테이블, OrderStatus.COOKING, 주문항목_목록));
	}

	@DisplayName("주문의 상태변경: 식사->계산완료")
	@Test
	void changeOrderStatusIfNotCompletion() {
		final OrderTable 개별_주문테이블 = orderTable(1L, null, 1, false);
		final Order 주문 = Order.of(개별_주문테이블, OrderStatus.MEAL, 주문항목_목록);

		주문.changeOrderStatusIfNotCompletion(OrderStatus.COMPLETION);

		assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
	}

	@DisplayName("계산완료된 주문의 상태변경을 시도하면 예외발생")
	@Test
	void changeOrderStatusIfNotCompletion_when_completion() {
		final OrderTable 개별_주문테이블 = orderTable(1L, null, 2, false);
		final Order 주문 = Order.of(개별_주문테이블, OrderStatus.COMPLETION, 주문항목_목록);

		assertThatExceptionOfType(CanNotEditOrderStatusException.class)
			.isThrownBy(() -> 주문.changeOrderStatusIfNotCompletion(OrderStatus.COOKING));
	}
}
