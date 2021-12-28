package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationServiceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

public class MenuServiceTest extends IntegrationServiceTest {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;

    private static ProductResponse savedProduct;
    private static MenuGroupResponse savedMenuGroup;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        final ProductRequest product = new ProductRequest("후라이드", new BigDecimal(16000));
        savedProduct = productService.create(product);

        // given
        final MenuGroupRequest menuGroup = MenuGroupServiceTest.makeMenuGroupRequest("한마리메뉴");
        savedMenuGroup = menuGroupService.create(menuGroup);
    }

    @Test
    void create() {
        // given
        final MenuRequest menu = makeMenuRequest("후라이드치킨", new BigDecimal(16000), savedMenuGroup.getId(),
            savedProduct.getId(), 1);

        // when
        final MenuResponse savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getName()).isEqualTo("후라이드치킨");
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(savedMenuGroup.getId());
        assertThat(savedMenu.getMenuProducts()).isNotEmpty();
        assertThat(savedMenu.getMenuProducts().get(0).getSeq()).isNotNull();
        assertThat(savedMenu.getMenuProducts().get(0).getProductId()).isEqualTo(savedProduct.getId());
        assertThat(savedMenu.getMenuProducts().get(0).getQuantity()).isEqualTo(1);
    }

    @DisplayName("가격이 null이거나, 음수이거나, 상품의 가격 * 메뉴 상품의 수량의 총합보다 메뉴의 가격이 클 때 예외 발생")
    @ParameterizedTest
    @MethodSource("provideInvalidPrice")
    void createByInvalidPrice(final BigDecimal price) {
        // given
        final MenuRequest menu = makeMenuRequest("후라이드치킨", price, savedMenuGroup.getId(), savedProduct.getId(), 1);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    private static Stream<BigDecimal> provideInvalidPrice() {
        return Stream.of(null, new BigDecimal(-1), new BigDecimal(16001));
    }

    @Test
    void list() {
        // given
        final MenuRequest menu = makeMenuRequest("후라이드치킨", new BigDecimal(16000), savedMenuGroup.getId(),
            savedProduct.getId(), 1);
        final MenuResponse savedMenu = menuService.create(menu);

        // when
        final List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus.get(0).getId()).isNotNull();
        assertThat(menus.get(0).getName()).isEqualTo("후라이드치킨");
        assertThat(menus.get(0).getMenuGroupId()).isEqualTo(savedMenuGroup.getId());
        assertThat(menus.get(0).getMenuProducts()).isNotEmpty();
        assertThat(menus.get(0).getMenuProducts().get(0).getSeq()).isNotNull();
        assertThat(menus.get(0).getMenuProducts().get(0).getProductId()).isEqualTo(savedProduct.getId());
        assertThat(menus.get(0).getMenuProducts().get(0).getQuantity()).isEqualTo(1);
    }

    public static MenuRequest makeMenuRequest(
        final String name, final BigDecimal price, final Long menuGroupId, final Long productId, final int quantity
    ) {
        final MenuProductRequest menuProductRequest = new MenuProductRequest(productId, quantity);
        return new MenuRequest(name, price, menuGroupId, Collections.singletonList(menuProductRequest));
    }
}
