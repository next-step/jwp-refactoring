package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.infrastructure.MenuGroupRepository;
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
        MenuGroup menuGroup = MenuGroup.from(menuGroupRequest.getName());
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.of(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}
