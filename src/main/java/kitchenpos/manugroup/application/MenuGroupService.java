package kitchenpos.manugroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.manugroup.domain.MenuGroup;
import kitchenpos.manugroup.domain.MenuGroupRepository;
import kitchenpos.manugroup.dto.MenuGroupRequest;
import kitchenpos.manugroup.dto.MenuGroupResponse;

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
