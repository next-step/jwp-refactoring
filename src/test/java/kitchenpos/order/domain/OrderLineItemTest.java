package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

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
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;

@DisplayName("주문 항목")
class OrderLineItemTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		Menu menu = Menu.of(
			MenuName.of("후라이드+후라이드"),
			Price.of(BigDecimal.valueOf(25000)),
			MenuGroup.of(MenuGroupName.of("추천메뉴")),
			MenuProducts.of(Collections.singletonList(
				MenuProduct.of(
					Product.of(ProductName.of("후라이드치킨"), Price.of(BigDecimal.valueOf(17000))),
					Quantity.of(2L)))));
		Quantity quantity = Quantity.of(1L);

		// when
		OrderLineItem orderLineItem = OrderLineItem.of(menu, quantity);

		// then
		assertThat(orderLineItem.getMenu()).isEqualTo(menu);
		assertThat(orderLineItem.getQuantity()).isEqualTo(quantity);
	}
}
