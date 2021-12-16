package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductException;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuService(
		final MenuRepository menuRepository,
		final MenuGroupRepository menuGroupRepository,
		final ProductRepository productRepository
	) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public Menu create(final MenuRequest menuRequest) {
		final MenuGroup menuGroup = menuGroupFindById(menuRequest.getMenuGroupId());
		final Menu savedMenu = Menu.of(menuRequest, menuGroup);
		savedMenu.addMenuProducts(generateMenuProducts(menuRequest, savedMenu), menuRequest.getPrice());
		return menuRepository.save(savedMenu);
	}

	private List<MenuProduct> generateMenuProducts(MenuRequest menuRequest, Menu menu) {
		return menuRequest.getMenuProducts()
			.stream()
			.map(it -> generateMenuProduct(menu, it))
			.collect(Collectors.toList());
	}

	private MenuProduct generateMenuProduct(Menu menu, MenuProductRequest menuProductRequest) {
		return MenuProduct.of(menu, getProduct(menuProductRequest), menuProductRequest.getQuantity());
	}

	private Product getProduct(MenuProductRequest menuProductRequest) {
		return productRepository.findById(menuProductRequest.getProductId())
			.orElseThrow(() -> {
				throw new ProductException(ErrorCode.PRODUCT_IS_NULL);
			});
	}

	private MenuGroup menuGroupFindById(Long id) {
		return menuGroupRepository.findById(id)
			.orElseThrow(() -> {
				throw new MenuException(ErrorCode.MENU_GROUP_IS_NOT_NULL);
			});
	}

	public List<Menu> list() {
		return menuRepository.findAll();
	}
}
