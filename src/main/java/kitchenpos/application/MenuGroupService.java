package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
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
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        return MenuGroupResponse.of(menuGroupDao.save(menuGroupRequest.toMenuGroup()));
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.toList(menuGroupDao.findAll());
    }
}
