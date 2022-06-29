package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.dao.MenuProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    MenuService menuService;

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    MenuProductRepository menuProductRepository;

    @Mock
    ProductRepository productRepository;

    Menu 후라이드치킨 = new Menu();
    Product 후라이드;
    MenuProduct 후라이드치킨상품;

    @BeforeEach
    void setUp() {
        createProduct();
        createMenu();
        createMenuProduct();
    }

    void createProduct() {
        후라이드 = new Product("후라이드", BigDecimal.valueOf(15000));
        후라이드.setId(1L);
    }

    void createMenu() {
        후라이드치킨.setId(1L);
        후라이드치킨.setName("후라이드치킨");
        후라이드치킨.setPrice(new BigDecimal(15000));
    }

    void createMenuProduct() {
        후라이드치킨상품 = new MenuProduct(후라이드치킨.getId(), 후라이드.getId(), 1L);
        후라이드치킨.setMenuProducts(Collections.singletonList(후라이드치킨상품));
    }

    @Test
    @DisplayName("메뉴를 저장한다")
    void create() {
        // given
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(any())).willReturn(Optional.ofNullable(후라이드));
        given(menuDao.save(any())).willReturn(후라이드치킨);
        given(menuProductRepository.save(any())).willReturn(후라이드치킨상품);

        // when
        Menu actual = menuService.create(후라이드치킨);

        // then
        assertThat(actual).isEqualTo(후라이드치킨);
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴의 금액은 0원 이상이다")
    void create_priceException() {
        // given
        Menu 양념치킨 = new Menu();
        양념치킨.setPrice(new BigDecimal(-1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(양념치킨)
        );
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴는 존재하는 메뉴그룹 정보를 가지고 있다")
    void create_nonExistMenuGroupError() {
        // given
        given(menuGroupRepository.existsById(후라이드치킨.getMenuGroupId())).willReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(후라이드치킨)
        );
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴는 존재하는 상품 정보를 가져야 한다")
    void create_nonProductInfoError() {
        // given
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(후라이드치킨상품.getProductId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(후라이드치킨)
        );
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴상품에 속한 상품들의 금액 합보다 메뉴 가격이 작아야 한다")
    void create_totalPriceError() {
        // given
        후라이드치킨.setPrice(new BigDecimal(20000));
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(후라이드치킨상품.getProductId())).willReturn(Optional.ofNullable(후라이드));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(후라이드치킨)
        );
    }

    @Test
    @DisplayName("메뉴 리스트를 조회한다")
    void list() {
        // given
        given(menuDao.findAll()).willReturn(Collections.singletonList(후라이드치킨));
        given(menuProductRepository.findAllByMenuId(후라이드치킨.getId())).willReturn(Collections.singletonList(후라이드치킨상품));

        List<Menu> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual).containsExactly(후라이드치킨)
        );
    }
}
