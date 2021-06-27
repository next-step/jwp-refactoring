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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuService 클래스")
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

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        @Nested
        @DisplayName("메뉴가 주어지면")
        class Context_with_menu {
            final Menu givenMenu = new Menu();

            @BeforeEach
            void setUp() {
                MenuProduct menuProduct1 = new MenuProduct();
                menuProduct1.setQuantity(1);
                MenuProduct menuProduct2 = new MenuProduct();
                menuProduct2.setQuantity(1);

                Product product1 = new Product();
                product1.setPrice(BigDecimal.valueOf(1000));
                Product product2 = new Product();
                product2.setPrice(BigDecimal.valueOf(1000));

                givenMenu.setPrice(BigDecimal.valueOf(1000));
                givenMenu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

                when(menuGroupDao.existsById(any()))
                        .thenReturn(true);
                when(productDao.findById(any()))
                        .thenReturn(Optional.of(product1));
                when(productDao.findById(any()))
                        .thenReturn(Optional.of(product2));
                when(menuDao.save(any(Menu.class)))
                        .thenReturn(givenMenu);
            }

            @Test
            @DisplayName("주어진 메뉴를 저장하고, 저장된 객체를 리턴한다.")
            void it_returns_saved_menu() {
                Menu actual = menuService.create(givenMenu);

                assertThat(actual).isEqualTo(givenMenu);
            }
        }

        @Nested
        @DisplayName("메뉴의 가격이 없이 메뉴가 주어지면")
        class Context_with_no_price {
            final Menu givenMenu = new Menu();

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> menuService.create(givenMenu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("메뉴의 가격이 음수인 메뉴가 주어지면")
        class Context_with_negative_price {
            final Menu givenMenu = new Menu();

            @BeforeEach
            void setUp() {
                givenMenu.setPrice(BigDecimal.valueOf(-1000));
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> menuService.create(givenMenu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("메뉴 그룹에 포함되지 않은 메뉴가 주어지면")
        class Context_with_not_include_menu_group {
            final Menu givenMenu = new Menu();

            @BeforeEach
            void setUp() {
                givenMenu.setPrice(BigDecimal.valueOf(1000));
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> menuService.create(givenMenu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("메뉴에 포함된 상품이 존재하지 않게 주어지면")
        class Context_with_menu_and_not_exist_products {
            final Menu givenMenu = new Menu();

            @BeforeEach
            void setUp() {
                MenuProduct menuProduct1 = new MenuProduct();
                menuProduct1.setQuantity(1);
                MenuProduct menuProduct2 = new MenuProduct();
                menuProduct2.setQuantity(1);

                givenMenu.setPrice(BigDecimal.valueOf(1000));
                givenMenu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

                when(menuGroupDao.existsById(any()))
                        .thenReturn(true);
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> menuService.create(givenMenu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("메뉴에 포함된 상품 합이 올바르지 않게 주어지면")
        class Context_with_menu_and_invalid_price {
            final Menu givenMenu = new Menu();

            @BeforeEach
            void setUp() {
                MenuProduct menuProduct1 = new MenuProduct();
                menuProduct1.setQuantity(1);
                MenuProduct menuProduct2 = new MenuProduct();
                menuProduct2.setQuantity(1);

                Product product1 = new Product();
                product1.setPrice(BigDecimal.valueOf(1000));
                Product product2 = new Product();
                product2.setPrice(BigDecimal.valueOf(1000));

                givenMenu.setPrice(BigDecimal.valueOf(3000));
                givenMenu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

                when(menuGroupDao.existsById(any()))
                        .thenReturn(true);
                when(productDao.findById(any()))
                        .thenReturn(Optional.of(product1));
                when(productDao.findById(any()))
                        .thenReturn(Optional.of(product2));
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> menuService.create(givenMenu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {

        @Nested
        @DisplayName("저장된 메뉴 목록이 주어지면")
        class Context_with_menu_groups {
            final Menu givenMenu1 = new Menu();
            final Menu givenMenu2 = new Menu();

            @BeforeEach
            void setUp() {
                when(menuDao.findAll())
                        .thenReturn(Arrays.asList(givenMenu1, givenMenu2));
            }

            @Test
            @DisplayName("메뉴 목록을 리턴한다.")
            void it_returns_menus() {
                List<Menu> actual = menuService.list();
                assertThat(actual).containsExactly(givenMenu1, givenMenu2);
            }
        }
    }
}
