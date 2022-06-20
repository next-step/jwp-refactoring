package kitchenpos.application;

import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
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

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream().map(MenuGroupResponse::new).collect(Collectors.toList());
    }
}
