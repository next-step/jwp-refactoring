package kichenpos.menu.ui;

import kichenpos.menu.application.MenuService;
import kichenpos.menu.ui.dto.MenuCreateRequest;
import kichenpos.menu.ui.dto.MenuCreateResponse;
import kichenpos.menu.ui.dto.MenuResponse;
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
    public ResponseEntity<MenuCreateResponse> create(@RequestBody final MenuCreateRequest request) {
        final MenuCreateResponse created = new MenuCreateResponse(menuService.create(request));
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list()
                        .stream()
                        .map(MenuResponse::new)
                        .collect(Collectors.toList())
                )
                ;
    }
}
