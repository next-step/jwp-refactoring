package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(MenuGroupRequest menuGroupRequest) {
        return new MenuGroupResponse(create(menuGroupRequest.toMenuGroup()));
    }

    @Transactional
    public MenuGroup create(MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.of(menuGroupRepository.findAll());
    }

    public List<MenuGroup> findAll() {
        return menuGroupRepository.findAll();
    }

	public MenuGroup findById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
            .orElseThrow(EntityNotFoundException::new);
	}
}
