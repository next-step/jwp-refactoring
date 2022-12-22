package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.ui.dto.MenuGroupRequest;
import kitchenpos.menu.ui.dto.MenuGroupResponse;

@RestController
public class MenuGroupRestController {
	private final MenuGroupService menuGroupService;

	public MenuGroupRestController(MenuGroupService menuGroupService) {
		this.menuGroupService = menuGroupService;
	}

	@PostMapping("/api/menu-groups")
	public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupRequest menuGroupRequest) {
		MenuGroup created = menuGroupService.create(menuGroupRequest.toMenuGroup());
		return ResponseEntity
			.created(createUri(created))
			.body(new MenuGroupResponse(created));
	}

	@GetMapping("/api/menu-groups")
	public ResponseEntity<List<MenuGroupResponse>> list() {
		return ResponseEntity.ok()
							 .body(
								 MenuGroupResponse.of(
									 menuGroupService.findAll()));
	}

	private URI createUri(MenuGroup created) {
		return URI.create("/api/menu-groups/" + created.getId());
	}
}
