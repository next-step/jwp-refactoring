package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest request) {
        final MenuResponse created = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }

    @RestController
    public static class MenuGroupRestController {
        private final MenuService.MenuGroupService menuGroupService;

        public MenuGroupRestController(final MenuService.MenuGroupService menuGroupService) {
            this.menuGroupService = menuGroupService;
        }

        @PostMapping("/api/menu-groups")
        public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest request) {
            MenuGroupResponse created = menuGroupService.create(request);
            URI uri = URI.create("/api/menu-groups/" + created.getId());
            return ResponseEntity.created(uri).body(created);
        }

        @GetMapping("/api/menu-groups")
        public ResponseEntity<List<MenuGroupResponse>> list() {
            return ResponseEntity.ok().body(menuGroupService.list());
        }
    }
}
