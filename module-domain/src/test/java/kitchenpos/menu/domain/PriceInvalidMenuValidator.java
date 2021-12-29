package kitchenpos.menu.domain;

import java.util.List;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuProductDto;

public class PriceInvalidMenuValidator implements MenuValidator {
	@Override
	public void validateMenuGroupExist(Long menuGroupId) {
		// do not throw exception
	}

	@Override
	public void validateProductsExist(List<MenuProductDto> menuProductDto) {
		// do not throw exception
	}

	@Override
	public void validateMenuPriceIsLessThanOrEqualToTotalMenuProductsPrice(
		Price price,
		List<MenuProductDto> menuProductDto
	) {
		throw new IllegalArgumentException();
	}
}
