package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import kitchenpos.dto.menugroup.MenuGroupResponse;
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
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroupRequest.toMenuGroup());
        return MenuGroupResponse.from(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
