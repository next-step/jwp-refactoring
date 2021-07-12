package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuRepository menuRepository;

    public MenuService(MenuGroupService menuGroupService, ProductService productService, MenuRepository menuRepository) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuRepository = menuRepository;
    }


    @Transactional
    public MenuResponse create(MenuRequest menuRequest) {
        priceValidCheck(menuRequest.getPrice());
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        MenuProducts menuProducts = findMenuProducts(menuRequest.getMenuProducts());
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);

        return MenuResponse.of(menuRepository.save(menu));
    }

    private void priceValidCheck(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 0원 이상이어야 합니다.");
        }
    }

    private MenuProducts findMenuProducts(List<MenuProductRequest> menuProductRequests) {
        productValidCheck(menuProductRequests);
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productService.findById(menuProductRequest.getProductId());
            menuProducts.add(menuProductRequest.toEntity(product));
        }
        return new MenuProducts(menuProducts);
    }

    private void productValidCheck(List<MenuProductRequest> menuProductRequests) {
        if (menuProductRequests == null || menuProductRequests.isEmpty()) {
            throw new IllegalArgumentException("포함된 상품이 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("등록된 메뉴가 아닙니다."));
    }
}
