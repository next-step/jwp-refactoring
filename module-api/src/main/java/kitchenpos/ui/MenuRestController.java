package kitchenpos.ui;

import kitchenpos.application.MenuQueryService;
import kitchenpos.application.command.MenuService;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuViewResponse;
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
    private final MenuQueryService menuQueryService;

    public MenuRestController(MenuService menuService, MenuQueryService menuQueryService) {
        this.menuService = menuService;
        this.menuQueryService = menuQueryService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuViewResponse> create(@RequestBody final MenuCreateRequest menuCreateRequest) throws RuntimeException {
        final Long id = menuService.create(menuCreateRequest.toCreate());

        return ResponseEntity.created(URI.create("/api/menus/" + id))
                .body(menuQueryService.findById(id));
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuViewResponse>> list() {
        return ResponseEntity.ok()
                .body(menuQueryService.list());
    }
}
