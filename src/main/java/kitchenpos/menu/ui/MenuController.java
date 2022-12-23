package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
public class MenuController {
    private final MenuService menuService;

    public MenuController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest menuRequest) {
        final MenuResponse created = menuService.create(menuRequest);
        return ResponseEntity.created(URI.create("/api/menus/" + created.getId()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list());
    }
}
