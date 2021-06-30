package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.response.MenuViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuViewResponse> create(@RequestBody final MenuCreateRequest menuCreateRequest) {
        final Menu created = menuService.create(menuCreateRequest.toEntity());
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(MenuViewResponse.of(created));
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuViewResponse>> list() {
        List<MenuViewResponse> results = menuService.list()
                .stream()
                .map(MenuViewResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(results);
    }
}
