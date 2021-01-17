package kitchenpos.mockito;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MenuGroupServiceTest {
	@Mock
	private MenuGroupRepository menuGroupRepository;

	private MenuGroupService menuGroupService;

	@Mock
	private MenuGroup menuGroup;

	@BeforeEach
	void setUp() {
		menuGroupService = new MenuGroupService(menuGroupRepository);
		assertThat(menuGroupService).isNotNull();
		menuGroup = mock(MenuGroup.class);
	}

	@Test
	@DisplayName("메뉴 그룹을등록한다")
	void create() {
		given(menuGroupRepository.save(any())).willReturn(menuGroup);
//		assertThat(menuGroupService.create(menuGroup)).isEqualTo(menuGroup);
	}

	@Test
	@DisplayName("메뉴 그룹 목록을조회한다")
	void listMenuGroups() {
		given(menuGroupRepository.findAll()).willReturn(new ArrayList<>(Arrays.asList(mock(MenuGroup.class), mock(MenuGroup.class))));

		assertThat(menuGroupService.listMenuGroups()).isNotNull();
		assertThat(menuGroupService.listMenuGroups()).isNotEmpty();
		assertThat(menuGroupService.listMenuGroups().size()).isEqualTo(2);
	}

}
