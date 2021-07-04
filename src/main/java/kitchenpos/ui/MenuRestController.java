package kitchenpos.ui;

import kitchenpos.application.command.MenuService;
import kitchenpos.application.query.MenuQueryService;
import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuCreate;
import kitchenpos.domain.menuproduct.MenuProductCreate;
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
    private final MenuQueryService menuQueryService;

    public MenuRestController(MenuService menuService, MenuQueryService menuQueryService) {
        this.menuService = menuService;
        this.menuQueryService = menuQueryService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuViewResponse> create(@RequestBody final MenuCreateRequest menuCreateRequest) {
        List<MenuProductCreate> menuProductCreates = menuCreateRequest.getMenuProducts()
                .stream()
                .map(item -> new MenuProductCreate(item.getMenuId(), item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());

        MenuCreate menuCreate = new MenuCreate(menuCreateRequest.getName(),
                new Price(menuCreateRequest.getPrice()),
                menuCreateRequest.getMenuGroupId(),
                menuProductCreates);

        final Menu created = menuService.create(menuCreate);
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(MenuViewResponse.of(created));
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuViewResponse>> list() {
        List<MenuViewResponse> results = menuQueryService.list()
                .stream()
                .map(MenuViewResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(results);
    }
}
