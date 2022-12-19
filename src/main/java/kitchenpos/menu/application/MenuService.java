package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuValidator menuValidator;

	public MenuService(
		MenuRepository menuRepository,
		MenuValidator menuValidator
	) {
		this.menuRepository = menuRepository;
		this.menuValidator = menuValidator;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {
		Menu menu = menuRequest.toEntity();
		menuValidator.validate(menu);
		return MenuResponse.of(menuRepository.save(menu));
	}

	public List<MenuResponse> list() {
		return menuRepository.findAll()
			.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}

}
