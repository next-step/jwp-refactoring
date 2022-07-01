package kitchenpos.menugroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.request.MenuGroupRequest;
import kitchenpos.menugroup.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = MenuGroup.of(menuGroupRequest.getName());
        menuGroup = menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.of(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
            .map(MenuGroupResponse::of)
            .collect(Collectors.toList());
    }
}
