package kitchenpos.menu.application;

import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.application.MenuService;
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
    private MenuValidator menuValidator;

    private Menu A_세트;

    @BeforeEach
    void setUp() {
        A_세트 = new Menu("A세트", BigDecimal.valueOf(5000), 1L, Collections.singletonList(new MenuProduct(1L, 1)));
    }


    @DisplayName("메뉴 생성")
    @Test
    void create() {
        // given
        MenuRequest request = new MenuRequest("A세트", BigDecimal.valueOf(5000), 1L,
                Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(2L, 1)));
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(menuRepository.save(any())).willReturn(A_세트);

        // when
        MenuResponse response = menuService.create(request);

        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo("A세트"),
                () -> assertThat(response.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void list() {
        // given
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

}
