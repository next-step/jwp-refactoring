package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.dto.MenuProductDto;

@Component
public class MenuValidatorImpl implements MenuValidator {
	private final MenuGroups menuGroups;
	private final Products products;

	public MenuValidatorImpl(MenuGroups menuGroups, Products products) {
		this.menuGroups = menuGroups;
		this.products = products;
	}

	@Override
	public void validateMenuGroupExist(Long menuGroupId) {
		if (!menuGroups.contains(menuGroupId)) {
			throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
		}
	}

	@Override
	public void validateProductsExist(List<MenuProductDto> menuProductDto) {
		List<Long> productIds = menuProductDto
			.stream()
			.map(MenuProductDto::getProductId)
			.collect(Collectors.toList());

		if (!products.containsAll(productIds)) {
			throw new IllegalArgumentException("상품이 존재하지 않습니다.");
		}
	}

	@Override
	public void validateMenuPriceIsLessThanOrEqualToTotalMenuProductsPrice(
		Price menuPrice,
		List<MenuProductDto> menuProductDto
	) {
		Price menuProductsTotalPrice = menuProductDto.stream().map(dto -> {
				Product product = products.findById(dto.getProductId());
				Quantity quantity = Quantity.from(dto.getQuantity());
				return product.getPrice().multiply(quantity);
			}).reduce(Price::add)
			.get();

		if (menuPrice.compareTo(menuProductsTotalPrice) > 0) {
			throw new IllegalArgumentException("메뉴의 가격은 메뉴상품들의 전체 가격보다 적거나 같아야 합니다.");
		}
	}
}
