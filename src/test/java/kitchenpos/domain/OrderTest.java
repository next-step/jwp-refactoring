package kitchenpos.domain;

import kitchenpos.common.OrderValidationException;
import kitchenpos.common.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

	Order order;
	private Menu 짜장면;
	private Menu 짬뽕;
	private OrderItem orderItem_짬뽕;
	private OrderItem orderItem_짜장면;
	private OrderTable orderTable;

	@BeforeEach
	void setUp() {
		orderTable = new OrderTable(20, false);

		MenuGroup 중식 = new MenuGroup("중식");
		짜장면 = new Menu("짜장면", 7000, 중식);
		MenuProduct menuProduct1 = new MenuProduct(짜장면, new Product("짜장면", 7000), 1);
		짜장면.addMenuProducts(Collections.singletonList(menuProduct1));
		짬뽕 = new Menu("짬뽕", 6000, 중식);
		MenuProduct menuProduct2 = new MenuProduct(짬뽕, new Product("짬뽕", 6000), 1);
		짬뽕.addMenuProducts(Collections.singletonList(menuProduct2));

		orderItem_짜장면 = OrderItem.of(짜장면, new Quantity(77));
		orderItem_짬뽕 = OrderItem.of(짬뽕, new Quantity(99));

		order = Order.createCookingOrder(orderTable, Arrays.asList(orderItem_짜장면, orderItem_짬뽕));
	}

	@DisplayName("새로운 주문을 생성한다.")
	@Test
	void createCookingOrder() {
		Order newOrder = Order.createCookingOrder(orderTable, Arrays.asList(orderItem_짜장면, orderItem_짬뽕));

		assertThat(newOrder.getOrderTable()).isEqualTo(orderTable);
		assertThat(newOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
		assertThat(newOrder.getOrderLineItems())
				.map(OrderLineItem::getMenu)
				.containsExactlyInAnyOrder(orderItem_짬뽕.getMenu(), orderItem_짜장면.getMenu());
		assertThat(newOrder.getOrderLineItems())
				.map(OrderLineItem::getQuantity)
				.containsExactlyInAnyOrder(new Quantity(99), new Quantity(77));
	}

	@DisplayName("주문의 상태를 변경한다.")
	@Test
	void changeOrderStatus() {
		order.changeOrderStatus(OrderStatus.MEAL);

		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
	}

	@DisplayName("이미 완료된 주문의 상태를 변경하려 할 때 예외 발생")
	@Test
	void changeOrderStatus_ExceptionAlreadyCompletion() {
		order.changeOrderStatus(OrderStatus.COMPLETION);

		assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
				.isInstanceOf(OrderValidationException.class)
				.hasMessageMatching(Order.MSG_CANNOT_CHANGE_COMPLETION);
	}
}
