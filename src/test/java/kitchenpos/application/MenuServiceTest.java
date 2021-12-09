package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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


    private MenuGroup 치킨_메뉴그룹 = new MenuGroup();
    private MenuGroup 사이드_메뉴그룹 = new MenuGroup();

    private Menu 뿌링클콤보 = new Menu();

    private Product 뿌링클치킨 = new Product();
    private Product 치킨무 = new Product();
    private Product 코카콜라 = new Product();

    private MenuProduct 뿌링클콤보_뿌링클치킨 = new MenuProduct();
    private MenuProduct 뿌링클콤보_치킨무 = new MenuProduct();
    private MenuProduct 뿌링클콤보_코카콜라 = new MenuProduct();

    @BeforeEach
    void setUp() {
        치킨_메뉴그룹.setId(1L);
        치킨_메뉴그룹.setName("치킨");

        사이드_메뉴그룹.setId(2L);
        사이드_메뉴그룹.setName("사이드");

        뿌링클콤보.setId(1L);
        뿌링클콤보.setName("뿌링클콤보");
        뿌링클콤보.setPrice(BigDecimal.valueOf(18_000));
        뿌링클콤보.setMenuGroupId(치킨_메뉴그룹.getId());
        뿌링클콤보.setMenuProducts(List.of(뿌링클콤보_뿌링클치킨, 뿌링클콤보_치킨무, 뿌링클콤보_코카콜라));

        뿌링클치킨.setId(1L);
        뿌링클치킨.setName("뿌링클치킨");
        뿌링클치킨.setPrice(BigDecimal.valueOf(15_000));   
        
        치킨무.setId(2L);
        치킨무.setName("치킨무");
        치킨무.setPrice(BigDecimal.valueOf(1_000));

        코카콜라.setId(3L);
        코카콜라.setName("코카콜라");
        코카콜라.setPrice(BigDecimal.valueOf(3_000));
        
        뿌링클콤보_뿌링클치킨.setSeq(1L);
        뿌링클콤보_뿌링클치킨.setMenuId(뿌링클콤보.getId());
        뿌링클콤보_뿌링클치킨.setProductId(뿌링클치킨.getId());
        뿌링클콤보_뿌링클치킨.setQuantity(1L);

        뿌링클콤보_치킨무.setSeq(2L);
        뿌링클콤보_치킨무.setMenuId(뿌링클콤보.getId());
        뿌링클콤보_치킨무.setProductId(치킨무.getId());
        뿌링클콤보_치킨무.setQuantity(1L);

        뿌링클콤보_코카콜라.setSeq(3L);
        뿌링클콤보_코카콜라.setMenuId(뿌링클콤보.getId());
        뿌링클콤보_코카콜라.setProductId(코카콜라.getId());
        뿌링클콤보_코카콜라.setQuantity(1L);

        when(menuGroupDao.existsById(this.치킨_메뉴그룹.getId())).thenReturn(true);
        when(productDao.findById(this.뿌링클콤보_뿌링클치킨.getProductId())).thenReturn(Optional.of(this.뿌링클치킨));
        when(productDao.findById(this.뿌링클콤보_치킨무.getProductId())).thenReturn(Optional.of(this.치킨무));
        when(productDao.findById(this.뿌링클콤보_코카콜라.getProductId())).thenReturn(Optional.of(this.코카콜라));
        when(menuDao.save(any(Menu.class))).thenReturn(this.뿌링클콤보);
    }

    @DisplayName("메뉴가 저장된다.")
    @Test
    void craete_menu() {
        // given
        Menu 신규메뉴 = new Menu();

        신규메뉴.setName("뿌링클콤보");
        신규메뉴.setPrice(BigDecimal.valueOf(18_000));
        신규메뉴.setMenuGroupId(this.치킨_메뉴그룹.getId());
        신규메뉴.setMenuProducts(List.of(this.뿌링클콤보_뿌링클치킨, this.뿌링클콤보_치킨무, this.뿌링클콤보_코카콜라));

        // when
        Menu savedMenu = menuService.create(신규메뉴);

        // then
        Assertions.assertThat(savedMenu).isEqualTo(this.뿌링클콤보);
    }

    @DisplayName("메뉴가격이 없으면 예외가 발생한다.")
    @Test
    void exception_createMenu_nullPrice() {
        // given
        Menu 신규메뉴 = new Menu();

        신규메뉴.setName("뿌링클콤보");
        신규메뉴.setPrice(null);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> menuService.create(신규메뉴));
    }

    @DisplayName("메뉴가격이 0보다 작으면 예외가 발생한다.")
    @ValueSource(ints = {0, -1, -10})
    @ParameterizedTest(name="[{index}] 메뉴가격은 [{0}]")
    void exception_createMenu_underZeroPrice(int price) {
        // given
        Menu 신규메뉴 = new Menu();

        신규메뉴.setName("뿌링클콤보");
        신규메뉴.setPrice(BigDecimal.valueOf(price));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> menuService.create(신규메뉴));
    }

    @DisplayName("메뉴에대한 메뉴그룹이 없으면 예외가 발생한다.")
    @Test
    void exception_createMenu_containNotExistMenuGroup() {
        // given
        Menu 신규메뉴 = new Menu();

        신규메뉴.setPrice(BigDecimal.valueOf(18_000));
        신규메뉴.setMenuGroupId(this.사이드_메뉴그룹.getId());

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> menuService.create(신규메뉴));
    }

    @DisplayName("미등록 상품이 포함된 메뉴를 생성시 예외가 발생한다.")
    @Test
    void exception_createMenu_notExistProduct() {
        // given
        Menu 신규메뉴 = new Menu();

        신규메뉴.setPrice(BigDecimal.valueOf(18_000));
        신규메뉴.setMenuGroupId(this.치킨_메뉴그룹.getId());
        신규메뉴.setMenuProducts(List.of(this.뿌링클콤보_뿌링클치킨, this.뿌링클콤보_치킨무, this.뿌링클콤보_코카콜라));

        when(productDao.findById(this.코카콜라.getId())).thenThrow(IllegalArgumentException.class);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> menuService.create(신규메뉴));
    }

    @DisplayName("메뉴 가격이 상품의 가격 총합이 보다 클 시 예외가 발생한다.")
    @Test
    void exception_createMenu_productPriceSumGreaterThanMenuPrice() {
        // given
        Menu 신규메뉴 = new Menu();

        신규메뉴.setPrice(BigDecimal.valueOf(20_000));
        신규메뉴.setMenuGroupId(this.치킨_메뉴그룹.getId());
        신규메뉴.setMenuProducts(List.of(this.뿌링클콤보_뿌링클치킨, this.뿌링클콤보_치킨무, this.뿌링클콤보_코카콜라));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> menuService.create(신규메뉴));
    }

    @DisplayName("메뉴가 조회된다.")
    @Test
    void search_menu() {
        // given
        when(menuDao.findAll()).thenReturn(List.of(this.뿌링클콤보));
        when(menuProductDao.findAllByMenuId(this.뿌링클콤보.getId())).thenReturn(this.뿌링클콤보.getMenuProducts());

        // when
        List<Menu> searchedMenu = menuService.list();

        // then
        Assertions.assertThat(searchedMenu).isEqualTo(List.of(this.뿌링클콤보));
    }
}
