package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupDao menuGroupDao, MenuGroupRepository menuGroupRepository) {
        this.menuGroupDao = menuGroupDao;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }

    //TODO re
    @Transactional
    public MenuGroup create_re(final MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    //TODO re
    public List<MenuGroup> list_re() {
        return menuGroupRepository.findAll();
    }
}
