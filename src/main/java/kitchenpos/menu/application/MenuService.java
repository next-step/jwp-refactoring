package kitchenpos.menu.application;

import kitchenpos.common.NotFoundException;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
	static final String MSG_CANNOT_FIND_PRODUCT = "Cannot find Product by productId";
	static final String MSG_CANNOT_FIND_MENUGROUP = "Cannot find MenuGroup by menuGroupId";

	private final MenuDao menuDao;
	private final MenuGroupDao menuGroupDao;
	private final ProductDao productDao;

	public MenuService(final MenuDao menuDao,
	                   final MenuGroupDao menuGroupDao,
	                   final ProductDao productDao) {
		this.menuDao = menuDao;
		this.menuGroupDao = menuGroupDao;
		this.productDao = productDao;
	}

	@Transactional
	public MenuResponse create(MenuRequest menuRequest) {
		MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_MENUGROUP));
		Menu menu = menuDao.save(createMenu(menuRequest, menuGroup));
		List<MenuProduct> menuProducts = createMenuProducts(menu, menuRequest.getMenuProductRequests());
		menu.addMenuProducts(menuProducts);
		return MenuResponse.of(menu);
	}

	private List<MenuProduct> createMenuProducts(Menu menu, List<MenuProductRequest> menuProductRequests) {
		List<Product> products = getProducts(menuProductRequests);
		return menuProductRequests.stream()
				.map(menuProductRequest -> createMenuProduct(products, menu, menuProductRequest))
				.collect(Collectors.toList());
	}

	private List<Product> getProducts(List<MenuProductRequest> menuProductRequests) {
		final List<Long> productIds = menuProductRequests.stream()
				.map(MenuProductRequest::getProductId).collect(Collectors.toList());
		return productDao.findAllById(productIds);
	}

	private MenuProduct createMenuProduct(List<Product> products, Menu menu, MenuProductRequest menuProductRequest) {
		Product product = findProduct(products, menuProductRequest);
		return new MenuProduct(menu, product, menuProductRequest.getQuantity());
	}

	private Product findProduct(List<Product> products, MenuProductRequest menuProductRequest) {
		return products.stream()
				.filter(iter -> Objects.equals(menuProductRequest.getProductId(), iter.getId()))
				.findFirst()
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_PRODUCT));
	}

	private Menu createMenu(MenuRequest menuRequest, MenuGroup menuGroup) {
		return new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuGroup);
	}

	public List<MenuResponse> list() {
		List<Menu> menus = menuDao.findAllWithMenuGroupFetchJoin();
		return menus.stream()
				.map(MenuResponse::of)
				.collect(Collectors.toList());
	}
}
