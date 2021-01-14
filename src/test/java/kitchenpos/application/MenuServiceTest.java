package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 서비스에 관련한 기능")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;
    @InjectMocks
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
        Product 짬뽕 = new Product();
        짬뽕.setId(1L);
        짬뽕.setName("짬뽕");
        짬뽕.setPrice(new BigDecimal(8_000));
        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(짬뽕));
        given(menuDao.save(짬뽕_짜장면)).willReturn(짬뽕_짜장면);
        given(menuProductDao.save(menuProduct)).willReturn(menuProduct);
        // When
        Menu actual = menuService.create(짬뽕_짜장면);
        // Then
        assertThat(actual.getId()).isEqualTo(짬뽕_짜장면.getId());
        assertThat(actual.getName()).isEqualTo(짬뽕_짜장면.getName());
        assertThat(actual.getPrice()).isEqualTo(짬뽕_짜장면.getPrice());
        assertThat(actual.getMenuGroupId()).isEqualTo(짬뽕_짜장면.getMenuGroupId());
        assertThat(actual.getMenuProducts()).isEqualTo(짬뽕_짜장면.getMenuProducts());
    }

    @DisplayName("`메뉴`의 가격은 필수이고, 0원 이상이다.")
    @Test
    void priceRange() {
        // Given
        짬뽕_짜장면.setPrice(null);
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(짬뽕_짜장면));
        짬뽕_짜장면.setPrice(new BigDecimal(-1));
        assertThrows(IllegalArgumentException.class, () -> menuService.create(짬뽕_짜장면));
    }

    @DisplayName("`메뉴`의 가격은 `메뉴 상품`의 가격의 합보다 크면 안된다.")
    @Test
    void exceptionToCreateMenuWithInvalidPriceOverSum() {
        // Given
        Product 짬뽕 = new Product();
        짬뽕.setId(1L);
        짬뽕.setName("짬뽕");
        짬뽕.setPrice(new BigDecimal(5_000));
        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(productDao.findById(1L)).willReturn(Optional.of(짬뽕));
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(짬뽕_짜장면));
    }

    @DisplayName("모든 `메뉴` 목록을 조회한다.")
    @Test
    void findAllMenus() {
        // Given
        given(menuDao.findAll()).willReturn(Arrays.asList(짬뽕_짜장면));
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
