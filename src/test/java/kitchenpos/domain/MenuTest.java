package kitchenpos.domain;

import static java.util.Arrays.*;
import static kitchenpos.domain.MenuGroupTest.*;
import static kitchenpos.domain.MenuProductsTest.*;
import static kitchenpos.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

	@DisplayName("가격이 음수인 메뉴는 생성 될 수 없다.")
	@Test
	void negativePriceTest() {
		assertThatThrownBy(() -> Menu.create(
			Name.valueOf("치킨"),
			Price.wonOf(-1),
			치킨그룹,
			MenuProducts.of(asList(후라이드치킨2개, 피자3개))))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가격은 음수가 될 수 없습니다.");
	}

	@DisplayName("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없습니다.")
	@Test
	void moreExpensiveThanMenuProductsTest() {
		assertThatThrownBy(() -> Menu.create(
			Name.valueOf("치킨_피자_세트"),
			Price.wonOf(8001),
			치킨그룹,
			MenuProducts.of(asList(후라이드치킨2개, 피자3개))))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없습니다.");
	}

	@DisplayName("메뉴 이름, 가격, 메뉴 그룹, 메뉴상품 리스트 들로 메뉴를 만들 수 있다")
	@Test
	void createTest() {
		// given
		MenuProduct 후라이드치킨1개 = new MenuProduct(후라이드치킨, 1);

		// when
		Menu menu = Menu.create(
			Name.valueOf("치킨_피자_세트"),
			Price.wonOf(1000),
			치킨그룹,
			MenuProducts.of(asList(후라이드치킨1개)));

		// than
		assertThat(menu.getName()).isEqualTo(Name.valueOf("치킨_피자_세트"));
		assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(1000));
		assertThat(menu.getMenu()).isEqualTo(치킨그룹);
		assertThat(menu.getMenuProducts()).containsExactly(후라이드치킨1개);
		assertThat(후라이드치킨1개.getMenu()).isEqualTo(menu);
	}
}