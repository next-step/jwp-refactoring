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
    private final MenuGroupDao menuGroupDao;
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupDao menuGroupDao, MenuGroupRepository menuGroupRepository) {
        this.menuGroupDao = menuGroupDao;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup createTemp(MenuGroup menuGroup) {
        MenuGroupEntity savedMenuGroup = menuGroupRepository.save(new MenuGroupEntity(menuGroup.getName()));
        return new MenuGroup(savedMenuGroup.getId(), savedMenuGroup.getName());
    }

    @Transactional
    public MenuGroup create(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> listTemp() {
        List<MenuGroupEntity> menuGroupEntities = menuGroupRepository.findAll();;
        return menuGroupEntities.stream().map(menuGroupEntity -> MenuGroup.of(menuGroupEntity)).collect(Collectors.toList());
    }
}
