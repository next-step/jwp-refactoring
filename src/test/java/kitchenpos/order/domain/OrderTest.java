package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@DataJpaTest
class OrderTest {

	@Autowired
	OrderRepository orders;
	@Autowired
	OrderTableRepository orderTables;
	@Autowired
	MenuRepository menus;
	@Autowired
	MenuGroupRepository menuGroups;
	@Autowired
	ProductRepository products;

	OrderTable orderTable;

	private Menu menu;

	@BeforeEach
	void setUp() {
		orderTable = orderTables.save(new OrderTable(1, true));
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
		Order actualOrder = orders.save(new Order(orderTable.getId(), orderLineItems));
		List<OrderLineItem> actualOrderLineItems = actualOrder.getOrderLineItems().toList();

		assertThat(actualOrderLineItems).isNotEmpty();
		assertThat(actualOrderLineItems)
			.extracting(OrderLineItem::getMenuId)
			.containsExactly(menu.getId());
	}
}
