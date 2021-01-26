package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final ProductRepository productRepository;

	public MenuService(
		final MenuRepository menuRepository,
		ProductRepository productRepository) {
		this.menuRepository = menuRepository;
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

		Menu finalSavedMenu = addCreateMenuProductTarget(savedMenu, menuRequest.getMenuProducts());
		return MenuResponse.of(finalSavedMenu);
	}

	@Transactional(readOnly = true)
	public List<MenuResponse> list() {
		final List<Menu> menus = menuRepository.findAll();
		return menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}

	private Menu addCreateMenuProductTarget(Menu savedMenu, List<MenuProductRequest> menuProductRequests) {
		for (MenuProductRequest menuProductRequest : menuProductRequests) {
			Product product = productRepository.findById(menuProductRequest.getProductId()).orElseThrow(IllegalArgumentException::new);
			savedMenu.addMenuProduct(new MenuProduct(savedMenu, product, menuProductRequest.getQuantity()));
		}
		savedMenu.validateSumOfPriceToAddMenuProduct();
		//MenuProducts menuProducts = new MenuProducts(findMenuProducts);
		return savedMenu;
	}

}
