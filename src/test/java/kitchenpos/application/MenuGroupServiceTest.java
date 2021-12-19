package kitchenpos.application;

import static kitchenpos.menugroup.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 통합 테스트")
class MenuGroupServiceTest extends IntegrationTest {
	@Autowired
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹을 등록한다.")
	@Test
	void register() {
		// when
		MenuGroup menuGroup = menuGroupService.create(추천_메뉴_그룹().toMenuGroup());

		// then
		assertThat(menuGroup.getId()).isNotNull();
	}

	@DisplayName("메뉴 그룹 이름이 없는 경우 메뉴 그룹 등록에 실패한다.")
	@Test
	void registerFailOnEmptyName() {
		// when
		ThrowableAssert.ThrowingCallable throwingCallable = () -> menuGroupService.create(이름없는_메뉴_그룹().toMenuGroup());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		MenuGroup 추천_메뉴_그룹 = menuGroupService.create(추천_메뉴_그룹().toMenuGroup());
		MenuGroup 비추천_메뉴_그룹 = menuGroupService.create(비추천_메뉴_그룹().toMenuGroup());

		// when
		List<MenuGroup> menuGroups = menuGroupService.list();

		// then
		List<Long> actualIds = menuGroups.stream()
			.map(MenuGroup::getId)
			.collect(Collectors.toList());
		List<Long> expectedIds = Stream.of(추천_메뉴_그룹, 비추천_메뉴_그룹)
			.map(MenuGroup::getId)
			.collect(Collectors.toList());
		assertThat(actualIds).containsAll(expectedIds);
	}
}
