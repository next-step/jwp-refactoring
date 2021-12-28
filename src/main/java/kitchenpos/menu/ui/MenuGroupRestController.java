package kitchenpos.menu.ui;

import static kitchenpos.utils.ResponseUtils.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@RestController
@RequestMapping(MenuGroupRestController.BASE_URL)
public class MenuGroupRestController {

	public static final String BASE_URL = "/api/menu-groups";

	private final MenuGroupService menuGroupService;

	public MenuGroupRestController(final MenuGroupService menuGroupService) {
		this.menuGroupService = menuGroupService;
	}

	@PostMapping
	public ResponseEntity<MenuGroupResponse> create(@RequestBody final MenuGroupRequest request) {
		final MenuGroupResponse menuGroup = menuGroupService.create(request);
		return ResponseEntity.created(createdUrl(BASE_URL, menuGroup.getId()))
			.body(menuGroup)
			;
	}

	@GetMapping
	public ResponseEntity<List<MenuGroupResponse>> getList() {
		return ResponseEntity.ok()
			.body(menuGroupService.getList())
			;
	}
}
