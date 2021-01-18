package kitchenpos.menugroup.application;

import kitchenpos.dao.MenuGroupDao;
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
        MenuGroup menuGroup = new MenuGroup(menuGroupRequest.getId(), menuGroupRequest.getName());
        return MenuGroupResponse.of(menuGroupDao.save(menuGroup));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll().stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}
