package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateCreate(Menu menu) {
        validateMenuGroup(menu.getMenuGroupId());
        validateMenuProducts(menu.getPrice(), menu.getMenuProducts());
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validateMenuProducts(Price menuPrice, MenuProducts menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts.getMenuProducts())) {
            throw new IllegalArgumentException("메뉴 상품은 1개 이상이어야 합니다.");
        }
        if (menuPrice.isMoreExpensive(calculateTotalPrice(menuProducts))) {
            throw new IllegalArgumentException("메뉴가 메뉴 상품들의 합계보다 비쌉니다.");
        }
    }

    private Price calculateTotalPrice(MenuProducts menuProducts) {
        Map<Long, Quantity> menuProductsMap = menuProducts.toMap();
        List<Long> productIds = new ArrayList<>(menuProductsMap.keySet());

        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("상품 수가 일치하지 않습니다.");
        }
        return Price.sum(products.stream()
                .map(product -> {
                    Quantity quantity = menuProductsMap.get(product.getId());
                    BigDecimal value = BigDecimal.valueOf(quantity.getQuantity());
                    return product.getPrice().multiply(value).getPrice();
                })
                .collect(Collectors.toList()));
    }
}
