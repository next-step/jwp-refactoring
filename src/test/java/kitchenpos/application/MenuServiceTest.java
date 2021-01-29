package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@DisplayName("애플리케이션 테스트 보호 - 메뉴 서비스")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuGroup 치킨세트;
    private Menu 후라이드한마리양념치킨한마리;
    private MenuProduct 후라이드치킨한마리;
    private MenuProduct 양념치킨한마리;

    @BeforeEach
    public void setup() {
        후라이드치킨 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));

        양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(16000));

        치킨세트 = new MenuGroup("후라이드앙념치킨");

        후라이드한마리양념치킨한마리 = new Menu();
        후라이드한마리양념치킨한마리.setId(1L);
        후라이드한마리양념치킨한마리.setName("후라이드+양념");
        후라이드한마리양념치킨한마리.setMenuGroupId(치킨세트.getId());
        후라이드한마리양념치킨한마리.setPrice(BigDecimal.valueOf(32000));

        후라이드치킨한마리 = new MenuProduct();
        후라이드치킨한마리.setProductId(후라이드치킨.getId());
        후라이드치킨한마리.setQuantity(1);

        양념치킨한마리 = new MenuProduct();
        양념치킨한마리.setProductId(양념치킨.getId());
        양념치킨한마리.setQuantity(1);

        후라이드한마리양념치킨한마리.setMenuProducts(Arrays.asList(후라이드치킨한마리, 양념치킨한마리));
    }

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuDao menuDao;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        given(menuGroupRepository.existsById(치킨세트.getId())).willReturn(true);
        given(productRepository.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));
        given(productRepository.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));
        given(menuDao.save(후라이드한마리양념치킨한마리)).willReturn(후라이드한마리양념치킨한마리);
        given(menuProductDao.save(후라이드치킨한마리)).willReturn(후라이드치킨한마리);
        given(menuProductDao.save(양념치킨한마리)).willReturn(양념치킨한마리);

        Menu savedMenu = menuService.create(후라이드한마리양념치킨한마리);

        assertThat(savedMenu).isEqualTo(후라이드한마리양념치킨한마리);
    }

    @DisplayName("메뉴 생성 예외: 메뉴 가격이 없음")
    @Test
    void createThrowExceptionWhenNoMenuPrice() {
        후라이드한마리양념치킨한마리.setPrice(null);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드한마리양념치킨한마리));
    }

    @DisplayName("메뉴 생성 예외: 메뉴 가격이 0보다 작음")
    @Test
    void createThrowExceptionWhenMenuPriceLessThanZero() {
        후라이드한마리양념치킨한마리.setPrice(BigDecimal.valueOf(-1));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드한마리양념치킨한마리));
    }

    @DisplayName("메뉴 생성 예외: 메뉴 그룹이 없음")
    @Test
    void createThrowExceptionWhenNoMenuGroup() {
        given(menuGroupRepository.existsById(치킨세트.getId())).willReturn(false);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드한마리양념치킨한마리));
    }

    @DisplayName("메뉴 생성 예외: 메뉴에 속한 메뉴상품 목록 중 데이터베이스에 없는 상품이 있음")
    @Test
    void createThrowExceptionWhenNoMenuProducts() {
        given(menuGroupRepository.existsById(치킨세트.getId())).willReturn(true);
        given(productRepository.findById(후라이드치킨.getId())).willReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드한마리양념치킨한마리));
    }

    @DisplayName("메뉴 생성 예외: 조회한 상품의 가격과 메뉴 상품의 수량을 곱한 금액의 합이 메뉴의 가격보다 작음")
    @Test
    void createThrowExceptionWhenMenuPriceNotEqualsSumOfProductPriceAndMenuProductQuantity() {
        given(menuGroupRepository.existsById(치킨세트.getId())).willReturn(true);
        given(productRepository.findById(후라이드치킨.getId())).willReturn(Optional.of(후라이드치킨));
        given(productRepository.findById(양념치킨.getId())).willReturn(Optional.of(양념치킨));
        후라이드한마리양념치킨한마리.setPrice(BigDecimal.valueOf(99999));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(후라이드한마리양념치킨한마리));
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void list() {
        given(menuDao.findAll()).willReturn(Collections.singletonList(후라이드한마리양념치킨한마리));
        given(menuProductDao.findAllByMenuId(후라이드한마리양념치킨한마리.getId()))
                .willReturn(후라이드한마리양념치킨한마리.getMenuProducts());

        List<Menu> menus = menuService.list();

        assertThat(menus).containsExactly(후라이드한마리양념치킨한마리);
        assertThat(menus.get(0).getMenuProducts()).containsAll(후라이드한마리양념치킨한마리.getMenuProducts());
    }

}
