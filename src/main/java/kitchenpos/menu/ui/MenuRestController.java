package kitchenpos.menu.ui;

import io.swagger.annotations.ApiOperation;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
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

    @ApiOperation("메뉴 생성")
    @PostMapping("/api/menus")
    public ResponseEntity<Menu> create(@RequestBody final Menu menu) {
        final Menu created = menuService.create(menu);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @ApiOperation("전체 메뉴 조회")
    @GetMapping("/api/menus")
    public ResponseEntity<List<Menu>> list() {
        return ResponseEntity.ok().body(menuService.list());
    }
}
