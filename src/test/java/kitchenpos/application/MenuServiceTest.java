package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuService 클래스 테스트")
public class MenuServiceTest {
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

    @Nested
    @DisplayName("create 메서드 테스트")
    public class CreateMethod {
        @Nested
        @DisplayName("메뉴 등록 성공")
        public class Success {
            @Test
            public void testCase() {
                // given
                final Menu mockMenu = setup();

                // when
                final Menu createdMenu = menuService.create(mockMenu);

                // then
                assertAll(
                        () -> assertThat(createdMenu.getName()).isEqualTo(mockMenu.getName()),
                        () -> assertThat(createdMenu.getPrice()).isEqualTo(mockMenu.getPrice()),
                        () -> assertThat(createdMenu.getMenuGroupId()).isEqualTo(mockMenu.getMenuGroupId()),
                        () -> assertAllMenuProducts(createdMenu, mockMenu)
                );
            }

            private Menu setup() {
                final long menuGroupId = 1L;
                Mockito.when(menuGroupDao.existsById(menuGroupId)).thenReturn(true);
                final List<Long> productsId =
                        Stream.of(11, 12, 13).map(Long::new).collect(Collectors.toList());
                final List<Product> products = productsId
                        .stream()
                        .map(productId -> {
                            final Product product = new Product();
                            product.setId(productId);
                            product.setPrice(BigDecimal.valueOf(100 * productId));
                            Mockito.when(productDao.findById(productId)).thenReturn(Optional.of(product));
                            return product;
                        })
                        .collect(Collectors.toList());
                final long menuId = 3L;
                final List<MenuProduct> menuProducts = new ArrayList<>();
                for (int i = 0; i < products.size(); ++i) {
                    final Product product = products.get(i);
                    final MenuProduct menuProduct = new MenuProduct();
                    menuProduct.setSeq((long) i + 1);
                    menuProduct.setMenuId(menuId);
                    menuProduct.setProductId(product.getId());
                    menuProduct.setQuantity(product.getId() - 10);
                    menuProducts.add(menuProduct);
                }
                BigDecimal sumPrice = BigDecimal.ZERO;
                for (Product product : products) {
                    final MenuProduct menuProduct = menuProducts
                            .stream()
                            .filter(x -> x.getProductId().equals(product.getId()))
                            .findFirst()
                            .get();
                    sumPrice = sumPrice.add(
                            product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
                }
                return mockMenu(menuId, "test menu", sumPrice, menuGroupId, menuProducts);
            }

            private Menu mockMenu(long menuId, String name, BigDecimal price, long menuGroupId,
                                  List<MenuProduct> menuProducts) {
                final Menu menu = new Menu();
                menu.setId(menuId);
                menu.setName(name);
                menu.setPrice(price);
                menu.setMenuGroupId(menuGroupId);
                menu.setMenuProducts(menuProducts);
                Mockito.when(menuDao.save(Mockito.any())).thenReturn(menu);
                return menu;
            }

            private void assertAllMenuProducts(Menu assertTarget, Menu assertOrigin) {
                final List<MenuProduct> targetMenuProducts = assertTarget.getMenuProducts();
                final List<MenuProduct> originMenuProducts = assertOrigin.getMenuProducts();
                assertThat(targetMenuProducts.size()).isEqualTo(originMenuProducts.size());
                targetMenuProducts.forEach(
                        targetMenuProduct -> assertThat(originMenuProducts.contains(targetMenuProduct)));
            }
        }

        @Nested
        @DisplayName("가격을 입력하지 않으면 메뉴 등록 실패")
        public class ErrorPriceNull {
            @Test
            public void testCase() {
                // given
                final Menu menu = setup();

                // when - then
                assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
            }

            private Menu setup() {
                final Menu menu = new Menu();
                menu.setPrice(null);
                return menu;
            }
        }

        @Nested
        @DisplayName("가격을 음수이면 메뉴 등록 실패")
        public class ErrorPriceNegative {
            @ParameterizedTest
            @ValueSource(ints = {Integer.MIN_VALUE, -2, -1})
            public void testCase(int priceValue) {
                // given
                final Menu menu = setup(priceValue);

                // when - then
                assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
            }

            private Menu setup(int priceValue) {
                final Menu menu = new Menu();
                menu.setPrice(BigDecimal.valueOf(priceValue));
                return menu;
            }
        }

        @Nested
        @DisplayName("지정한 메뉴 그룹이 없으면 메뉴 등록 실패")
        public class ErrorMenuGroupDoesNotExist {
            @Test
            public void testCase() {
                // given
                final Menu menu = setup();

                // when - then
                assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
            }

