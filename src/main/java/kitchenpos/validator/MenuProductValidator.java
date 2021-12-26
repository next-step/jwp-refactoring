package kitchenpos.validator;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.PriceNotAcceptableException;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MenuProductValidator {

    private static final String ERROR_MESSAGE_MENU_PRICE_HIGH = "메뉴 가격은 상품 리스트의 가격 합보다 작거나 같아야 합니다.";

    private final ProductRepository productRepository;

    public MenuProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateMenuPriceIsLessThanMenuProductsSum(Price menuPrice,
        List<MenuProductRequest> menuProductRequests) {
        List<Price> menuProductPrices = getMenuProductPrices(menuProductRequests);
        Price sumOfMenuProducts = Price.sumPrices(menuProductPrices);

        if (menuPrice.isBiggerThan(sumOfMenuProducts)) {
            throw new PriceNotAcceptableException(ERROR_MESSAGE_MENU_PRICE_HIGH);
        }
    }

    private List<Price> getMenuProductPrices(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(it -> calculateMenuProductPrice(it))
            .collect(Collectors.toList());
    }

    private Price calculateMenuProductPrice(MenuProductRequest menuProductRequest) {
        Product findProduct = productRepository.findById(menuProductRequest.getProductId())
            .orElseThrow(ProductNotFoundException::new);
        return findProduct.getPrice().multiply(new Quantity(menuProductRequest.getQuantity()));
    }
}
