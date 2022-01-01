package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequestMapping(value = "/api/menu-groups")
@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest menuGroupRequest) {
        final MenuGroupResponse menuGroup = menuGroupService.create(menuGroupRequest);
        return ResponseEntity.created(URI.create("/api/menu-groups/" + menuGroup.getId())).body(menuGroup);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok().body(menuGroupService.list());
    }
}
