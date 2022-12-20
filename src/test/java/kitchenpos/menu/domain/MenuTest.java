package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuTest {

	@Autowired
	MenuRepository menus;
	@Autowired
	MenuGroupRepository menuGroups;
	@Autowired
	ProductRepository products;

	MenuGroup menuGroup;
	List<Product> productsList;
	List<MenuProduct> menuProducts;


	@BeforeEach
	void setUp() {
		menuGroup = menuGroups.save(new MenuGroup("치킨"));
		productsList = products.saveAll(
			Lists.newArrayList(new Product("무우", 1_000L),
							   new Product("닭", 10_000L)));
		menuProducts = productsList
							   .stream()
							   .map(product -> new MenuProduct(product, 1))
							   .collect(Collectors.toList());
	}

	@Test
	void testMenuProductCascade() {
		Menu aMenu = menus.save(new Menu(
			"양념치킨", 1_000L, menuGroup.getId(), menuProducts));

		List<MenuProduct> menuProducts = aMenu.getMenuProducts();

		assertThat(menuProducts).isNotEmpty();
		assertThat(menuProducts)
			.extracting(MenuProduct::getProduct)
			.containsExactlyInAnyOrderElementsOf(productsList);
	}
}
