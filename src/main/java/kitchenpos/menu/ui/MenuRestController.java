package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.utils.ResponseUtils;

@RequestMapping(MenuRestController.BASE_URL)
@RestController
public class MenuRestController {

	public static final String BASE_URL = "/api/menus";

	private final MenuService menuService;

	public MenuRestController(final MenuService menuService) {
		this.menuService = menuService;
	}

	@PostMapping
	public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest request) {
		final MenuResponse created = menuService.create(request);
		final URI uri = ResponseUtils.createdUrl(BASE_URL, created.getId());
		return ResponseEntity.created(uri)
			.body(created)
			;
	}

	@GetMapping
	public ResponseEntity<List<MenuResponse>> list() {
		return ResponseEntity.ok()
			.body(menuService.list())
			;
	}
}
