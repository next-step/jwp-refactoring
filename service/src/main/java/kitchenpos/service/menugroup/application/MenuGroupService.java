package kitchenpos.service.menugroup.application;

import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.service.menugroup.dto.MenuGroupRequest;
import kitchenpos.service.menugroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupDao) {
        this.menuGroupRepository = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(MenuGroupRequest request) {
        return new MenuGroupResponse(menuGroupRepository.save(request.toMenuGroup()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream().map(MenuGroupResponse::new).collect(Collectors.toList());
    }
}
