package kitchenpos.menugroup.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@RequestMapping("/api/menu-groups")
@RestController
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
			.body(MenuGroupResponse.from(created));
	}

	@GetMapping
	public ResponseEntity<List<MenuGroupResponse>> list() {
		return ResponseEntity.ok()
			.body(MenuGroupResponse.ofList(menuGroupService.list()));
	}
}
