package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.moduledomain.common.exception.NoResultDataException;
import kitchenpos.moduledomain.menu.MenuGroup;
import kitchenpos.moduledomain.menu.MenuGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroup) {
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup.toMenuGroup());
        return MenuGroupResponse.of(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return MenuGroupResponse.ofList(menuGroups);
    }

    public Long findByIdThrow(Long id) {
        MenuGroup menuGroup = menuGroupDao.findById(id)
            .orElseThrow(NoResultDataException::new);
        return menuGroup.getId();
    }
}
