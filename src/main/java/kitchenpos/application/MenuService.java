package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
	private final MenuDao menuDao;
	private final MenuGroupDao menuGroupDao;
	private final MenuProductDao menuProductDao;
	private final ProductDao productDao;

	public MenuService(final MenuDao menuDao,
	                   final MenuGroupDao menuGroupDao,
	                   final MenuProductDao menuProductDao,
	                   final ProductDao productDao) {
		this.menuDao = menuDao;
		this.menuGroupDao = menuGroupDao;
		this.menuProductDao = menuProductDao;
		this.productDao = productDao;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {
		final BigDecimal price = menuRequest.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}

		MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
				.orElseThrow(() -> new IllegalArgumentException(""));

		final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();

		validatePrice(price, menuProductRequests);
		final Menu savedMenu = menuDao.save(createMenu(menuRequest, menuGroup));
		final List<MenuProduct> savedMenuProducts = menuProductRequests.stream()
				.map(menuProductRequest -> createMenuProduct(savedMenu, menuProductRequest))
				.peek(menuProductDao::save)
				.collect(Collectors.toList());

		savedMenu.setMenuProducts(savedMenuProducts);

		return MenuResponse.of(savedMenu);
	}

	private void validatePrice(BigDecimal price, List<MenuProductRequest> menuProductRequests) {
		BigDecimal sum = BigDecimal.ZERO;
		for (final MenuProductRequest menuProductRequest : menuProductRequests) {
			final Product product = productDao.findById(menuProductRequest.getProductId())
					.orElseThrow(IllegalArgumentException::new);
			sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
		}

		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException();
		}
	}

	private Menu createMenu(MenuRequest menuRequest, MenuGroup menuGroup) {
		Menu menu = new Menu();
		menu.setName(menuRequest.getName());
		menu.setPrice(menuRequest.getPrice());
		menu.setMenuGroup(menuGroup);
		return menu;
	}

	private MenuProduct createMenuProduct(Menu savedMenu, MenuProductRequest menuProductRequest) {
		final Product product = productDao.findById(menuProductRequest.getProductId())
				.orElseThrow(IllegalArgumentException::new);

		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setQuantity(menuProductRequest.getQuantity());
		menuProduct.setMenu(savedMenu);
		menuProduct.setProduct(product);
		return menuProduct;
	}

	public List<MenuResponse> list() {
		final List<Menu> menus = menuDao.findAll();

		for (final Menu menu : menus) {
			menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
		}

		return menus.stream()
				.map(MenuResponse::of)
				.collect(Collectors.toList());
	}
}
