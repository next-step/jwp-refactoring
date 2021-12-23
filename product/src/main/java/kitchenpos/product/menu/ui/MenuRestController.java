package kitchenpos.product.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.product.menu.application.MenuService;
import kitchenpos.product.menu.ui.request.MenuRequest;
import kitchenpos.product.menu.ui.response.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> create(@RequestBody MenuRequest request) {
        MenuResponse created = menuService.create(request);
        return ResponseEntity.created(uri(created))
            .body(created);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> list(
        @RequestParam(value = "ids", required = false) List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return ResponseEntity.ok()
                .body(menuService.list());
        }
        return ResponseEntity.ok()
            .body(menuService.listByIds(ids));
    }

    private URI uri(MenuResponse created) {
        return URI.create("/api/menus/" + created.getId());
    }
}
