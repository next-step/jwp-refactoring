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

import kitchenpos.application.fixture.Fixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MenuTest {
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

    @BeforeEach
    void setUp() {
        //given
        메뉴등록_사전DB저장내용_설정();
    }

    @DisplayName("메뉴가 저장된다.")
    @Test
    void craete_menu() {
        // given
        Menu 신규메뉴 = new Menu();

        신규메뉴.setName("뿌링클콤보");
        신규메뉴.setPrice(BigDecimal.valueOf(18_000));
        신규메뉴.setMenuGroupId(Fixture.치킨_메뉴그룹.getId());
        신규메뉴.setMenuProducts(List.of(Fixture.뿌링클콤보_뿌링클치킨, Fixture.뿌링클콤보_치킨무, Fixture.뿌링클콤보_코카콜라));

        // when
        Menu savedMenu = menuService.create(신규메뉴);

        // then
        Assertions.assertThat(savedMenu).isEqualTo(Fixture.뿌링클콤보);
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
        신규메뉴.setMenuGroupId(Fixture.사이드_메뉴그룹.getId());

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
        신규메뉴.setMenuGroupId(Fixture.치킨_메뉴그룹.getId());
        신규메뉴.setMenuProducts(List.of(Fixture.뿌링클콤보_뿌링클치킨, Fixture.뿌링클콤보_치킨무, Fixture.뿌링클콤보_코카콜라));

        삭제된_상품_조회_설정(Fixture.코카콜라);

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
        신규메뉴.setMenuGroupId(Fixture.치킨_메뉴그룹.getId());
        신규메뉴.setMenuProducts(List.of(Fixture.뿌링클콤보_뿌링클치킨, Fixture.뿌링클콤보_치킨무, Fixture.뿌링클콤보_코카콜라));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> menuService.create(신규메뉴));
    }

    @DisplayName("메뉴가 조회된다.")
    @Test
    void search_menu() {
        // given
        메뉴_등록_설정();

        // when
        List<Menu> searchedMenu = menuService.list();

        // then
        Assertions.assertThat(searchedMenu).isEqualTo(List.of(Fixture.뿌링클콤보));
    }

    private void 삭제된_상품_조회_설정(Product product) {
        when(productDao.findById(product.getId())).thenThrow(IllegalArgumentException.class);
    }

    private void 메뉴_등록_설정() {
        when(menuDao.findAll()).thenReturn(List.of(Fixture.뿌링클콤보));
        when(menuProductDao.findAllByMenuId(Fixture.뿌링클콤보.getId())).thenReturn(Fixture.뿌링클콤보.getMenuProducts());
    }

    private void 메뉴등록_사전DB저장내용_설정() {
        when(menuGroupDao.existsById(Fixture.치킨_메뉴그룹.getId())).thenReturn(true);
        when(productDao.findById(Fixture.뿌링클콤보_뿌링클치킨.getProductId())).thenReturn(Optional.of(Fixture.뿌링클치킨));
        when(productDao.findById(Fixture.뿌링클콤보_치킨무.getProductId())).thenReturn(Optional.of(Fixture.치킨무));
        when(productDao.findById(Fixture.뿌링클콤보_코카콜라.getProductId())).thenReturn(Optional.of(Fixture.코카콜라));
        when(menuDao.save(any(Menu.class))).thenReturn(Fixture.뿌링클콤보);
    }
}
