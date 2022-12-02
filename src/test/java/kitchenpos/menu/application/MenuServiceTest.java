package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.product.dao.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static kitchenpos.menu.application.MenuService.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MenuService")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("가격을 필수값으로 갖는다.")
    @ParameterizedTest
    @NullSource
    void create_fail_MenuGroupNull(BigDecimal price) {
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(price)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("가격은 0원보다 작을 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"-1"})
    void create_fail_minimumPrice(BigDecimal price) {
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(price)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MINIMUM_PRICE_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴 그룹이 없을 경우 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"1"})
    void create_fail_menuGroup(BigDecimal price) {
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest(price)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE);
    }
}
