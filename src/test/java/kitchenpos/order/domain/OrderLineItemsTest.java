package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupName;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;

@DisplayName("주문 항목들")
class OrderLineItemsTest {
	@DisplayName("생성")
	@Test
	void of() {
		// given
		OrderLineItem orderLineItem = OrderLineItem.of(
			Menu.of(
				MenuName.of("후라이드+후라이드"),
				Price.of(BigDecimal.valueOf(25000)),
				MenuGroup.of(MenuGroupName.of("추천메뉴")),
				MenuProducts.of(Collections.singletonList(
					MenuProduct.of(
						Product.of(ProductName.of("후라이드치킨"), Price.of(BigDecimal.valueOf(17000))),
						Quantity.of(2L))))),
			Quantity.of(1L));

		// when
		OrderLineItems orderLineItems = OrderLineItems.of(Collections.singletonList(orderLineItem));

		// then
		assertThat(orderLineItems).isNotNull();
	}

	@DisplayName("생성 실패 - 주문 항목이 없는 경우")
	@ParameterizedTest
	@NullAndEmptySource
	void ofFailOnEmptyOrderLineItem(List<OrderLineItem> orderLineItems) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> OrderLineItems.of(orderLineItems);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
