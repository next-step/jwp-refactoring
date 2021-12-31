package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
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
        List<Product> products = getValidProducts(menuRequest);

        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new NotFoundMenuGroupException(menuRequest.getMenuGroupId()));

        Menu menu = Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup, createMenuProduct(menuRequest, products));
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> createMenuProduct(MenuRequest menuRequest, List<Product> products) {
        return products.stream()
                .map(menuRequest::createMenuProduct)
                .collect(Collectors.toList());
    }

    private List<Product> getValidProducts(MenuRequest menuRequest) {
        List<Long> productIds = menuRequest.getProductIds();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        if (productIds.size() != products.size()) {
            throw new NotCreatedProductException();
        }

        BigDecimal sum = products.stream()
                .map(product -> product.getTotalPrice(menuRequest.getQuantity(product.getId())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menuRequest.getPrice().compareTo(sum) > 0) {
            throw new DifferentOrderAndMenuPriceException();
        }
        return products;
    }

}
