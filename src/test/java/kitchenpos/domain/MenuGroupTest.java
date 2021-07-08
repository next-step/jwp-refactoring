package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Name;

class MenuGroupTest {

	public static MenuGroup 치킨그룹 = new MenuGroup(Name.valueOf("치킨그룹"));

	@DisplayName("메뉴 그룹의 이름은 필수 정보이다.")
	@Test
	void createMenuWithNullValueTest() {
		assertThatThrownBy(() -> new MenuGroup(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴 그룹의 이름은 필수 정보 입니다.");
	}
}