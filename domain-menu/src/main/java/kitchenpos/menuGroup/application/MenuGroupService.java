package kitchenpos.menuGroup.application;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = MenuGroup.createMenuGroup(request.getName());
        return new MenuGroupResponse(menuGroupRepository.save(menuGroup));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupResponse::new)
                .collect(Collectors.toList());
    }
}
