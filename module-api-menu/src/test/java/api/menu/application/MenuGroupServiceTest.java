package api.menu.application;

import api.menu.dto.MenuGroupRequest;
import api.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {

	@Autowired
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹을 생성한다.")
	@ParameterizedTest
	@ValueSource(strings = {"중식", "한식", "일식"})
	void create(String name) {
		// given
		MenuGroupRequest request = new MenuGroupRequest(name);

		// when
		MenuGroupResponse response = menuGroupService.create(request);

		// then
		assertThat(response.getId()).isNotNull();
		assertThat(response.getName()).isEqualTo(name);
	}

	@DisplayName("메뉴 그룹 리스트를 반환한다.")
	@Test
	void list() {
		create("중식");
		create("일식");
		create("한식");
		List<MenuGroupResponse> menuGroups = menuGroupService.list();

		assertThat(menuGroups)
				.map(MenuGroupResponse::getName)
				.contains("중식", "일식", "한식");

	}
}
