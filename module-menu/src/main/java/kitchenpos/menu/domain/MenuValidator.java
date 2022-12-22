package kitchenpos.menu.domain;

import kitchenpos.annoation.DomainService;
import kitchenpos.menu.exception.InvalidMenuPriceException;

@DomainService
public class MenuValidator {

	public void validate(Menu menu) {
		validateMenuPrice(menu);
	}

	private void validateMenuPrice(Menu menu) {
		Money menuPrice = menu.getPrice();
		Money allProductPrices = menu.sumAllProductsPrice();
		if (allProductPrices.isGreaterThan(menuPrice)) {
			throw new InvalidMenuPriceException(menuPrice, allProductPrices);
		}
	}

}
