package kitchenpos.application;

import kitchenpos.MenuGroupService;
import kitchenpos.MenuService;
import kitchenpos.ProductService;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.exceptions.InvalidMenuPriceException;
import kitchenpos.domain.menu.exceptions.MenuGroupEntityNotFoundException;
import kitchenpos.domain.menu.exceptions.ProductEntityNotFoundException;
import dto.menu.MenuProductRequest;
import dto.menu.MenuRequest;
import dto.menu.MenuResponse;
import dto.menugroup.MenuGroupRequest;
import dto.menugroup.MenuGroupResponse;
import dto.product.ProductRequest;
import dto.product.ProductResponse;
import kitchenpos.utils.FixtureUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuServiceTest extends FixtureUtils {
    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("올바르지 않은 메뉴 가격으로 메뉴를 등록할 수 없다.")
    @ParameterizedTest
    @NullSource
    @MethodSource("menuCreateFailByInvalidPriceResource")
    void menuCreateFailByInvalidPrice(BigDecimal invalidPrice) {
        // given
        Long menuGroupId = 1L;
        String menuName = "말도 안되는 가격";

        List<MenuProductRequest> menuProductRequests = Arrays.asList(
                MenuProductRequest.of(1L, 1L), MenuProductRequest.of(2L, 1L));
        MenuRequest menuRequest = MenuRequest.of(menuName, invalidPrice, menuGroupId, menuProductRequests);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(InvalidMenuPriceException.class)
                .hasMessage("가격은 음수일 수 없습니다.");
    }
    public static Stream<Arguments> menuCreateFailByInvalidPriceResource() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of(BigDecimal.valueOf(-2))
        );
    }

    @DisplayName("존재하지 않는 메뉴 그룹으로 메뉴를 등록할 수 없다.")
    @Test
    void createFailWithNotExistMenuGroupTest() {
        // given
        Long notExistMenuGroupId = 1000L;
        List<MenuProductRequest> menuProductRequests = Collections.singletonList(MenuProductRequest.of(1L, 1L));
        MenuRequest menuRequest = MenuRequest.of("menu", BigDecimal.ONE, notExistMenuGroupId, menuProductRequests);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(MenuGroupEntityNotFoundException.class)
                .hasMessage("존재하지 않은 메뉴 그룹으로 메뉴를 등록할 수 없습니다.");
    }

    @DisplayName("메뉴 상품들 가격의 총합보다 비싸게 메뉴 가격을 정할 수 없다.")
    @Test
    void createFailWithTooExpensivePriceTest() {
        // given
        BigDecimal tooExpensivePrice = BigDecimal.valueOf(1000000);
        String menuName = "너무너무 비싼 메뉴";

        MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("놀라운 메뉴 그룹"));

        ProductResponse product1 = productService.create(new ProductRequest("test1", BigDecimal.ONE));
        ProductResponse product2 = productService.create(new ProductRequest("test2", BigDecimal.ONE));

        List<MenuProductRequest> menuProductRequests = Arrays.asList(
                MenuProductRequest.of(product1.getId(), 1L), MenuProductRequest.of(product2.getId(), 1L));
        MenuRequest menuRequest = MenuRequest.of(menuName, tooExpensivePrice, menuGroup.getId(), menuProductRequests);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(InvalidMenuPriceException.class)
                .hasMessage("메뉴의 가격은 구성된 메뉴 상품들의 가격 합보다 비쌀 수 없습니다.");
    }

    @DisplayName("존재하지 않는 상품으로 구성된 메뉴 상품으로 메뉴를 만들 수 없다.")
    @Test
    void createFailWithNotExistProduct() {
        // given
        MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("놀라운 메뉴 그룹"));

        MenuProduct notExistMenuProduct= MenuProduct.of(10000000L, 1L);

        List<MenuProductRequest> menuProductRequests = Collections.singletonList(MenuProductRequest.of(notExistMenuProduct));
        MenuRequest menuRequest = MenuRequest.of("menu", BigDecimal.ONE, menuGroup.getId(), menuProductRequests);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(ProductEntityNotFoundException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createMenuTest() {
        // given
        int expectedSize = 2;
        BigDecimal menuPrice = BigDecimal.ONE;
        String menuName = "신메뉴";

        MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("놀라운 메뉴 그룹"));

        ProductResponse product1 = productService.create(new ProductRequest("test1", BigDecimal.ONE));
        ProductResponse product2 = productService.create(new ProductRequest("test2", BigDecimal.ONE));

        List<MenuProductRequest> menuProductRequests = Arrays.asList(
                MenuProductRequest.of(product1.getId(), 1L), MenuProductRequest.of(product2.getId(), 1L));
        MenuRequest menuRequest = MenuRequest.of(menuName, menuPrice, menuGroup.getId(), menuProductRequests);

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        assertThat(menuResponse.getId()).isNotNull();
        assertThat(menuResponse.getMenuProducts()).hasSize(expectedSize);
    }

    @DisplayName("메뉴 목록을 불러올 수 있다.")
    @Test
    void getMenusTest() {
        // given
        BigDecimal menuPrice = BigDecimal.ONE;
        String menuName = "새로 나온 엄청난 메뉴";

        MenuGroupResponse menuGroup = menuGroupService.create(new MenuGroupRequest("놀라운 메뉴 그룹"));

        ProductResponse product1 = productService.create(new ProductRequest("test1", BigDecimal.ONE));
        ProductResponse product2 = productService.create(new ProductRequest("test2", BigDecimal.ONE));

        List<MenuProductRequest> menuProductRequests = Arrays.asList(
                MenuProductRequest.of(product1.getId(), 1L), MenuProductRequest.of(product2.getId(), 1L));
        MenuRequest menuRequest = MenuRequest.of(menuName, menuPrice, menuGroup.getId(), menuProductRequests);

        menuService.create(menuRequest);

        // when
        List<MenuResponse> menus = menuService.list();
        Stream<String> names = menus.stream()
                .map(MenuResponse::getName);

        // then
        assertThat(names).contains(menuName);
    }
}
