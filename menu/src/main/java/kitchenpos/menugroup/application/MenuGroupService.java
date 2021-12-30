package kitchenpos.menugroup.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menugroup.domain.domain.MenuGroup;
import kitchenpos.menugroup.domain.repo.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupAddRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@Service
public class MenuGroupService {

	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Transactional
	public MenuGroupResponse create(final MenuGroupAddRequest request) {
		final MenuGroup menuGroup = menuGroupRepository.save(request.toEntity());
		return MenuGroupResponse.of(menuGroup);
	}

	@Transactional(readOnly = true)
	public List<MenuGroupResponse> list() {
		final List<MenuGroup> menuGroups = menuGroupRepository.findAll();
		return menuGroups.stream()
			.map(MenuGroupResponse::of)
			.collect(Collectors.toList());
	}
}
