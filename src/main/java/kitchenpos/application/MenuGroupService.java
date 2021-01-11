package kitchenpos.application;

import kitchenpos.domain.menuGroup.MenuGroup;
import kitchenpos.domain.menuGroup.MenuGroupRepository;
import kitchenpos.ui.dto.menuGroup.MenuGroupRequest;
import kitchenpos.ui.dto.menuGroup.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = new MenuGroup(menuGroupRequest.getName());
        MenuGroup saved = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}
