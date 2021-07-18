package kitchenpos.menu.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.entity.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.domain.value.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuPriceGreaterThanProductsSumException;
import kitchenpos.menu.exception.NotFoundMenuGroupException;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.entity.Product;
import kitchenpos.product.domain.repository.ProductRepository;
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
        validateProductPriceSum(menuRequest);
        return MenuResponse.of(menuRepository.save(toMenuEntity(menuRequest)));
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }

    private Menu toMenuEntity(MenuRequest menuRequest) {
        return Menu.of(menuRequest.getName(),
            Price.of(menuRequest.getPrice()),
            menuRequest.getMenuGroupId());
    }

    private void validateProductPriceSum(MenuRequest menuRequest) {
        List<Product> products = productRepository.findAllById(getProductIds(menuRequest));

        Price menuPrice = Price.of(menuRequest.getPrice());
        Price productsSum = Price.of(
            BigDecimal.valueOf(products.stream().mapToDouble(Product::price).sum()));

        if (menuPrice.isGreaterThan(productsSum)) {
            throw new MenuPriceGreaterThanProductsSumException();
        }
    }

    private List<Long> getProductIds(MenuRequest menuRequest) {
        return menuRequest.getMenuProducts().stream()
            .map(MenuProductRequest::getProductId).collect(
                Collectors.toList());
    }

    private void validateMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
            .orElseThrow(NotFoundMenuGroupException::new);
    }
}
