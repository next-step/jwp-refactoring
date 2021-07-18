package kitchenpos.menu.event;

import kitchenpos.exception.MenuException;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MenuEventHandler {

    private static final String NOT_EXISTS_MENU_GROUP_ERROR_MESSAGE = "미등록 메뉴 그룹 입니다.";
    private static final String NOT_EQUAL_PRODUCT_COUNT_ERROR_MESSAGE = "미등록 상품을 메뉴 상품으로 등록 요청 하였습니다.";
    private static final String ILLEGAL_MENU_PRICE_ERROR_MESSAGE = "메뉴의 가격이 메뉴 상품들의 총합보다 클 수 없습니다.";

    private final ProductService productService;
    private final MenuGroupRepository menuGroupRepository;

    public MenuEventHandler(ProductService productService, MenuGroupRepository menuGroupRepository) {
        this.productService = productService;
        this.menuGroupRepository = menuGroupRepository;
    }

    @EventListener
    public void createMenuValidEvent(CreateMenuEvent createMenuEvent) {
        MenuRequest menuRequest = createMenuEvent.getMenuRequest();
        checkExistsMenuGroup(menuRequest.getMenuGroupId());
        Products products = productService.findProductsByIds(menuRequest.toProductIds());
        validateMenuProducts(menuRequest, products);
    }

    private void checkExistsMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new MenuException(NOT_EXISTS_MENU_GROUP_ERROR_MESSAGE);
        }
    }

    private void validateMenuProducts(MenuRequest menuRequest, Products products) {
        List<MenuProductRequest> menuProducts = menuRequest.getMenuProducts();
        if (products.productSize() != menuProducts.size()) {
            throw new MenuException(NOT_EQUAL_PRODUCT_COUNT_ERROR_MESSAGE);
        }
        BigDecimal totalProductAmount = calcTotalProductAmount(menuProducts, products);
        if (menuRequest.getPrice().compareTo(totalProductAmount) > 0) {
            throw new MenuException(ILLEGAL_MENU_PRICE_ERROR_MESSAGE);
        }
    }

    private BigDecimal calcTotalProductAmount(List<MenuProductRequest> menuProducts, Products products) {
        Map<Long, Long> menuProductMap = menuProducts.stream()
                .collect(Collectors.toMap(MenuProductRequest::getProductId, MenuProductRequest::getQuantity));
        return products.calcTotalProductAmount(menuProductMap);
    }
}
