package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
	private final MenuGroupDao menuGroupDao;

	public MenuGroupService(final MenuGroupDao menuGroupDao) {
		this.menuGroupDao = menuGroupDao;
	}

	@Transactional
	public MenuGroupResponse create(MenuGroupRequest request) {
		MenuGroup menuGroup = new MenuGroup(request.getName());
		return MenuGroupResponse.of(menuGroupDao.save(menuGroup));
	}

	public List<MenuGroupResponse> list() {
		return menuGroupDao.findAll().stream()
				.map(MenuGroupResponse::of)
				.collect(Collectors.toList());
	}
}
