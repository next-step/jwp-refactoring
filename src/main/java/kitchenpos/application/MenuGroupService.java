package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.group.MenuGroup;
import kitchenpos.domain.menu.group.MenuGroupCreate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroup create(final MenuGroupCreate create) {
        return menuGroupDao.save(new MenuGroup(create.getName()));
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
