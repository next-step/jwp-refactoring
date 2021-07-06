package kitchenpos.menu.application;

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
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
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
    public static final Product 치즈버거 = new Product(1L, "치즈버거", 치즈버거_가격);
    public static final Product 감자튀김 = new Product(2L, "감자튀김", 감자튀김_가격);
    public static final Product 콜라 = new Product(3L, "콜라", 콜라_가격);
    public static final Long 치즈버거세트_메뉴ID = 1L;
    public static final MenuProduct 치즈버거세트_치즈버거 = new MenuProduct(치즈버거세트_메뉴ID, 치즈버거.getId(), 1L);
    public static final MenuProduct 치즈버거세트_감자튀김 = new MenuProduct(치즈버거세트_메뉴ID, 감자튀김.getId(), 1L);
    public static final MenuProduct 치즈버거세트_콜라 = new MenuProduct(치즈버거세트_메뉴ID, 콜라.getId(), 1L);
    public static final MenuGroup 패스트푸드 = new MenuGroup(1L, "패스트푸드");

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

    @DisplayName("메뉴를 등록한다.")
    @Test
    void createSuccess() {
        // Given
        Menu 치즈버거세트 = new Menu("치즈버거세트", 치즈버거세트_가격, 패스트푸드.getId(), Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
        MenuRequest 메뉴 = MenuRequest.of(치즈버거세트);
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(치즈버거.getId())).willReturn(Optional.of(치즈버거));
        given(productDao.findById(감자튀김.getId())).willReturn(Optional.of(감자튀김));
        given(productDao.findById(콜라.getId())).willReturn(Optional.of(콜라));
        given(menuDao.save(any())).willReturn(치즈버거세트);

        // When
        menuService.create(메뉴);

        // Then
        verify(menuGroupDao, times(1)).existsById(any());
        verify(productDao, times(3)).findById(any());
        verify(menuDao, times(1)).save(any());
    }

    @DisplayName("메뉴는 존재하는 메뉴그룹에 포함되어야 한다.")
    @Test
    void create_Fail_01() {
        // Given
        Menu 치즈버거세트 = new Menu(1L, "치즈버거세트", 치즈버거세트_가격, 패스트푸드, Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
        MenuRequest 메뉴 = MenuRequest.of(치즈버거세트);
        given(menuGroupDao.existsById(any())).willReturn(false);

        // When & Then
        assertThatThrownBy(() -> menuService.create(메뉴))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴는 메뉴상품들과 함께 등록한다.")
    @Test
    void create_Fail_02() {
        // Given
        List<MenuProductRequest> 빈_메뉴_상품_목록 = new ArrayList<>();
        MenuRequest 메뉴상품이_없는_치즈버거세트 = new MenuRequest("치즈버거세트", 치즈버거세트_가격, 패스트푸드.getId(), 빈_메뉴_상품_목록);

        // When & Then
        assertThatThrownBy(() -> menuService.create(메뉴상품이_없는_치즈버거세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 필수입력항목이다.")
    @Test
    void create_Fail_03() {
        // Given
        Menu 가격이없는_치즈버거세트 = new Menu(1L, "치즈버거세트", null, 패스트푸드, Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
        MenuRequest 메뉴 = MenuRequest.of(가격이없는_치즈버거세트);

        // When & Then
        assertThatThrownBy(() -> menuService.create(메뉴))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 0원 이상이어야 한다.")
    @Test
    void create_Fail_04() {
        // Given
        Menu 가격이_음수인_치즈버거세트 = new Menu(1L, "치즈버거세트", new BigDecimal(-1), 패스트푸드, Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
        MenuRequest 메뉴 = MenuRequest.of(가격이_음수인_치즈버거세트);

        // When & Then
        assertThatThrownBy(() -> menuService.create(메뉴))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 메뉴상품들 가격의 합을 초과할 수 없다.")
    @Test
    void create_Fail_05() {
        // Given
        BigDecimal 치즈버거상품들의_가격합 = 치즈버거_가격.add(감자튀김_가격).add(콜라_가격);
        BigDecimal 치즈버거상품들의_가격합을_초과한_가격 = 치즈버거상품들의_가격합.add(new BigDecimal(1));
        Menu 가격을_초과한_치즈버거세트 = new Menu("치즈버거세트", 치즈버거상품들의_가격합을_초과한_가격, 패스트푸드.getId(), Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
        MenuRequest 메뉴 = MenuRequest.of(가격을_초과한_치즈버거세트);

        // When & Then
        assertThatThrownBy(() -> menuService.create(메뉴))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // Given
        Menu 치즈버거세트 = new Menu(1L, "치즈버거세트", 치즈버거세트_가격, 패스트푸드, Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
        List<Menu> menus = new ArrayList<>(Arrays.asList(치즈버거세트));
        given(menuDao.findAll()).willReturn(menus);

        // When & Then
        assertThat(menuService.list()).hasSize(1);
        verify(menuDao, times(1)).findAll();
    }

}
