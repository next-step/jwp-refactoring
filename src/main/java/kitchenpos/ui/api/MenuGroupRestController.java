package kitchenpos.ui.api;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.menu.MenuGroupRequest;
import kitchenpos.ui.dto.menu.MenuGroupResponse;
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

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
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

    @PostMapping("/api/menu-groups2")
    public ResponseEntity<MenuGroupResponse> create2(@RequestBody final MenuGroupRequest request) {
        final MenuGroup created = menuGroupService.create(request.toMenuGroup());
        final URI uri = URI.create("/api/menu-groups2/" + created.getId());
        return ResponseEntity.created(uri)
                .body(MenuGroupResponse.of(created))
                ;
    }

    @GetMapping("/api/menu-groups2")
    public ResponseEntity<List<MenuGroupResponse>> list2() {
        return ResponseEntity.ok()
                .body(MenuGroupResponse.ofList(menuGroupService.list()))
                ;
    }
}
