package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.repository.MenuGroupRepository;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	@Transactional
	public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
		MenuGroup saved = menuGroupRepository.save(menuGroupRequest.toEntity());
		return MenuGroupResponse.from(saved);
	}

	public List<MenuGroupResponse> list() {
		return menuGroupRepository.findAll()
			.stream()
			.map(MenuGroupResponse::from)
			.collect(Collectors.toList());
	}

	public MenuGroup findMenuGroupById(Long id) {
		return menuGroupRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(ErrorMessage.notFoundEntity("메뉴그룹", id)));
	}
}
