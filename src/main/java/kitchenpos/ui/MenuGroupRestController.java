package kitchenpos.ui;

import kitchenpos.application.command.MenuGroupService;
import kitchenpos.application.query.MenuGroupQueryService;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;
    private final MenuGroupQueryService menuGroupQueryService;

    public MenuGroupRestController(MenuGroupService menuGroupService, MenuGroupQueryService menuGroupQueryService) {
        this.menuGroupService = menuGroupService;
        this.menuGroupQueryService = menuGroupQueryService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupViewResponse> create(@RequestBody final MenuGroupCreateRequest menuGroupCreateRequest) {
        final Long id = menuGroupService.create(menuGroupCreateRequest.toCreate());

        return ResponseEntity.created(URI.create("/api/menu-groups/" + id))
                .body(menuGroupQueryService.findById(id));
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupViewResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupQueryService.list());
    }
}
