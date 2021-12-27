package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.domain.Menu;
import kitchenpos.menu.domain.domain.MenuProduct;
import kitchenpos.menugroup.domain.domain.MenuGroup;
import kitchenpos.order.domain.domain.Order;
import kitchenpos.order.domain.domain.OrderLineItem;
import kitchenpos.order.domain.domain.OrderStatus;
import kitchenpos.order.exception.CanNotEditOrderStatusException;
import kitchenpos.order.exception.InvalidOrderTableException;
import kitchenpos.order.exception.NotFoundOrderLineItemsException;
import kitchenpos.ordertable.domain.domain.OrderTable;
import kitchenpos.product.domain.domain.Product;

class OrderTest {

	private final Product 초밥 = Product.of(1L, "초밥", 3_000);
	private final MenuProduct 메뉴초밥 = MenuProduct.of(1L, 초밥, 10);
	private final MenuGroup 메인메뉴그룹 = MenuGroup.of(1L, "메인");
	private final Menu 일식메뉴 = Menu.of(1L, "일식", 30_000, 메인메뉴그룹, Arrays.asList(메뉴초밥));
	private final List<OrderLineItem> 주문항목_목록 = Arrays.asList(OrderLineItem.of(일식메뉴, 2L));

	@DisplayName("주문 테이블이 없으면 예외발생")
	@Test
	void of_null_order_table() {
		assertThatExceptionOfType(InvalidOrderTableException.class)
			.isThrownBy(() -> Order.ofCooking(null, 주문항목_목록));
	}

	@DisplayName("주문 테이블이 비어있으면 예외발생")
	@Test
	void of_empty_order_table() {
		final OrderTable 빈_주문테이블 = OrderTable.of(1L, null, 4, true);

		assertThatExceptionOfType(InvalidOrderTableException.class)
			.isThrownBy(() -> Order.of(빈_주문테이블, OrderStatus.COOKING, 주문항목_목록));
	}

	@DisplayName("주문의 상태변경: 식사->계산완료")
	@Test
	void changeOrderStatus() {
		final OrderTable 개별_주문테이블 = OrderTable.of(1L, null, 1, false);
		final Order 주문 = Order.of(개별_주문테이블, OrderStatus.MEAL, 주문항목_목록);

		주문.changeOrderStatus(OrderStatus.COMPLETION);

		assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
	}

	@DisplayName("계산완료된 주문의 상태변경을 시도하면 예외발생")
	@Test
	void changeOrderStatus_when_completion() {
		final OrderTable 개별_주문테이블 = OrderTable.of(1L, null, 2, false);
		final Order 주문 = Order.of(개별_주문테이블, OrderStatus.COMPLETION, 주문항목_목록);

		assertThatExceptionOfType(CanNotEditOrderStatusException.class)
			.isThrownBy(() -> 주문.changeOrderStatus(OrderStatus.COOKING));
	}

	@DisplayName("주문항목 목록이 없으면 예외발생")
	@Test
	void of_null_order_line_items() {
		final OrderTable 개별_주문테이블 = OrderTable.of(1L, null, 3, false);

		assertThatExceptionOfType(NotFoundOrderLineItemsException.class)
			.isThrownBy(() -> Order.ofCooking(개별_주문테이블, null));
	}

	@DisplayName("주문항목 목록이 없거나 비어있으면 예외발생")
	@Test
	void of_empty_order_line_items() {
		final OrderTable 개별_주문테이블 = OrderTable.of(1L, null, 3, false);

		assertThatExceptionOfType(NotFoundOrderLineItemsException.class)
			.isThrownBy(() -> Order.ofCooking(개별_주문테이블, Collections.emptyList()));
	}
}
