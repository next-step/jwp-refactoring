package kitchenpos.menu.domain;

import static kitchenpos.domain.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

	@Test
	@DisplayName("메뉴와 함께 생성되면 메뉴상품들의 메뉴가 모두 전달된 메뉴로 업데이트 되어야한다.")
	void constructWithMenu() {
		//when
		MenuProducts actual = new MenuProducts(메뉴_간장치킨, menuProducts);

		//then
		List<Long> distinctMenuIds = actual.list().stream()
			.map(MenuProduct::getMenuId)
			.distinct()
			.collect(Collectors.toList());

		assertThat(distinctMenuIds.size()).isEqualTo(1);
		assertThat(distinctMenuIds).isEqualTo(Arrays.asList(메뉴_간장치킨.getId()));
	}
}
