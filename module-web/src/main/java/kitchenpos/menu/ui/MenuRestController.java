package kitchenpos.menu.ui;

import java.net.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import kitchenpos.menu.application.*;
import kitchenpos.menu.dto.*;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest menuRequest) {
        final MenuResponse saveMenu = menuService.save(menuRequest);
        final URI uri = URI.create("/api/menus/" + saveMenu.getId());
        return ResponseEntity.created(uri)
                .body(saveMenu);
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.findAll());
    }
}
