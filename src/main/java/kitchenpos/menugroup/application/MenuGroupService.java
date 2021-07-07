package kitchenpos.menugroup.application;

import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroup create(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
