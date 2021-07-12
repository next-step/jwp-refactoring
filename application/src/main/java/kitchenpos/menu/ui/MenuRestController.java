package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.dto.CreateMenuRequest;
import kitchenpos.menu.dto.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody CreateMenuRequest menuRequest) {
        MenuDto created = menuService.create(menuRequest.toDomainDto());
        URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri).body(MenuResponse.of(created));
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok().body(menuService.list()
                                                   .stream()
                                                   .map(MenuResponse::of)
                                                   .collect(toList()));
    }
}
