package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.event.MenuCreateEvent;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ApplicationEventPublisher eventPublisher;

	public MenuService(final MenuRepository menuRepository,
		MenuGroupRepository menuGroupRepository, ApplicationEventPublisher eventPublisher) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {
		MenuGroup menuGroup = findMenuGroupByMenuGroupId(menuRequest.getMenuGroupId());
		Menu menu = menuRepository.save(menuRequest.toMenu(menuGroup));
		eventPublisher.publishEvent(new MenuCreateEvent(menu, menuRequest.getMenuProductRequests()));
		return MenuResponse.of(menu);
	}

	public List<MenuResponse> list() {
		return MenuResponse.of(menuRepository.findAll());
	}

	private MenuGroup findMenuGroupByMenuGroupId(Long id) {
		return menuGroupRepository.findById(id)
			.orElseThrow(() -> new MenuException("메뉴 그룹이 존재하지 않습니다."));
	}


}
