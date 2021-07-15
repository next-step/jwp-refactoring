package kitchenpos.menu.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import kitchenpos.generic.exception.CalculationFailedException;
import kitchenpos.generic.price.domain.Price;
import kitchenpos.menu.exception.ExceedingTotalPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        validate(menu, getProducts(menu));
    }

    void validate(Menu menu, List<Product> products) {
        if (!getMenuGroup(menu).isPresent()) {
            throw new MenuGroupNotFoundException("해당 ID 의 메뉴그룹이 존재하지 않습니다.");
        }

        if (menu.getMenuProducts().isEmpty()) {
            throw new IllegalArgumentException("제품 매핑 정보는 1개 이상 존재해야 합니다.");
        }

        if (menu.getPrice().isBiggerThan(totalPriceOf(products))) {
            throw new ExceedingTotalPriceException("메뉴 가격이 제품 가격의 총 합을 초과합니다.");
        }
    }

    private List<Product> getProducts(Menu menu) {
        return menu.getMenuProducts().mapList(this::getProduct);
    }

    private Product getProduct(MenuProduct menuProduct) {
        return productRepository.findById(menuProduct.getProductId())
            .orElseThrow(() -> new ProductNotFoundException("해당 ID의 제품이 존재하지 않습니다."));
    }

    private Price totalPriceOf(List<Product> products) {
        return products.stream()
            .map(Product::getPrice)
            .reduce(Price::add)
            .orElseThrow(() -> new CalculationFailedException("단품 가격의 합계를 계산하지 못했습니다."));
    }

    private Optional<MenuGroup> getMenuGroup(Menu menu) {
        return menuGroupRepository.findById(menu.getMenuGroupId());
    }
}
