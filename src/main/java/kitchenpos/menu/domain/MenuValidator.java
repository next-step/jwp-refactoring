package kitchenpos.menu.domain;

import org.springframework.stereotype.Component;

import kitchenpos.common.Price;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@Component
public class MenuValidator {

	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	public void validate(Menu menu) {
		validateMenuGroup(menu);
		validatePrice(menu);
	}

	private void validateMenuGroup(Menu menu) {
		if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
			throw new EntityNotFoundException(MenuGroup.ENTITY_NAME, menu.getMenuGroupId());
		}
	}

	private void validatePrice(Menu menu) {
		Price menuProductsPrice = getTotalPrice(menu.getMenuProducts());
		if (menu.moreExpensive(menuProductsPrice)) {
			throw new IllegalArgumentException(ErrorMessage.PRICE_HIGHER_THAN_MENU_PRODUCTS_TOTAL_PRICES);
		}
	}

	private Price getTotalPrice(MenuProducts menuProducts) {
		return menuProducts.value()
			.stream()
			.map(it -> productPrice(it.getProductId(), it))
			.reduce(Price::add)
			.orElse(Price.ZERO);
	}

	private Price productPrice(Long productId, MenuProduct menuProduct) {
		Price price = productRepository.findById(productId)
			.orElseThrow(() -> new EntityNotFoundException(Product.ENTITY_NAME, productId))
			.getPrice();
		return menuProduct.getTotalPrice(price);
	}
}
