package kitchenpos.application;

import static kitchenpos.menugroup.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
	@Mock
	private MenuGroupDao menuGroupDao;
	@InjectMocks
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹을 등록한다.")
	@Test
	void register() {
		// given
		given(menuGroupDao.save(any())).willReturn(추천_메뉴_그룹());

		// when
		MenuGroup menuGroup = menuGroupService.create(추천_메뉴_그룹_요청().toMenuGroup());

		// then
		assertThat(menuGroup.getId()).isNotNull();
	}

	@DisplayName("메뉴 그룹 이름이 없는 경우 메뉴 그룹 등록에 실패한다.")
	@Test
	void registerFailOnEmptyName() {
		// given
		given(menuGroupDao.save(any())).willThrow(RuntimeException.class);

		// when
		ThrowingCallable throwingCallable = () -> menuGroupService.create(이름없는_메뉴_그룹_요청().toMenuGroup());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		given(menuGroupDao.findAll()).willReturn(Arrays.asList(추천_메뉴_그룹(), 비추천_메뉴_그룹()));

		// when
		List<MenuGroup> menuGroups = menuGroupService.list();

		// then
		List<Long> actualIds = menuGroups.stream()
			.map(MenuGroup::getId)
			.collect(Collectors.toList());
		List<Long> expectedIds = Stream.of(추천_메뉴_그룹(), 비추천_메뉴_그룹())
			.map(MenuGroup::getId)
			.collect(Collectors.toList());
		assertThat(actualIds).containsAll(expectedIds);
	}
}
