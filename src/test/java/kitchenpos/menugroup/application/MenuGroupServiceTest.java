package kitchenpos.menugroup.application;

import static kitchenpos.common.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@InjectMocks
	private MenuGroupService menuGroupService;

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@Test
	void create() {
		final MenuGroup 일식 = menuGroup(1L, "일식");
		given(menuGroupRepository.save(any())).willReturn(일식);

		final MenuGroup created = menuGroupService.create(menuGroup(null, "일식"));

		assertThat(created.getId()).isNotNull();
	}

	@Test
	void list() {
		final MenuGroup 한식 = menuGroup(1L, "한식");
		final MenuGroup 중식 = menuGroup(2L, "중식");
		given(menuGroupRepository.findAll()).willReturn(Arrays.asList(한식, 중식));

		assertThat(menuGroupService.list()).containsExactly(한식, 중식);
	}
}
