package kitchenpos.menuproduct.service;

import kitchenpos.generic.Money;
import kitchenpos.generic.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menuproduct.domain.MenuProduct;
import kitchenpos.menuproduct.domain.MenuProductRepository;
import kitchenpos.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuProductService {
    private final MenuProductRepository menuProductRepository;
    private final ProductService productService;

    public MenuProductService(MenuProductRepository menuProductRepository, ProductService productService) {
        this.menuProductRepository = menuProductRepository;
        this.productService = productService;
    }

    public List<MenuProductResponse> saveProducts(final Menu menu, final List<MenuProductRequest> menuProducts) {
        List<MenuProduct> products = getMenuProducts(menu, menuProducts);
        checkGraterThanMenuPrice(menu.getPrice(), products.stream()
                .map(MenuProduct::getAmount)
                .reduce(Money.ZERO_MONEY, Money::sum));
        return MenuProductResponse.ofProducts(menuProductRepository.saveAll(products));
    }

    @Transactional(readOnly = true)
    public List<MenuProductResponse> list(final Menu menu) {
        return menuProductRepository
                .findAllByMenu(menu)
                .stream()
                .map(MenuProductResponse::ofProduct)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> getMenuProducts(Menu menu, List<MenuProductRequest> menuProducts) {
        return menuProducts
                .stream()
                .map(request -> new MenuProduct(
                        menu,
                        productService.findById(request.getProductId()),
                        request.ofQuantity()))
                .collect(Collectors.toList());
    }

    private void checkGraterThanMenuPrice(Money source, Money target) {
        if (source.isGraterThan(target)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격 보다 큽니다.");
        }
    }
}
