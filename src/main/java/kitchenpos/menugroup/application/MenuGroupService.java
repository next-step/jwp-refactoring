package kitchenpos.menugroup.application;

import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupEntity;
import kitchenpos.menugroup.domain.MenuGroupRepository;
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

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        List<MenuGroupEntity> menuGroupEntities = menuGroupRepository.findAll();;
        return menuGroupEntities.stream().map(menuGroupEntity -> MenuGroup.of(menuGroupEntity)).collect(Collectors.toList());
    }
}
