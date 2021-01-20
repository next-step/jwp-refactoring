package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.*;
import kitchenpos.exception.BadPriceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;


@DisplayName("메뉴 서비스")
public class MenuServiceTest extends ServiceTestBase {
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuService menuService;

    private MenuGroupResponse menuGroup;
    private ProductResponse product;

    @Autowired
    public MenuServiceTest(MenuGroupService menuGroupService, ProductService productService, MenuService menuService) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuService = menuService;
    }

    @BeforeEach
    void setUp() {
        super.setUp();

        menuGroup = menuGroupService.create(MenuGroupServiceTest.createRequest("추천메뉴"));
        product = productService.create(ProductServiceTest.createRequest("후라이드", 17_000L));
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void createRequest() {
        List<MenuProductRequest> menuProducts = Collections.singletonList(createMenuProduct(product.getId(), 2L));
        MenuRequest request = createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts);
        MenuResponse savedMenu = menuService.create(request);

        assertThat(savedMenu.getId()).isNotNull();
    }

    @DisplayName("가격이 부적합한 메뉴를 등록한다")
    @Test
    void createMenuWithIllegalArguments() {
        List<MenuProductRequest> menuProducts = Collections.singletonList(createMenuProduct(product.getId(), 2L));
        MenuRequest request = createRequest("후라이드+후라이드", 300000L, menuGroup.getId(), menuProducts);

        assertThatExceptionOfType(BadPriceException.class)
                .isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("메뉴 그룹이 등록되지 않은 메뉴를 등록한다")
    @Test
    void createMenuWithNotExistsMenuGroup() {
        List<MenuProductRequest> menuProducts = Collections.singletonList(createMenuProduct(product.getId(), 2L));
        MenuRequest request = createRequest("후라이드+후라이드", 19_000L, 99L, menuProducts);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("제품이 등록되지 않은 메뉴를 등록한다")
    @Test
    void createMenuWithNotExistsProduct() {
        List<MenuProductRequest> menuProducts = Collections.singletonList(createMenuProduct(99L, 2L));
        MenuRequest request = createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> menuService.create(request));
    }

    @DisplayName("제품을 조회한다")
    @Test
    void findAllProduct() {
        List<MenuProductRequest> menuProducts = Collections.singletonList(createMenuProduct(product.getId(), 2L));
        MenuResponse createdResponse = menuService.create(createRequest("후라이드+후라이드", 19_000L, menuGroup.getId(), menuProducts));
        MenuResponse createdResponse2 = menuService.create(createRequest("양념+후라이드", 20_000L, menuGroup.getId(), menuProducts));

        List<MenuResponse> menus = menuService.list();
        assertThat(menus.size()).isEqualTo(2);
        assertThat(menus).contains(createdResponse, createdResponse2);
    }

    public static MenuRequest createRequest(String name, long price, Long menuGroupId, List<MenuProductRequest> menuProuducts) {
        return MenuRequest.builder()
                .name(name)
                .price(price)
                .menuGroupId(menuGroupId)
                .menuProducts(menuProuducts)
                .build();
    }

    public static MenuProductRequest createMenuProduct(Long productId, Long quantity) {
        return MenuProductRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
