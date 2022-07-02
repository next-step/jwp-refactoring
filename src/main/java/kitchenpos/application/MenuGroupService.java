package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupDao menuGroupDao, MenuGroupRepository menuGroupRepository) {
        this.menuGroupDao = menuGroupDao;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest request) {
        MenuGroup menuGroup = MenuGroupRequest.toEntity(request);
        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }
}
