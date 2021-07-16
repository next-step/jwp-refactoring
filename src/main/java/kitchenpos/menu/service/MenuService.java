package kitchenpos.menu.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.entity.Menu;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.entity.MenuProduct;
import kitchenpos.menu.domain.entity.MenuRepository;
import kitchenpos.menu.domain.value.Price;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NotFoundMenuGroupException;
import kitchenpos.menu.exception.NotFoundProductException;
import kitchenpos.product.domain.entity.Product;
import kitchenpos.product.domain.entity.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public MenuResponse create(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());

        Menu menu = Menu.of(menuRequest.getName(),
            Price.of(menuRequest.getPrice()),
            menuRequest.getMenuGroupId(),
            findMenuProducts(menuRequest));
        return MenuResponse.of(menuRepository.save(menu));
    }

    private List<MenuProduct> findMenuProducts(MenuRequest menuRequest) {
        return menuRequest.getMenuProducts()
            .stream()
            .map(menuProductRequest -> {
                Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(NotFoundProductException::new);
                return new MenuProduct(product, menuProductRequest.getQuantity());
            }).collect(Collectors.toList());
    }

    private void validateMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
            .orElseThrow(NotFoundMenuGroupException::new);
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}
