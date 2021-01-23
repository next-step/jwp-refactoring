package kitchenpos.service;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.Quantity;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;

    public MenuService(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository, final MenuRepository menuRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
    }

    public MenuResponse save(final MenuRequest menuRequest) {
        List<Long> ids = menuRequest.getMenuProductRequest().stream().map(MenuProductRequest::getProductId).collect(toList());
        Map<Long, Product> products = productRepository.findByIdIn(ids).stream().collect(toMap(Product::getId, Function.identity()));

        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);

        Menu menu = menuRequest.toMenu(menuGroup);
        menuRequest.getMenuProductRequest()
                .forEach(p -> menu.addMenuProduct(new MenuProduct(menu, products.get(p.getProductId()), new Quantity(p.getQuantity()))));
        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}
