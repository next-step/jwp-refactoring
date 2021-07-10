package kitchenpos.menu;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductDao;
import kitchenpos.product.domain.ProductDao;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    private Product 후라이드;
    private MenuGroup 한마리메뉴;
    private Menu 후라이드치킨;
    private MenuProduct 후라이드_한마리;

    @BeforeEach
    void setUp() {
        후라이드 = new Product();
        후라이드.setId(1L);
        후라이드.setName("후라이드");
        후라이드.setPrice(BigDecimal.valueOf(16000));

        한마리메뉴 = new MenuGroup("한마리메뉴");
        한마리메뉴.setId(1L);

        후라이드_한마리 = new MenuProduct();
        후라이드_한마리.setMenuId(1L);
        후라이드_한마리.setProductId(후라이드.getId());
        후라이드_한마리.setQuantity(1L);

        후라이드치킨 = new Menu();
        후라이드치킨.setId(1L);
        후라이드치킨.setName("후라이드치킨");
        후라이드치킨.setMenuProducts(Arrays.asList(후라이드_한마리));
    }

    @DisplayName("0원 이상의 가격으로 메뉴를 등록한다")
    @Test
    void 메뉴_등록() {
        //Given
        후라이드치킨.setMenuProducts(Arrays.asList(후라이드_한마리));
        후라이드치킨.setMenuGroupId(한마리메뉴.getId());
        후라이드치킨.setPrice(BigDecimal.valueOf(16000));

        when(menuGroupRepository.existsById(한마리메뉴.getId())).thenReturn(true);
        when(productDao.findById(후라이드.getId())).thenReturn(Optional.of(후라이드));
        when(menuDao.save(후라이드치킨)).thenReturn(후라이드치킨);
        when(menuProductDao.save(후라이드_한마리)).thenReturn(후라이드_한마리);

        //When
        Menu 생성된_메뉴 = menuService.create(후라이드치킨);

        //Then
        assertThat(생성된_메뉴.getId()).isNotNull();
        assertThat(생성된_메뉴.getName()).isEqualTo("후라이드치킨");
    }

    @DisplayName("가격이 입력되지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 가격_입력되지_않음_예외발생() {
        //Given
        후라이드치킨.setPrice(null);

        //When+Then
        //When + Then
        assertThatThrownBy(() -> menuService.create(후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0원 미만인 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 가격_0원미만_예외발생() {
        //Given
        후라이드치킨.setPrice(BigDecimal.valueOf(-1L));

        //When + Then
        assertThatThrownBy(() -> menuService.create(후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 메뉴상품 목록의 가격합보다 큰 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 가격_메뉴상품_목록_가격합보다_큰_경우_예외발생() {
        //Given
        후라이드치킨.setPrice(후라이드.getPrice().add(BigDecimal.valueOf(1000)));

        //When + Then
        assertThatThrownBy(() -> menuService.create(후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 기존에 등록되어 있지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 메뉴그룹_등록되어있지_않은_경우_예외발생() {
        //Given
        후라이드치킨.setPrice(BigDecimal.valueOf(16000));
        후라이드치킨.setMenuGroupId(99L);
        when(menuGroupRepository.existsById(99L)).thenReturn(false);

        //When + Then
        assertThatThrownBy(() -> menuService.create(후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품이 기존에 상품으로 등록되어 있지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 메뉴상품_등록되어있지_않은_경우_예외발생() {
        //Given
        후라이드치킨.setMenuGroupId(1L);
        후라이드치킨.setPrice(BigDecimal.valueOf(16000));
        when(menuGroupRepository.existsById(1L)).thenReturn(true);
        when(productDao.findById(후라이드.getId())).thenThrow(IllegalArgumentException.class);

        //When + Then
        assertThatThrownBy(() -> menuService.create(후라이드치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void 메뉴_목록_조회() {
        //Given
        List<Menu> 입력한_메뉴_목록 = new ArrayList<>(Arrays.asList(후라이드치킨));
        when(menuDao.findAll()).thenReturn(입력한_메뉴_목록);

        //When
        List<Menu> 조회된_메뉴_목록 = menuService.list();

        //Then
        assertThat(조회된_메뉴_목록).isNotNull()
                .hasSize(입력한_메뉴_목록.size())
                .containsExactly(후라이드치킨);

    }
}
