package kitchenpos.menu.application;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Component
public class MenuValidator {
	private final static int COMPARE_ZERO = 0;

	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuValidator(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository) {
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	public void validate(MenuRequest menuRequest) {
		existedMenuGroup(menuRequest.getMenuGroupId());
		validateMenuPrice(menuRequest);
	}

	private void validateMenuPrice(MenuRequest menuRequest) {
		validateNullPrice(menuRequest.getPrice());
		if (calculateTotalPrice(menuRequest).compareTo(new BigDecimal(menuRequest.getPrice())) < COMPARE_ZERO) {
			throw new MenuException(ErrorCode.PRODUCT_PRICE_IS_UNDER_SUM_PRICE);
		}
	}

	private void validateNullPrice(Integer price) {
		if (price == null) {
			throw new MenuException(ErrorCode.PRICE_IS_NOT_NULL);
		}
	}

	private BigDecimal calculateTotalPrice(MenuRequest menu) {
		return menu.getMenuProducts()
			.stream()
			.map(menuProductRequest -> {
				Product product = productFindById(menuProductRequest.getProductId());
				return product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
			}).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private Product productFindById(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> {
				throw new MenuException(ErrorCode.PRODUCT_IS_NULL);
			});
	}

	private void existedMenuGroup(Long menuGroupId) {
		if (!menuGroupRepository.existsById(menuGroupId)) {
			throw new MenuException(ErrorCode.MENU_GROUP_IS_NOT_NULL);
		}
	}
}
