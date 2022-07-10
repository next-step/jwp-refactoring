package kitchenpos.ui;

import kitchenpos.menuGroup.application.MenuGroupService;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<MenuGroupResponse> create(@RequestBody @Valid final MenuGroupRequest request) {
        final MenuGroupResponse response = menuGroupService.create(request);
        final URI uri = URI.create("/api/menu-groups/" + response.getMenuGroupId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok(menuGroupService.list());
    }
}
