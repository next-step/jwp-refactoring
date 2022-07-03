package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroupRequest.toEntity(request));

        return MenuGroupResponse.of(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupResponse.of(menuGroups);
    }
}
