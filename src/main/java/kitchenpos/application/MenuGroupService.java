package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupRequest.toEntity();
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
