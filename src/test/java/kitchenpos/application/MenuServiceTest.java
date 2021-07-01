package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

@DisplayName("메뉴 서비스")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    public static final BigDecimal 치즈버거_가격 = new BigDecimal(4500);
    public static final BigDecimal 감자튀김_가격 = new BigDecimal(2000);
    public static final BigDecimal 콜라_가격 = new BigDecimal(1000);
    public static final BigDecimal 치즈버거세트_가격 = new BigDecimal(6000);

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private MenuProductDao menuProductDao;
    @InjectMocks
    private MenuService menuService;

    private Product 치즈버거;
    private Product 감자튀김;
    private Product 콜라;
    private MenuProduct 치즈버거세트_치즈버거;
    private MenuProduct 치즈버거세트_감자튀김;
    private MenuProduct 치즈버거세트_콜라;
    private Menu 치즈버거세트;
    private MenuGroup 패스트푸드;

    @BeforeEach
    void setup() {
        패스트푸드 = new MenuGroup(1L, "패스트푸드");
        치즈버거 = new Product(1L, "치즈버거", 치즈버거_가격);
        감자튀김 = new Product(2L, "감자튀김", 감자튀김_가격);
        콜라 = new Product(3L, "콜라", 콜라_가격);
        치즈버거세트_치즈버거 = new MenuProduct(1L, 1L, 치즈버거.getId(), 1);
        치즈버거세트_감자튀김 = new MenuProduct(2L, 1L, 감자튀김.getId(), 1);
        치즈버거세트_콜라 = new MenuProduct(3L, 1L, 콜라.getId(), 1);
        치즈버거세트 = new Menu(1L, "치즈버거세트", 치즈버거세트_가격, 패스트푸드.getId(), Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void createSuccess() {
        // Given
        given(menuDao.save(any())).willReturn(치즈버거세트);
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(치즈버거.getId())).willReturn(Optional.of(치즈버거));
        given(productDao.findById(감자튀김.getId())).willReturn(Optional.of(감자튀김));
        given(productDao.findById(콜라.getId())).willReturn(Optional.of(콜라));
        given(menuProductDao.save(치즈버거세트_치즈버거)).willReturn(치즈버거세트_치즈버거);
        given(menuProductDao.save(치즈버거세트_감자튀김)).willReturn(치즈버거세트_감자튀김);
        given(menuProductDao.save(치즈버거세트_콜라)).willReturn(치즈버거세트_콜라);

        // When
        menuService.create(치즈버거세트);

        // Then
        verify(menuDao, times(1)).save(any());
        verify(menuGroupDao, times(1)).existsById(any());
        verify(productDao, times(3)).findById(any());
        verify(menuProductDao, times(3)).save(any());
    }

    @DisplayName("메뉴는 존재하는 메뉴그룹에 포함되어야 한다.")
    @Test
    void create_Fail_01() {
        // Given
        given(menuGroupDao.existsById(any())).willReturn(false);

        // When & Then
        assertThatThrownBy(() -> menuService.create(치즈버거세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 메뉴상품들과 함께 등록한다.")
    @Test
    void create_Fail_02() {
        // Given
        List<MenuProduct> 빈_메뉴_상품_목록 = new ArrayList<>();
        치즈버거세트.setMenuProducts(빈_메뉴_상품_목록);

        // When & Then
        assertThatThrownBy(() -> menuService.create(치즈버거세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 필수입력항목이다.")
    @Test
    void create_Fail_03() {
        // Given
        치즈버거세트.setPrice(null);

        // When & Then
        assertThatThrownBy(() -> menuService.create(치즈버거세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 0원 이상이어야 한다.")
    @Test
    void create_Fail_04() {
        // Given
        치즈버거세트.setPrice(new BigDecimal(-1));

        // When & Then
        assertThatThrownBy(() -> menuService.create(치즈버거세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 메뉴상품들 가격의 합을 초과할 수 없다.")
    @Test
    void create_Fail_05() {
        // Given
        BigDecimal 치즈버거상품들의_가격합 = 치즈버거_가격.add(감자튀김_가격).add(콜라_가격);
        BigDecimal 치즈버거상품들의_가격합을_초과한_가격 = 치즈버거상품들의_가격합.add(new BigDecimal(1));
        치즈버거세트.setPrice(치즈버거상품들의_가격합을_초과한_가격);

        // When & Then
        assertThatThrownBy(() -> menuService.create(치즈버거세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // Given
        List<Menu> menus = new ArrayList<>();
        menus.add(치즈버거세트);
        given(menuDao.findAll()).willReturn(menus);
        given(menuProductDao.findAllByMenuId(any())).willReturn(new ArrayList<>(Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김)));

        // When & Then
        assertThat(menuService.list()).hasSize(1);
        verify(menuProductDao, times(1)).findAllByMenuId(any());
    }

}
