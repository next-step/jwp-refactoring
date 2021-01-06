package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Transactional
	public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
		MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.create(menuGroupRequest.getName()));
		return MenuGroupResponse.of(menuGroup);
	}

	public List<MenuGroupResponse> list() {
		List<MenuGroup> menuGroups = menuGroupRepository.findAll();
		return menuGroups.stream()
			.map(MenuGroupResponse::of)
			.collect(Collectors.toList());
	}
}
