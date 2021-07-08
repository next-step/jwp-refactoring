package kitchenpos.menu.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.manugroup.domain.MenuGroup;
import kitchenpos.manugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
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
	public Menu create(final MenuRequest menuRequest) {
		MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(() -> new MenuException("메뉴 그룹이 존재하지 않습니다."));

		List<Product> products = menuRequest.getMenuProductIds().stream()
			.map(menuProductId -> productRepository.findById(menuProductId).orElseThrow(() -> new MenuException("해당 상품이 존재하지 않습니다.")))
			.collect(Collectors.toList());
		Menu menu = menuRequest.toMenu(menuGroup, new MenuProducts(Arrays.asList()));
		return menuRepository.save(menu);
	}

	public List<Menu> list() {
		/*
		final List<Menu> menus = menuRepository.findAll();

		for (final Menu menu : menus) {
			menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
		}*/

		return menuRepository.findAll();
	}
}
