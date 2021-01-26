package api.menu.application;

import api.menu.dto.MenuGroupRequest;
import api.menu.dto.MenuGroupResponse;
import domain.menu.MenuGroup;
import domain.menu.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Transactional
	public MenuGroupResponse create(MenuGroupRequest request) {
		MenuGroup menuGroup = new MenuGroup(request.getName());
		return MenuGroupResponse.of(menuGroupRepository.save(menuGroup));
	}

	public List<MenuGroupResponse> list() {
		return menuGroupRepository.findAll().stream()
				.map(MenuGroupResponse::of)
				.collect(Collectors.toList());
	}
}
