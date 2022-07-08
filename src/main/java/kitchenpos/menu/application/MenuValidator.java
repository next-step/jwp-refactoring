package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.global.domain.Amount;
import kitchenpos.global.domain.Price;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void existMenuGroup(MenuGroup menuGroup) {
        if (ObjectUtils.isEmpty(menuGroup)) {
            throw new IllegalArgumentException("메뉴그룹은 반드시 필요합니다.");
        }

    }

    public void validateMenuProduct(MenuRequest menuRequest) {
        Price menuPrice = Price.from(menuRequest.getPrice());

        Amount totalAmount = Amount.createSumAmounts(menuRequest.getMenuProducts()
                .stream()
                .map(this::getAmount)
                .collect(Collectors.toList()));

        validatePrice(menuPrice, totalAmount);
    }

    private Amount getAmount(MenuProductRequest menuProductRequest) {
        Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("없는 상품이 존재합니다."));
        return Amount.of(product.getPrice(), menuProductRequest.getQuantity());
    }

    private void validatePrice(Price price, Amount totalAmount) {
        if (price.isBigThen(totalAmount)) {
            throw new IllegalArgumentException("메뉴의 가격은 구성하고 있는 메뉴 상품들의 가격(상품가격 * 수량)의 합계보다 작아야 합니다.");
        }
    }
}
