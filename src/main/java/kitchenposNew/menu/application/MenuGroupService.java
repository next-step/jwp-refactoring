package kitchenposNew.menu.application;

import kitchenposNew.menu.domain.MenuGroup;
import kitchenposNew.menu.domain.MenuGroupRepository;
import kitchenposNew.menu.dto.MenuGroupRequest;
import kitchenposNew.menu.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup persistMenuGroup = menuGroupRepository.save(menuGroupRequest.toMenuGroup());
        return MenuGroupResponse.of(persistMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(menuGroup -> MenuGroupResponse.of(menuGroup))
                .collect(Collectors.toList());
    }
}
