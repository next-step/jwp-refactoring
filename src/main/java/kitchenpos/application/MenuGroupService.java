package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.global.exception.EntityNotFoundException;
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
        final MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroup.builder()
                .name(request.getName())
                .build());

        return MenuGroupMapper.toMenuGroupResponse(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public MenuGroup findMenuGroup(Long id) {
        return menuGroupDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("menu not found. find menu id is %d", id)));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        final List<MenuGroup> menuGroups = menuGroupDao.findAll();

        return MenuGroupMapper.toMenuGroupResponses(menuGroups);
    }
}
