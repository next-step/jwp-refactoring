package kitchenpos.menugroup.ui;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/menu-groups")
@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupRequest menuGroupRequest) {
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);
        return ResponseEntity
                .created(URI.create("/api/menu-groups/" + menuGroupResponse.getId()))
                .body(menuGroupResponse);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok().body(menuGroupService.list());
    }
}
