package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderTest {

    Long ORDER_TABLE_ID = 1L;

    @Autowired
	OrderRepository orders;
	@Autowired
	MenuRepository menus;
	@Autowired
	MenuGroupRepository menuGroups;
	@Autowired
	ProductRepository products;

	private Menu menu;

	@BeforeEach
	void setUp() {
		MenuGroup menuGroup = menuGroups.save(new MenuGroup("치킨"));
		List<Product> productList = products.saveAll(
			Lists.newArrayList(new Product("무우", 1_000L),
							   new Product("닭", 10_000L)
			));

		menu = menus.save(
			new Menu(
				"양념치킨",
				1_000L,
				menuGroup.getId(),
				MenuProduct.of(productList)));
	}

	@Test
	void testCascadeOrderLineItems() {
		OrderLineItems orderLineItems = new OrderLineItems(
			Lists.newArrayList(new OrderLineItem(menu.getId(), 3))
		);
		Order actualOrder = orders.save(new Order(ORDER_TABLE_ID, orderLineItems));
		List<OrderLineItem> actualOrderLineItems = actualOrder.getOrderLineItems().toList();

		Assertions.assertThat(actualOrderLineItems).isNotEmpty();
		Assertions.assertThat(actualOrderLineItems)
			.extracting(OrderLineItem::getMenuId)
			.containsExactly(menu.getId());
	}
}
