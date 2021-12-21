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

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

@DisplayName("주문 항목들")
class OrderLineItemsTest {
	@DisplayName("생성")
	@Test
	void of() {
		// given
		OrderLineItem orderLineItem = OrderLineItem.of(
			Menu.of(
				Name.of("후라이드+후라이드"),
				Price.of(BigDecimal.valueOf(25000)),
				MenuGroup.of(Name.of("추천메뉴")),
				MenuProducts.of(Collections.singletonList(
					MenuProduct.of(
						Product.of(Name.of("후라이드치킨"), Price.of(BigDecimal.valueOf(17000))),
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
