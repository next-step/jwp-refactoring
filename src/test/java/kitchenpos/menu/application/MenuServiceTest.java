package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.product.dao.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static kitchenpos.menu.application.MenuService.PRICE_NOT_NULL_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MenuService")
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


    @DisplayName("메뉴 그룹을 필수값으로 갖는다.")
    @Test
    void create_fail_MenuGroupNull() {
        assertThatThrownBy(() -> menuService.create(new MenuCreateRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }
}
