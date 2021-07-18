package kitchenpos.menugroup.ui;

import java.net.URI;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@RestController
@RequestMapping("/api/menu-groups")
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest menuGroupRequest) {
        final MenuGroupResponse menuGroup = menuGroupService.create(menuGroupRequest);
        final URI uri = URI.create("/api/menu-groups/" + menuGroup.getId());

        return ResponseEntity.created(uri)
            .body(menuGroup);
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> list() {
        final List<MenuGroupResponse> menuGroups = menuGroupService.list();

        return ResponseEntity.ok()
            .body(menuGroups);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, IllegalArgumentException.class})
    public ResponseEntity<Void> handleIllegalArgsException(final RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
