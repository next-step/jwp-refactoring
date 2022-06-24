package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroupEntity;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.request.MenuGroupRequest;
import kitchenpos.menu.domain.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroupEntity menuGroup = MenuGroupEntity.of(menuGroupRequest.getName());
        menuGroup = menuGroupRepository.save(menuGroup);
        return MenuGroupResponse.of(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        List<MenuGroupEntity> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
            .map(MenuGroupResponse::of)
            .collect(Collectors.toList());
    }
}
