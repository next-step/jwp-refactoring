package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuGroupService menuGroupService;

    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {

        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());

        final BigDecimal price = menuRequest.getPrice();
        Menu menu = new Menu(menuRequest.getName(), new Price(price), menuGroup);

        List<Product> products = menuRequest.getProductIds().stream().map(id -> productService.findById(id)).collect(Collectors.toList());
        menu.addMenuProducts(products);

//        BigDecimal sum = BigDecimal.ZERO;
//        for (final MenuProduct menuProduct : menuProducts) {
//            final Product product = productService.findById(menuProduct.getProductId())
//                    .orElseThrow(IllegalArgumentException::new);
//            sum = sum.add(product.getPrice().getValue().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
//        }
//
//        if (price.compareTo(sum) > 0) {
//            throw new IllegalArgumentException();
//        }

        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
