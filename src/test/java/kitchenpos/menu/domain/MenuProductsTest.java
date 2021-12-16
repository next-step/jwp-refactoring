package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.PriceException;
import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;

@DisplayName("단위 테스트 : 메뉴 상품 일급 컬렉션 관련")
class MenuProductsTest {

	private Product product1;
	private Product product2;
	private List<MenuProduct> menuProductList;
	private MenuProducts menuProducts;

	@BeforeEach
	void setup() {
		product1 = Product.of("p1", 10000);
		product2 = Product.of("p1", 20000);
		MenuProduct menuProduct1 = MenuProduct.of(null, product1, 1L);
		MenuProduct menuProduct2 = MenuProduct.of(null, product2, 1L);
		menuProductList = Arrays.asList(menuProduct1, menuProduct2);
		menuProducts = MenuProducts.from(menuProductList);
	}

	@DisplayName("메뉴 상품들의 총 합계 가격 반환 테스트")
	@Test
	void calculateSum() {
		// when
		Price sum = menuProducts.getSum();

		// then
		assertThat(sum.getPrice().intValue()).isEqualTo(30000);
	}

	@DisplayName("메뉴 가격과 상품들의 가격을 비교하는 테스트")
	@Test
	void compareMenuPriceNSum() {
		// given
		Price menuPrice = Price.from(40000);

		// when
		boolean result = menuProducts.comparePrice(menuPrice);

		// then
		assertThat(result).isTrue();
	}

	@DisplayName("메뉴 가격이 상품들의 가격보다 클때 예외처리 테스트")
	@Test
	void comparePriceException() {
		// given
		Price menuPrice = Price.from(40000);

		// when // then
		assertThatThrownBy(() -> {
			menuProducts.validateMenuPrice(menuPrice);
		}).isInstanceOf(PriceException.class);
	}
}

