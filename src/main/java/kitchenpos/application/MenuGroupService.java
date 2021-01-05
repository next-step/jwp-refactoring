package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;

@Service
public class MenuGroupService {
	private final MenuGroupDao menuGroupDao;

	public MenuGroupService(final MenuGroupDao menuGroupDao) {
		this.menuGroupDao = menuGroupDao;
	}

	@Transactional
	public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
		MenuGroup menuGroup = menuGroupDao.save(MenuGroup.create(menuGroupRequest.getName()));
		return MenuGroupResponse.of(menuGroup);
	}

	public List<MenuGroupResponse> list() {
		List<MenuGroup> menuGroups = menuGroupDao.findAll();
		return menuGroups.stream()
			.map(MenuGroupResponse::of)
			.collect(Collectors.toList());
	}
}
