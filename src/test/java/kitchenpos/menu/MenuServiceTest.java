package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.dao.MenuProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
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
    MenuRepository menuDao;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    MenuProductRepository menuProductRepository;

    @Mock
    ProductRepository productRepository;

    Menu 후라이드치킨;
    Product 후라이드;
    MenuProduct 후라이드치킨상품;
    MenuRequest 상품;

    @BeforeEach
    void setUp() {
        후라이드 = new Product("후라이드", BigDecimal.valueOf(15000));
        후라이드치킨 = new Menu("후라이드치킨", BigDecimal.valueOf(15000));
        후라이드치킨상품 = new MenuProduct(후라이드치킨, 후라이드.getId(), 1L);
        후라이드치킨.setMenuProducts(Collections.singletonList(후라이드치킨상품));

        상품 = new MenuRequest(후라이드치킨.getName(), BigDecimal.valueOf(15000), 후라이드치킨.getMenuGroupId(), 후라이드치킨.getMenuProducts());
    }

    @Test
    @DisplayName("메뉴를 저장한다")
    void create() {
        // given
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(any())).willReturn(Optional.ofNullable(후라이드));
        given(menuDao.save(any())).willReturn(후라이드치킨);

        // when
        MenuResponse actual = menuService.create(상품);

        // then
        assertThat(actual.getName()).isEqualTo("후라이드치킨");
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴의 금액은 0원 이상이다")
    void create_priceException() {
        // given
        MenuRequest 양념치킨 = new MenuRequest("양념치킨", BigDecimal.valueOf(-1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(상품)
        );
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴는 존재하는 메뉴그룹 정보를 가지고 있다")
    void create_nonExistMenuGroupError() {
        // given
        given(menuGroupRepository.existsById(후라이드치킨.getMenuGroupId())).willReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(상품)
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
                () -> menuService.create(상품)
        );
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴상품에 속한 상품들의 금액 합보다 메뉴 가격이 작아야 한다")
    void create_totalPriceError() {
        // given
        MenuRequest 상품 = new MenuRequest(후라이드치킨.getName(), BigDecimal.valueOf(20000), 후라이드치킨.getMenuGroupId(), 후라이드치킨.getMenuProducts());
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(후라이드치킨상품.getProductId())).willReturn(Optional.ofNullable(후라이드));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(상품)
        );
    }

    @Test
    @DisplayName("메뉴 리스트를 조회한다")
    void list() {
        // given
        given(menuDao.findAll()).willReturn(Collections.singletonList(후라이드치킨));
        given(menuProductRepository.findAllByMenuId(후라이드치킨.getId())).willReturn(Collections.singletonList(후라이드치킨상품));

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
