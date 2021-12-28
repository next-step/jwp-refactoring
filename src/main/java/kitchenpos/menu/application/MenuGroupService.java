package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@Service
@Transactional
public class MenuGroupService {

	private final MenuGroupRepository menuGroupRepository;

	public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
		this.menuGroupRepository = menuGroupRepository;
	}

	public MenuGroupResponse create(final MenuGroupRequest request) {
		MenuGroup menuGroup = menuGroupRepository.save(request.toEntity());
		return new MenuGroupResponse(menuGroup);
	}

	@Transactional(readOnly = true)
	public List<MenuGroupResponse> getList() {
		return menuGroupRepository.findAll().stream().map(MenuGroupResponse::new)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public MenuGroup getById(Long menuGroupId) {
		return menuGroupRepository.findById(menuGroupId)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "메뉴 그룹(id: {})를 찾을 수 없습니다", menuGroupId));
	}
}
