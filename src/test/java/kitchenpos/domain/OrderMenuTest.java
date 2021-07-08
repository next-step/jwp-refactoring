package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;

class OrderMenuTest {

	@DisplayName("주문메뉴의 주문한 메뉴 id, 이름, 가격은 필수정보이다.")
	@Test
	void createValidateTest() {
		Name menuName = Name.valueOf("떡볶이");
		Price menuPrice = Price.wonOf(3000);
		Long menuId = 1L;

		assertAll(
			() -> assertThatThrownBy(() -> OrderMenu.of(null, menuName, menuPrice))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("주문메뉴 필수정보가 부족합니다."),
			() -> assertThatThrownBy(() -> OrderMenu.of(menuId, null, menuPrice))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("주문메뉴 필수정보가 부족합니다."),
			() -> assertThatThrownBy(() -> OrderMenu.of(menuId, menuName, null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("주문메뉴 필수정보가 부족합니다.")
		);

	}

}
