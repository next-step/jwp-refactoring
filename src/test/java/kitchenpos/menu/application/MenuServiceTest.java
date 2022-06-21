package kitchenpos.menu.application;

import static kitchenpos.helper.Converter.convert;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayName("메뉴 관련 Service 기능 테스트")
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    private MenuGroup 신메뉴;
    private MenuGroup 없는메뉴;
    private Product 양념치킨상품;
    private Product 반반치킨상품;
    private MenuProduct 양념치킨;
    private MenuProduct 반반치킨;

    @BeforeEach
    void setUp() {
        신메뉴   = new MenuGroup(4L, "신메뉴");
        없는메뉴 = new MenuGroup(5L, "없는메뉴");

        양념치킨상품 = new Product(2L, "양념치킨", convert(16000));
        반반치킨상품 = new Product(3L, "반반치킨", convert(16000));
        양념치킨 = new MenuProduct(null, 양념치킨상품.getId(), 2);
        반반치킨 = new MenuProduct(null, 반반치킨상품.getId(), 1);
    }


    @Order(1)
    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //given
        Menu request = new Menu(null, "양반치킨", convert(10000), 신메뉴.getId(), Arrays.asList(양념치킨, 반반치킨));

        //when
        Menu result = menuService.create(request);

        //then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getMenuProducts().get(0).getMenuId()).isEqualTo(result.getId()),
                () -> assertThat(result.getMenuProducts().get(1).getMenuId()).isEqualTo(result.getId())
        );
    }

    @Order(2)
    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {

        //when
        List<Menu> results = menuService.list();

        //then
        assertThat(results.stream().map(Menu::getName).collect(Collectors.toList()))
                .contains("양반치킨");

    }

    @DisplayName("메뉴 그룹이 등록 되어있지 않은 경우 메뉴를 등록 할 수 없다.")
    @Test
    void create_empty_menu_group_id() {
        //given
        Menu request = new Menu(null, "양반치킨", convert(10000), 없는메뉴.getId(), Arrays.asList(양념치킨, 반반치킨));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->  menuService.create(request));

    }

    @DisplayName("메뉴 가격이 금액(가격 * 수량)보다 큰 경우 메뉴를 등록할 수 없다.")
    @Test
    void create_price_greater_than_amount() {
        //given
        Menu request = new Menu(null, "양반치킨 v2", convert(50000), 신메뉴.getId(), Arrays.asList(양념치킨, 반반치킨));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() ->  menuService.create(request));

    }

    @DisplayName("메뉴 가격이 null 이거나 0원 미만이면 메뉴를 등록할 수 없다.")
    @Test
    void create_price_null_or_less_then_zero() {
        //given
        Menu request_price_less_then_zero = new Menu(null, "양반치킨 v2", convert(-1), 신메뉴.getId(), Arrays.asList(양념치킨, 반반치킨));
        Menu request_price_null = new Menu(null, "양반치킨 v2", null, 신메뉴.getId(), Arrays.asList(양념치킨, 반반치킨));

        //when then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request_price_less_then_zero)),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(request_price_null))
        );

    }

}
