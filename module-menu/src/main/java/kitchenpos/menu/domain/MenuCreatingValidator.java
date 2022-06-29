package kitchenpos.menu.domain;

import kitchenpos.core.domain.Amount;
import kitchenpos.core.domain.DomainService;
import kitchenpos.core.domain.Price;
import kitchenpos.core.domain.Quantity;
import kitchenpos.core.exception.InvalidPriceException;
import kitchenpos.menugroup.exception.NotFoundMenuGroupException;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.NotFoundProductException;

import java.util.List;

@DomainService
public class MenuCreatingValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuCreatingValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(Long menuGroupId, Price price, List<MenuProduct> menuProducts) {
        validateExistMenuGroup(menuGroupId);
        validateAmount(price, menuProducts);
    }

    private void validateExistMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new NotFoundMenuGroupException();
        }
    }

    private void validateAmount(Price price, List<MenuProduct> menuProducts) {
        Amount amount = calculateTotalAmount(menuProducts);
        if (price.toAmount().isGatherThan(amount)) {
            throw new InvalidPriceException("상품들 금액의 합이 메뉴 가격보다 클 수 없습니다.");
        }
    }

    private Amount calculateTotalAmount(List<MenuProduct> menuProducts) {
        Amount totalAmount = Amount.ZERO;
        for (MenuProduct menuProduct: menuProducts) {
            Product product = findProductById(menuProduct.getProductId());
            Amount amount = calculateAmount(product.getPrice(), menuProduct.getQuantity());
            totalAmount = totalAmount.add(amount);
        }
        return totalAmount;
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(NotFoundProductException::new);
    }

    private Amount calculateAmount(Price price, Quantity quantity) {
        return price.multiply(quantity);
    }
}
