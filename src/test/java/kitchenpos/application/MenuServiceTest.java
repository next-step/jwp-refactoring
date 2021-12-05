package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * - 메뉴를 등록할 수 있다
 * - 메뉴의 가격이 올바르지 않으면 등록할 수 없다.
 *     - 메뉴의 가격은 존재해야 한다
 *     - 메뉴의 가격은 0원 이상이어야한다.
 * - 메뉴의 메뉴 그룹이 존재하지 않으면 등록할 수 없다.
 * - 메뉴 상품의 상품이 존재하지 않으면 등록할 수 없다
 * - 메뉴 상품의 가격은 상품의 가격과 메뉴 상품의 가격의 곱이다
 * - 메뉴의 가격이 올바르지 않으면 등록 할 수 없다
 *     - 메뉴의 가격은 메뉴 상품의 가격의 합보다 작아야 한다.
 * - 메뉴 목록을 조회할 수 있다
 */
class MenuServiceTest {

    private static final int 수량 = 2;
    private static final String 메뉴_이름 = "후라이드+후라이드";
    private static final BigDecimal 메뉴_가격 = new BigDecimal(19_000);
    private static final MenuGroup 메뉴_그룹 = 메뉴_그룹();
    private static final Product 상품 = 상품("강정치킨", new BigDecimal(17_000));

    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;
    private MenuProductDao menuProductDao;
    private ProductDao productDao;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuDao = new InMemoryMenuDao();
        menuGroupDao = new InMemoryMenuGroupDao();
        menuProductDao = new InMemoryMenuProductDao();
        productDao = new InMemoryProductDao();
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @Test
    void create_메뉴를_등록할_수_있다() {
        MenuGroup 저장된_메뉴_그룹 = menuGroupDao.save(메뉴_그룹);
        Product 저장된_상품 = productDao.save(상품);

        Menu savedMenu = menuService.create(메뉴(저장된_메뉴_그룹, 메뉴_이름, 메뉴_가격, 메뉴_상품(저장된_상품, 수량)));

        assertAll(
                () -> assertThat(savedMenu.getPrice()).isEqualTo(메뉴_가격),
                () -> assertThat(savedMenu.getName()).isEqualTo(메뉴_이름),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(저장된_메뉴_그룹.getId()),
                () -> assertThat(savedMenu.getMenuProducts().size()).isEqualTo(1)
        );
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1"})
    void create_메뉴의_가격이_올바르지_않으면_등록할_수_없다(BigDecimal 올바르지_않은_가격) {
        MenuGroup 저장된_메뉴_그룹 = menuGroupDao.save(메뉴_그룹);
        Product 저장된_상품 = productDao.save(상품);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(메뉴(저장된_메뉴_그룹, 메뉴_이름, 올바르지_않은_가격, 메뉴_상품(저장된_상품, 수량))));
    }

    private static Menu 메뉴(long menuGroupId, String name, BigDecimal price, MenuProduct menuProduct) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Arrays.asList(menuProduct));
        return menu;
    }

    private static Menu 메뉴(MenuGroup menuGroup, String name, BigDecimal price, MenuProduct menuProduct) {
        return 메뉴(menuGroup.getId(), name, price, menuProduct);
    }

    private static MenuProduct 메뉴_상품(Product savedProduct, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    private static MenuGroup 메뉴_그룹() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        return menuGroup;
    }

    private static Product 상품(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}