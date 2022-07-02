package kitchenpos.menu.ui;

import kitchenpos.exception.KitchenPosArgumentException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest menu) {
        final MenuResponse created = menuService.create(menu);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }

    @ExceptionHandler(KitchenPosArgumentException.class)
    public ResponseEntity handleKitchenPosArgException(KitchenPosArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
