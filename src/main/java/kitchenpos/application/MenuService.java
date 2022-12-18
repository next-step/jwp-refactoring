package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;

@Service
@Transactional(readOnly = true)
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductService productService;

	public MenuService(MenuRepository menuRepository,
					   MenuGroupRepository menuGroupRepository,
					   ProductService productService) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productService = productService;
	}

	@Transactional
	public MenuResponse create(MenuRequest menuRequest) {
		Menu menu = toMenu(menuRequest);
		return new MenuResponse(create(menu));
	}

	@Transactional
	public Menu create(Menu menu) {
		menu.validatePrice();
		return menuRepository.save(menu);
	}

	public List<MenuResponse> list() {
		return MenuResponse.of(findAll());
	}

	public List<Menu> findAll() {
		return menuRepository.findAll();
	}

	private Menu toMenu(MenuRequest menuRequest) {
		// TODO move to validator
		validateMenuGroupExists(menuRequest.getMenuGroupId());

		List<Product> products = productService.findAllById(menuRequest.toProductsId());

		return menuRequest.toMenu(multiplyQuantity(menuRequest, products));
	}

	private void validateMenuGroupExists(Long menuGroupId) {
		if (!menuGroupRepository.existsById(menuGroupId)) {
			throw new EntityNotFoundException(menuGroupId, MenuGroup.class);
		}
	}

	private Map<Product, Integer> multiplyQuantity(MenuRequest menuRequest, List<Product> products) {
		Map<Long, Integer> productsCount = menuRequest.toProducts();

		return products.stream()
			.collect(Collectors.toMap(
				Function.identity(),
				product -> productsCount.get(product.getId()),
				Integer::sum
			));
	}

	public List<Menu> findAllById(List<Long> idList) {
		return menuRepository.findAllById(idList);
	}
}
