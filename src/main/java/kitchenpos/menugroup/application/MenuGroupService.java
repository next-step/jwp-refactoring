package kitchenpos.menugroup.application;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menugroup.dto.MenuGroupsResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(menuGroupRequest.getName()));
        return menuGroup.toResponse();
    }

    public MenuGroupsResponse list() {
        return MenuGroupsResponse.of(menuGroupDao.findAll().stream()
                .map(MenuGroup::toResponse)
                .collect(Collectors.toList()));
    }
}
