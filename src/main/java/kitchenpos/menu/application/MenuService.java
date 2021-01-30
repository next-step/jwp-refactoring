package kitchenpos.menu.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.utils.ValidationUtils;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupService menuGroupService;
	private final ProductService productService;

	public MenuService(
		final MenuRepository menuRepository,
		final MenuGroupService menuGroupService,
		final ProductService productService) {
		this.menuRepository = menuRepository;
		this.menuGroupService = menuGroupService;
		this.productService = productService;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {
		MenuGroup menuGroup = menuGroupService.findMenuGroupById(menuRequest.getMenuGroupId());


		Menu menu = Menu.builder()
			.name(menuRequest.getName())
			.price(menuRequest.getPrice())
			.menuGroup(menuGroup)
			.menuProducts(createMenuProducts(menuRequest))
			.build();

		return MenuResponse.of(menuRepository.save(menu));
	}

	@Transactional(readOnly = true)
	public List<MenuResponse> list() {
		final List<Menu> menus = menuRepository.findAll();

		return MenuResponse.of(menus);
	}

	public Map<Long, Menu> findAllMenuByIds(List<Long> ids) {
		List<Menu> menus = menuRepository.findAllById(ids);

		ValidationUtils.validateListSize(menus, ids, "존재하지 않는 상품이 있습니다.");

		return menus.stream()
			.collect(Collectors.toMap(Menu::getId, Function.identity()));
	}

	private List<MenuProduct> createMenuProducts(MenuRequest menuRequest) {
		Map<Long, Product> products = productService.findAllProductByIds(menuRequest.getProductsIds());

		return menuRequest.getMenuProducts()
			.stream()
			.map(request -> {
				Product product = products.get(request.getProductId());
				return MenuProduct.builder().product(product).quantity(request.getQuantity()).build();
			})
			.collect(Collectors.toList());
	}
}
