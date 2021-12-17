package kitchenpos.domain.menu.ui;

import kitchenpos.domain.menu.application.MenuService;
import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.menu.dto.MenuExistRequest;
import kitchenpos.domain.menu.dto.MenuRequest;
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
    public ResponseEntity<Menu> create(@RequestBody final MenuRequest request) {
        final Menu created = menuService.create(request);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<Menu>> list() {
        return ResponseEntity.ok()
                .body(menuService.list())
                ;
    }

    @GetMapping("/api/menus/check")
    public ResponseEntity validMenuExist(@RequestBody final MenuExistRequest request) {
        menuService.validateMenuExist(request);
        return ResponseEntity.ok().build();
    }
}
