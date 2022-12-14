package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹2;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.MenuGroup2;
import kitchenpos.domain.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	MenuGroupRepository menuGroupRepository;

	@InjectMocks
	MenuGroupService menuGroupService;

	@Test
	@DisplayName("메뉴 그룹 생성")
	void testCreateMenuGroup() {
		MenuGroup2 menuGroup = 메뉴그룹2();

		menuGroupService.create(menuGroup);

		verify(menuGroupRepository, times(1)).save(menuGroup);
	}

	@Test
	@DisplayName("메뉴 목록 조회")
	void testMenuLst() {
		menuGroupService.list();

		verify(menuGroupRepository, times(1)).findAll();
	}

}
