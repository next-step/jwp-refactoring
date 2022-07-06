package kitchenpos.menu.validator;

import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductService productService;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductService productService, MenuGroupRepository menuGroupRepository) {
        this.productService = productService;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateMenuGroupExist(MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("그룹이 존재하지 않습니다.");
        }
    }

    public void validateProduct(Price price, List<MenuProduct> menuProducts) {
        long priceSum = menuProducts.stream().
            mapToLong(menuProduct -> productService.findPriceByIdOrElseThrow(menuProduct.getProductId()) * menuProduct.getQuantity()).sum();
        if (price.isGreaterThan(priceSum)) {
            throw new IllegalArgumentException();
        }
    }
}
