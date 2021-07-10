package kitchenpos.menu.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    MenuDao menuDao;
    @Mock
    MenuGroupDao menuGroupDao;
    @Mock
    MenuProductDao menuProductDao;
    @Mock
    ProductDao productDao;

    @InjectMocks
    MenuService menuService;

    Product 프로덕트_후라이드_치킨;
    Product 프로덕트_양념_치킨;
    MenuGroup 메뉴그룹_추천메뉴;
    MenuProduct 메뉴프로덕트_후라이드_치킨;
    MenuProduct 메뉴프로덕트_후라이드_양념_메뉴_후라이드치킨;
    MenuProduct 메뉴프로덕트_후라이드_양념_메뉴_양념치킨;
    Menu 메뉴_후라이드_후라이드;
    Menu 메뉴_후라이드_양념;

    long 존재하지않는아이디 = 99L;
    BigDecimal 프로덕트의_합보다_큰_가격 = BigDecimal.valueOf(9999999);


    @BeforeEach
    void setUp() {
        프로덕트_후라이드_치킨 = new Product();
        프로덕트_후라이드_치킨.setId(1L);
        프로덕트_후라이드_치킨.setName("후라이드치킨");
        프로덕트_후라이드_치킨.setPrice(BigDecimal.valueOf(13000));

        프로덕트_양념_치킨 = new Product();
        프로덕트_양념_치킨.setId(2L);
        프로덕트_양념_치킨.setName("양념치킨");
        프로덕트_양념_치킨.setPrice(BigDecimal.valueOf(14000));

        메뉴그룹_추천메뉴 = new MenuGroup();
        메뉴그룹_추천메뉴.setId(1L);
        메뉴그룹_추천메뉴.setName("추천메뉴");

        메뉴프로덕트_후라이드_치킨 = new MenuProduct();
        메뉴프로덕트_후라이드_치킨.setMenuId(1L);
        메뉴프로덕트_후라이드_치킨.setProductId(프로덕트_후라이드_치킨.getId());
        메뉴프로덕트_후라이드_치킨.setQuantity(2);
        메뉴프로덕트_후라이드_치킨.setSeq(1L);

        메뉴프로덕트_후라이드_양념_메뉴_후라이드치킨 = new MenuProduct();
        메뉴프로덕트_후라이드_양념_메뉴_후라이드치킨.setMenuId(2L);
        메뉴프로덕트_후라이드_양념_메뉴_후라이드치킨.setProductId(프로덕트_후라이드_치킨.getId());
        메뉴프로덕트_후라이드_양념_메뉴_후라이드치킨.setQuantity(1);
        메뉴프로덕트_후라이드_양념_메뉴_후라이드치킨.setSeq(1L);

        메뉴프로덕트_후라이드_양념_메뉴_양념치킨 = new MenuProduct();
        메뉴프로덕트_후라이드_양념_메뉴_양념치킨.setMenuId(2L);
        메뉴프로덕트_후라이드_양념_메뉴_양념치킨.setProductId(프로덕트_양념_치킨.getId());
        메뉴프로덕트_후라이드_양념_메뉴_양념치킨.setQuantity(1);
        메뉴프로덕트_후라이드_양념_메뉴_양념치킨.setSeq(2L);

        메뉴_후라이드_후라이드 = new Menu();
        메뉴_후라이드_후라이드.setId(1L);
        메뉴_후라이드_후라이드.setName("후라이드+후라이드");
        메뉴_후라이드_후라이드.setMenuGroupId(메뉴그룹_추천메뉴.getId());
        메뉴_후라이드_후라이드.setPrice(BigDecimal.valueOf(23000));
        메뉴_후라이드_후라이드.setMenuProducts(Arrays.asList(메뉴프로덕트_후라이드_치킨));

        메뉴_후라이드_양념 = new Menu();
        메뉴_후라이드_양념.setId(2L);
        메뉴_후라이드_양념.setName("후라이드+양념");
        메뉴_후라이드_양념.setMenuGroupId(메뉴그룹_추천메뉴.getId());
        메뉴_후라이드_양념.setPrice(BigDecimal.valueOf(24000));
        메뉴_후라이드_양념.setMenuProducts(Arrays.asList(메뉴프로덕트_후라이드_치킨));
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() {
        //given
        when(menuGroupDao.existsById(메뉴_후라이드_후라이드.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(메뉴프로덕트_후라이드_치킨.getProductId())).thenReturn(Optional.of(프로덕트_후라이드_치킨));
        when(menuDao.save(메뉴_후라이드_후라이드)).thenReturn(메뉴_후라이드_후라이드);
        when(menuProductDao.save(메뉴프로덕트_후라이드_치킨)).thenReturn(메뉴프로덕트_후라이드_치킨);

        //when
        Menu createdMenu = menuService.create(메뉴_후라이드_후라이드);

        //then
        assertThat(createdMenu.getName()).isEqualTo(메뉴_후라이드_후라이드.getName());
    }

    @Test
    @DisplayName("메뉴가격이 0보다 작거나 비어있는경우 메뉴 생성을 실패한다.")
    void create_with_exception_when_price_smaller_than_zero_or_null() {
        assertAll(() -> {
                    //given
                    메뉴_후라이드_후라이드.setPrice(null);

                    //when && then
                    assertThatThrownBy(() -> menuService.create(메뉴_후라이드_후라이드))
                            .isInstanceOf(IllegalArgumentException.class);
                }, () -> {
                    //given
                    메뉴_후라이드_후라이드.setPrice(BigDecimal.valueOf(-1));

                    //when && then
                    assertThatThrownBy(() -> menuService.create(메뉴_후라이드_후라이드))
                            .isInstanceOf(IllegalArgumentException.class);
                }
        );
    }

    @Test
    @DisplayName("메뉴 그룹이 없는 경우 메뉴 생성을 실패한다.")
    void create_with_exception_when_menu_group_is_null() {
        //given
        메뉴_후라이드_후라이드.setMenuGroupId(존재하지않는아이디);
        when(menuGroupDao.existsById(존재하지않는아이디)).thenReturn(false);

        //when && then
        assertThatThrownBy(() -> menuService.create(메뉴_후라이드_후라이드))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 포함된 상품의 가격합보다 큰 경우 메뉴 생성을 실패한다.")
    void create_with_exception_when_menu_price_greater_than_sum_of_product() {
        //given
        메뉴_후라이드_후라이드.setPrice(프로덕트의_합보다_큰_가격);
        when(menuGroupDao.existsById(메뉴_후라이드_후라이드.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(메뉴프로덕트_후라이드_치킨.getProductId())).thenReturn(Optional.of(프로덕트_후라이드_치킨));

        //when && then
        assertThatThrownBy(() -> menuService.create(메뉴_후라이드_후라이드))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        //given
        when(menuDao.findAll()).thenReturn(Arrays.asList(메뉴_후라이드_후라이드, 메뉴_후라이드_양념));
        when(menuProductDao.findAllByMenuId(메뉴_후라이드_후라이드.getId())).thenReturn(Arrays.asList(메뉴프로덕트_후라이드_치킨));
        when(menuProductDao.findAllByMenuId(메뉴_후라이드_양념.getId())).thenReturn(Arrays.asList(메뉴프로덕트_후라이드_양념_메뉴_후라이드치킨, 메뉴프로덕트_후라이드_양념_메뉴_양념치킨));

        //when
        List<Menu> menus = menuService.list();

        //then
        assertThat(menus).containsExactly(메뉴_후라이드_후라이드, 메뉴_후라이드_양념);
    }
}