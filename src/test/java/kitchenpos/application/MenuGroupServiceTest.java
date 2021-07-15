package kitchenpos.application;

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

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 기능 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	private MenuGroup 추천메뉴;
	private MenuGroup 계절메뉴;

	@Mock
	private MenuGroupDao menuGroupDao;

	@InjectMocks
	private MenuGroupService menuGroupService;

	@BeforeEach
	void setup() {
		추천메뉴 = new MenuGroup();
		추천메뉴.setId(1L);
		추천메뉴.setName("추천메뉴");

		계절메뉴 = new MenuGroup();
		계절메뉴.setId(2L);
		계절메뉴.setName("계절메뉴");
	}

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	public void create() {
		// given
		given(menuGroupDao.save(추천메뉴)).willReturn(추천메뉴);

		// when
		MenuGroup createdMenuGroup = menuGroupService.create(추천메뉴);

		// then
		assertThat(createdMenuGroup.getId()).isEqualTo(this.추천메뉴.getId());
		assertThat(createdMenuGroup.getName()).isEqualTo(this.추천메뉴.getName());
	}

	@DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
	@Test
	public void list() {
		// given
		given(menuGroupDao.findAll()).willReturn(Arrays.asList(추천메뉴, 계절메뉴));

		// when
		List<MenuGroup> menuGroups = menuGroupService.list();

		// then
		assertThat(menuGroups).containsExactly(추천메뉴, 계절메뉴);
	}

}
