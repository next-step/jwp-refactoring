package kitchenpos.application;

import kitchenpos.domain.menuGroup.MenuGroup;
import kitchenpos.domain.menuGroup.MenuGroupRepository;
import kitchenpos.dto.menuGroup.MenuGroupRequest;
import kitchenpos.dto.menuGroup.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = MenuGroup.of(menuGroupRequest);
        return MenuGroupResponse.of(menuGroupRepository.save(menuGroup));
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}
