package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.IllegalMenuPriceException;
import kitchenpos.menu.exception.NoMenuGroupException;
import kitchenpos.menuproduct.exception.NoMenuProductException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
                .body(menuService.list());
    }

    @ExceptionHandler({
            NoMenuGroupException.class, IllegalMenuPriceException.class,
            NoMenuProductException.class
    })
    public ResponseEntity handleException() {
        return ResponseEntity.badRequest().build();
    }
}
