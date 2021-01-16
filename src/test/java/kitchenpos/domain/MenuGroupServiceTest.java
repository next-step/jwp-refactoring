package kitchenpos.domain;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MenuGroupServiceTest {
	@Mock
	private MenuGroupDao menuGroupDao;

	private MenuGroupService menuGroupService;

	@BeforeEach
	void setUp() {
		menuGroupService = new MenuGroupService(menuGroupDao);
		assertThat(menuGroupService).isNotNull();
	}

	@Test
	@DisplayName("메뉴 그룹을등록한다")
	void create(){
		MenuGroup menuGroup = new MenuGroup();
		when(menuGroupDao.save(any())).thenReturn( menuGroup);

		assertThat(menuGroupService.create(menuGroup)).isEqualTo(menuGroup);
	}

	@Test
	@DisplayName("메뉴 그룹 목록을조회한다")
	void list(){
		when(menuGroupDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new MenuGroup(), new MenuGroup())));

		assertThat(menuGroupService.list()).isNotNull();
		assertThat(menuGroupService.list()).isNotEmpty();
		assertThat(menuGroupService.list().size()).isEqualTo(2);
	}

}
