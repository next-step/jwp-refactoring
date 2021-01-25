package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@Service
public class MenuGroupService {
	private final MenuGroupDao menuGroupDao;

	public MenuGroupService(final MenuGroupDao menuGroupDao) {
		this.menuGroupDao = menuGroupDao;
	}

	@Transactional
	public MenuGroupResponse create(final MenuGroupRequest request) {
		return MenuGroupResponse.from(menuGroupDao.save(request.toMenuGroup()));
	}

	public List<MenuGroupResponse> list() {
		return menuGroupDao.findAll()
			.stream()
			.map(MenuGroupResponse::from)
			.collect(Collectors.toList());
	}
}
