package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/menu-groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping
    public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest menuGroupRequest) {
        final MenuGroup created = menuGroupService.create(menuGroupRequest);
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(MenuGroupResponse.of(created));
    }

    @GetMapping
    public ResponseEntity<List<MenuGroupResponse>> list() {
        List<MenuGroupResponse> body = menuGroupService.list().stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(body);
    }
}
