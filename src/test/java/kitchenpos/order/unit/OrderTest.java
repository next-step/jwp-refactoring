package kitchenpos.order.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.Empty;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

@DisplayName("주문 관련 단위테스트")
public class OrderTest {
	private OrderLineItems 주문상품;
	private OrderTable 채워진_테이블;
	private OrderTable 빈_테이블;

	@BeforeEach
	void setUp() {
		MenuGroup 중식 = MenuGroup.of("중식");
		Product 짜장면 = Product.of("짜장면", BigDecimal.valueOf(6000));
		Product 군만두 = Product.of("군만두", BigDecimal.valueOf(2000));
		MenuProduct 짜장면_1개 = MenuProduct.of(짜장면, 1);
		MenuProduct 군만두_2개 = MenuProduct.of(군만두, 2);
		Menu 짜장세트 = Menu.of("짜장세트", BigDecimal.valueOf(6000), 중식, MenuProducts.of(Arrays.asList(짜장면_1개, 군만두_2개)));
		주문상품 = OrderLineItems.of(Arrays.asList(OrderLineItem.of(짜장세트, 1)));
		빈_테이블 = OrderTable.of(2, true);
		채워진_테이블 = OrderTable.of(2, false);

	}

	@DisplayName("주문을 생성할 수 있다.")
	@Test
	void createOrder() {
		// when
		Order 주문 = Order.of(채워진_테이블, 주문상품);
		// then
		assertAll(
			() -> assertThat(주문.getOrderTable().getEmpty()).isEqualTo(Empty.of(false)),
			() -> assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
			() -> assertThat(주문.getOrderLineItems().getSize()).isEqualTo(1)
		);
	}

	@DisplayName("빈테이블에는 주문을 생성할때 예외가 발생한다.")
	@Test
	void createOrder_empty_table_exception() {
		// when - then
		assertThatThrownBy(() -> Order.of(빈_테이블, 주문상품))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.CANNOT_ORDER_WHEN_TABLE_IS_EMPTY);
	}

	@DisplayName("주문의 상태를 변경할 수 있다.")
	@Test
	void updateStatus() {
		// given
		Order 주문 = Order.of(채워진_테이블, 주문상품);
		// when
		주문.updateOrderStatus(OrderStatus.MEAL);
		// then
		assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
	}

	@DisplayName("주문의이 완료된경우 주문 상태를 변경할 수 있다.")
	@Test
	void updateStatus_when_completed_exception() {
		// given
		Order 주문 = Order.of(채워진_테이블, 주문상품);
		주문.updateOrderStatus(OrderStatus.COMPLETION);
		// when - then
		assertThatThrownBy(() -> 주문.updateOrderStatus(OrderStatus.MEAL))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorMessage.CANNOT_CHANGE_ORDER_STATUS_WHEN_COMPLETED);
	}

}
