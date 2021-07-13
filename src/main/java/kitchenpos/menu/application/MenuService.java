package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuService(final MenuRepository menuRepository,
		MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {
		MenuGroup menuGroup = findMenuGroupByMenuGroupId(menuRequest.getMenuGroupId());
		List<MenuProduct> menuProducts = findAllMenuProductByProductId(menuRequest.getMenuProductRequests());
		Menu menu = menuRequest.toMenu(menuGroup, new MenuProducts(menuProducts));
		return MenuResponse.of(menuRepository.save(menu));
	}

	public List<MenuResponse> list() {
		return MenuResponse.of(menuRepository.findAll());
	}

	private MenuGroup findMenuGroupByMenuGroupId(Long id) {
		return menuGroupRepository.findById(id)
			.orElseThrow(() -> new MenuException("메뉴 그룹이 존재하지 않습니다."));
	}

	private List<MenuProduct> findAllMenuProductByProductId(List<MenuProductRequest> menuProductRequests) {
		return menuProductRequests.stream()
			.map(menuProductRequest -> {
				Product product = findProductById(menuProductRequest.getProductId());
				return menuProductRequest.toMenuProduct(product);
			})
			.collect(Collectors.toList());
	}

	private Product findProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new MenuException("해당 상품이 존재하지 않습니다."));
	}
}
