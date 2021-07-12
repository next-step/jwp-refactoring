package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴 상품들 도메인 테스트")
public class MenuProductsTest {

	MenuProduct firstMenuProduct;
	MenuProduct secondMenuProduct;

	@BeforeEach
	void setUp() {
		Product firstProduct = new Product(1L, "상품", new Price(new BigDecimal(1000)));
		firstMenuProduct = new MenuProduct(1L, firstProduct, new Quantity(10));

		Product secondProduct = new Product(1L, "상품", new Price(new BigDecimal(500)));
		secondMenuProduct = new MenuProduct(1L, secondProduct, new Quantity(1));
	}
	
	@DisplayName("메뉴 상품들 생성 테스트")
	@Test
	void 메뉴_상품들_생성_테스트() {
		MenuProducts menuProducts = new MenuProducts(Arrays.asList(firstMenuProduct, secondMenuProduct));
		assertThat(menuProducts).isNotNull();
	}

	@DisplayName("메뉴 상품들의 총 가격 테스트") 
	@Test
	void 메뉴_상품들의_총_가격_테스트() {
		MenuProducts menuProducts = new MenuProducts(Arrays.asList(firstMenuProduct, secondMenuProduct));
		assertThat(menuProducts.getSumMenuProductPrice().value()).isEqualTo(new Price(new BigDecimal(10500)).value());
	}
}
