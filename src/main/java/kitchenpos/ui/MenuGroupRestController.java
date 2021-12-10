package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.menu.aplication.NewMenuGroupService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
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
    private final NewMenuGroupService newMenuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService, NewMenuGroupService newMenuGroupService) {
        this.menuGroupService = menuGroupService;
        this.newMenuGroupService = newMenuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroup> create(@RequestBody final MenuGroup menuGroup) {
        final MenuGroup created = menuGroupService.create(menuGroup);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroup>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list())
                ;
    }

    @PostMapping("/new/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> saveMenuGroup(@RequestBody final MenuGroupRequest menuGroupRequest) {
        final MenuGroupResponse created = newMenuGroupService.create(menuGroupRequest);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping("/new/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> findAllMenuGroup() {
        return ResponseEntity.ok()
                .body(newMenuGroupService.findAllMenuGroup());
    }
}
