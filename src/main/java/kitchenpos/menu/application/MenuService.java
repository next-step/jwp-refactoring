package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    final private MenuGroupRepository menuGroupRepository;
	final private ProductRepository productRepository;
	final private MenuRepository menuRepository;

	public MenuService(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository, final MenuRepository menuRepository) {
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
		this.menuRepository = menuRepository;
	}

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
		MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalAccessError::new);
		Menu menu = menuRepository.save(new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuGroup));
		List<MenuProduct> menuProducts = collectMenuProduct(menuRequest, menu);
		menu.addMenuProducts(menuProducts);
		// Menu savedMenu = menuRepository.save(menu);

		return MenuResponse.of(menu);
    }

	private List<MenuProduct> collectMenuProduct(MenuRequest menuRequest, Menu menu) {
		List<MenuProduct> menuProducts = new ArrayList<>();
		for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
			Product product = productRepository.findById(menuProductRequest.getProductId()).orElseThrow(IllegalArgumentException::new);
			menuProducts.add(new MenuProduct(menu, product, menuProductRequest.getQuantity()));
		}

		return menuProducts;
	}

	public List<MenuResponse> list() {
		List<Menu> menus = menuRepository.findAll();

		return menus.stream()
			.map(menu -> MenuResponse.of(menu))
			.collect(Collectors.toList());
    }
}
