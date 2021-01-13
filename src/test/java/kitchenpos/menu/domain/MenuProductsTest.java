package kitchenpos.menu.domain;

import static kitchenpos.utils.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductsTest {

	private MenuProduct menuProduct1;
	private MenuProduct menuProduct2;
	private MenuProduct menuProduct3;
	private List<MenuProduct> menuProducts;
	private BigDecimal priceSum;

	@BeforeEach
	void setUp() {
		menuProduct1 = 메뉴상품_후라이드;
		menuProduct2 = 메뉴상품_양념치킨;
		menuProduct3 = 메뉴상품_간장치킨;
		menuProducts = Arrays.asList(menuProduct1, menuProduct2, menuProduct3);
		priceSum = 메뉴상품_후라이드.getProductPrice().multiply(BigDecimal.valueOf(메뉴상품_후라이드.getQuantity()))
			.add(메뉴상품_양념치킨.getProductPrice().multiply(BigDecimal.valueOf(메뉴상품_양념치킨.getQuantity())))
			.add(메뉴상품_간장치킨.getProductPrice().multiply(BigDecimal.valueOf(메뉴상품_간장치킨.getQuantity())));
	}

	@Test
	@DisplayName("메뉴 구성 상품의 가격 * 메뉴에 포함된 수량 의 합을 정확히 계산해야 한다.")
	void priceSum() {
		//given
		MenuProducts actual = new MenuProducts(menuProducts);

		//when
		BigDecimal sum = actual.priceSum();

		//then
		assertThat(sum).isEqualByComparingTo(priceSum);
	}
}
