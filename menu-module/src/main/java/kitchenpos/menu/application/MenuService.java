package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Products;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = new Menu(menuRequest.getName(), BigDecimal.valueOf(menuRequest.getPrice()),
                checkMenuGroup(menuRequest), menuRequest.getMenuProducts());

        List<Long> productIds = menu.toProductIds();
        Products products = new Products(productRepository.findAllById(productIds));
        Long sum = products.calculateSumPrice(productIds);
        menu.validateLimitPrice(sum);

        return MenuResponse.from(menuRepository.save(menu));
    }

    private Long checkMenuGroup(MenuRequest menuRequest) {
        menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        return menuRequest.getMenuGroupId();
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
             .map(MenuResponse::from)
             .collect(Collectors.toList());
    }
}
