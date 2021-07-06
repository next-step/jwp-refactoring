package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService2;
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
public class MenuRestController2 {
    private final MenuService2 menuService;

    public MenuRestController2(final MenuService2 menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/v2/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest menu) {
        final MenuResponse created = menuService.create(menu);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/v2/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.findAllMenus())
                ;
    }
}
