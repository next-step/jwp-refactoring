package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@InjectMocks
	private MenuGroupService menuGroupService;

	@Mock
	private MenuGroupDao menuGroupDao;

	@DisplayName("메뉴 그룹을 생성한다.")
	@Test
	void create() {
		MenuGroup menuGroup = mock(MenuGroup.class);

		menuGroupService.create(menuGroup);

		verify(menuGroupDao).save(menuGroup);
	}

	@DisplayName("메뉴 그룹 리스트를 반환한다.")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 20})
	void list(int size) {
		given(menuGroupDao.findAll()).willReturn(MockFixture.anyMenuGroups(size));

		List<MenuGroup> menuGroups = menuGroupService.list();

		assertThat(menuGroups).hasSize(size);
	}

}
