package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.repository.MenuGroupRepository;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroup menuGroup) {
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        return new MenuGroupResponse(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return menuGroups.stream()
            .map(MenuGroupResponse::new)
            .collect(Collectors.toList());
    }
}
