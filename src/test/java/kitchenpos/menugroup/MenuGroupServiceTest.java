package kitchenpos.menugroup;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

	@Mock
	private MenuGroupDao menuGroupDao;

	@InjectMocks
	private MenuGroupService menuGroupService;

	MenuGroup 피자;
	MenuGroup 치킨;

	@BeforeEach
	void setUp() {
		피자 = 메뉴그룹_생성(1L, "피자");
		치킨 = 메뉴그룹_생성(2L, "치킨");
	}

	@DisplayName("메뉴 그룹을 생성한다.")
	@Test
	void 메뉴_그룹을_등록한다() {
		given(menuGroupDao.save(피자)).willReturn(피자);

		MenuGroup createdMenuGroup = menuGroupService.create(피자);

		assertThat(createdMenuGroup.getId()).isEqualTo(피자.getId());
		assertThat(createdMenuGroup.getName()).isEqualTo(피자.getName());
	}

	@DisplayName("메뉴 그룹 리스트를 조회한다.")
	@Test
	void 메뉴_그룹_리스트를_조회한다() {
		given(menuGroupDao.findAll()).willReturn(Arrays.asList(피자, 치킨));

		List<MenuGroup> 메뉴그룹들 = menuGroupService.list();

		assertThat(메뉴그룹들).containsAll(Arrays.asList(피자, 치킨));
	}

	public static MenuGroup 메뉴그룹_생성(Long id, String name) {
		MenuGroup 메뉴그룹 = new MenuGroup(id, name);
		return 메뉴그룹;
	}

}
