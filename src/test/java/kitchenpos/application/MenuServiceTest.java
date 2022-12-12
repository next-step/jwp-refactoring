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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuProductDao menuProductDao;
    @InjectMocks
    private MenuService menuService;

    private Product 후라이드치킨_상품;
    private Product 콜라_상품;

    private MenuGroup 한마리메뉴_메뉴그룹;

    private Menu 후라이드치킨_메뉴;
    private Menu 콜라_메뉴;

    private MenuProduct 후라이드치킨_메뉴상품;
    private MenuProduct 콜라_메뉴상품;

    private List<MenuProduct> 메뉴_상품;

    @BeforeEach
    void setUp() {
        한마리메뉴_메뉴그룹 = MenuGroup.of(1L, "한마리메뉴");

        후라이드치킨_상품 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(16_000L));
        콜라_상품 = Product.of(2L, "콜라", BigDecimal.valueOf(2_000L));

        후라이드치킨_메뉴 = Menu.of(1L, "후라이드치킨", 후라이드치킨_상품.getPrice(), 한마리메뉴_메뉴그룹.getId());
        콜라_메뉴 = Menu.of(2L, "콜라", 콜라_상품.getPrice(), 한마리메뉴_메뉴그룹.getId());

        후라이드치킨_메뉴상품 = MenuProduct.of(1L, 후라이드치킨_메뉴.getId(), 후라이드치킨_상품.getId(), 1L);
        콜라_메뉴상품 = MenuProduct.of(2L, 콜라_메뉴.getId(), 콜라_상품.getId(), 1L);

        메뉴_상품 = Arrays.asList(후라이드치킨_메뉴상품, 콜라_메뉴상품);
    }

    @DisplayName("메뉴의 가격은 무조건 존재해야 한다.")
    @Test
    void 메뉴의_가격은_무조건_존재해야_한다() {
        // given
        후라이드치킨_메뉴.setPrice(null);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(후라이드치킨_메뉴));
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다.")
    @Test
    void 상품의_가격은_0원_이상이어야_한다() {
        // given
        후라이드치킨_메뉴.setPrice(new BigDecimal(-1));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(후라이드치킨_메뉴));
    }

    @DisplayName("메뉴 그룹 아이디가 존재하지 않으면 등록할 수 없다.")
    @Test
    void 메뉴_그룹_아이디가_존재하지_않으면_등록할_수_없다() {
        // given
        when(menuGroupDao.existsById(anyLong())).thenReturn(false);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(후라이드치킨_메뉴));
    }

    @DisplayName("메뉴 상품은 모두 등록된 상품이어야 한다.")
    @Test
    void 메뉴_상품은_모두_등록된_상품이어야_한다() {
        // given
        후라이드치킨_메뉴.setMenuProducts(메뉴_상품);
        when(menuGroupDao.existsById(후라이드치킨_메뉴.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(후라이드치킨_메뉴상품.getProductId())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(후라이드치킨_메뉴));
    }

    @DisplayName("메뉴의 가격은 메뉴 상품들의 가격의 총 가격보다 클 수 없다.")
    @Test
    void 메뉴의_가격은_메뉴_상품들의_가격의_총_가격보다_클_수_없다() {
        // given
        BigDecimal 총_가격 = 후라이드치킨_상품.getPrice()
                .add(콜라_상품.getPrice())
                .add(new BigDecimal(1));

        후라이드치킨_메뉴.setPrice(총_가격);
        후라이드치킨_메뉴.setMenuProducts(메뉴_상품);
        when(menuGroupDao.existsById(후라이드치킨_메뉴.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(후라이드치킨_메뉴상품.getProductId())).thenReturn(Optional.of(후라이드치킨_상품));
        when(productDao.findById(콜라_메뉴상품.getProductId())).thenReturn(Optional.of(콜라_상품));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(후라이드치킨_메뉴));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void 메뉴를_등록한다() {
        // given
        후라이드치킨_메뉴.setMenuProducts(메뉴_상품);
        when(menuGroupDao.existsById(후라이드치킨_메뉴.getMenuGroupId())).thenReturn(true);
        when(productDao.findById(후라이드치킨_메뉴상품.getProductId())).thenReturn(Optional.of(후라이드치킨_상품));
        when(productDao.findById(콜라_메뉴상품.getProductId())).thenReturn(Optional.of(콜라_상품));
        when(menuDao.save(후라이드치킨_메뉴)).thenReturn(후라이드치킨_메뉴);
        when(menuProductDao.save(후라이드치킨_메뉴상품)).thenReturn(후라이드치킨_메뉴상품);
        when(menuProductDao.save(콜라_메뉴상품)).thenReturn(콜라_메뉴상품);

        // when
        Menu 저장된_메뉴 = menuService.create(후라이드치킨_메뉴);

        // then
        assertAll(() -> {
            assertThat(저장된_메뉴.getId()).isEqualTo(후라이드치킨_메뉴.getId());
            assertThat(저장된_메뉴.getName()).isEqualTo(후라이드치킨_메뉴.getName());
            assertThat(저장된_메뉴.getPrice()).isEqualTo(후라이드치킨_메뉴.getPrice());
            assertThat(저장된_메뉴.getMenuGroupId()).isEqualTo(후라이드치킨_메뉴.getMenuGroupId());
            assertThat(저장된_메뉴.getMenuProducts()).hasSize(메뉴_상품.size());
        });
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void 메뉴를_조회한다() {
        // given
        when(menuDao.findAll()).thenReturn(Collections.singletonList(후라이드치킨_메뉴));
        when(menuProductDao.findAllByMenuId(후라이드치킨_메뉴.getId())).thenReturn(메뉴_상품);

        // when
        List<Menu> 메뉴_목록 = menuService.list();

        // then
        assertAll(() -> {
            assertThat(메뉴_목록).hasSize(1);
            assertThat(메뉴_목록).containsExactly(후라이드치킨_메뉴);
        });
    }
}
