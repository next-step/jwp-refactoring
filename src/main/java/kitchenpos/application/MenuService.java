package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
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
		if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
			throw new IllegalArgumentException();
		}

		final List<MenuProductRequest> menuProducts = menuRequest.getMenuProductRequests();

		BigDecimal sum = BigDecimal.ZERO;
		for (final MenuProductRequest menuProductRequest : menuProducts) {
			final Product product = productDao.findById(menuProductRequest.getProductId())
					.orElseThrow(IllegalArgumentException::new);
			sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
		}

		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException();
		}

		final Menu savedMenu = menuDao.save(createMenu(menuRequest));
		final List<MenuProduct> savedMenuProducts = menuProducts.stream()
				.map(menuProductRequest -> createMenuProduct(savedMenu, menuProductRequest))
				.peek(menuProductDao::save)
				.collect(Collectors.toList());

		savedMenu.setMenuProducts(savedMenuProducts);

		return MenuResponse.of(savedMenu);
	}

	private Menu createMenu(MenuRequest menuRequest) {
		Menu menu = new Menu();
		menu.setName(menuRequest.getName());
		menu.setPrice(menuRequest.getPrice());
		menu.setMenuGroupId(menuRequest.getMenuGroupId());
		return menu;
	}

	private MenuProduct createMenuProduct(Menu savedMenu, MenuProductRequest menuProductRequest) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setSeq(menuProductRequest.getSeq());
		menuProduct.setQuantity(menuProductRequest.getQuantity());
		menuProduct.setMenuId(savedMenu.getId());
		menuProduct.setProductId(menuProductRequest.getProductId());
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
