package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
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
    private MenuDao menuDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuService menuService;

    private Product 후라이드;
    private Product 양념치킨;
    private MenuProduct 후라이드_메뉴상품;
    private MenuProduct 양념치킨_메뉴상품;
    private MenuGroup 두마리메뉴;

    @BeforeEach
    void setUp() {
        super.setUp();

        후라이드 = this.productDao.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        양념치킨 = this.productDao.save(new Product("양념치킨", BigDecimal.valueOf(16000)));
        후라이드_메뉴상품 = new MenuProduct(후라이드.getId(), 1);
        양념치킨_메뉴상품 = new MenuProduct(양념치킨.getId(), 1);
        두마리메뉴 = this.menuGroupDao.save(new MenuGroup("두마리메뉴"));
    }

    @Test
    @DisplayName("가격이 1원이상으로 책정되어야 한다.")
    void createFail_price() {
        Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(-1), 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.menuService.create(후라이드_양념_세트));
    }

    @Test
    @DisplayName("속할 메뉴그룹이 지정되어야 한다.")
    void createFail_menuGroup() {
        Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(32000), null, Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.menuService.create(후라이드_양념_세트));
    }

    @Test
    @DisplayName("메뉴에 속한 상품 가격의 합보다 같거나 작아야한다.")
    void createFail_comparePriceToProduct() {
        Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(33000), 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.menuService.create(후라이드_양념_세트));
    }

    @Test
    @DisplayName("메뉴 등록에 성공한다.")
    void create() {
        Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(31000), 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));

        후라이드_양념_세트 = this.menuService.create(후라이드_양념_세트);

        assertThat(후라이드_양념_세트.getId()).isNotNull();
        assertThat(후라이드_양념_세트.getMenuProducts()).hasSize(2);
    }

    @Test
    @DisplayName("메뉴를 모두 조회한다.")
    void list() {
        Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(31000), 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));
        Menu 임시_메뉴 = new Menu("닭강정", BigDecimal.valueOf(5000), 두마리메뉴.getId(), Collections.singletonList(후라이드_메뉴상품));
        후라이드_양념_세트 = this.menuDao.save(후라이드_양념_세트);
        임시_메뉴 = this.menuDao.save(임시_메뉴);
        후라이드_양념_세트.setMenuProducts(Collections.emptyList());
        임시_메뉴.setMenuProducts(Collections.emptyList());

        List<Menu> list = this.menuService.list();

        assertThat(list).containsExactly(후라이드_양념_세트, 임시_메뉴);
    }

}
