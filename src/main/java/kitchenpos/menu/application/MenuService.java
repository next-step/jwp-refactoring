package kitchenpos.menu.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

	private final ProductService productService;
	private final MenuGroupService menuGroupService;
	private final MenuRepository menuRepository;

	public MenuService(
		  final ProductService productService,
		  MenuGroupService menuGroupService, MenuRepository menuRepository) {
		this.productService = productService;
		this.menuGroupService = menuGroupService;
		this.menuRepository = menuRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest request) {
		List<Product> products = productService.findAllByIds(request.getProductIds());
		MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
		kitchenpos.menu.domain.Menu menu = request.toEntity(menuGroup, products);
		kitchenpos.menu.domain.Menu saved = menuRepository.save(menu);

		return MenuResponse.of(saved);
	}

	public List<MenuResponse> list() {
		return menuRepository.findAll().stream()
			  .map(MenuResponse::of)
			  .collect(Collectors.toList());
	}

	public List<Menu> findAllByIds(Set<Long> menuIds) {
		List<Menu> menus = menuRepository.findAllById(menuIds);
		if (menus.isEmpty() || menus.size() != menuIds.size()) {
			throw new EntityNotFoundException();
		}

		return menus;
	}
}
