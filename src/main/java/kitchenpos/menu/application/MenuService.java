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
/*
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
*/

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
		Menu menu = new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuGroup);

		List<MenuProduct> menuProducts = new ArrayList<>();
		for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
			Product product = productRepository.findById(menuProductRequest.getProductId()).orElseThrow(IllegalAccessError::new);
			menuProducts.add(new MenuProduct(menu, product, menuProductRequest.getQuantity()));
		}

		menu.addMenuProducts(menuProducts);
		Menu savedMenu = menuRepository.save(menu);

		return MenuResponse.of(savedMenu);
/*
		if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
			throw new IllegalArgumentException();
		}
*/

/*
		final List<MenuProductRequest> menuProducts = menuRequest.getMenuProducts();
		MenuProduct

		BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : menuProducts) {
            final ProductRequest product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
*/

/*
        final MenuRequest savedMenu = menuDao.save(menuRequest);

        final Long menuId = savedMenu.getId();
        final List<MenuProductRequest> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);
*/
    }

    public List<MenuResponse> list() {
		List<Menu> menus = menuRepository.findAll();

		return menus.stream()
			.map(menu -> MenuResponse.of(menu))
			.collect(Collectors.toList());
/*
        final List<MenuRequest> menus = menuDao.findAll();

        for (final MenuRequest menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }
*/

    }
}
