package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceTest {
    private MenuGroupService menuGroupService;
    private MenuService menuService;
    private ProductService productService;

    @Autowired
    public MenuServiceTest(MenuGroupService menuGroupService, MenuService menuService, ProductService productService) {
        this.menuGroupService = menuGroupService;
        this.menuService = menuService;
        this.productService = productService;
    }

    private Product 제육볶음;
    private Product 소불고기;
    private List<MenuProduct> 고기반찬;
    private MenuGroup 점심특선;

    @BeforeEach
    void beforeEach() {
        제육볶음 = productService.create(Product.of("제육볶음", BigDecimal.valueOf(1000)));
        소불고기 = productService.create(Product.of("소불고기", BigDecimal.valueOf(1000)));
        고기반찬 = Arrays.asList(MenuProduct.of(제육볶음.getId(), 1), MenuProduct.of(소불고기.getId(), 1));
        점심특선 = 메뉴묶음_요청("점심특선");
    }

    @Test
    void create() {
        // when
        Menu 점심특선 = menuService.create(Menu.of("점심특선", BigDecimal.valueOf(2000), this.점심특선.getId(), 고기반찬));

        // then
        assertThat(점심특선.getId()).isNotNull();
    }

    @Test
    void create_throwsException_ifPriceLessThanZero() {
        // when
        // then
        assertThatThrownBy(() -> menuService.create(Menu.of("점심특선", BigDecimal.valueOf(0), -1L, 고기반찬)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_throwsException_ifNonExistMenuGroupId() {
        // when
        // then
        assertThatThrownBy(() -> menuService.create(Menu.of("점심특선", BigDecimal.valueOf(2000), -1L, 고기반찬)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_throwsException_ifWrongAmount() {
        // when
        // then
        assertThatThrownBy(() -> menuService.create(Menu.of("점심특선", BigDecimal.valueOf(4000), 점심특선.getId(), 고기반찬)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    MenuGroup 메뉴묶음_요청(String name) {
        return menuGroupService.create(new MenuGroup(name));
    }
}
