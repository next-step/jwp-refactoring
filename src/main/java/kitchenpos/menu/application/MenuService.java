package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;
	private final MenuProductRepository menuProductRepository;

	public MenuService(
			final MenuRepository menuRepository,
			final MenuGroupRepository menuGroupRepository,
			final ProductRepository productRepository,
			final MenuProductRepository menuProductRepository
	) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
		this.menuProductRepository = menuProductRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest request) {
		List<Product> products = productRepository.findAllById(request.getProductIds());
		MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId()).orElseThrow(() -> new IllegalArgumentException());

		List<MenuProduct> menuProducts = request.toMenuProducts(menuGroup, products);
		List<MenuProduct> savedMenuProducts = menuProductRepository.saveAll(menuProducts);

		return MenuResponse.of(savedMenuProducts);
	}

	public static MenuResponse of(List<MenuProduct> savedMenuProducts) {
		List<MenuProductResponse> menuProductResponse = savedMenuProducts.stream()
				.map(MenuProductResponse::of)
				.collect(Collectors.toList());

		Menu savedMenu = savedMenuProducts.get(0).getMenu();

		return new MenuResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
				savedMenu.getMenuGroup().getId(), menuProductResponse);
	}

	public List<MenuResponse> listMenus() {
		List<Menu> savedMenus = menuRepository.findAll();
		return savedMenus.stream()
				.map(menu -> {
					List<MenuProduct> menuProducts = menuProductRepository.findAllByMenu(menu);
					return MenuResponse.of(menuProducts);
				})
				.collect(Collectors.toList());
	}
}
