package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
	@Mock
	private MenuGroupDao menuGroupDao;

	@InjectMocks
	private MenuGroupService menuGroupService;

	@Test
	void menuGroupCreateTest() {
		MenuGroup menuGroup = new MenuGroup();
		when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);
		assertThat(menuGroupService.create(menuGroup)).isNotNull();
	}

	@Test
	void getMenuGroupListTest() {
		when(menuGroupDao.findAll()).thenReturn(Lists.newArrayList(new MenuGroup(), new MenuGroup()));
		assertThat(menuGroupService.list()).hasSize(2);
	}
}
