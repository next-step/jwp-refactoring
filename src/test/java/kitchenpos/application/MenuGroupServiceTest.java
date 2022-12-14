package kitchenpos.application;

import static kitchenpos.generator.MenuGroupGenerator.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@Mock
	private MenuGroupDao menuGroupDao;

	@InjectMocks
	private MenuGroupService menuGroupService;

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void createMenuGroupTest() {
		// given
		MenuGroup menuGroup = 메뉴_그룹("순살치킨");

		// when
		menuGroupService.create(menuGroup);

		// then
		verify(menuGroupDao, only()).save(any());
	}

	@DisplayName("메뉴 그룹들을 조회할 수 있다.")
	@Test
	void listMenuGroupTest() {
		// when
		menuGroupService.list();

		// then
		verify(menuGroupDao, only()).findAll();
	}

}