package kitchenpos.menugroup.ui;

import io.swagger.annotations.ApiOperation;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @ApiOperation("메뉴 그룹 생성")
    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroup> create(@RequestBody final MenuGroup menuGroup) {
        final MenuGroup created = menuGroupService.create(menuGroup);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @ApiOperation("메뉴 그룹 조회")
    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroup>> list() {
        return ResponseEntity.ok().body(menuGroupService.list());
    }
}
