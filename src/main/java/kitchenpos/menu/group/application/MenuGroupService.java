package kitchenpos.menu.group.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.menu.group.domain.MenuGroup;
import kitchenpos.menu.group.domain.MenuGroupRepository;
import kitchenpos.menu.group.dto.MenuGroupRequest;
import kitchenpos.menu.group.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        return MenuGroupResponse.of(menuGroupRepository.save(menuGroupRequest.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.ofList(menuGroupRepository.findAll());
    }
}
