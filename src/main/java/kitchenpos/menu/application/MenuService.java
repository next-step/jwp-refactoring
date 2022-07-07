package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.validator.MenuValidator;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final ProductService productService, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidator.validateMenuGroupExist(menuRequest);
        menuValidator.validateProduct(Price.of(menuRequest.getPrice()),convertToMenuProduct(menuRequest.getMenuProducts()));
        final Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), convertToMenuProduct(menuRequest.getMenuProducts()));
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    private List<MenuProduct> convertToMenuProduct(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(request -> new MenuProduct(productService.findByIdOrElseThrow(request.getProductId()).getId(), request.getQuantity())).
            collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.toMenuResponses(menus);
    }


}
