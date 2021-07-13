package kitchenpos.menu.application;

import kitchenpos.menu.domain.entity.*;
import kitchenpos.menu.domain.value.Price;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.entity.Product;
import kitchenpos.product.domain.entity.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = Menu.of(menuRequest.getName(),
                Price.of(menuRequest.getPrice()),
                findMenuGroup(menuRequest.getMenuGroupId()),
                findMenuProducts(menuRequest));
        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> findMenuProducts(MenuRequest menuRequest) {
        return menuRequest.getMenuProducts()
                .stream()
                .map(menuProductRequest -> {
                    Product product = productRepository.findById(menuProductRequest.getMenuId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new MenuProduct(product, menuProductRequest.getQuantity());
                }).collect(Collectors.toList());
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}
