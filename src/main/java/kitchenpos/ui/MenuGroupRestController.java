package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupCreate;
import kitchenpos.dto.response.MenuGroupViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupViewResponse> create(@RequestBody final MenuGroup menuGroup) {
        final MenuGroup created = menuGroupService.create(new MenuGroupCreate(menuGroup.getName()));
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(MenuGroupViewResponse.of(created))
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupViewResponse>> list() {
        List<MenuGroupViewResponse> results = menuGroupService.list()
                .stream()
                .map(MenuGroupViewResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(results);
    }
}
