package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 하와이안피자;
    private Product 콜라;
    private Product 피클;
    private MenuGroup 피자;
    private Menu 하와이안피자세트;
    private MenuProduct 하와이안피자상품;
    private MenuProduct 콜라상품;
    private MenuProduct 피클상품;


    @BeforeEach
    void setUp() {
        하와이안피자 = new Product(1L, "하와이안피자", BigDecimal.valueOf(15_000));
        콜라 = new Product(2L, "콜라", BigDecimal.valueOf(2_000));
        피클 = new Product(3L, "피클", BigDecimal.valueOf(1_000));
        피자 = new MenuGroup(1L, "피자");
        하와이안피자세트 = new Menu(1L, "하와이안피자세트", BigDecimal.valueOf(18_000L), 피자.getId(), new ArrayList<>());
        하와이안피자상품 = new MenuProduct(1L, 하와이안피자세트.getId(), 하와이안피자.getId(), 1L);
        콜라상품 = new MenuProduct(2L, 하와이안피자세트.getId(), 콜라.getId(), 1L);
        피클상품 = new MenuProduct(3L, 하와이안피자세트.getId(), 피클.getId(), 1L);
        하와이안피자세트.setMenuProducts(Arrays.asList(하와이안피자상품, 콜라상품, 피클상품));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        when(menuGroupRepository.existsById(하와이안피자세트.getMenuGroupId())).thenReturn(true);
        when(productRepository.findById(하와이안피자상품.getProductId())).thenReturn(Optional.of(하와이안피자));
        when(productRepository.findById(콜라상품.getProductId())).thenReturn(Optional.of(콜라));
        when(productRepository.findById(피클상품.getProductId())).thenReturn(Optional.of(피클));
        when(menuDao.save(하와이안피자세트)).thenReturn(하와이안피자세트);
        when(menuProductDao.save(하와이안피자상품)).thenReturn(하와이안피자상품);
        when(menuProductDao.save(콜라상품)).thenReturn(콜라상품);
        when(menuProductDao.save(피클상품)).thenReturn(피클상품);

        // when
        Menu result = menuService.create(하와이안피자세트);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(하와이안피자세트.getId()),
            () -> assertThat(result.getName()).isEqualTo(하와이안피자세트.getName())
        );
    }

    @DisplayName("메뉴 가격이 null이면 예외가 발생한다.")
    @Test
    void createMenuNullPriceException() {
        // given
        하와이안피자세트 = new Menu(1L, "하와이안피자세트", null, 피자.getId(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(하와이안피자세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0원보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -1000, -10000})
    void crateMenuUnderZeroPriceException(int price) {
        // given
        하와이안피자세트 = new Menu(1L, "하와이안피자세트", BigDecimal.valueOf(price), 피자.getId(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(하와이안피자세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 없는 메뉴를 생성하면 예외가 발생한다.")
    @Test
    void createMenuNotExistMenuGroupException() {
        // given
        하와이안피자세트 = new Menu(1L, "하와이안피자세트", BigDecimal.valueOf(18_000L), 피자.getId(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> menuService.create(하와이안피자세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void notExistProductException() {
        // given
        하와이안피자세트 = new Menu(1L, "하와이안피자세트", BigDecimal.valueOf(18_000L), 피자.getId(),
            Collections.singletonList(하와이안피자상품));
        when(menuGroupRepository.existsById(하와이안피자세트.getMenuGroupId())).thenReturn(true);
        when(productRepository.findById(하와이안피자상품.getProductId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> menuService.create(하와이안피자세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 모든 상품 가격의 합보다 작아야 한다.")
    @Test
    void menuPriceBiggerAllProductPriceException() {
        // given
        하와이안피자세트.setPrice(BigDecimal.valueOf(20_000));
        when(menuGroupRepository.existsById(하와이안피자세트.getMenuGroupId())).thenReturn(true);
        when(productRepository.findById(하와이안피자상품.getProductId())).thenReturn(Optional.of(하와이안피자));
        when(productRepository.findById(콜라상품.getProductId())).thenReturn(Optional.of(콜라));
        when(productRepository.findById(피클상품.getProductId())).thenReturn(Optional.of(피클));

        // when & then
        assertThatThrownBy(() -> menuService.create(하와이안피자세트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회할 수 있다.")
    @Test
    void findAllMenu() {
        // given
        List<Menu> menus = Arrays.asList(하와이안피자세트);
        when(menuDao.findAll()).thenReturn(menus);
        when(menuProductDao.findAllByMenuId(하와이안피자세트.getId())).thenReturn(Arrays.asList(하와이안피자상품, 콜라상품, 피클상품));

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result).hasSize(1)
            .containsExactly(하와이안피자세트);
    }
}
