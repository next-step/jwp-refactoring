package kitchenpos.product.group.application;

import java.util.List;
import kitchenpos.product.group.domain.MenuGroupCommandService;
import kitchenpos.product.group.domain.MenuGroupQueryService;
import kitchenpos.product.group.ui.request.MenuGroupRequest;
import kitchenpos.product.group.ui.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupCommandService commandService;
    private final MenuGroupQueryService queryService;

    public MenuGroupService(MenuGroupCommandService commandService,
        MenuGroupQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Transactional
    public MenuGroupResponse create(MenuGroupRequest request) {
        return MenuGroupResponse.from(commandService.save(request.toEntity()));
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.listFrom(queryService.findAll());
    }
}
