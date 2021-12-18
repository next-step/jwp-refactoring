package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupTest;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

	@InjectMocks
	private MenuGroupService menuGroupService;

	@Mock
	private MenuGroupDao menuGroupDao;

	@DisplayName("메뉴 그룹을 생성한다")
	@Test
	void createTest() {
		// given
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName("추천메뉴");

		MenuGroup persist = new MenuGroup();
		persist.setId(1L);
		persist.setName("추천메뉴");

		given(menuGroupDao.save(any())).willReturn(persist);

		// when
		MenuGroup result = menuGroupService.create(menuGroup);

		// then
		assertThat(result.getId()).isEqualTo(persist.getId());
		assertThat(result.getName()).isEqualTo(persist.getName());
	}

	@DisplayName("메뉴 그룹 목록을 조회한다")
	@Test
	void listTest() {
		// given
		List<MenuGroup> persist = new ArrayList<>();
		persist.add(MenuGroupTest.치킨류);
		
		given(menuGroupDao.findAll()).willReturn(persist);

		// when
		List<MenuGroup> result = menuGroupService.list();

		// then
		assertThat(result).containsAll(persist);
	}
}
