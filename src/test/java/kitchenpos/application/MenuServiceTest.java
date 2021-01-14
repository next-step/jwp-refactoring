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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private Menu 강정치킨;
    private Menu 양념치킨;
    private Menu 후라이드치킨;
    private Product 상품;
    private List<Menu> 메뉴리스트 = new ArrayList<>();
    private List<MenuProduct> 상품리스트 = new ArrayList<>();

    private MenuService menuService;

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;

    @BeforeEach
    public void setUp() {
        상품 = new Product("테스트상품",new BigDecimal(20000));
        상품리스트.add(new MenuProduct(2L,1));
        상품리스트.add(new MenuProduct(3L,1));
        강정치킨 = new Menu("강정치킨", new BigDecimal(17000),1L, 상품리스트);
        양념치킨 = new Menu("양념치킨", new BigDecimal(18000),2L, 상품리스트);
        후라이드치킨 = new Menu("후라이드치킨", new BigDecimal(16000),3L, 상품리스트);
        메뉴리스트.add(강정치킨);
        메뉴리스트.add(양념치킨);
        메뉴리스트.add(후라이드치킨);
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("메뉴 등록 테스트")
    @Test
    void createMenu() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.ofNullable(상품));
        mockSaveMenu(강정치킨);
        mockSaveMenuProduct(상품리스트.get(0));

        Menu resultMenu = menuService.create(강정치킨);

        assertAll(
                () -> assertThat(resultMenu.getName()).isEqualTo(강정치킨.getName()),
                () -> assertThat(resultMenu.getPrice()).isEqualTo(강정치킨.getPrice()),
                () -> assertThat(resultMenu.getMenuProducts().size()).isEqualTo(강정치킨.getMenuProducts().size()),
                () -> assertThat(resultMenu.getMenuProducts().get(0).getProductId()).isEqualTo(강정치킨.getMenuProducts().get(0).getProductId())
        );
    }

    @DisplayName("메뉴 등록 예외 테스트: 메뉴값 null")
    @Test
    void nullMenu() {
        assertThatThrownBy(() -> menuService.create(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("메뉴 등록 예외 테스트: 가격정보 null or 음수")
    @Test
    void invaildPrice() {
        강정치킨.setPrice(null);
        assertThatThrownBy(() -> menuService.create(강정치킨))
                .isInstanceOf(IllegalArgumentException.class);

        강정치킨.setPrice(new BigDecimal(-500));
        assertThatThrownBy(() -> menuService.create(강정치킨))
                .isInstanceOf(IllegalArgumentException.class);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(강정치킨)
        );
        assertThat(exception.getMessage()).isEqualTo("입력된 가격이 올바르지 않습니다.");
    }

    @DisplayName("메뉴 등록 예외 테스트: 메뉴그룹값 올바르지 않을때")
    @Test
    void invalidMenuGroup() {
        when(menuGroupDao.existsById(any())).thenReturn(false);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(강정치킨)
        );
        assertThat(exception.getMessage()).isEqualTo("메뉴그룹 값을 찾을 수 없습니다.");
    }


    @DisplayName("메뉴 등록 예외 테스트: 메뉴의 상품정보가 잘못 된 경우")
    @Test
    void invalidProductInfo() {
        when(menuGroupDao.existsById(any())).thenReturn(true);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(강정치킨)
        );
        assertThat(exception.getMessage()).isEqualTo("상품을 찾을 수 없습니다.");
    }

    @DisplayName("메뉴 등록 예외 테스트: 메뉴 가격이 잘못된 경우")
    @Test
    void invalidTotalPrice() {
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.ofNullable(상품));
        강정치킨.setPrice(new BigDecimal(500000));
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.create(강정치킨)
        );
        assertThat(exception.getMessage()).isEqualTo("상품가격 총합과 메뉴의 가격이 올바르지 않습니다.");
    }

    @DisplayName("메뉴목록 조회 테스트")
    @Test
    void findMenuList() {
        when(menuDao.findAll()).thenReturn(메뉴리스트);
        when(menuProductDao.findAllByMenuId(any())).thenReturn(상품리스트);

        List<Menu> resultMenus = menuService.list();

        List<String> menuNames =resultMenus.stream()
                .map(menu -> menu.getName())
                .collect(Collectors.toList());
        List<String> expectedMenuNames = 메뉴리스트.stream()
                .map(menu -> menu.getName())
                .collect(Collectors.toList());

        assertThat(resultMenus.size()).isEqualTo(메뉴리스트.size());
        assertThat(menuNames).containsExactlyElementsOf(expectedMenuNames);

    }

    private void mockSaveMenuProduct(MenuProduct menuProduct) {
        when(menuProductDao.save(menuProduct)).thenReturn(menuProduct);
    }

    private void mockSaveMenu(Menu menu) {
        when(menuDao.save(menu)).thenReturn(menu);
    }
}
