package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Name;

@DisplayName("메뉴 그룹")
class MenuGroupTest {

	@DisplayName("생성")
	@Test
	void from() {
		// given
		Name name = Name.from("추천 메뉴");

		// when
		MenuGroup menuGroup = MenuGroup.from(name);

		// then
		assertThat(menuGroup.getName()).isEqualTo(name);
	}
}
