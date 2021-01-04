package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;

@Service
public class MenuService {
	private final MenuDao menuDao;
	private final MenuGroupDao menuGroupDao;
	private final MenuProductDao menuProductDao;
	private final ProductDao productDao;

	public MenuService(
		final MenuDao menuDao,
		final MenuGroupDao menuGroupDao,
		final MenuProductDao menuProductDao,
		final ProductDao productDao
	) {
		this.menuDao = menuDao;
		this.menuGroupDao = menuGroupDao;
		this.menuProductDao = menuProductDao;
		this.productDao = productDao;
	}

	@Transactional
	public Menu create(final MenuRequest menuRequest) {
		final BigDecimal price = menuRequest.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}

		final List<Long> menuProductIds = menuRequest.getMenuProductIds();
		List<MenuProduct> menuProducts = menuProductDao.findAllById(menuProductIds);

		BigDecimal sum = BigDecimal.ZERO;
		for (final MenuProduct menuProduct : menuProducts) {
			final Product product = productDao.findById(menuProduct.getProductId())
				.orElseThrow(IllegalArgumentException::new);
			sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
		}

		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException();
		}

		if (menuRequest.getMenuGroupId() == null) {
			throw new IllegalArgumentException();
		}
		final MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
			.orElseThrow(IllegalArgumentException::new);
		final Menu savedMenu = menuDao.save(Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup));

		final List<MenuProduct> savedMenuProducts = new ArrayList<>();
		for (final MenuProduct menuProduct : menuProducts) {
			menuProduct.setMenu(savedMenu);
			savedMenuProducts.add(menuProductDao.save(menuProduct));
		}
		savedMenu.setMenuProducts(savedMenuProducts);

		return savedMenu;
	}

	public List<Menu> list() {
		final List<Menu> menus = menuDao.findAll();

		/*for (final Menu menu : menus) {
			menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
		}*/

		return menus;
	}
}
