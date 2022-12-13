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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
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
    MenuService menuService;

    private Menu 기본메뉴;
    private MenuGroup 조식메뉴그룹;
    private MenuProduct 기본메뉴_아메리카노;
    private MenuProduct 기본메뉴_스콘;
    private Product 아메리카노;
    private Product 스콘;

    @BeforeEach
    void setUp() {
        아메리카노 = new Product(1L, "아메리카노", BigDecimal.valueOf(3000));
        스콘 = new Product(2L, "스콘", BigDecimal.valueOf(2000));
        조식메뉴그룹 = new MenuGroup(1L, "조식메뉴그룹");
        기본메뉴_아메리카노 = new MenuProduct(1L, 1L, 아메리카노.getId(), 1);
        기본메뉴_스콘 = new MenuProduct(2L, 1L, 스콘.getId(), 1);
        기본메뉴 = new Menu(1L, "기본메뉴", BigDecimal.valueOf(5000), 조식메뉴그룹.getId(), Arrays.asList(기본메뉴_아메리카노, 기본메뉴_스콘));
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void 메뉴_등록() {
        // given
        given(productDao.findById(아메리카노.getId())).willReturn(Optional.of(아메리카노));
        given(productDao.findById(스콘.getId())).willReturn(Optional.of(스콘));
        given(menuGroupDao.existsById(조식메뉴그룹.getId())).willReturn(true);
        given(menuProductDao.save(기본메뉴_아메리카노)).willReturn(기본메뉴_아메리카노);
        given(menuProductDao.save(기본메뉴_스콘)).willReturn(기본메뉴_스콘);
        given(menuDao.save(기본메뉴)).willReturn(기본메뉴);

        // when
        Menu saveMenu = menuService.create(기본메뉴);

        // then
        assertThat(saveMenu.getId()).isEqualTo(기본메뉴.getId());
        assertThat(saveMenu.getName()).isEqualTo(기본메뉴.getName());
        assertThat(saveMenu.getPrice()).isEqualTo(기본메뉴.getPrice());
        assertThat(saveMenu.getMenuGroupId()).isEqualTo(기본메뉴.getMenuGroupId());
        assertThat(saveMenu.getMenuProducts()).isEqualTo(기본메뉴.getMenuProducts());
    }

    @Test
    @DisplayName("메뉴의 가격이 0원 미만이면 오류 발생한다.")
    void error_메뉴_등록_가격_zero_미만() {
        // given
        Menu 가격zero미만 = new Menu(1L, "가격zero미만", BigDecimal.valueOf(-7000L), 조식메뉴그룹.getId(), Arrays.asList(기본메뉴_아메리카노, 기본메뉴_스콘));

        // then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(가격zero미만));
    }

    @Test
    @DisplayName("메뉴 그룹이 등록되어 있지 않으면 오류 발생한다.")
    void error_메뉴_등록_메뉴_그룹_미등록() {
        // given
        Menu 메뉴그룹미등록 = new Menu(1L, "메뉴그룹미등록", BigDecimal.valueOf(-7000L), null, Arrays.asList(기본메뉴_아메리카노, 기본메뉴_스콘));

        // then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(메뉴그룹미등록));
    }

    @Test
    @DisplayName("메뉴 상품이 등록되어 있지 않으면 오류 발생한다.")
    void error_메뉴_등록_메뉴_상품_미등록() {
        // given
        given(menuGroupDao.existsById(조식메뉴그룹.getId())).willReturn(true);
        given(productDao.findById(아메리카노.getId())).willReturn(Optional.of(아메리카노));

        // then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(기본메뉴));
    }

    @Test
    @DisplayName("메뉴 상품들 가격의 합보다 메뉴의 가격이 크면 오류 발생한다.")
    void error_메뉴_등록_메뉴_가격_초과() {
        // given
        given(productDao.findById(아메리카노.getId())).willReturn(Optional.of(아메리카노));
        given(productDao.findById(스콘.getId())).willReturn(Optional.of(스콘));
        given(menuGroupDao.existsById(조식메뉴그룹.getId())).willReturn(true);
        Menu 가격초과 = new Menu(1L, "가격초과", BigDecimal.valueOf(8000), 조식메뉴그룹.getId(), Arrays.asList(기본메뉴_아메리카노, 기본메뉴_스콘));

        // then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(가격초과));
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void 메뉴_목록_조회() {
        // given
        given(menuDao.findAll()).willReturn(Arrays.asList(기본메뉴));

        // when
        List<Menu> menuList = menuService.list();

        // then
        assertThat(menuList).containsExactly(기본메뉴);
    }

}
