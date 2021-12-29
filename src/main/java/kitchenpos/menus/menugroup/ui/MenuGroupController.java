package kitchenpos.menus.menugroup.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menus.menugroup.application.MenuGroupService;
import kitchenpos.menus.menugroup.dto.MenuGroupRequest;
import kitchenpos.menus.menugroup.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupController {

    private static final String BASE_PATH = "/api/menu-groups/";

    private final MenuGroupService menuGroupService;

    public MenuGroupController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping(BASE_PATH)
    public ResponseEntity<MenuGroupResponse> register(@RequestBody final MenuGroupRequest request) {
        final MenuGroupResponse response = menuGroupService.register(request);
        final URI uri = URI.create(BASE_PATH + response.getId());
        return ResponseEntity.created(uri)
            .body(response);
    }

    @GetMapping(BASE_PATH)
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
            .body(menuGroupService.list());
    }
}
