package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 서비스에 관련한 기능")
@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;

    private Menu 짬뽕_짜장면;
    private MenuProduct menuProduct;

    @BeforeEach
    void beforeEach() {
        menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        짬뽕_짜장면 = new Menu();
        짬뽕_짜장면.setId(1L);
        짬뽕_짜장면.setName("짬뽕_짜장면");
        짬뽕_짜장면.setMenuGroupId(1L);
        짬뽕_짜장면.setPrice(new BigDecimal(14_000));
        짬뽕_짜장면.setMenuProducts(Collections.singletonList(menuProduct));
    }

    @DisplayName("`메뉴`를 생성한다.")
    @Test
    void createMenu() {
        // Given
        ProductRequest productRequest1 = new ProductRequest("짬뽕", BigDecimal.valueOf(8_000));
        ProductResponse 짬뽕 = productService.create(productRequest1);
        ProductRequest productRequest2 = new ProductRequest("짜장면", BigDecimal.valueOf(6_000));
        ProductResponse 짜장면 = productService.create(productRequest2);
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(짬뽕.getId(), 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(짜장면.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("짬뽕_짜장면", BigDecimal.valueOf(14_000), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));

        // When
        Menu 짬뽕_짜장면2 = menuService.create(menuRequest);

        // Then
        assertAll(
                () -> assertThat(짬뽕_짜장면2.getId()).isNotNull(),
                () -> assertThat(짬뽕_짜장면2.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(짬뽕_짜장면2.getPrice().intValue()).isEqualTo(menuRequest.getPrice().intValue()),
                () -> assertThat(짬뽕_짜장면2.getMenuGroupId()).isEqualTo(menuRequest.getMenuGroupId()),
                () -> assertThat(짬뽕_짜장면2.getMenuProducts()).extracting(MenuProduct::getProductId)
                        .containsAnyElementsOf(Collections.singletonList(menuProductRequest1.getProductId())),
                () -> assertThat(짬뽕_짜장면2.getMenuProducts()).extracting(MenuProduct::getQuantity)
                        .containsAnyElementsOf(Collections.singletonList(menuProductRequest1.getQuantity()))
        );
    }

    @DisplayName("가격은 필수이고, 0원 이상이 아니면 `메뉴`를 생성할 수 없다.")
    @Test
    void exceptionToCreateMenuWithInvalidPrice() {
        // Given
        ProductRequest productRequest1 = new ProductRequest("짬뽕", BigDecimal.valueOf(8_000));
        ProductResponse 짬뽕 = productService.create(productRequest1);
        ProductRequest productRequest2 = new ProductRequest("짜장면", BigDecimal.valueOf(6_000));
        ProductResponse 짜장면 = productService.create(productRequest2);
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(짬뽕.getId(), 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(짜장면.getId(), 1L);
        MenuRequest invalidMenuRequest1 = new MenuRequest("짬뽕_짜장면", null, 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(invalidMenuRequest1));

        // Given
        MenuRequest invalidMenuRequest2 = new MenuRequest("짬뽕_짜장면", BigDecimal.valueOf(-1), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(invalidMenuRequest2));
    }

    @DisplayName("`메뉴`의 가격이 `메뉴 상품`의 가격의 합보다 크면 `메뉴`를 생성할 수 없다.")
    @Test
    void exceptionToCreateMenuWithInvalidPriceOverSum() {
        // Given
        ProductRequest productRequest1 = new ProductRequest("짬뽕", BigDecimal.valueOf(8_000));
        ProductResponse 짬뽕 = productService.create(productRequest1);
        ProductRequest productRequest2 = new ProductRequest("짜장면", BigDecimal.valueOf(6_000));
        ProductResponse 짜장면 = productService.create(productRequest2);
        MenuProductRequest menuProductRequest1 = new MenuProductRequest(짬뽕.getId(), 1L);
        MenuProductRequest menuProductRequest2 = new MenuProductRequest(짜장면.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("짬뽕_짜장면", BigDecimal.valueOf(15_000), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(menuRequest));
    }

    @DisplayName("모든 `메뉴` 목록을 조회한다.")
    @Test
    void findAllMenus() {
        // Given
        given(menuDao.findAll()).willReturn(Collections.singletonList(짬뽕_짜장면));

        // When
        List<Menu> actual = menuService.list();

        // Then
        assertAll(
                () -> assertThat(actual).extracting(Menu::getId).containsExactly(짬뽕_짜장면.getId()),
                () -> assertThat(actual).extracting(Menu::getName).containsExactly(짬뽕_짜장면.getName()),
                () -> assertThat(actual).extracting(Menu::getPrice).containsExactly(짬뽕_짜장면.getPrice()),
                () -> assertThat(actual).extracting(Menu::getMenuGroupId).containsExactly(짬뽕_짜장면.getMenuGroupId()),
                () -> assertThat(actual.stream().map(Menu::getMenuProducts).collect(Collectors.toList()))
                        .containsExactly(짬뽕_짜장면.getMenuProducts())
        );
    }
}
