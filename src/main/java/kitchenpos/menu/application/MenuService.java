package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;

@Service
@Transactional
public class MenuService {
	private final MenuDao menuDao;
	private final MenuGroupService menuGroupService;
	private final ProductService productService;

	public MenuService(
		final MenuDao menuDao,
		final MenuGroupService menuGroupService,
		final ProductService productService
	) {
		this.menuDao = menuDao;
		this.menuGroupService = menuGroupService;
		this.productService = productService;
	}

	public MenuResponse create(final MenuRequest request) {
		MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());

		List<MenuProduct> menuProducts = request.getMenuProducts()
			.stream()
			.map(it -> new MenuProduct.Builder()
				.product(productService.findById(it.getProductId()))
				.quantity(it.getQuantity())
				.build())
			.collect(Collectors.toList());

		return MenuResponse.from(menuDao.save(request.toMenu(menuGroup, menuProducts)));
	}

	@Transactional(readOnly = true)
	public List<MenuResponse> list() {
		return MenuResponse.newList(menuDao.findAll());
	}

	public Menu findById(final Long id) {
		return menuDao.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 Menu가 존재하지 않습니다."));
	}
}
