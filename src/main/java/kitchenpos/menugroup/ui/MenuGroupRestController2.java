package kitchenpos.menugroup.ui;

import kitchenpos.menugroup.application.MenuGroupService2;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class MenuGroupRestController2 {
    private final MenuGroupService2 menuGroupService;

    public MenuGroupRestController2(final MenuGroupService2 menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest menuGroup) {
        final MenuGroupResponse created = menuGroupService.create(menuGroup);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> findAllMenuGroups() {
        return ResponseEntity.ok()
                .body(menuGroupService.findAllMenuGroups())
                ;
    }
}
