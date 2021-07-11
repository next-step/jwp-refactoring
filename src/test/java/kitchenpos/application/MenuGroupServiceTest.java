package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
	private MenuGroupService menuGroupService;

	@Mock
	private MenuGroupDao menuGroupDao;

	@BeforeEach
	void setUp() {
		menuGroupService = new MenuGroupService(menuGroupDao);
	}

	@Test
	void createMenuGroup() {
		// given
		when(menuGroupDao.save(any())).thenReturn(new MenuGroup());
		// when
		MenuGroup menuGroup = menuGroupService.create(new MenuGroup());
		// then
		assertThat(menuGroup).isEqualTo(new MenuGroup());
	}

	@Test
	void findMenuGroup() {
		// given
		when(menuGroupDao.findAll()).thenReturn(Arrays.asList(new MenuGroup(), new MenuGroup()));
		// when
		List<MenuGroup> menuGroups = menuGroupService.list();
		// then
		assertThat(menuGroups.size()).isEqualTo(2);
	}
}
