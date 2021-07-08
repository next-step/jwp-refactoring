package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.utils.domain.MenuGroupObjects;
import kitchenpos.utils.domain.MenuObjects;
import kitchenpos.utils.domain.MenuProductObjects;
import kitchenpos.utils.domain.ProductObjects;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 서비스")
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;
    private MenuObjects menuObjects;

    private MenuProductObjects menuProductObjects;
    private MenuGroupObjects menuGroupObjects;
    private ProductObjects productObjects;
    private MenuGroup menuGroup1;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;
    private MenuProduct menuProduct3;
    private MenuProduct menuProduct4;
    private Menu menu1;
    private Menu menu2;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        menuObjects = new MenuObjects();
        menuGroupObjects = new MenuGroupObjects();
        menuProductObjects = new MenuProductObjects();
        menu1 = menuObjects.getMenu1();
        menu2 = menuObjects.getMenu2();
        menuGroup1 = menuGroupObjects.getMenuGroup1();
        menuProduct1 = menuProductObjects.getMenuProduct1();
        menuProduct2 = menuProductObjects.getMenuProduct2();
        menuProduct3 = menuProductObjects.getMenuProduct3();
        menuProduct4 = menuProductObjects.getMenuProduct4();
        productObjects = new ProductObjects();
        product1 = productObjects.getProduct1();
        product2 = productObjects.getProduct2();

        menuProduct1.setProductId(product1.getId());
        menuProduct2.setProductId(product2.getId());
        menuProduct1.setMenuId(menu1.getId());
        menuProduct2.setMenuId(menu1.getId());
        menuProduct3.setMenuId(menu2.getId());
        menuProduct4.setMenuId(menu2.getId());
        menu1.setMenuGroupId(menuGroup1.getId());
        menu1.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
    }

    @TestFactory
    @DisplayName("전체 메뉴 조회")
    List<DynamicTest> find_allMenu() {
        // mocking
        when(menuDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(menu1, menu2)));
        when(menuProductDao.findAllByMenuId(menu1.getId())).thenReturn(new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2)));
        when(menuProductDao.findAllByMenuId(menu2.getId())).thenReturn(new ArrayList<>(Arrays.asList(menuProduct3, menuProduct4)));

        // when
        List<Menu> findMenus = menuService.list();

        // then
        return Arrays.asList(
                dynamicTest("결과 메뉴 목록 크기 확인.", () -> assertThat(findMenus.size()).isEqualTo(2)),
                dynamicTest("결과 메뉴 목록의 ID 값들 확인", () -> assertThat(findMenus).extracting("id").contains(menu1.getId(), menu2.getId())),
                dynamicTest("메뉴에 포함된 메뉴상품 목록 확인.", () -> {
                    List<MenuProduct> menuProducts1 = findMenus.get(0).getMenuProducts();
                    List<MenuProduct> menuProducts2 = findMenus.get(1).getMenuProducts();
                    assertAll(
                            () -> assertThat(menuProducts1).extracting("seq").contains(menuProduct1.getSeq(), menuProduct2.getSeq()),
                            () -> assertThat(menuProducts2).extracting("seq").contains(menuProduct3.getSeq(), menuProduct4.getSeq())
                    );
                })
        );
    }

    @TestFactory
    @DisplayName("메뉴를 등록한다.")
    List<DynamicTest> create_menu() {
        // mocking
        when(menuGroupDao.existsById(anyLong())).thenReturn(true);
        when(productDao.findById(menuProduct1.getProductId())).thenReturn(Optional.of(product1));
        when(productDao.findById(menuProduct2.getProductId())).thenReturn(Optional.of(product2));
        when(menuDao.save(any(Menu.class))).thenReturn(menu1);
        when(menuProductDao.save(menuProduct1)).thenReturn(menuProduct1);
        when(menuProductDao.save(menuProduct2)).thenReturn(menuProduct2);

        // when
        Menu savedMenu = menuService.create(menu1);

        // then
        return Arrays.asList(
                dynamicTest("메뉴 ID 확인됨.", () -> assertThat(savedMenu.getId()).isEqualTo(menu1.getId())),
                dynamicTest("메뉴 그룹 ID 확인됨.", () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup1.getId())),
                dynamicTest("메뉴상품의 상품 ID 확인됨.", () -> assertThat(savedMenu.getMenuProducts()).extracting("productId").contains(product1.getId(), product2.getId()))
        );
    }

    @TestFactory
    @DisplayName("메뉴 등록 시 예외발생 상황")
    List<DynamicTest> exceptions() {
        return Arrays.asList(
                dynamicTest("메뉴금액 미입력 오류 발생함.", () -> {
                    // given
                    menu1.setPrice(null);

                    // then
                    assertThatThrownBy(() -> menuService.create(menu1))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("메뉴금액 음수 입력 오류 발생함.", () -> {
                    // given
                    menu1.setPrice(BigDecimal.valueOf(-1));

                    // then
                    assertThatThrownBy(() -> menuService.create(menu1))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("등록되지 않은 메뉴그룹으로 등록 시도 시 오류 발생함.", () -> {
                    // given
                    menu1.setPrice(BigDecimal.valueOf(16000.00));

                    // mocking
                    when(menuGroupDao.existsById(anyLong())).thenReturn(false);

                    // then
                    assertThatThrownBy(() -> menuService.create(menu1))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("메뉴상품에 등록된 상품 ID로 상품조회 실패 시 오류 발생함.", () -> {
                    // given
                    menu1.setPrice(BigDecimal.valueOf(16000.00));

                    // mocking
                    when(menuGroupDao.existsById(anyLong())).thenReturn(true);
                    when(productDao.findById(menuProduct1.getProductId())).thenReturn(Optional.empty());

                    // then
                    assertThatThrownBy(() -> menuService.create(menu1))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("메뉴에 포함된 상품의 가격 X 개수의 합계 금액이 메뉴의 가격보다 작을 경우 오류 발생함.", () -> {
                    // given
                    menu1.setPrice(BigDecimal.valueOf(16000.00));
                    menuProduct1.setQuantity(4);
                    product1.setPrice(BigDecimal.valueOf(1900.00));
                    menuProduct2.setQuantity(4);
                    product2.setPrice(BigDecimal.valueOf(2000.00));

                    // mocking
                    when(menuGroupDao.existsById(anyLong())).thenReturn(true);
                    when(productDao.findById(menuProduct1.getProductId())).thenReturn(Optional.of(product1));
                    when(productDao.findById(menuProduct2.getProductId())).thenReturn(Optional.of(product2));

                    // then
                    assertThatThrownBy(() -> menuService.create(menu1))
                            .isInstanceOf(IllegalArgumentException.class);
                })
        );
    }
}
