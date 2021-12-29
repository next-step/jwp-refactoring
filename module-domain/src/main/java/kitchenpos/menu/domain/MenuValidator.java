package kitchenpos.menu.domain;

import java.util.List;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuProductDto;

public interface MenuValidator {
	void validateMenuGroupExist(Long menuGroupId);

	void validateProductsExist(List<MenuProductDto> menuProductDto);

	void validateMenuPriceIsLessThanOrEqualToTotalMenuProductsPrice(Price price, List<MenuProductDto> menuProductDto);
}
