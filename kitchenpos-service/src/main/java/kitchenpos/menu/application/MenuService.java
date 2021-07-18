package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductService productService;

	public MenuService(
			final MenuRepository menuRepository,
			final MenuGroupRepository menuGroupRepository,
			final ProductService productService) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productService = productService;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {
		if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
			throw new IllegalArgumentException();
		}

		Menu menu = menuRepository.save(menuRequest.toMenu());
		final List<MenuProduct> menuProducts = menuRequest.getMenuProductRequests().stream()
				.map(menuProductRequest -> new MenuProduct(menu, productService.findById(menuProductRequest.getProductId()), menuProductRequest.getQuantity()))
				.collect(Collectors.toList());

		menu.addMenuProduct(menuProducts);
		return MenuResponse.of(menu);
	}

	public List<MenuResponse> list() {
		final List<Menu> menus = menuRepository.findAll();
		return MenuResponse.of(menus);
	}
}
