package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menuGroup.MenuGroup;
import kitchenpos.domain.menuGroup.MenuGroupRepository;
import kitchenpos.domain.menuProduct.MenuProduct;
import kitchenpos.domain.menuProduct.MenuProductRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menuProduct.MenuProductRequest;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 관련 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    MenuProductRepository menuProductRepository;

    @InjectMocks
    MenuService menuService;

    private MenuProduct requestMenuProductSeq1;
    private MenuProduct requestMenuProductSeq2;
    private List<MenuProduct> menuProducts;
    private Product 초밥;
    private Product 우동;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        초밥 = new Product(1L, "초밥", BigDecimal.valueOf(10000));
        우동 = new Product(2L, "우동", BigDecimal.valueOf(3000));
        menuGroup = new MenuGroup(1L, "런치메뉴");

        //Menu menu1 = 메뉴_데이터_생성(1L, BigDecimal.valueOf(10000));
        //Menu menu2 = 메뉴_데이터_생성(2L, BigDecimal.valueOf(20000));

        requestMenuProductSeq1 = new MenuProduct(1L, null, 초밥, 2);
        requestMenuProductSeq2 = new MenuProduct(2L, null, 우동, 2);
        menuProducts = Arrays.asList(requestMenuProductSeq1, requestMenuProductSeq2);
    }

    @DisplayName("메뉴를 생성할 수 있다")
    @Test
    void create() {
        // given
        MenuRequest request = 메뉴_요청_데이터_생성(BigDecimal.valueOf(26000));
        Menu 예상값 = 메뉴_데이터_생성(1L, BigDecimal.valueOf(26000));
        given(menuGroupRepository.findById(any())).willReturn(Optional.ofNullable(menuGroup));
        given(productRepository.findById(1L)).willReturn(Optional.ofNullable(초밥));
        given(productRepository.findById(2L)).willReturn(Optional.ofNullable(우동));
        given(productRepository.existsAllByIdIn(anyList())).willReturn(true);
        given(menuRepository.save(any(Menu.class))).willReturn(예상값);

        // when
        MenuResponse 메뉴_생성_결과 = 메뉴_생성(request);

        // then
        메뉴_값_비교(메뉴_생성_결과, MenuResponse.of(예상값));
    }

    @DisplayName("메뉴를 생성할 수 있다 - 메뉴의 가격은 0원 이상이어야 한다")
    @Test
    void create_exception1() {
        // given
        MenuRequest request1 = 메뉴_요청_데이터_생성(null);

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request1))
                .isInstanceOf(NoSuchElementException.class);

        // given
        MenuRequest request2 = 메뉴_요청_데이터_생성(BigDecimal.valueOf(-1));

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request2))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("메뉴를 생성할 수 있다 - 유효한 메뉴 그룹이 지정되어야 한다")
    @Test
    void create_exception2() {
        // given
        MenuRequest request = 메뉴_요청_데이터_생성( BigDecimal.valueOf(25000));
        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("메뉴를 생성할 수 있다 - 유효한 상품들이 지정되어야 한다")
    @Test
    void create_exception3() {
        // given
        MenuRequest request = 메뉴_요청_데이터_생성(BigDecimal.valueOf(25000));
        given(menuGroupRepository.findById(any())).willReturn(Optional.ofNullable(menuGroup));
        given(productRepository.existsAllByIdIn(anyList())).willReturn(false);
        given(productRepository.findById(1L)).willReturn(Optional.ofNullable(초밥));
        given(productRepository.findById(2L)).willReturn(Optional.ofNullable(우동));

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 수 있다" +
            " - 메뉴의 가격은 포함된 상품들의 금액에 합보다 작거나 같아야 한다")
    @Test
    void create_exception4() {
        // given
        MenuRequest request = 메뉴_요청_데이터_생성(BigDecimal.valueOf(26001));
        given(menuGroupRepository.findById(any())).willReturn(Optional.ofNullable(menuGroup));
        given(productRepository.findById(1L)).willReturn(Optional.ofNullable(초밥));
        given(productRepository.findById(2L)).willReturn(Optional.ofNullable(우동));

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void list() {
        // given
        Menu menuId1 = 메뉴_데이터_생성(1L, BigDecimal.valueOf(10000));
        Menu menuId2 = 메뉴_데이터_생성(2L, BigDecimal.valueOf(20000));
        menuId1.bindMenuProducts();
        menuId2.bindMenuProducts();
        List<Menu> 예상값 = Arrays.asList(
                menuId1,
                menuId2
        );
        given(menuRepository.findAll()).willReturn(예상값);

        // when
        List<MenuResponse> 메뉴_목록_조회_결과 = menuService.list();

        // then
        assertAll(
                () -> 메뉴_값_비교(메뉴_목록_조회_결과.get(0), MenuResponse.of(예상값.get(0))),
                () -> 메뉴_값_비교(메뉴_목록_조회_결과.get(1), MenuResponse.of(예상값.get(1)))
        );
    }

    private MenuRequest 메뉴_요청_데이터_생성(BigDecimal price) {
        return new MenuRequest("메뉴이름", price, 1L,
                menuProducts.stream()
                        .map(MenuProductRequest::of)
                        .collect(Collectors.toList()));
    }

    private Menu 메뉴_데이터_생성(Long id, BigDecimal price) {
        return new Menu(id, "메뉴이름", price, new MenuGroup(1L, "런치메뉴"), menuProducts);
    }

    private void 메뉴_값_비교(MenuResponse result, MenuResponse expectation) {
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectation.getId()),
                () -> assertThat(result.getName()).isEqualTo(expectation.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(expectation.getPrice()),
                () -> assertThat(result.getMenuGroupId()).isEqualTo(expectation.getMenuGroupId()),
                () -> assertThat(result.getMenuProducts()).isEqualTo(expectation.getMenuProducts())
        );
    }

    private Optional<Product> 상품_조회(long id) {
        return productRepository.findById(id);
    }

    private MenuProduct 메뉴_상품_생성(MenuProduct menuProduct) {
        return menuProductRepository.save(menuProduct);
    }

    private MenuResponse 메뉴_생성(MenuRequest menuRequest) {
        return menuService.create(menuRequest);
    }
}
