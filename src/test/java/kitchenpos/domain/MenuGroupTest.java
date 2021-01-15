package kitchenpos.domain;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import org.junit.jupiter.api.BeforeEach;
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
public class MenuGroupTest {
	@Mock
	private MenuGroupDao menuGroupDao;

	MenuGroupService menuGroupService;

	@BeforeEach
	void setUp() {
		menuGroupService = new MenuGroupService(menuGroupDao);
		assertThat(menuGroupService).isNotNull();
	}

	@Test
	void 메뉴_그룹을_등록한다(){
		MenuGroup menuGroup = new MenuGroup();
		when(menuGroupDao.save(any())).thenReturn( menuGroup);

		assertThat(menuGroupService.create(menuGroup)).isEqualTo(menuGroup);
	}

	@Test
	void 메뉴_그룹_목록을_조회한다(){
		when(menuGroupDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new MenuGroup(), new MenuGroup())));

		assertThat(menuGroupService.list()).isNotNull();
		assertThat(menuGroupService.list()).isNotEmpty();
		assertThat(menuGroupService.list().size()).isEqualTo(2);
	}

}
