package domain.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

	private Menu menu;
	private MenuProduct 짜장면;
	private MenuProduct 짬뽕;
	private MenuProduct 탕수육;
	private MenuProduct 콜라;

	@BeforeEach
	void setUp() {
		menu = new Menu("짜짬탐", 20000, new MenuGroup("중식"));
		짜장면 = new MenuProduct(new Product("짜장면", 5000), 1);
		짬뽕 = new MenuProduct(new Product("짬봉", 6000), 1);
		탕수육 = new MenuProduct(new Product("탕수육", 10000), 1);
		콜라 = new MenuProduct(new Product("콜라", 1000), 1);
	}

	@DisplayName("메뉴의 구성 상품을 추가한다.")
	@Test
	void addMenuProducts() {
		menu.addMenuProducts(Arrays.asList(짜장면, 짬뽕, 탕수육));

		assertThat(menu.getMenuProducts())
				.containsExactly(짜장면, 짬뽕, 탕수육);
	}

	@DisplayName("메뉴의 구성 상품을 계속 추가할 수 있다.")
	@Test
	void addMenuProducts_Continue() {
		menu.addMenuProducts(Arrays.asList(짜장면, 짬뽕, 탕수육));

		menu.addMenuProducts(Collections.singletonList(콜라));

		assertThat(menu.getMenuProducts())
				.containsExactly(짜장면, 짬뽕, 탕수육, 콜라);
	}

	@DisplayName("상품을 추가할 때 메뉴의 가격이 상품의 가격합을 초과할경우 예외 발생.")
	@Test
	void addMenuProducts_Exception() {
		assertThatThrownBy(() -> menu.addMenuProducts(Arrays.asList(짜장면, 짬뽕)))
				.isInstanceOf(MenuValidationException.class)
				.hasMessageMatching(Menu.MSG_PRICE_RULE);
	}
}
