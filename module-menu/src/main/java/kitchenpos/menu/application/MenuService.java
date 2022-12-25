package kitchenpos.menu.application;

import java.math.BigDecimal;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(MenuRequest request) {
        MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
        List<Product> products = findAllProductByIds(request.findAllProductIds());
        validatePrice(request.getPrice(), products);
        Menu menu = request.toMenu(menuGroup, products);
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<Product> findAllProductByIds(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (productIds.size() != products.size()) {
            throw new IllegalArgumentException("요청한 상품 메뉴의 개수가 일치하지 않습니다.");
        }
        return products;
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }

    private void validatePrice(BigDecimal price, List<Product> products) {
        Price productsPrice = products.stream()
            .map(Product::getPrice)
            .reduce(Price.of(BigDecimal.ZERO), Price::add);

        Price.of(price).validateTotalPrice(productsPrice);
    }
}
