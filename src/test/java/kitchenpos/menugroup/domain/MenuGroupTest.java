package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Name;

@DisplayName("메뉴 그룹")
class MenuGroupTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		Name name = Name.of("추천 메뉴");

		// when
		MenuGroup menuGroup = MenuGroup.of(name);

		// then
		assertThat(menuGroup.getName()).isEqualTo(name);
	}
}
