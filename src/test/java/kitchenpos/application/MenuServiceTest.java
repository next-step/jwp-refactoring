package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    private Menu A_세트;
    private Product 감자_튀김;
    private Product 햄버거;
    private Product 치즈볼;

    @BeforeEach
    void setUp() {
        A_세트 = new Menu(1L, "A세트", BigDecimal.valueOf(6000), 1L);
        감자_튀김 = new Product(1L, "감자튀김", BigDecimal.valueOf(1500));
        햄버거 = new Product(2L, "햄버거", BigDecimal.valueOf(3500));
        치즈볼 = new Product(3L, "피자", BigDecimal.valueOf(1000));
    }


    @DisplayName("메뉴 생성")
    @Test
    void create() {
        // given
        MenuRequest request = new MenuRequest("A세트", BigDecimal.valueOf(5000), 1L,
                Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(2L, 1)));
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(menuRepository.save(any())).willReturn(A_세트);
        given(productRepository.findById(any())).willReturn(Optional.of(감자_튀김));
        given(productRepository.findById(any())).willReturn(Optional.of(햄버거));

        // when
        MenuResponse response = menuService.create(request);

        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo("A세트"),
                () -> assertThat(response.getMenuProducts()).hasSize(2)
        );
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void list() {
        // given
        A_세트.addMenuProducts(Collections.singletonList(new MenuProduct(감자_튀김, 4)));
        given(menuRepository.findAll()).willReturn(Collections.singletonList(A_세트));

        // when
        List<MenuResponse> responses = menuService.list();

        // then
        assertAll(
                () -> assertThat(responses).hasSize(1),
                () -> assertThat(responses.stream().map(MenuResponse::getName)).containsExactly("A세트"),
                () -> assertThat(responses.get(0).getMenuProducts()).hasSize(1)
        );
    }

    @Test
    void 메뉴_가격은_0미만일_경우() {
        // given
        MenuRequest request = new MenuRequest("A세트", BigDecimal.valueOf(-5000), 1L,
                Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(2L, 1)));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록할_메뉴_그룹이_없는_경우() {
        // given
        MenuRequest request = new MenuRequest("A세트", BigDecimal.valueOf(5000), 1L,
                Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(2L, 1)));
        given(menuGroupRepository.existsById(A_세트.getMenuGroupId())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록된_상품으로만_메뉴을_구성해야한다() {
        // given
        MenuRequest request = new MenuRequest("A세트", BigDecimal.valueOf(5000), 1L,
                Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(2L, 1)));
        given(menuGroupRepository.existsById(request.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(감자_튀김.getId())).willReturn(Optional.of(감자_튀김));
        given(productRepository.findById(햄버거.getId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_구성_상품들의_합보다_큰_경우() {
        // given
        MenuRequest request = new MenuRequest("A세트", BigDecimal.valueOf(5000), 1L,
                Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(3L, 1)));
        given(menuRepository.save(any())).willReturn(A_세트);
        given(menuGroupRepository.existsById(A_세트.getMenuGroupId())).willReturn(true);
        given(productRepository.findById(감자_튀김.getId())).willReturn(Optional.of(감자_튀김));
        given(productRepository.findById(치즈볼.getId())).willReturn(Optional.of(치즈볼));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
