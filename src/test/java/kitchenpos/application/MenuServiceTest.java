package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

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
    @DisplayName("메뉴 등록에 성공한다.")
    void create() {
        Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(31000), 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));

        후라이드_양념_세트 = this.menuService.create(후라이드_양념_세트);

        assertThat(후라이드_양념_세트.getId()).isNotNull();
        assertThat(후라이드_양념_세트.getMenuProducts()).hasSize(2);
    }

    @TestFactory
    @DisplayName("메뉴 등록시 값들의 유효성 검사에 따라 에러처리를 확인한다.")
    Stream<DynamicTest> createFail_menuGroup() {
        return Stream.of(
            DynamicTest.dynamicTest("메뉴 이름이 없는 경우", () -> {
                Menu 후라이드_양념_세트 = new Menu(null, BigDecimal.valueOf(31000), 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));
                메뉴그룹_생성_실패(후라이드_양념_세트, DataIntegrityViolationException.class);
            }),
            DynamicTest.dynamicTest("가격이 없는 경우", () -> {
                Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", null, 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));
                메뉴그룹_생성_실패(후라이드_양념_세트, IllegalArgumentException.class);
            }),
            DynamicTest.dynamicTest("메뉴 그룹이 없는 경우", () -> {
                Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(31000), null, Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));
                메뉴그룹_생성_실패(후라이드_양념_세트, IllegalArgumentException.class);
            }),
            DynamicTest.dynamicTest("상품이 없는 경우", () -> {
                Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(31000), 두마리메뉴.getId(), null);
                메뉴그룹_생성_실패(후라이드_양념_세트, NullPointerException.class);
            }),
            DynamicTest.dynamicTest("가격이 0원 미만일 경우", () -> {
                Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(-1), 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));
                메뉴그룹_생성_실패(후라이드_양념_세트, IllegalArgumentException.class);
            }),
            DynamicTest.dynamicTest("상품의 가격 합보다 메뉴의 가격이 클 경우", () -> {
                Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(33000), 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));
                메뉴그룹_생성_실패(후라이드_양념_세트, IllegalArgumentException.class);
            })
        );
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



    private void 메뉴그룹_생성_실패(Menu menu, Class<? extends Exception> exception) {
        assertThatExceptionOfType(exception)
            .isThrownBy(() -> this.menuService.create(menu));
    }

}
