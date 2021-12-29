package kitchenpos.menu.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.Price;
import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;

@Component
@Transactional(readOnly = true)
public class MenuValidator {

	private final ProductService productService;

	public MenuValidator(ProductService productService) {
		this.productService = productService;
	}

	public void isOverPrice(Menu menu) {
		Price totalPrice = getTotalPrice(menu.getMenuProducts());
		if (menu.getPrice().isGreaterThan(totalPrice)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "메뉴 가격은 구성상품들의 총합보다 작거나 같아야 합니다");
		}
	}

	private Price getTotalPrice(MenuProducts menuProducts) {
		return menuProducts.toList().stream()
			.map(this::calculatePrice)
			.reduce(Price.ZERO, Price::add);
	}

	private Price calculatePrice(MenuProduct menuProduct) {
		Product product = productService.getById(menuProduct.getProductId());
		return product.getPrice().multiply(menuProduct.getQuantity().toLong());
	}
}
