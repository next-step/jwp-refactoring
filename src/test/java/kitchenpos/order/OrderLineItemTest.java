package kitchenpos.order;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.Quantity;

@DisplayName("주문 항목 도메인 테스트")
public class OrderLineItemTest {

	Menu menu;

	@BeforeEach
	void setUp() {
		MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
		Product product = new Product(1L, "상품이름", new Price(new BigDecimal(1000)));
		MenuProduct menuProduct = new MenuProduct(1L, product, new Quantity(1));
		menu = new Menu(1L, "메뉴", new Price(new BigDecimal(1000)), menuGroup);
	}

	@Test
	void 주문항목_생성() {
		OrderLineItem actual = new OrderLineItem(1L, menu, new Quantity(1));
		assertThat(actual).isNotNull();
	}
}
