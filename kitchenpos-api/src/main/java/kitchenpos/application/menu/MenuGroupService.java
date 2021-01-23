package kitchenpos.application.menu;


import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Transactional
	public MenuGroupResponse createMenuGroup(final MenuGroupRequest request) {
		MenuGroup menuGroup = menuGroupRepository.save(request.toEntity());
		return MenuGroupResponse.of(menuGroup);
	}

	public List<MenuGroupResponse> listMenuGroups() {
		List<MenuGroup> menuGroups = menuGroupRepository.findAll();
		return MenuGroupResponse.of(menuGroups);
	}
}
