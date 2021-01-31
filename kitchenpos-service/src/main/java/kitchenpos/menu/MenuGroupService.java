package kitchenpos.menu;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@Service
public class MenuGroupService {
	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Transactional
	public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
		MenuGroup menuGroup = menuGroupRepository.save(menuGroupRequest.toMenuGroup());

		return MenuGroupResponse.of(menuGroup);
	}

	@Transactional(readOnly = true)
	public List<MenuGroupResponse> list() {
		return MenuGroupResponse.of(menuGroupRepository.findAll());
	}

	public MenuGroup findMenuGroupById(Long id) {
		return menuGroupRepository.findById(id).orElseThrow(IllegalArgumentException::new);
	}
}
