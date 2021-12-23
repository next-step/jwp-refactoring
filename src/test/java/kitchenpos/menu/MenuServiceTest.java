package kitchenpos.menu;

import kitchenpos.AcceptanceTest;
import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 관련 기능")
class MenuServiceTest extends AcceptanceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않을 경우 예외가 발생한다.")
    void nonExistMenuGroup() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000)));
        });
    }

    @Test
    @DisplayName("메뉴에 등록하고자 하는 상품이 존재하지 않을 경우 예외가 발생한다.")
    void nonExistProduct() {
        // given
        final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("추천메뉴"));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct())));
        });
    }

    @Test
    @DisplayName("메뉴의 금액이 상품의 총 금액보다 크다면 예외가 발생한다.")
    void menuPriceMoreThanProductPriceSum() {
        // given
        final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("추천메뉴"));
        final Product savedProduct = productDao.save(new Product("후라이드", BigDecimal.valueOf(8000)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        });
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void createMenu() {
        // given
        final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("추천메뉴"));
        final Product savedProduct = productDao.save(new Product("후라이드", BigDecimal.valueOf(9000)));

        // when
        Menu menu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));

        // then
        assertAll(
                () -> assertThat(menu.getId()).isNotNull(),
                () -> assertThat(menu.getName()).isEqualTo("후라이드+후라이드"),
                () -> assertThat(menu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(18000)),
                () -> assertThat(menu.getMenuGroupId()).isNotNull(),
                () -> assertThat(menu.getMenuProducts()).extracting("seq").isNotNull(),
                () -> assertThat(menu.getMenuProducts()).extracting("menuId").contains(menu.getId()),
                () -> assertThat(menu.getMenuProducts()).extracting("productId").contains(savedProduct.getId()),
                () -> assertThat(menu.getMenuProducts()).extracting("quantity").contains(2L)
        );
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void findMenu() {
        // given
        final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("추천메뉴"));
        final Product savedProduct = productDao.save(new Product("후라이드", BigDecimal.valueOf(9000)));
        menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));

        // when
        List<Menu> findByMenus = menuService.list();

        // then
        assertThat(findByMenus.size()).isNotZero();
    }
}
