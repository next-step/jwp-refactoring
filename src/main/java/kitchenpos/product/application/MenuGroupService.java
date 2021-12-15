package kitchenpos.product.application;

import java.util.List;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.product.domain.MenuGroup;
import kitchenpos.product.domain.MenuGroupRepository;
import kitchenpos.product.ui.request.MenuGroupRequest;
import kitchenpos.product.ui.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(MenuGroupRequest request) {
        return MenuGroupResponse.from(menuGroupRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.listFrom(menuGroupRepository.findAll());
    }
}
