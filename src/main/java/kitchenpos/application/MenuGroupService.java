package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.mapper.MenuGroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        return MenuGroupMapper.toMenuGroupResponse(menuGroupDao.save(MenuGroup.builder()
                .name(request.getName())
                .build()));
    }

    @Transactional(readOnly = true)
    public MenuGroup findMenuGroup(Long id) {
        return menuGroupDao.findById(id)
                .orElseThrow(IllegalAccessError::new);
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupMapper.toMenuGroupResponses(menuGroupDao.findAll());
    }
}
