package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NotExistMenuGroupException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
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
                .orElseThrow(() -> new NotExistMenuGroupException("존재하지 않는 메뉴그룹입니다."));

        final MenuProducts menuProducts = getMenuProducts(menuRequest);

        final Menu savedMenu = menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts));

        return MenuResponse.of(savedMenu);
    }

    private MenuProducts getMenuProducts(MenuRequest menuRequest) {
        List<Long> productIds = menuRequest.getProductIds();
        List<Product> findProducts = productRepository.findByIdIn(productIds);

        return new MenuProducts(menuRequest.getMenuProducts(), findProducts, new Price(menuRequest.getPrice()));
    }

    public List<MenuResponse> list() {
        List<MenuResponse> menuResponses = menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
        return menuResponses;
    }
}
