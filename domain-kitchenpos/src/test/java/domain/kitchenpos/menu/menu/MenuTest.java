package domain.kitchenpos.menu.menu;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import domain.kitchenpos.menu.product.Product;

@DisplayName("메뉴 도메인 테스트")
public class MenuTest {

	@DisplayName("메뉴 생성")
	@Test
	void create() {
		final String name = "양념치킨";
		final BigDecimal price = new BigDecimal(16000);
		final MenuGroup menuGroup = new MenuGroup("치킨메뉴그룹");

		final Menu menu = new Menu(name, price, menuGroup);

		assertThat(menu).isNotNull();
		assertThat(menu.getName()).isEqualTo(name);
		assertThat(menu.getPrice()).isEqualTo(price);
		assertThat(menu.getMenuGroup().getName()).isEqualTo(menuGroup.getName());
	}

	@DisplayName("메뉴 상품 가격의 합이 메뉴의 가격보다 작음")
	@Test
	void throwExceptionWhenSumOfMenuProductsPriceLessThanMenuPrice() {
		final String name = "양념치킨_후라이드치킨_반반";
		final BigDecimal price = new BigDecimal(20000);
		final MenuGroup menuGroup = new MenuGroup("치킨메뉴그룹");
		final Menu menu = new Menu(name, price, menuGroup);
		final Product 양념치킨_반마리 = new Product("양념치킨_반마리", new BigDecimal(10000));
		final Product 후라이드치킨_반마리 = new Product("후라이드치킨_반마리", new BigDecimal(9999));

		MenuProduct menuProduct1 = new MenuProduct(menu, 양념치킨_반마리, 1L);
		MenuProduct menuProduct2 = new MenuProduct(menu, 후라이드치킨_반마리, 1L);

		assertThatIllegalArgumentException()
			.isThrownBy(() ->  menu.addMenuProducts(Arrays.asList(menuProduct1, menuProduct2)));
	}

}
