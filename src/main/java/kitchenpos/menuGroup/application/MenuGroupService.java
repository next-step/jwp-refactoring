package kitchenpos.menuGroup.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroupRequest;
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
    public MenuGroupRequest create(final MenuGroupRequest menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroupRequest> list() {
        return menuGroupDao.findAll();
    }
}
