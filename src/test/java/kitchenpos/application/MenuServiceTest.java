package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;


@DisplayName("메뉴 서비스 테스트")
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

    private Product 지코바치킨;
    private MenuGroup 메뉴그룹;
    private MenuProduct 메뉴상품;
    private Menu 오늘의메뉴;

    @BeforeEach
    void setUp() {
        지코바치킨 = 상품fixture설정(1L, "지코바치킨", BigDecimal.valueOf(20000));
        메뉴그룹 = 메뉴그룹fixture설정(1L, "메뉴그룹");
        메뉴상품 = 메뉴상품fixture설정(1L, 지코바치킨.getId(), 1L);
        오늘의메뉴 = 메뉴fixture설정(1L, "오늘의메뉴", 지코바치킨.getPrice(), 메뉴그룹.getId(), Arrays.asList(메뉴상품));
    }

    @Test
    void 메뉴_등록_성공() {
        given(menuGroupDao.existsById(오늘의메뉴.getMenuGroupId())).willReturn(true);
        given(productDao.findById(메뉴상품.getProductId())).willReturn(Optional.of(지코바치킨));
        given(menuDao.save(오늘의메뉴)).willReturn(오늘의메뉴);
        given(menuProductDao.save(메뉴상품)).willReturn(메뉴상품);

        Menu createdMenu = menuService.create(오늘의메뉴);

        assertAll(
                () -> assertThat(createdMenu.getName()).isEqualTo(오늘의메뉴.getName()),
                () -> assertThat(createdMenu).isNotNull()
        );
    }

    @Test
    void 메뉴_등록_실패_메뉴의_가격이_음수() {
        Menu menu = 메뉴fixture설정(100L, "메뉴이름", BigDecimal.valueOf(-1000), 메뉴그룹.getId(), Arrays.asList(메뉴상품));
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_등록_실패_메뉴그룹이_존재하지않음() {
        long 존재하지않는_메뉴그룹ID = 1000L;
        Menu menu = 메뉴fixture설정(100L, "메뉴이름", BigDecimal.valueOf(1000), 존재하지않는_메뉴그룹ID, Arrays.asList(메뉴상품));
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(false);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_등록_실패_상품이_존재하지않음() {
        MenuProduct 존재하지않는_메뉴상품 = new MenuProduct();
        Menu menu = 메뉴fixture설정(100L, "메뉴이름", BigDecimal.valueOf(1000), 메뉴그룹.getId(), Arrays.asList(존재하지않는_메뉴상품));
        given(menuGroupDao.existsById(menu.getMenuGroupId())).willReturn(true);
        given(productDao.findById(존재하지않는_메뉴상품.getProductId())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_등록_실패_메뉴price는_상품들의가격합보다_클수없음() {
        Product 특별상품 = 상품fixture설정(2L, "특별상품", BigDecimal.valueOf(3000));
        Product 신선상품 = 상품fixture설정(3L, "신선상품", BigDecimal.valueOf(4000));
        MenuProduct 특별메뉴상품 = 메뉴상품fixture설정(3L, 특별상품.getId(), 1L);
        MenuProduct 신선메뉴상품 = 메뉴상품fixture설정(4L, 신선상품.getId(), 1L);
        Menu 신상메뉴 = 메뉴fixture설정(2L, "신상메뉴", BigDecimal.valueOf(10000), 메뉴그룹.getId(), Arrays.asList(특별메뉴상품, 신선메뉴상품));

        given(menuGroupDao.existsById(신상메뉴.getMenuGroupId())).willReturn(true);
        given(productDao.findById(특별메뉴상품.getProductId())).willReturn(Optional.of(특별상품));
        given(productDao.findById(신선메뉴상품.getProductId())).willReturn(Optional.of(신선상품));

        assertThatThrownBy(() -> menuService.create(신상메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴조회() {
        given(menuDao.findAll()).willReturn(Arrays.asList(오늘의메뉴));
        given(menuProductDao.findAllByMenuId(오늘의메뉴.getId()))
                .willReturn(Arrays.asList(메뉴상품));

        List<Menu> list = menuService.list();

        assertAll(
                () -> assertThat(list.size()).isEqualTo(1),
                () -> assertThat(list.get(0).getName()).isEqualTo("오늘의메뉴")
        );
    }


    private Menu 메뉴fixture설정(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName("오늘의메뉴");
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    private MenuProduct 메뉴상품fixture설정(Long seq, Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    private MenuGroup 메뉴그룹fixture설정(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    private Product 상품fixture설정(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }


}
