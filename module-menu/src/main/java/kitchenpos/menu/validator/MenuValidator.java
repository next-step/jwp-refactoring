package kitchenpos.menu.validator;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static kitchenpos.common.Messages.*;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validateCreateMenu(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        validateMenuProducts(Price.of(menuRequest.getPrice()), menuRequest.getMenuProducts());
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new NoSuchElementException(MENU_GROUP_NOT_EXISTS);
        }
    }

    private void validateMenuProducts(Price price, List<MenuProductRequest> menuProducts) {
        List<Long> productsIds = menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findByIdIn(productsIds);

        if (products.size() != menuProducts.size()) {
            throw new NoSuchElementException(PRODUCT_FIND_IN_NO_SUCH);
        }

        if (price.isExpensive(productsTotalPrice(products, menuProducts))) {
            throw new IllegalArgumentException(MENU_PRICE_EXPENSIVE);
        }
    }

    private BigDecimal productsTotalPrice(List<Product> products, List<MenuProductRequest> menuProducts) {
        Map<Long, Long> menuProductRequests = menuProducts.stream()
                .collect(Collectors.toMap(
                        MenuProductRequest::getProductId,
                        MenuProductRequest::getQuantity
                ));

        return products.stream().map(product -> {
            Long quantity = menuProductRequests.get(product.getId());
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
