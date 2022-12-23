package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.exception.InvalidMenuPriceException;

class MenuValidatorTest {

	MenuValidator menuValidator;

	String MENU_NAME = "menu";
	Long MENU_GROUP_ID = 1L;
	List<MenuProduct> products;

	@BeforeEach
	void setUp() {
		menuValidator = new MenuValidator();
		products = Lists.newArrayList(
			new MenuProduct(new Product(1L, "product1", Money.valueOf(1000)), 1),
			new MenuProduct(new Product(2L, "product2", Money.valueOf(1000)), 1),
			new MenuProduct(new Product(3L, "product3", Money.valueOf(1000)), 1));
	}

	@Test
	@DisplayName("메뉴의 가격이 상품목록 가격 합보다 같거나 클 경우 등록 성공")
	void testValidateMenuPriceSuccess() {
		Money validPrice = sumProductsPrice();
		Menu validMenu = new Menu(MENU_NAME, validPrice.longValue(), MENU_GROUP_ID, products);

		assertThatCode(() -> menuValidator.validate(validMenu))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("메뉴의 가격이 상품목록 가격 합보다 작을 경우 등록 실패")
	void testCreateMenuWhenMenuPriceGreaterThanSumOfProductsPrice() {
		Money invalidMenuPrice = sumProductsPrice().minus(1);
		Menu invalidMenu = new Menu(MENU_NAME,
									invalidMenuPrice.longValue(),
									MENU_GROUP_ID,
									products);

		assertThatThrownBy(() -> menuValidator.validate(invalidMenu))
			.isInstanceOf(InvalidMenuPriceException.class);
	}

	private Money sumProductsPrice() {
		return products.stream()
						.map(MenuProduct::totalPrice)
						.reduce(Money.ZERO, Money::add);
	}
}
