package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
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

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    private MenuGroup createMenuGroup;
    private Product createdProduct;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("양식");
        createMenuGroup = menuGroupService.create(menuGroup);

        Product product = new Product();
        product.setName("파스타");
        product.setPrice(BigDecimal.valueOf(12_000));
        createdProduct = productService.create(product);
    }

    @Test
    @DisplayName("menu 생성")
    void menu_create_test() {
        //given
        Menu menuRequest = MENU_REQUEST_생성("파스타", 12_000);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(createdProduct.getId());
        menuProduct.setQuantity(1L);
        menuRequest.setMenuProducts(Collections.singletonList(menuProduct));

        //when
        Menu createdMenu = MENU_생성_테스트(menuRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdMenu.getId()).isNotNull();
            assertThat(createdMenu.getName()).isEqualTo(menuRequest.getName());
        });
    }

    @Test
    @DisplayName("menu의 price는 0 원 이상이어야 한다.")
    void menu_create_price_null_test() {
        //given
        Menu menuRequest = MENU_REQUEST_생성("파스타", null);

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
        Menu menuRequest = MENU_REQUEST_생성("파스타", 14_000);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(createdProduct.getId());
        menuProduct.setQuantity(1L);
        menuRequest.setMenuProducts(Collections.singletonList(menuProduct));


        //when
        //then
        assertThatThrownBy(() -> {
            Menu createdMenu = MENU_생성_테스트(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);


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
