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
	void from() {
		// given
		OrderLineItem orderLineItem = OrderLineItem.of(
			Menu.of(
				Name.from("후라이드+후라이드"),
				Price.from(BigDecimal.valueOf(25000)),
				MenuGroup.from(Name.from("추천메뉴")),
				MenuProducts.from(Collections.singletonList(
					MenuProduct.of(
						Product.of(Name.from("후라이드치킨"), Price.from(BigDecimal.valueOf(17000))),
						Quantity.from(2L))))),
			Quantity.from(1L));

		// when
		OrderLineItems orderLineItems = OrderLineItems.from(Collections.singletonList(orderLineItem));

		// then
		assertThat(orderLineItems).isNotNull();
	}

	@DisplayName("생성 실패 - 주문 항목이 없는 경우")
	@ParameterizedTest
	@NullAndEmptySource
	void fromFailOnEmptyOrderLineItem(List<OrderLineItem> orderLineItems) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> OrderLineItems.from(orderLineItems);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
