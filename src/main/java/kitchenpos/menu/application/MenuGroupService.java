package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.persistence.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupDao;

    public MenuGroupService(final MenuGroupRepository menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        return MenuGroupResponse.of(menuGroupDao.save(menuGroupRequest.toMenuGroup()));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll()
                .stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}
