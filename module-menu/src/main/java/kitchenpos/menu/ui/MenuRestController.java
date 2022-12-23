package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.application.MenuMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;

@RestController
public class MenuRestController {
	private final MenuService menuService;
	private final MenuMapper menuMapper;

	public MenuRestController(MenuService menuService, MenuMapper menuMapper) {
		this.menuService = menuService;
		this.menuMapper = menuMapper;
	}

	@PostMapping("/api/menus")
	public ResponseEntity<MenuResponse> create(@RequestBody MenuRequest menuRequest) {
		Menu createdMenu = menuService.create(menuMapper.toMenu(menuRequest));

		return ResponseEntity
			.created(createUri(createdMenu))
			.body(new MenuResponse(createdMenu));
	}

	@GetMapping("/api/menus")
	public ResponseEntity<List<MenuResponse>> list() {
		return ResponseEntity
			.ok()
			.body(MenuResponse.of(menuService.findAll()));
	}

	private URI createUri(Menu createdMenu) {
		return URI.create("/api/menus/" + createdMenu.getId());
	}
}
