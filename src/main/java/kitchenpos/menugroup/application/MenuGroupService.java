package kitchenpos.menugroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Transactional
	public MenuGroupResponse create(final MenuGroupRequest request) {
		MenuGroup savedMenuGroup = menuGroupRepository.save(request.toEntity());
		return MenuGroupResponse.of(savedMenuGroup);
	}

	public List<MenuGroupResponse> list() {
		return menuGroupRepository.findAll().stream()
			  .map(MenuGroupResponse::of)
			  .collect(Collectors.toList());
	}

	public boolean existsById(Long menuGroupId) {
		return menuGroupRepository.existsById(menuGroupId);
	}
}
