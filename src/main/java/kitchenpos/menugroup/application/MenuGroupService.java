package kitchenpos.menugroup.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@Transactional
@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        final MenuGroup menuGroup = new MenuGroup(menuGroupRequest.getName());
        final MenuGroup saved = menuGroupRepository.save(menuGroup);

        return MenuGroupResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
            .stream()
            .map(MenuGroupResponse::of)
            .collect(Collectors.toList());
    }
}
