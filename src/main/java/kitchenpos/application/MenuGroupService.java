package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroupResponse create(final MenuGroup entity) {
        MenuGroup menuGroup = menuGroupDao.save(entity);
        return MenuGroupResponse.of(menuGroup.getId(), menuGroup.getName());
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll().stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}
