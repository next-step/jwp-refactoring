package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.TestDomainConstructor;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
	@Mock
	private MenuGroupDao menuGroupDao;
	@InjectMocks
	private MenuGroupService menuGroupService;

	@Test
	@DisplayName("메뉴그룹을 등록할 수 있다.")
	void create() {
		//given
		String name = "메뉴그룹1";
		MenuGroup menuGroup = TestDomainConstructor.menuGroup(name);
		MenuGroup savedMenuGroup = TestDomainConstructor.menuGroupWithId(name, 1L);
		when(menuGroupDao.save(menuGroup)).thenReturn(savedMenuGroup);

		//when
		MenuGroup result = menuGroupService.create(menuGroup);

		//then
		assertThat(result).isEqualTo(savedMenuGroup);
	}
}
