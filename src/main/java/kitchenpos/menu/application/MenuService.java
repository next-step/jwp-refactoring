package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuDto;

@Service
@Transactional(readOnly = true)
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuValidator menuValidator;

	public MenuService(MenuRepository menuRepository, MenuValidator menuValidator) {
		this.menuRepository = menuRepository;
		this.menuValidator = menuValidator;
	}

	@Transactional
	public MenuDto create(MenuCreateRequest request) {
		Menu menu = menuRepository.save(
			Menu.of(
				Name.from(request.getName()),
				Price.from(request.getPrice()),
				request.getMenuGroupId(),
				request.getMenuProducts(),
				menuValidator));
		return MenuDto.from(menu);
	}

	public List<MenuDto> list() {
		List<Menu> menus = menuRepository.findAll();
		return menus.stream()
			.map(MenuDto::from)
			.collect(Collectors.toList());
	}
}
