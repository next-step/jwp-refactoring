package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private MenuService menuService;
    private Menu menu;
    private Product product1;
    private Product product2;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @BeforeEach
    void setUp() {

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(2L);
        menuGroup.setName("한마리 메뉴");

        product1 = new Product();
        product1.setId(1L);
        product1.setName("후라이드");
        product1.setPrice(BigDecimal.valueOf(16000));

        product2 = new Product();
        product2.setId(2L);
        product2.setName("양념치킨");
        product2.setPrice(BigDecimal.valueOf(16000));

        menu = new Menu();
        menu.setId(1L);
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuGroupId(2L);
        menu.setName("후라이드 치킨");

        menuProduct1 = new MenuProduct();
        menuProduct1.setSeq(1L);
        menuProduct1.setMenuId(menu.getId());
        menuProduct1.setProductId(product1.getId());
        menuProduct1.setQuantity(20);

        menuProduct2 = new MenuProduct();
        menuProduct2.setSeq(2L);
        menuProduct2.setMenuId(menu.getId());
        menuProduct2.setProductId(product2.getId());
        menuProduct2.setQuantity(30);

        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @Test
    @DisplayName("가격이 0보다 작은 경우 예외가 발생한다")
    void notValidPriceTest() {

        // given
        menu.setPrice(BigDecimal.valueOf(-1));

        // then
        Assertions.assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹에 속하지 않은 메뉴인 경우 예외가 발생한다")
    void notExistMenuTest() {

        // given
        when(menuGroupDao.existsById(any())).thenReturn(false);

        // then
        Assertions.assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 포함된 메뉴 상품이 존재하지 않는 상품인 경우 예외가 발생한다")
    void notExistProductTest() {

        // given
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 금액이 메뉴 상품 금액의 총합 보다 큰 경우 예외가 발생한다")
    void notValidProductPriceTest() {

        // given
        menu.setPrice(BigDecimal.valueOf(1000000000000L));

        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(product1));
        when(productDao.findById(any())).thenReturn(Optional.of(product2));

        // then
        Assertions.assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void createTest() {

        // given
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(product1));
        when(productDao.findById(any())).thenReturn(Optional.of(product2));
        when(menuDao.save(any())).thenReturn(menu);

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu.getId()).isEqualTo(menu.getId());
        assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
        assertThat(savedMenu.getName()).isEqualTo(menu.getName());
        assertThat(savedMenu.getPrice()).isEqualTo(menu.getPrice());
        assertThat(savedMenu.getMenuProducts()).hasSize(menu.getMenuProducts().size());
        assertThat(savedMenu.getMenuProducts()).hasSameElementsAs(menu.getMenuProducts());
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다")
    void listTest() {

        // given
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        when(menuDao.findAll()).thenReturn(Collections.singletonList(menu));
        when(menuProductDao.findAllByMenuId(menu.getId())).thenReturn(menuProducts);

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
        assertThat(menus).containsExactly(menu);
    }
}