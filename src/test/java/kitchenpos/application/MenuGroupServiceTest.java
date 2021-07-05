package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {
	@Autowired
	private MenuGroupDao menuGroupDao;

	@Autowired
	private MenuGroupService menuGroupService;

	@Test
	public void 메뉴그룹_추가() {
		MenuGroup menuGroup = new MenuGroup();
		String menuGroupname = "추천메뉴";
		menuGroup.setName(menuGroupname);

		MenuGroup savedMenugroup = menuGroupService.create(menuGroup);

		assertThat(savedMenugroup.getName()).isEqualTo(menuGroupname);
	}

	@Test
	public void 메뉴그룹_조회() {
		MenuGroup menuGroup = new MenuGroup();
		String menuGroupname = "추천메뉴";
		menuGroup.setName(menuGroupname);

		menuGroupDao.save(menuGroup);

		List<MenuGroup> menuGroups = menuGroupService.list();

		assertThat(menuGroups)
				.extracting("name")
				.contains(menuGroupname);
	}
}