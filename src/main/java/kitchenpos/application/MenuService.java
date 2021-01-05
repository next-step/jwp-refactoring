package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

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
	public MenuResponse create(final MenuRequest menuRequest) {

		final List<Long> productIds = menuRequest.getProductIds();
		List<Product> products = productDao.findAllById(productIds);

		if (menuRequest.getMenuGroupId() == null) {
			throw new IllegalArgumentException();
		}
		final MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
			.orElseThrow(IllegalArgumentException::new);
		final Menu savedMenu = menuDao.save(Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup, products, menuRequest.getQuantities()));
		return MenuResponse.of(savedMenu);
	}

	public List<MenuResponse> list() {
		final List<Menu> menus = menuDao.findAll();
		return menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}
}
