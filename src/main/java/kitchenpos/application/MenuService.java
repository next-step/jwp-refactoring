package kitchenpos.application;

import kitchenpos.common.Price;
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
		// TODO: 2021-01-18 모든 예외 메세지 문자엺 추가
		// TODO: 2021-01-18 모든 예외 커스텀 예외로
		// TODO: 2021-01-18 모든 커스텀 예외 핸들러 처리
		MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
				.orElseThrow(() -> new IllegalArgumentException(""));

		final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();

		validatePrice(menuRequest, menuProductRequests);
		final Menu savedMenu = menuDao.save(createMenu(menuRequest, menuGroup));
		final List<MenuProduct> savedMenuProducts = menuProductRequests.stream()
				.map(menuProductRequest -> createMenuProduct(savedMenu, menuProductRequest))
				.peek(menuProductDao::save)
				.collect(Collectors.toList());

		savedMenu.addMenuProducts(savedMenuProducts);

		return MenuResponse.of(savedMenu);
	}

	private void validatePrice(MenuRequest menuRequest, List<MenuProductRequest> menuProductRequests) {
		Price sum = Price.ZERO;
		for (final MenuProductRequest menuProductRequest : menuProductRequests) {
			final Product product = productDao.findById(menuProductRequest.getProductId())
					.orElseThrow(IllegalArgumentException::new);
			BigDecimal quantity = BigDecimal.valueOf(menuProductRequest.getQuantity());
			sum = sum.add(product.getPrice().multiply(quantity));
		}

		Price price = new Price(menuRequest.getPrice());
		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException();
		}
	}

	private Menu createMenu(MenuRequest menuRequest, MenuGroup menuGroup) {
		return new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuGroup);
	}

	private MenuProduct createMenuProduct(Menu savedMenu, MenuProductRequest menuProductRequest) {
		final Product product = productDao.findById(menuProductRequest.getProductId())
				.orElseThrow(IllegalArgumentException::new);

		MenuProduct menuProduct = new MenuProduct(savedMenu, product, menuProductRequest.getQuantity());
		return menuProduct;
	}

	public List<MenuResponse> list() {
		final List<Menu> menus = menuDao.findAll();
		// TODO : fetch join
		for (final Menu menu : menus) {
			menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
		}

		return menus.stream()
				.map(MenuResponse::of)
				.collect(Collectors.toList());
	}
}
