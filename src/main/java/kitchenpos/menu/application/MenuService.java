package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.DifferentOrderAndMenuPriceException;
import kitchenpos.menu.exception.NotCreatedProductException;
import kitchenpos.menu.exception.NotFoundMenuGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new NotFoundMenuGroupException(menuRequest.getMenuGroupId()));

        MenuProducts menuProducts = createMenuProducts(menuRequest);
        menuProducts.checkOverPrice(menuRequest.getPrice());

        Menu menu = Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private MenuProducts createMenuProducts(MenuRequest menuRequest) {
        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();
        return new MenuProducts(
                menuProductRequests.stream()
                        .map(this::createMenuProduct)
                        .collect(Collectors.toList())
        );
    }

    private MenuProduct createMenuProduct(MenuProductRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(NotCreatedProductException::new);
        return MenuProduct.of(product, request.getQuantity());
    }

}
