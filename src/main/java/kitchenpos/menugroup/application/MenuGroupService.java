package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupEntity;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(MenuGroup menuGroup) {
        MenuGroupEntity savedMenuGroup = menuGroupRepository.save(new MenuGroupEntity(menuGroup.getName()));
        return new MenuGroup(savedMenuGroup.getId(), savedMenuGroup.getName());
    }

    @Transactional
    public MenuGroupResponse createTemp(MenuGroupRequest menuGroupRequest) {
        MenuGroupEntity savedMenuGroup = menuGroupRepository.save(new MenuGroupEntity(menuGroupRequest.getName()));
        return MenuGroupResponse.of(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        List<MenuGroupEntity> menuGroupEntities = menuGroupRepository.findAll();;
        return menuGroupEntities.stream().map(menuGroupEntity -> MenuGroupResponse.of(menuGroupEntity)).collect(Collectors.toList());
    }
}
