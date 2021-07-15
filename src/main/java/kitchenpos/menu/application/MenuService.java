package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.menugroup.application.MenuGroupValidator;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupValidator menuGroupValidator;
    private final MenuValidator menuValidator;
    private final ProductMenuService productMenuService;

    @Autowired
    public MenuService(final MenuRepository menuRepository, final MenuGroupValidator menuGroupValidator,
                       final MenuValidator menuValidator, final ProductMenuService productMenuService) {
        this.menuRepository = menuRepository;
        this.menuGroupValidator = menuGroupValidator;
        this.menuValidator = menuValidator;
        this.productMenuService = productMenuService;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupValidator.validateExistsMenuGroupById(menuRequest.getMenuGroupId());
        Menu menu = menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId()));
        menuValidator.validateMenuPrice(menu, getTotalProductsPrice(menuRequest));
        menuRequest.getMenuProductRequests()
                .forEach(menuProductRequest -> menu.addMenuProduct(createMenuProduct(menu, menuProductRequest)));
        return MenuResponse.of(menu);
    }

    private BigDecimal getTotalProductsPrice(MenuRequest menuRequest) {
        return menuRequest.getMenuProductRequests()
                .stream()
                .map(menuProductRequest -> productMenuService.calculateProductsPrice(menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .reduce(BigDecimal.ZERO, (total, productPrice) -> total.add(productPrice));
    }

    public List<MenuResponse> findAllMenu() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public Menu findMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new MenuNotFoundException("조회된 메뉴가 없습니다. 입력 ID : " + id));
    }

    private MenuProduct createMenuProduct(Menu menu, MenuProductRequest menuProductRequest) {
        return new MenuProduct(menu, menuProductRequest.getProductId(), menuProductRequest.getQuantity());
    }
}
