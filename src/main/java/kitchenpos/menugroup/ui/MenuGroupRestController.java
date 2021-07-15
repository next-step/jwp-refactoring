package kitchenpos.menugroup.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menugroup.service.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest menuGroup) {
        final MenuGroup created = menuGroupService.create(menuGroup);
        return ResponseEntity.created(URI.create("/api/menu-groups/" + created.getId()))
            .body(MenuGroupResponse.of(created));
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
            .body(menuGroupService.list()
                .stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList()));
    }
}
