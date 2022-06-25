package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuProductV2;
import kitchenpos.menu.domain.MenuV2;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.ProductV2;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final Long price = menuRequest.getPrice();

        if (Objects.isNull(price) || price < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        if (menuRequest.getMenuProducts().isEmpty()) {
            throw new IllegalArgumentException();
        }

        final List<Long> productIds = menuRequest.getMenuProducts().stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());

        final List<ProductV2> products = productRepository.findAll().stream()
                .filter(it -> productIds.contains(it.getId()))
                .collect(Collectors.toList());

        long sum = 0L;
        for (ProductV2 product : products) {
            final Long productId = product.getId();
            final MenuProductRequest filterMenuProductRequest = menuRequest.getMenuProducts().stream()
                    .filter(it -> it.getProductId().equals(productId))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum + product.getPrice() * filterMenuProductRequest.getQuantity();
        }

        if (price.longValue() > sum) {
            throw new IllegalArgumentException();
        }

        final MenuV2 savedMenu = menuRepository.save(menuRequest.toMenu());
        final List<MenuProductV2> savedMenuProducts = new ArrayList<>();

        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final MenuProductV2 menuProduct = new MenuProductV2(null, savedMenu, menuProductRequest.getProductId(),
                    menuProductRequest.getQuantity());
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }

        savedMenu.setMenuProducts(savedMenuProducts);
        return savedMenu.toMenuResponse();
    }

    public List<MenuResponse> list() {
        final List<MenuV2> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuV2::toMenuResponse)
                .collect(Collectors.toList());
    }
}
