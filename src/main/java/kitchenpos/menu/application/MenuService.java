package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuService {
	private final MenuGroupService menuGroupService;
	private final ProductService productService;
	private final MenuRepository menuRepository;

	public MenuService(MenuGroupService menuGroupService, ProductService productService,
		MenuRepository menuRepository) {
		this.menuGroupService = menuGroupService;
		this.productService = productService;
		this.menuRepository = menuRepository;
	}

	@Transactional
	public Menu create(final MenuRequest menuRequest) {
		MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
		List<MenuProduct> menuProducts = createMenuProducts(menuRequest);
		Menu menu = menuRequest.toMenu(menuGroup, menuProducts);
		return menuRepository.save(menu);
	}

	@Transactional(readOnly = true)
	public List<Menu> list() {
		return menuRepository.findAll();
	}

	private List<MenuProduct> createMenuProducts(MenuRequest menuRequest) {
		return menuRequest.getMenuProductRequests().stream()
			.map(mp -> new MenuProduct(productService.findById(mp.getProductId()), mp.getQuantity()))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Menu findById(long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다"));

	}
}
