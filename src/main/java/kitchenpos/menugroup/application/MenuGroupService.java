package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
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
    public MenuGroupResponse create(MenuGroupRequest menuGroupRequest) {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(menuGroupRequest.getName()));
        return MenuGroupResponse.of(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroupEntities = menuGroupRepository.findAll();;
        return menuGroupEntities.stream().map(menuGroupEntity -> MenuGroupResponse.of(menuGroupEntity)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuGroup findById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."));
    }
}
