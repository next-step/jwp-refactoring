package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
class MenuServiceTest {

    private static final String MENU_NAME = "메뉴1";
    private static final BigDecimal PRICE = BigDecimal.valueOf(10_000);

    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = menuGroupDao.findById(4L).get();
        Product product1 = productService.findById(1L);
        Product product2 = productService.findById(2L);
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(1L, product1.getId(), 1),
            new MenuProduct(2L, product2.getId(), 1)
        );
        Menu menu = new Menu(
            MENU_NAME, product1.getPrice().add(product2.getPrice()), menuGroup.getId(), menuProducts
        );

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isNotNull();
        assertThat(savedMenu.getMenuProducts()).size().isEqualTo(2);
    }

    @DisplayName("메뉴 등록 예외 - 메뉴 금액은 0보다 커야 한다.")
    @Test
    void create_exception1() {
        // given
        Menu menu = new Menu(
            MENU_NAME, null, 1L, Collections.EMPTY_LIST
        );

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> menuService.create(menu))
            .withMessage("메뉴 금액은 0보다 커야 한다.");
    }

    @DisplayName("메뉴 등록 예외 - 메뉴는 메뉴 그룹에 속해야 한다.")
    @Test
    void create_exception2() {
        // given
        Menu menu = new Menu(
            MENU_NAME, PRICE, 9999999L, Collections.EMPTY_LIST
        );

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> menuService.create(menu))
            .withMessage("메뉴는 메뉴 그룹에 속해야 한다.");
    }

    @DisplayName("메뉴 등록 예외 - 메뉴의 금액은 메뉴 상품의 합과 같아야 한다.")
    @Test
    void create_exception3() {
        // given
        MenuGroup menuGroup = menuGroupDao.findById(4L).get();
        Product product1 = productService.findById(1L);
        Product product2 = productService.findById(2L);
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(1L, product1.getId(), 1),
            new MenuProduct(2L, product2.getId(), 1)
        );
        Menu menu = new Menu(
            MENU_NAME, product1.getPrice(), menuGroup.getId(), menuProducts
        );

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> menuService.create(menu))
            .withMessage("메뉴의 금액은 메뉴 상품의 합과 같아야 한다.");
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // when
        List<Menu> list = menuService.list();

        // then
        assertThat(list.size()).isGreaterThan(0);
    }
}
