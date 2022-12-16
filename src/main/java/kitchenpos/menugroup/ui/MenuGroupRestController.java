package kitchenpos.menugroup.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest menuGroupRequest) {
        final MenuGroup created = menuGroupService.create(menuGroupRequest);
        MenuGroupResponse menuGroupResponse = new MenuGroupResponse(created.getId(), created.getName());

        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(menuGroupResponse)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        List<MenuGroup> menuGroups = menuGroupService.list();
        List<MenuGroupResponse> menuGroupResponses = menuGroups.stream()
                .map(menuGroup -> new MenuGroupResponse(menuGroup.getId(), menuGroup.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(menuGroupResponses)
                ;
    }
}
