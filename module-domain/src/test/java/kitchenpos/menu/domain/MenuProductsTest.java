package kitchenpos.menu.domain;

import static kitchenpos.menu.MenuProductFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품들")
class MenuProductsTest {

	@DisplayName("생성")
	@Test
	void from() {
		// given
		MenuProduct menuProduct1 = 강정치킨_메뉴_상품();
		MenuProduct menuProduct2 = 양념치킨_메뉴_상품();

		// when
		MenuProducts.from(Arrays.asList(menuProduct1, menuProduct2));
	}

	@DisplayName("생성 실패 - 한개 이상이어야 한다.")
	@Test
	void fromFailOnEmpty() {
		// given
		List<MenuProduct> empty = Collections.emptyList();

		// when
		ThrowingCallable throwingCallable = () -> MenuProducts.from(empty);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
