package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuService menuService;

    private Product 후라이드;
    private Product 양념치킨;
    private MenuProduct 후라이드_메뉴상품;
    private MenuProduct 양념치킨_메뉴상품;
    private MenuGroup 두마리메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        후라이드 = this.productRepository.save(new Product("후라이드", BigDecimal.valueOf(16000)));
        양념치킨 = this.productRepository.save(new Product("양념치킨", BigDecimal.valueOf(16000)));
        후라이드_메뉴상품 = new MenuProduct(후라이드, 1);
        양념치킨_메뉴상품 = new MenuProduct(양념치킨, 1);
        두마리메뉴 = this.menuGroupRepository.save(new MenuGroup("두마리메뉴"));
    }

    @Test
    @DisplayName("메뉴 등록에 성공한다.")
    void create() {
        Menu 후라이드_양념_세트 =
            new Menu("후라이드_양념_세트", BigDecimal.valueOf(31000), 두마리메뉴.getId(), Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품));

        MenuResponse menuResponse = this.menuService.create(MenuRequest.of(후라이드_양념_세트));

        assertThat(menuResponse.getId()).isNotNull();
        assertThat(menuResponse.getMenuProducts()).hasSize(2);
    }

    @TestFactory
    @DisplayName("메뉴 등록시 값들의 유효성 검사에 따라 에러처리를 확인한다.")
    Stream<DynamicTest> createFail_menuGroup() {
        List<MenuProductRequest> menuProducts = Arrays.asList(MenuProductRequest.of(후라이드_메뉴상품), MenuProductRequest.of(양념치킨_메뉴상품));
        return Stream.of(
            DynamicTest.dynamicTest("메뉴 이름이 없는 경우", () -> {
                메뉴그룹_생성_실패(null, BigDecimal.valueOf(31000), 두마리메뉴.getId(), menuProducts, NullPointerException.class);
            }),
            DynamicTest.dynamicTest("가격이 없는 경우", () -> {
                메뉴그룹_생성_실패("후라이드_양념_세트", null, 두마리메뉴.getId(), menuProducts, NullPointerException.class);
            }),
            DynamicTest.dynamicTest("메뉴 그룹이 없는 경우", () -> {
                메뉴그룹_생성_실패("후라이드_양념_세트", BigDecimal.valueOf(31000), null, menuProducts, NullPointerException.class);
            }),
            DynamicTest.dynamicTest("상품이 없는 경우", () -> {
                메뉴그룹_생성_실패("후라이드_양념_세트", BigDecimal.valueOf(31000), 두마리메뉴.getId(), null, NullPointerException.class);
            }),
            DynamicTest.dynamicTest("가격이 0원 미만일 경우", () -> {
                메뉴그룹_생성_실패("후라이드_양념_세트", BigDecimal.valueOf(-1), 두마리메뉴.getId(), menuProducts, IllegalArgumentException.class);
            }),
            DynamicTest.dynamicTest("상품의 가격 합보다 메뉴의 가격이 클 경우", () -> {
                메뉴그룹_생성_실패("후라이드_양념_세트", BigDecimal.valueOf(33000), 두마리메뉴.getId(), menuProducts, IllegalArgumentException.class);
            })
        );
    }

    @Test
    @DisplayName("메뉴를 모두 조회한다.")
    void list() {
        List<MenuProduct> menuProducts = Arrays.asList(후라이드_메뉴상품, 양념치킨_메뉴상품);
        Menu 후라이드_양념_세트 = new Menu("후라이드_양념_세트", BigDecimal.valueOf(0), 두마리메뉴.getId(), menuProducts);
        Menu menu = this.menuRepository.save(후라이드_양념_세트);

        List<MenuResponse> menuResponses = this.menuService.list();

        assertThat(menuResponses).contains(MenuResponse.of(menu));
    }

    private void 메뉴그룹_생성_실패(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductRequest> menuProductRequests, Class<? extends Exception> exception) {
        MenuRequest menuRequest = new MenuRequest(name, price, menuGroupId, menuProductRequests);

        assertThatExceptionOfType(exception)
            .isThrownBy(() -> this.menuService.create(menuRequest));
    }

}
