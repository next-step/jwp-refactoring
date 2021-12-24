package kitchenpos.menu.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuAddRequest;
import kitchenpos.menu.dto.MenuProductAddRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NotFoundMenuProductException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.NotFoundMenuGroupException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Service
public class MenuService {

	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuService(
		final MenuRepository menuRepository,
		final MenuGroupRepository menuGroupRepository,
		final ProductRepository productRepository
	) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuResponse create(final MenuAddRequest request) {
		final MenuGroup menuGroup = findMenuGroup(request.getMenuGroupId());
		final List<MenuProduct> menuProducts = createMenuProducts(request.getMenuProductAddRequests());

		final Menu menu = menuRepository.save(
			Menu.of(request.getName(), request.getPrice(), menuGroup, menuProducts)
		);
		return MenuResponse.of(menu);
	}

	@Transactional(readOnly = true)
	private MenuGroup findMenuGroup(Long menuGroupId) {
		return menuGroupRepository.findById(menuGroupId)
			.orElseThrow(NotFoundMenuGroupException::new);
	}

	@Transactional(readOnly = true)
	private Map<Long, Product> findProducts(List<MenuProductAddRequest> requests) {
		final List<Long> productIds = requests.stream()
			.map(MenuProductAddRequest::getProductId)
			.collect(Collectors.toList());
		return productRepository.findAllById(productIds)
			.stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));
	}

	private List<MenuProduct> createMenuProducts(List<MenuProductAddRequest> requests) {
		final Map<Long, Product> products = findProducts(requests);
		if (products.size() != requests.size()) {
			throw new NotFoundMenuProductException();
		}
		return requests.stream()
			.map(menuProductAddRequest -> MenuProduct.of(
				products.get(menuProductAddRequest.getProductId()),
				menuProductAddRequest.getQuantity()
			)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<MenuResponse> list() {
		final List<Menu> menus = menuRepository.findAll();
		return menus.stream().map(MenuResponse::of).collect(Collectors.toList());
	}
}
