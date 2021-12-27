package kitchenpos.menu.application;

import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuRequest;
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
