package kitchenpos.menu.domain;

import static kitchenpos.utils.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

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
	@DisplayName("메뉴구성상품 설정 시, 구성상품 가격의 합보다 메뉴의 가격이 비싸면 IllegalArgumentException 을 throw 해야한다.")
	void priceSum() {
		//given
		BigDecimal expensivePrice = priceSum.add(BigDecimal.valueOf(100000));

		//when-then
		assertThatThrownBy(() -> new Menu.Builder("비싼메뉴"
			, expensivePrice
			, new MenuGroup()
			, menuProducts)
			.build())
			.isInstanceOf(IllegalArgumentException.class);
	}
}
