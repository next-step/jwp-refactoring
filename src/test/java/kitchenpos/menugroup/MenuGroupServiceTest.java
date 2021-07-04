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

	MenuGroup 와퍼세트;
	MenuGroup 치즈와퍼세트;

	@BeforeEach
	void setUp() {
		와퍼세트 = new MenuGroup();
		와퍼세트.setId(1L);
		와퍼세트.setName("와퍼세트");
		치즈와퍼세트 = new MenuGroup();
		치즈와퍼세트.setId(2L);
		치즈와퍼세트.setName("치즈와퍼세트");
	}

	@DisplayName("메뉴 그룹을 생성한다.")
	@Test
	void 메뉴_그룹을_등록한다() {
		given(menuGroupDao.save(와퍼세트)).willReturn(와퍼세트);
		MenuGroup createdMenuGroup = menuGroupService.create(와퍼세트);
		assertThat(createdMenuGroup.getId()).isEqualTo(와퍼세트.getId());
		assertThat(createdMenuGroup.getName()).isEqualTo(와퍼세트.getName());
	}

	@DisplayName("메뉴 그룹 리스트를 조회한다.")
	@Test
	void 메뉴_그룹_리스트를_조회한다() {
		given(menuGroupDao.findAll()).willReturn(Arrays.asList(와퍼세트, 치즈와퍼세트));
		List<MenuGroup> 버거킹메뉴그룹 = menuGroupService.list();
		assertThat(버거킹메뉴그룹).containsAll(Arrays.asList(와퍼세트, 치즈와퍼세트));
	}

}
