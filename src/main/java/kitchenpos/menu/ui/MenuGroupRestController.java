package kitchenpos.menu.ui;

import kitchenpos.menu.aplication.MenuGroupService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
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
    public ResponseEntity<MenuGroupResponse> saveMenuGroup(@RequestBody final MenuGroupRequest menuGroupRequest) {
        final MenuGroupResponse saveMenuGroup = menuGroupService.saveMenuGroup(menuGroupRequest);
        final URI uri = URI.create("/api/menu-groups/" + saveMenuGroup.getId());
        return ResponseEntity.created(uri)
                .body(saveMenuGroup);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> findAllMenuGroup() {
        return ResponseEntity.ok()
                .body(menuGroupService.findAllMenuGroup());
    }
}
