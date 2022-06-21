package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest{

    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuService menuService;

    private Product 후라이드_닭강정;
    private Product 양념_닭강정;
    private MenuProduct 후라이드_닭강정_메뉴상품;
    private MenuProduct 양념_닭강정_메뉴상품;
    private MenuGroup 닭강정_치킨_세트;

    @BeforeEach
    void setUp() {
        super.setUp();

        후라이드_닭강정 = this.productDao.save(new Product("후라이드 닭강정", BigDecimal.valueOf(4000)));
        양념_닭강정 = this.productDao.save(new Product("양념 닭강정", BigDecimal.valueOf(5000)));
        후라이드_닭강정_메뉴상품 = new MenuProduct(후라이드_닭강정.getId(), 2);
        양념_닭강정_메뉴상품 = new MenuProduct(양념_닭강정.getId(), 3);
        닭강정_치킨_세트 = this.menuGroupDao.save(new MenuGroup("닭강정_치킨_세트"));
    }

    @Test
    @DisplayName("가격이 1원이상으로 책정되어야 한다.")
    void createFail_price() {
        Menu 닭강정_메뉴 = new Menu("닭강정", BigDecimal.valueOf(-1), 닭강정_치킨_세트.getId(), Arrays.asList(후라이드_닭강정_메뉴상품, 양념_닭강정_메뉴상품));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.menuService.create(닭강정_메뉴));
    }

    @Test
    @DisplayName("속할 메뉴그룹이 지정되어야 한다.")
    void createFail_menuGroup() {
        Menu 닭강정_메뉴 = new Menu("닭강정", BigDecimal.valueOf(23000), null, Arrays.asList(후라이드_닭강정_메뉴상품, 양념_닭강정_메뉴상품));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.menuService.create(닭강정_메뉴));
    }

    @Test
    @DisplayName("메뉴에 속한 상품 가격의 합보다 같거나 작아야한다.")
    void createFail_comparePriceToProduct() {
        Menu 닭강정_메뉴 = new Menu("닭강정", BigDecimal.valueOf(24000), 닭강정_치킨_세트.getId(), Arrays.asList(후라이드_닭강정_메뉴상품, 양념_닭강정_메뉴상품));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.menuService.create(닭강정_메뉴));
    }

    @Test
    @DisplayName("메뉴 등록에 성공한다.")
    void create() {
        Menu 닭강정_메뉴 = new Menu("닭강정", BigDecimal.valueOf(22000), 닭강정_치킨_세트.getId(), Arrays.asList(후라이드_닭강정_메뉴상품, 양념_닭강정_메뉴상품));

        닭강정_메뉴 = this.menuService.create(닭강정_메뉴);

        assertThat(닭강정_메뉴.getId()).isNotNull();
        assertThat(닭강정_메뉴.getMenuProducts()).hasSize(2);
    }

    @Test
    @DisplayName("메뉴를 모두 조회한다.")
    void list() {
        Menu 닭강정_메뉴_A = new Menu("닭강정", BigDecimal.valueOf(22000), 닭강정_치킨_세트.getId(), Arrays.asList(후라이드_닭강정_메뉴상품, 양념_닭강정_메뉴상품));
        Menu 닭강정_메뉴_B = new Menu("닭강정", BigDecimal.valueOf(5000), 닭강정_치킨_세트.getId(), Collections.singletonList(후라이드_닭강정_메뉴상품));
        닭강정_메뉴_A = this.menuService.create(닭강정_메뉴_A);
        닭강정_메뉴_B = this.menuService.create(닭강정_메뉴_B);

        List<Menu> list = this.menuService.list();

        assertThat(list).containsExactly(닭강정_메뉴_A, 닭강정_메뉴_B);
    }

}
