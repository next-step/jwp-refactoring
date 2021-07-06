package kitchenpos.menugroup.application;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menugroup.dto.MenuGroupListResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(menuGroupRequest.getName()));
        return menuGroup.toResponse();
    }

    @Transactional(readOnly = true)
    public MenuGroupListResponse list() {
        return MenuGroupListResponse.of(menuGroupDao.findAll().stream()
                .map(MenuGroup::toResponse)
                .collect(Collectors.toList()));
    }
}
