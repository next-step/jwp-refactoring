package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
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
	public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
		return MenuGroupResponse.of(menuGroupRepository.save(menuGroupRequest.toMenuGroup()));
	}

	public List<MenuGroupResponse> list() {
		return MenuGroupResponse.of(menuGroupRepository.findAll());
	}
}
