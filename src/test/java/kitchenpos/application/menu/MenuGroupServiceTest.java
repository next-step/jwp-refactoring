package kitchenpos.application.menu;

import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
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

	@DisplayName("메뉴 그룹을 생성.")
	@Test
	void 메뉴_그룹을_생성() {
		MenuGroup menuGroup = new MenuGroup(1L, "빵세트");

		given(menuGroupDao.save(menuGroup)).willReturn(menuGroup);

		Assertions.assertThat(menuGroupService.create(menuGroup)).isEqualTo(menuGroup);
	}

	@DisplayName("모든 메뉴 그룹 목록 조회.")
	@Test
	void 모든_메뉴_그룹_목록_조회() {
		MenuGroup menuGroup1 = new MenuGroup(1L, "빵세트");
		MenuGroup menuGroup2 = new MenuGroup(2L, "치킨세트");

		given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2));

		Assertions.assertThat(menuGroupService.list()).containsExactly(menuGroup1, menuGroup2);
	}
}
