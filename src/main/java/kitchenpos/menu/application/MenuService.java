package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository ProductRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = ProductRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        //final BigDecimal price = request.getPrice();

        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        List<Product> products = productRepository.findAllById(request.getMenuProductIds());

        List<MenuProductRequest> menuProductRequests = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductrequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductrequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);

            sum = sum.add(product.getPrice().value().multiply(BigDecimal.valueOf(menuProductrequest.getQuantity())));
        }

        final Menu savedMenu = menuRepository.save(request.createMenu(menuGroup, products));
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> findAll() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
