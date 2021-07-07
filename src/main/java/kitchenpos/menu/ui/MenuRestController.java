package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
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
    public ResponseEntity create(@RequestBody MenuRequest menuRequest) {
        final MenuResponse menuResponse = menuService.create(menuRequest);
        final URI uri = URI.create("/api/menus/" + menuResponse.getId());
        return ResponseEntity.created(uri)
                .body(menuResponse);
    }

    @GetMapping
    public ResponseEntity list() {
        return ResponseEntity.ok()
                .body(menuService.list())
                ;
    }
}
