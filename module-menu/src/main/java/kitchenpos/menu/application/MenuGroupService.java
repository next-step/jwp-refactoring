package kitchenpos.menu.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@Service
public class MenuGroupService {
	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Transactional
	public MenuGroupResponse create(final MenuGroupRequest menuGroup) {
		MenuGroup saved = menuGroupRepository.save(menuGroup.toMenuGroup());
		return MenuGroupResponse.of(saved);
	}

	public List<MenuGroupResponse> list() {
		List<MenuGroup> menuGroups = menuGroupRepository.findAll();
		return MenuGroupResponse.of(menuGroups);
	}
}
