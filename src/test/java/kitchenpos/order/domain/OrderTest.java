package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupName;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;

@DisplayName("주문")
class OrderTest {
	@DisplayName("생성")
	@Test
	void of() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), false);
		OrderLineItems orderLineItems = OrderLineItems.of(Collections.singletonList(OrderLineItem.of(
			Menu.of(
				MenuName.of("후라이드+후라이드"),
				Price.of(BigDecimal.valueOf(25000)),
				MenuGroup.of(MenuGroupName.of("추천메뉴")),
				MenuProducts.of(Collections.singletonList(
					MenuProduct.of(
						Product.of(ProductName.of("후라이드치킨"), Price.of(BigDecimal.valueOf(17000))),
						Quantity.of(2L))))),
			Quantity.of(1L))));

		// when
		Order order = Order.of(orderTable, orderLineItems);

		// then
		assertThat(order).isNotNull();
	}

	@DisplayName("생성 실패 - 주문 테이블이 비어있는 경우")
	@Test
	void ofFailOnEmptyOrderTable() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), true);
		OrderLineItems orderLineItems = OrderLineItems.of(Collections.singletonList(OrderLineItem.of(
			Menu.of(
				MenuName.of("후라이드+후라이드"),
				Price.of(BigDecimal.valueOf(25000)),
				MenuGroup.of(MenuGroupName.of("추천메뉴")),
				MenuProducts.of(Collections.singletonList(
					MenuProduct.of(
						Product.of(ProductName.of("후라이드치킨"), Price.of(BigDecimal.valueOf(17000))),
						Quantity.of(2L))))),
			Quantity.of(1L))));

		// when
		ThrowingCallable throwingCallable = () -> Order.of(orderTable, orderLineItems);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
