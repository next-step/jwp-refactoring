package kitchenpos.menu.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@Service
@Transactional
public class MenuGroupService {
	private final MenuGroupDao menuGroupDao;

	public MenuGroupService(final MenuGroupDao menuGroupDao) {
		this.menuGroupDao = menuGroupDao;
	}

	public MenuGroupResponse create(final MenuGroupRequest request) {
		return MenuGroupResponse.from(menuGroupDao.save(request.toMenuGroup()));
	}

	@Transactional(readOnly = true)
	public List<MenuGroupResponse> list() {
		return MenuGroupResponse.newList(menuGroupDao.findAll());
	}

	public MenuGroup findById(final Long id) {
		return menuGroupDao.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 MenuGroup이 존재하지 않습니다."));
	}
}
