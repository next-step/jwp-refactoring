package kitchenpos.menu.ui;

import java.net.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import kitchenpos.menu.application.*;
import kitchenpos.menu.dto.*;

@RestController
@RequestMapping("/api/menu-groups")
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> saveMenuGroup(@RequestBody final MenuGroupRequest menuGroupRequest) {
        final MenuGroupResponse saveMenuGroup = menuGroupService.save(menuGroupRequest);
        final URI uri = URI.create("/api/menu-groups/" + saveMenuGroup.getId());
        return ResponseEntity.created(uri)
                .body(saveMenuGroup);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> findAllMenuGroup() {
        return ResponseEntity.ok()
                .body(menuGroupService.findAll());
    }
}
