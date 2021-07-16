package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.exception.PriceException;

@DisplayName("메뉴 도메인 테스트")
public class MenuTest {

	@DisplayName("메뉴 생성")
	@Test
	void 메뉴_생성() {
		MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
		Product product = new Product(1L, "상품이름", new Price(new BigDecimal(1000)));
		Menu menu = new Menu(1L, "메뉴", new Price(new BigDecimal(1000)), menuGroup);
		assertThat(menu).isNotNull();

	}

	@DisplayName("메뉴 생성 시 - 메뉴 가격이 Null일 경우 에러 발생")
	@Test
	void 메뉴_생성_시_메뉴_가격이_NULL일_경우_에러() {
		MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
		Product product = new Product(1L, "상품이름", new Price(new BigDecimal(1000)));
		assertThatThrownBy(() ->
			new Menu(1L, "메뉴", new Price(null), menuGroup)
		).isInstanceOf(PriceException.class);
	}

	@DisplayName("메뉴 생성 시 - 메뉴 가격이 0보다 작을 경우 에러")
	@Test
	void 메뉴_생성_시_메뉴_가격이_0보다_작을_경우_에러() {
		MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
		Product product = new Product(1L, "상품이름", new Price(new BigDecimal(1000)));
		assertThatThrownBy(() ->
			new Menu(1L, "메뉴", new Price(BigDecimal.valueOf(-1000)), menuGroup)
		).isInstanceOf(PriceException.class);
	}

}
