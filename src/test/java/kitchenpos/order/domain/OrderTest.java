package kitchenpos.order.domain;

import static kitchenpos.menugroup.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;

@DisplayName("주문")
class OrderTest {
	@DisplayName("생성")
	@Test
	void of() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.from(4), false);
		OrderLineItems orderLineItems = OrderLineItems.from(Collections.singletonList(OrderLineItem.of(
			Menu.of(
				Name.from("후라이드+후라이드"),
				Price.from(BigDecimal.valueOf(25000)),
				추천_메뉴_그룹().getId(),
				MenuProducts.from(Collections.singletonList(
					MenuProduct.of(
						Product.of(Name.from("후라이드치킨"), Price.from(BigDecimal.valueOf(17000))),
						Quantity.from(2L))))),
			Quantity.from(1L))));

		// when
		Order order = Order.of(orderTable, orderLineItems);

		// then
		assertThat(order).isNotNull();
	}

	@DisplayName("생성 실패 - 주문 테이블이 비어있는 경우")
	@Test
	void ofFailOnEmptyOrderTable() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.from(4), true);
		OrderLineItems orderLineItems = OrderLineItems.from(Collections.singletonList(OrderLineItem.of(
			Menu.of(
				Name.from("후라이드+후라이드"),
				Price.from(BigDecimal.valueOf(25000)),
				추천_메뉴_그룹().getId(),
				MenuProducts.from(Collections.singletonList(
					MenuProduct.of(
						Product.of(Name.from("후라이드치킨"), Price.from(BigDecimal.valueOf(17000))),
						Quantity.from(2L))))),
			Quantity.from(1L))));

		// when
		ThrowingCallable throwingCallable = () -> Order.of(orderTable, orderLineItems);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
