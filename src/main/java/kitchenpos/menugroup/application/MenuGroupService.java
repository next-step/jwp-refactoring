package kitchenpos.menugroup.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
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
	public MenuGroupResponse create(final MenuGroupRequest request) {
		MenuGroup menuGroup = menuGroupRepository.save(request.toEntity());
		return MenuGroupResponse.of(menuGroup);
	}

	public List<MenuGroupResponse> list() {
		List<MenuGroup> menuGroups = menuGroupRepository.findAll();
		return MenuGroupResponse.of(menuGroups);
	}
}
