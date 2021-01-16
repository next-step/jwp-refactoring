package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final MenuProductRepository menuProductRepository;
	private final ProductRepository productRepository;

	public MenuService(
			final MenuRepository menuRepository,
			final MenuGroupRepository menuGroupRepository,
			final MenuProductRepository menuProductRepository,
			final ProductRepository productRepository
	) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.menuProductRepository = menuProductRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest request) {
		List<Product> products = productRepository.findAllById(request.getProductIds());
		MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId()).orElseThrow(()-> new IllegalArgumentException());
		Menu menu = request.toEntity(menuGroup, products);
		Menu saved = menuRepository.save(menu);

		return MenuResponse.of(saved);
	}

	public List<MenuResponse> list() {
		final List<Menu> menus = menuRepository.findAll();
		return MenuResponse.of(menus);
	}
}
