package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
    public static final int DEFAULT_PRICE = 12_000;
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    private MenuGroup createMenuGroup;
    private Product createdProduct1;
    private Product createdProduct2;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(양식);
        createMenuGroup = menuGroupService.create(menuGroup);

        Product product = new Product();
        product.setName(알리오올리오);
        product.setPrice(BigDecimal.valueOf(DEFAULT_PRICE));
        createdProduct1 = productService.create(product);

        Product product2 = new Product();
        product2.setName(까르보나라);
        product2.setPrice(BigDecimal.valueOf(DEFAULT_PRICE));
        createdProduct2 = productService.create(product2);
    }

    @Test
    @DisplayName("menu 생성")
    void menu_create_test() {
        //given
        Menu menuRequest = MENU_REQUEST_생성(알리오올리오, DEFAULT_PRICE);
        MenuProduct menuProduct = MENU_PRODUCT_생성(createdProduct1.getId(), 1L);
        menuRequest.setMenuProducts(Collections.singletonList(menuProduct));
        //when
        Menu createdMenu = MENU_생성_테스트(menuRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdMenu.getId()).isNotNull();
            assertThat(createdMenu.getName()).isEqualTo(menuRequest.getName());
        });
    }

    private MenuProduct MENU_PRODUCT_생성(Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    @Test
    @DisplayName("menu의 price는 0 원 이상이어야 한다.")
    void menu_create_price_null_test() {
        //given
        Menu menuRequest = MENU_REQUEST_생성(알리오올리오, null);

        //when
        //then
        assertThatThrownBy(() -> {
            Menu createdMenu = MENU_생성_테스트(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    @DisplayName("menu의 price는 해당 menu에 속한 product price의 sum보다 크면 안된다.")
    void menu_price_smaller_than_product_price_test() {
        //given
        Menu menuRequest = MENU_REQUEST_생성(알리오올리오, DEFAULT_PRICE + 2_000);
        MenuProduct menuProduct = MENU_PRODUCT_생성(createdProduct1.getId(), 1L);
        menuRequest.setMenuProducts(Collections.singletonList(menuProduct));

        //when
        //then
        assertThatThrownBy(() -> {
            Menu createdMenu = MENU_생성_테스트(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    @DisplayName("menu 리스트 조회")
    public void menu_show_test() throws Exception {
        //Given
        Menu menuRequest = MENU_REQUEST_생성(알리오올리오, DEFAULT_PRICE);
        MenuProduct menuProduct = MENU_PRODUCT_생성(createdProduct1.getId(), 1L);
        menuRequest.setMenuProducts(Collections.singletonList(menuProduct));
        Menu createdMenu1 = MENU_생성_테스트(menuRequest);

        Menu menuRequest2 = MENU_REQUEST_생성(까르보나라, DEFAULT_PRICE);
        MenuProduct menuProduct2 = MENU_PRODUCT_생성(createdProduct2.getId(), 1L);
        menuRequest2.setMenuProducts(Collections.singletonList(menuProduct2));
        Menu createdMenu2 = MENU_생성_테스트(menuRequest2);

        //When
        List<Menu> list = menuService.list();

        //then
        assertThat(list)
            .extracting(Menu::getName)
            .containsExactly(createdMenu1.getName(),createdMenu2.getName());
    }

    private Menu MENU_생성_테스트(Menu menuRequest) {
        return menuService.create(menuRequest);
    }

    private Menu MENU_REQUEST_생성(String name, Integer price) {
        Menu menu = new Menu();
        menu.setName(name);
        if (price != null) {
            menu.setPrice(BigDecimal.valueOf(price));
        }
        menu.setMenuGroupId(createMenuGroup.getId());
        return menu;
    }


}
