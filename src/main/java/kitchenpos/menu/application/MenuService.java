package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Service
@Transactional(readOnly = true)
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuService(
		MenuRepository menuRepository,
		MenuGroupRepository menuGroupRepository,
		ProductRepository productRepository
	) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuDto create(final MenuCreateRequest request) {
		Name name = Name.of(request.getName());
		Price price = Price.of(request.getPrice());
		MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
			.orElseThrow(IllegalArgumentException::new);
		MenuProducts menuProducts = MenuProducts.of(request.getMenuProducts()
			.stream()
			.map(this::findMenuProduct)
			.collect(Collectors.toList()));
		Menu menu = menuRepository.save(Menu.of(name, price, menuGroup, menuProducts));
		return MenuDto.of(menu);
	}

	public List<MenuDto> list() {
		List<Menu> menus = menuRepository.findAll();
		return menus.stream()
			.map(MenuDto::of)
			.collect(Collectors.toList());
	}

	private MenuProduct findMenuProduct(MenuProductDto menuProductDto) {
		Product product = productRepository.findById(menuProductDto.getProductId())
			.orElseThrow(IllegalArgumentException::new);
		Quantity quantity = Quantity.of(menuProductDto.getQuantity());
		return MenuProduct.of(product, quantity);
	}
}
