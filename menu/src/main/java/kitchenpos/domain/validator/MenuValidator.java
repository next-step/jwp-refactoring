package kitchenpos.domain.validator;

import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class MenuValidator {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    @Autowired
    public MenuValidator(MenuRepository menuRepository, MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    // 메뉴 그룹이 삭제될 때, 메뉴가 메뉴 그룹에 등록되어 사용되고 있는지 체크
    public boolean isExistsMenuByMenuGroup(MenuGroup menuGroup) {
        return menuRepository.existsByMenuGroup(menuGroup);
    }

    // 상품이 삭제될 때, 상품이 메뉴에 등록되어 사용되고 있는지 체크
    public boolean isExistsMenuByProduct(Product product) {
        return menuProductRepository.existsByProduct(product);
    }

    // 메뉴에 포함된 상품에 대해서 유효성 검증을 함
    public List<MenuProduct> validateMenuProducts(MenuRequest request) {
        List<Product> products = getProducts(request);
        List<MenuProduct> menuProducts = mapMenuProducts(request, products);
        validateTotalPrice(request.getPrice(), menuProducts);
        return menuProducts;
    }

    // 상품 조회
    private List<Product> getProducts(MenuRequest request) {
        List<Long> productIds = request.getMenuProducts()
                                       .stream()
                                       .map(MenuProductRequest::getProductId)
                                       .collect(Collectors.toList());
        return productRepository.findAllByIdIn(productIds);
    }

    // 상품과 등록 건수를 상품 번호로 매핑함
    private List<MenuProduct> mapMenuProducts(MenuRequest request, List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProduct : request.getMenuProducts()) {
            Product product = products.stream()
                                      .filter(prod -> prod.getId()
                                                          .equals(menuProduct.getProductId()))
                                      .findFirst()
                                      .orElseThrow(() -> new IllegalArgumentException("등록하고자 하는 상품이 존재하지 않습니다."));
            menuProducts.add(new MenuProduct(product, menuProduct.getQuantity()));
        }
        return menuProducts;
    }

    // 메뉴가 등록, 수정될 때, 메뉴 가격이 상품 가격 * 수량의 합계와 일치하는지 체크
    private void validateTotalPrice(BigDecimal price, List<MenuProduct> menuProducts) {
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);
        menuProducts.forEach(menuProduct -> totalPrice.set(totalPrice.get()
                                                                     .add(menuProduct.getAmount())));
        if (totalPrice.get()
                      .compareTo(price) != 0) {
            throw new IllegalArgumentException("메뉴 가격과 상품 합계의 가격이 일치하지 않습니다");
        }
    }
}
