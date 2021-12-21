package kitchenpos.menugroup.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupDto;

@RestController
public class MenuGroupRestController {
	private final MenuGroupService menuGroupService;

	public MenuGroupRestController(MenuGroupService menuGroupService) {
		this.menuGroupService = menuGroupService;
	}

	@PostMapping("/api/menu-groups")
	public ResponseEntity<MenuGroupDto> create(@RequestBody MenuGroupCreateRequest request) {
		final MenuGroupDto menuGroup = menuGroupService.create(request);
		final URI uri = URI.create("/api/menu-groups/" + menuGroup.getId());
		return ResponseEntity.created(uri).body(menuGroup);
	}

	@GetMapping("/api/menu-groups")
	public ResponseEntity<List<MenuGroupDto>> list() {
		List<MenuGroupDto> menuGroups = menuGroupService.list();
		return ResponseEntity.ok().body(menuGroups);
	}
}
