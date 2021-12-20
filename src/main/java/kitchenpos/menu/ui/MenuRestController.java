package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuDto;

@RestController(value = "ToBeMenuRestController")
public class MenuRestController {
	private final MenuService menuService;

	public MenuRestController(MenuService menuService) {
		this.menuService = menuService;
	}

	@PostMapping("/api/menus")
	public ResponseEntity<MenuDto> create(@RequestBody MenuCreateRequest request) {
		MenuDto menu = menuService.create(request);
		URI uri = URI.create("/api/menus/" + menu.getId());
		return ResponseEntity.created(uri).body(menu);
	}

	@GetMapping("/api/menus")
	public ResponseEntity<List<MenuDto>> list() {
		List<MenuDto> menus = menuService.list();
		return ResponseEntity.ok().body(menus);
	}
}
