package kitchenpos.menu.domain;

import org.springframework.stereotype.Component;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuCreateValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuCreateValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        List<Long> requestProducts = menu.productIds();
        List<Product> productList = productRepository.findAllById(requestProducts);

        existsById(menu.getMenuGroupId());
        existProducts(requestProducts, productList);
        checkValidPrice(menu, productList.stream().map(Product::getPrice).collect(Collectors.toList()));
    }

    public void existsById(Long id) {
        checkNullId(id);
        checkExist(id);
    }

    private void checkNullId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("메뉴그룹 id 값은 null이 아니어야 합니다");
        }
    }

    private void checkExist(Long id) {
        if (!menuGroupRepository.existsById(id)) {
            throw new IllegalArgumentException("메뉴그룹이 존재하지 않습니다. id:" + id);
        }
    }

    private void existProducts(List<Long> requestProducts, List<Product> dbProducts) {
        if (notEqualsRealProductCount(requestProducts, dbProducts)) {
            throw new IllegalArgumentException("존재하지 않는 상품이 포함되어 있습니다");
        }
    }

    private boolean notEqualsRealProductCount(List<Long> requestProducts, List<Product> productList) {
        return requestProducts.size() != productList.size();
    }

    private void checkValidPrice(Menu menu, List<Price> priceListe) {
        Price sum = totalPrice(priceListe);
        if (menu.getPrice().moreThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 상품 가격의 총 합과 같거나 작아야합니다");
        }
    }

    private Price totalPrice(List<Price> priceList) {
        return Price.from(BigDecimal.valueOf(priceList.stream()
                .map(Price::getPrice)
                .mapToInt(BigDecimal::intValue)
                .sum()));
    }
}
