package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 통합 테스트")
class MenuGroupServiceTest extends IntegrationTest {
	private static final String NAME = "추천메뉴";

	@Autowired
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void create() {
		// given
		MenuGroup request = new MenuGroup();
		request.setName(NAME);

		// when
		MenuGroup menuGroup = menuGroupService.create(request);

		// then
		assertThat(menuGroup.getId()).isNotNull();
		assertThat(menuGroup.getName()).isEqualTo(NAME);
	}

	@DisplayName("메뉴 그룹 이름이 빈 값이면 등록할 수 없다.")
	@ParameterizedTest
	@NullSource
	void createFailOnEmptyName(String name) {
		// given
		MenuGroup request = new MenuGroup();
		request.setName(name);

		// when
		ThrowingCallable throwingCallable = () -> menuGroupService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		MenuGroup request = new MenuGroup();
		request.setName(NAME);
		MenuGroup given = menuGroupService.create(request);

		// when
		List<MenuGroup> actual = menuGroupService.list();

		// then
		List<Long> actualIds = actual.stream().map(MenuGroup::getId).collect(Collectors.toList());

		assertAll(
			() -> assertThat(actual).isNotEmpty(),
			() -> assertThat(actualIds).contains(given.getId())
		);
	}
}
