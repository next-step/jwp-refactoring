package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.utils.IntegrationTest;

/**
 * @author : byungkyu
 * @date : 2021/01/19
 * @description :
 **/
@DisplayName("메뉴그룹")
class MenuGroupServiceTest extends IntegrationTest {

	@Autowired
	private MenuGroupService menuGroupService;

	@Autowired
	private MenuGroupDao menuGroupDao;

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void create() {
		// given
		MenuGroup menuGroup = new MenuGroup("한식");

		// when
		MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

		// then
		assertThat(savedMenuGroup.getId()).isNotNull();
		assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
	}

	@DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		List<MenuGroup> findAllByMenuGroupDao = menuGroupDao.findAll();

		// when
		List<MenuGroup> menuGroups = menuGroupService.list();

		// then
		assertThat(menuGroups.size()).isGreaterThan(0);
		assertThat(menuGroups).containsAll(findAllByMenuGroupDao);
	}
}