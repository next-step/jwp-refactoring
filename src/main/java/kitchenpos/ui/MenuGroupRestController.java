package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/menu-groups")
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> createMenuGroup(@RequestBody final MenuGroupCreateRequest request) {
        final MenuGroupResponse response = menuGroupService.createMenuGroup(request);
        final URI uri = URI.create("/api/menu-groups/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> findAllMenuGroups() {
        return ResponseEntity.ok().body(menuGroupService.findAllMenuGroups());
    }
}
