package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class MenuServiceTest {

    public static final String 알리오올리오 = "알리오올리오";
    public static final String 양식 = "양식";
    public static final String 까르보나라 = "까르보나라";
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(12_000);
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    private MenuGroupResponse createMenuGroup;
    private ProductResponse createdProduct1;
    private ProductResponse createdProduct2;
    private MenuProductRequest menuProduct;
    private MenuProductRequest menuProduct2;

    @BeforeEach
    void setUp() {
        MenuGroupRequest menuGroup = new MenuGroupRequest(양식);
        createMenuGroup = menuGroupService.create(menuGroup);

        ProductRequest product = new ProductRequest(알리오올리오, DEFAULT_PRICE);
        createdProduct1 = productService.create(product);

        ProductRequest product2 = new ProductRequest(까르보나라, DEFAULT_PRICE);
        createdProduct2 = productService.create(product2);

        menuProduct = MENU_PRODUCT_생성(createdProduct1.getId(), 1L);
        menuProduct2 = MENU_PRODUCT_생성(createdProduct2.getId(), 1L);

    }

    @Test
    @DisplayName("menu 생성")
    void menu_create_test() {
        //given
        MenuRequest menuRequest = MENU_REQUEST_생성(알리오올리오, DEFAULT_PRICE, menuProduct);
        //when
        MenuResponse createdMenu = MENU_생성_테스트(menuRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdMenu.getId()).isNotNull();
            assertThat(createdMenu.getName()).isEqualTo(menuRequest.getName());
        });
    }

    private MenuProductRequest MENU_PRODUCT_생성(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    @Test
    @DisplayName("menu의 price는 0 원 이상이어야 한다.")
    void menu_create_price_0_test() {
        //given
        MenuRequest menuRequest = MENU_REQUEST_생성(알리오올리오, BigDecimal.valueOf(0), menuProduct);

        //when
        MenuResponse createdMenu = MENU_생성_테스트(menuRequest);
        //then
        assertThat(createdMenu.getId()).isNotNull();
        assertThat(createdMenu.getPrice()).isZero();


    }

    @Test
    @DisplayName("menu의 price는 NULL일경우 실패한다.")
    void menu_create_price_null_test() {
        //given
        MenuRequest menuRequest = MENU_REQUEST_생성(알리오올리오, null, menuProduct);

        //when
        //then
        assertThatThrownBy(() -> {
            MenuResponse createdMenu = MENU_생성_테스트(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    @DisplayName("menu의 price는 해당 menu에 속한 product price의 sum보다 크면 안된다.")
    void menu_price_smaller_than_product_price_test() {
        //given
        MenuRequest menuRequest = MENU_REQUEST_생성(알리오올리오, DEFAULT_PRICE.add(BigDecimal.valueOf(2_000)), menuProduct);

        //when
        //then
        assertThatThrownBy(() -> {
            MenuResponse createdMenu = MENU_생성_테스트(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    @DisplayName("menu 리스트 조회")
    void menu_show_test() throws Exception {
        //Given
        MenuRequest menuRequest = MENU_REQUEST_생성(알리오올리오, DEFAULT_PRICE, menuProduct);
        MenuResponse createdMenu1 = MENU_생성_테스트(menuRequest);

        MenuRequest menuRequest2 = MENU_REQUEST_생성(까르보나라, DEFAULT_PRICE, menuProduct);
        MenuResponse createdMenu2 = MENU_생성_테스트(menuRequest2);

        //When
        List<MenuResponse> list = menuService.list();

        //then
        assertThat(list)
            .extracting(MenuResponse::getName)
            .containsExactly(createdMenu1.getName(), createdMenu2.getName());
        assertThat(list)
            .extracting(MenuResponse::getMenuProducts)
            .isNotNull();
    }

    private MenuResponse MENU_생성_테스트(MenuRequest menuRequest) {
        return menuService.create(menuRequest);
    }

    private MenuRequest MENU_REQUEST_생성(String name, BigDecimal price, MenuProductRequest menuProduct) {
        return new MenuRequest(name, price, createMenuGroup.getId(), Arrays.asList(menuProduct));
    }


}