            private Menu setup() {
                final long menuGroupId = 1L;
                Mockito.when(menuGroupDao.existsById(menuGroupId)).thenReturn(false);
                final Menu menu = new Menu();
                menu.setPrice(BigDecimal.valueOf(1234));
                menu.setMenuGroupId(menuGroupId);
                return menu;
            }
        }

        @Nested
        @DisplayName("메뉴 가격이 포함된 상품의 총합보다 크면 메뉴 등록 실패")
        public class ErrorMenuPriceExceedsSumOfProductPrice {
            @Test
            public void testCase() {
                // given
                final Menu menu = setup();

                // when - then
                assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
            }

            private Menu setup() {
                final long menuGroupId = 1L;
                Mockito.when(menuGroupDao.existsById(menuGroupId)).thenReturn(true);
                final List<Long> productsId =
                        Stream.of(11, 12, 13).map(Long::new).collect(Collectors.toList());
                final List<Product> products = productsId
                        .stream()
                        .map(productId -> {
                            final Product product = new Product();
                            product.setId(productId);
                            product.setPrice(BigDecimal.valueOf(100 * productId));
                            Mockito.when(productDao.findById(productId)).thenReturn(Optional.of(product));
                            return product;
                        })
                        .collect(Collectors.toList());
                final long menuId = 3L;
                final List<MenuProduct> menuProducts = new ArrayList<>();
                for (int i = 0; i < products.size(); ++i) {
                    final Product product = products.get(i);
                    final MenuProduct menuProduct = new MenuProduct();
                    menuProduct.setSeq((long) i + 1);
                    menuProduct.setMenuId(menuId);
                    menuProduct.setProductId(product.getId());
                    menuProduct.setQuantity(product.getId() - 10);
                    menuProducts.add(menuProduct);
                }
                BigDecimal sumPrice = BigDecimal.ZERO;
                for (Product product : products) {
                    final MenuProduct menuProduct = menuProducts
                            .stream()
                            .filter(x -> x.getProductId().equals(product.getId()))
                            .findFirst()
                            .get();
                    sumPrice = sumPrice.add(
                            product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
                }
                final BigDecimal price = sumPrice.add(BigDecimal.valueOf(1000));
                return mockMenu(menuId, "test menu", price, menuGroupId, menuProducts
                );
            }

            private Menu mockMenu(long menuId, String name, BigDecimal price, long menuGroupId,
                                  List<MenuProduct> menuProducts) {
                final Menu menu = new Menu();
                menu.setId(menuId);
                menu.setName(name);
                menu.setPrice(price);
                menu.setMenuGroupId(menuGroupId);
                menu.setMenuProducts(menuProducts);
                return menu;
            }
        }
    }

    @Nested
    @DisplayName("list 메서드 테스트")
    public class ListMethod {
        @Nested
        @DisplayName("메뉴 목록 조회 성공")
        public class Success {
            @Test
            public void testCase() {
                // given
                final List<Long> menuIds = Stream.of(1, 2, 3).map(Long::new).collect(Collectors.toList());
                final List<Long> productIds = Stream.of(11, 12, 13).map(Long::new).collect(Collectors.toList());
                setup(menuIds, productIds);

                // when
                final List<Menu> menus = menuService.list();

                // then
                assertAll(
                        () -> assertThat(menus.size()).isEqualTo(menuIds.size()),
                        () -> assertAllMenuItem(menus, menuIds)
                );
            }

            private void setup(List<Long> menuIds, List<Long> productIds) {
                final List<Menu> menus = menuIds.stream().map(this::newMenu).collect(Collectors.toList());
                Mockito.when(menuDao.findAll()).thenReturn(menus);
                final List<Product> products = productIds.stream().map(this::newProduct).collect(Collectors.toList());
                final List<MenuProduct> menuProducts = menus
                        .stream()
                        .map(menu ->
                                products
                                        .stream()
                                        .map(product -> {
                                            final MenuProduct menuProduct = new MenuProduct();
                                            menuProduct.setMenuId(menu.getId());
                                            menuProduct.setProductId(product.getId());
                                            return menuProduct;
                                        })
                                        .collect(Collectors.toList())
                        )
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                Mockito
                        .when(menuProductDao.findAllByMenuId(1L))
                        .thenReturn(menuProducts
                                .stream()
                                .filter(x -> x.getMenuId().equals(1L))
                                .collect(Collectors.toList())
                        );
            }

            private Menu newMenu(long menuId) {
                final Menu menu = new Menu();
                menu.setId(menuId);
                return menu;
            }

            private Product newProduct(long productId) {
                final Product product = new Product();
                product.setId(productId);
                return product;
            }

            private void assertAllMenuItem(List<Menu> menus, List<Long> menuIds) {
                menus.stream().map(Menu::getId).collect(Collectors.toList()).forEach(menuIds::contains);
            }
        }
    }
}
