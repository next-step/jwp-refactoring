package kitchenpos.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
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
	private MenuGroupRepository menuGroupRepository;

	@InjectMocks
	private MenuGroupService menuGroupService;

	@Test
	void menuGroupCreateTest() {
		MenuGroupRequest menuGroupRequest = new MenuGroupRequest();
		when(menuGroupRepository.save(menuGroupRequest.toMenuGroup())).thenReturn(menuGroupRequest.toMenuGroup());
		assertThat(menuGroupService.create(menuGroupRequest)).isNotNull();
	}

	@Test
	void getMenuGroupListTest() {
		when(menuGroupRepository.findAll()).thenReturn(Lists.newArrayList(new MenuGroup(), new MenuGroup()));
		assertThat(menuGroupService.list()).hasSize(2);
	}
}
