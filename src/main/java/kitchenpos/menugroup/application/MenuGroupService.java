package kitchenpos.menugroup.application;

import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupDao.save(menuGroupRequest.toMenuGroup());
        return MenuGroupResponse.from(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
