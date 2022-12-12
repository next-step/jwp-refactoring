package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/menu-groups")
@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupRequest request) {
        MenuGroupResponse response = menuGroupService.create(request);
        URI uri = URI.create("/api/menu-groups/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok().body(menuGroupService.list());
    }
}
