package kitchenpos.application;

import kitchenpos.domain.MenuGroupEntity;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
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
    public MenuGroupResponse create(MenuGroupRequest request) {
        MenuGroupEntity persistedMenuGroup = menuGroupRepository.save(new MenuGroupEntity(request.getName()));

        return MenuGroupResponse.of(persistedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(menuGroup -> MenuGroupResponse.of(menuGroup))
                .collect(Collectors.toList());
    }
}
