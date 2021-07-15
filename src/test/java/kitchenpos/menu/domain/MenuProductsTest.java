package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

	private Product 후라이드;
	private Product 양념;

	@BeforeEach
	void setUp() {
		후라이드 = new Product("후라이드", new Price(BigDecimal.valueOf(18000)));
		양념 = new Product("양념", new Price(BigDecimal.valueOf(19000)));
	}

	@DisplayName("메뉴 상품의 총 가격을 확인")
	@Test
	void getTotalPrice() {
		MenuProducts menuProducts = new MenuProducts();
		MenuProduct menuProduct = new MenuProduct(후라이드, 3L);
		menuProducts.addMenuProduct(menuProduct);

		Price actual = menuProducts.getTotalPrice();

		assertThat(actual).isEqualTo(new Price(BigDecimal.valueOf(54000L)));
	}

	@DisplayName("메뉴 상품의 추가로직 확인")
	@Test
	void testAddMenuProduct() {
		MenuProducts menuProducts = new MenuProducts();
		MenuProduct 후라이드메뉴상품 = new MenuProduct(후라이드, 1L);
		menuProducts.addMenuProduct(후라이드메뉴상품);
		MenuProduct 양념메뉴상품 = new MenuProduct(양념, 1L);
		menuProducts.addMenuProduct(양념메뉴상품);

		assertThat(menuProducts.getMenuProducts()).containsExactly(후라이드메뉴상품, 양념메뉴상품);
	}

}