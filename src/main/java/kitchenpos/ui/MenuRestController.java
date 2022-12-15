package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<Menu> create(@RequestBody final Menu menu) {
        final Menu created = menuService.create(menu);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Menu>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }
}
