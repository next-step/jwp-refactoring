package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupCreateRequest request) {
        final MenuGroupResponse created = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.listResponse())
                ;
    }

    @GetMapping("/api/menu-groups/{id}")
    public ResponseEntity<MenuGroupResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(menuGroupService.getMenuResponseGroup(id))
                ;
    }
}
