package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuProductRepository menuProductRepository;
	private final ProductRepository productRepository;

	public MenuService(
		final MenuRepository menuRepository,
		final MenuProductRepository menuProductRepository,
		final ProductRepository productRepository
	) {
		this.menuRepository = menuRepository;
		this.menuProductRepository = menuProductRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {
		Menu saveTargetMenu = new Menu.Builder()
			.name(menuRequest.getName())
			.price(menuRequest.getPrice())
			.menuGroupId(menuRequest.getMenuGroupId())
			.build();

		Menu savedMenu = menuRepository.save(saveTargetMenu);
		Menu finalSavedMenu = addCreateMenuProductTarget(savedMenu, menuRequest.getMenuGroupId());
		return MenuResponse.of(finalSavedMenu);
	}

	public List<MenuResponse> list() {
		final List<Menu> menus = menuRepository.findAll();
		return menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}

	private Menu addCreateMenuProductTarget(Menu savedMenu, Long menuGroupId) {
		final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menuGroupId);

		BigDecimal sum = BigDecimal.ZERO;
		for (MenuProduct menuProduct : menuProducts) {
			final Product product = productRepository.findById(menuProduct.getProductId())
				.orElseThrow(IllegalArgumentException::new);
			sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
			savedMenu.addMenuProduct(new MenuProduct(savedMenu, product, menuProduct.getQuantity()));
		}
		savedMenu.validateSumOfPrice(sum);
		return savedMenu;
	}

}
