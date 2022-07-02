package kitchenpos.application;

import kitchenpos.fixture.TestMenuGroupFactory;
import kitchenpos.fixture.TestMenuRequestFactory;
import kitchenpos.fixture.TestProductFactory;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 분식류;
    private Product 진매;
    private Product 진순이;
    private MenuProductRequest 메뉴_진매;
    private MenuProductRequest 메뉴_진순이;
    private MenuRequest 메뉴_요청;
    private Menu 메뉴;

    @BeforeEach
    void setUp() {
        분식류 = TestMenuGroupFactory.create(1L, "분식류");

        진매 = TestProductFactory.create(1L, "진라면 매운맛", 5_000);
        진순이 = TestProductFactory.create(2L, "진라면 순한맛", 5_000);
        메뉴_진매 = new MenuProductRequest(1L, 1);
        메뉴_진순이 = new MenuProductRequest(2L, 1);

        메뉴_요청 = TestMenuRequestFactory.toMenuRequest( "라면세트", 4_000, 1L, Arrays.asList(메뉴_진매, 메뉴_진순이));
        메뉴 = Menu.of(메뉴_요청, MenuProducts.of(Arrays.asList(MenuProduct.of(진매, 1), MenuProduct.of(진순이, 1))));
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다")
    void create() throws Exception {
        // given
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        given(menuRepository.save(any(Menu.class))).willReturn(메뉴);
        given(productRepository.findByIdIn(any())).willReturn(Arrays.asList(진매, 진순이));

        // when
        MenuResponse menu = menuService.create(메뉴_요청);

        // then
        assertThat(menu).isEqualTo(MenuResponse.of(메뉴));
    }

    @DisplayName("메뉴 가격은 0원 이상이어야 한다.")
    @ParameterizedTest
    @CsvSource(value = {"-1", "null"}, nullValues = {"null"})
    void createException1(BigDecimal price) throws Exception {
        // given
        MenuRequest menu = TestMenuRequestFactory.toMenuRequest("라면메뉴", price, 50L);
        // when & then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 메뉴그룹은 예외가 발생한다")
    @Test
    void createException2() throws Exception {
        // given
        MenuRequest menu = TestMenuRequestFactory.toMenuRequest("라면메뉴", 4_000, 50L);
        // when & then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴상품 총합이 메뉴가격보다 높으면 예외가 발생한다")
    @Test
    void createException3() throws Exception {
        // given
        MenuRequest menu = TestMenuRequestFactory.toMenuRequest("라면메뉴", 10_001, 50L, Arrays.asList(메뉴_진매, 메뉴_진순이));
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 전체 메뉴를 조회한다")
    @Test
    void list() throws Exception {
        // given
        given(menuRepository.findAll()).willReturn(Collections.singletonList(메뉴));

        // when
        List<MenuResponse> list = menuService.list();

        // then
        assertThat(list).containsExactly(MenuResponse.of(메뉴));
    }

}
