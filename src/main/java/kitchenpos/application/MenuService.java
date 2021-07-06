package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;

;

@Service
public class MenuService {
	private final MenuDao menuDao;
	private final MenuGroupRepository menuGroupRepository;
	private final MenuProductDao menuProductDao;
	private final ProductRepository productRepository;

	public MenuService(
		final MenuDao menuDao,
		final MenuGroupRepository menuGroupRepository,
		final MenuProductDao menuProductDao,
		final ProductRepository productDao
	) {
		this.menuDao = menuDao;
		this.menuGroupRepository = menuGroupRepository;
		this.menuProductDao = menuProductDao;
		this.productRepository = productDao;
	}

	@Transactional
	public Menu create(final Menu menu) {
		final BigDecimal price = menu.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("메뉴 가격은 0원 미만이 될 수 없습니다.");
		}

		if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
			throw new IllegalArgumentException("메뉴가 메뉴그룹에 등록되지 않았습니다.");
		}

		final List<MenuProduct> menuProducts = menu.getMenuProducts();

		BigDecimal sum = BigDecimal.ZERO;
		for (final MenuProduct menuProduct : menuProducts) {
			final Product product = productRepository.findById(menuProduct.getProductId())
				.orElseThrow(() -> new IllegalArgumentException("상품에 없는 메뉴상품입니다."));
			sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
		}

		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 가격의 합보다 작아야합니다.");
		}

		final Menu savedMenu = menuDao.save(menu);

		final Long menuId = savedMenu.getId();
		final List<MenuProduct> savedMenuProducts = new ArrayList<>();
		for (final MenuProduct menuProduct : menuProducts) {
			menuProduct.setMenuId(menuId);
			savedMenuProducts.add(menuProductDao.save(menuProduct));
		}
		savedMenu.setMenuProducts(savedMenuProducts);

		return savedMenu;
	}

	public List<Menu> list() {
		final List<Menu> menus = menuDao.findAll();

		for (final Menu menu : menus) {
			menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
		}

		return menus;
	}
}
