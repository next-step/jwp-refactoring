package kitchenpos.menu.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(menuGroupRequest.getName()));
        return MenuGroupResponse.of(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }

    public MenuGroup findById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_MENU_GROUP.getMessage()));
    }
}
