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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {

    private static final String BASE_PATH = "/api/menus/";

    private final MenuService menuService;

    public MenuController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping(BASE_PATH)
    public ResponseEntity<MenuResponse> register(@RequestBody final MenuRequest request) {
        final MenuResponse response = menuService.register(request);
        final URI uri = URI.create(BASE_PATH + response.getId());
        return ResponseEntity.created(uri)
            .body(response);
    }

    @GetMapping(BASE_PATH)
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
            .body(menuService.list());
    }
}
