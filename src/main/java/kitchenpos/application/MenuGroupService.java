package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
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

    public MenuGroupResponse create(MenuGroupRequest request) {
        MenuGroup menuGroup = menuGroupRepository.save(toEntity(request));
        return ofEntity(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
                .map(this::ofEntity)
                .collect(Collectors.toList());
    }

    private MenuGroup toEntity(MenuGroupRequest request) {
        return MenuGroup.builder()
                .name(request.getName())
                .build();
    }

    private MenuGroupResponse ofEntity(MenuGroup menuGroup) {
        return MenuGroupResponse.builder()
                .id(menuGroup.getId())
                .name(menuGroup.getName())
                .build();
    }
}
