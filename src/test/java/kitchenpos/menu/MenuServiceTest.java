package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuProductRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
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
    MenuRepository menuRepository;

    @Mock
    MenuGroupService menuGroupService;

    @Mock
    MenuProductRepository menuProductRepository;

    Menu 후라이드치킨;
    Product 후라이드;
    MenuRequest 상품;
    MenuProduct 후라이드치킨상품;

    @BeforeEach
    void setUp() {
        후라이드 = new Product("후라이드", BigDecimal.valueOf(15000));
        후라이드치킨 = new Menu("후라이드치킨", BigDecimal.valueOf(15000), 1L);
        후라이드치킨상품 = new MenuProduct(1L, 1L);
        후라이드치킨.saveMenuProducts(new MenuProducts(Collections.singletonList(후라이드치킨상품)));

        상품 = new MenuRequest("후라이드치킨", BigDecimal.valueOf(15000), 1L,
                Collections.singletonList(new MenuProductRequest(1L, 1L)));
    }

    @Test
    @DisplayName("메뉴를 저장한다")
    void create() {
        // given
        given(menuRepository.save(any())).willReturn(후라이드치킨);
        given(menuProductRepository.saveAll(any())).willReturn(Collections.singletonList(후라이드치킨상품));

        // when
        MenuResponse actual = menuService.create(상품);

        // then
        assertThat(actual.getName()).isEqualTo("후라이드치킨");
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴는 존재하는 메뉴그룹 정보를 가지고 있다")
    void create_nonExistMenuGroupError() {
        // given
        doThrow(new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."))
                .when(menuGroupService).existsById(any());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(상품)
        ).withMessageContaining("존재하지 않는 메뉴그룹입니다.");
    }

    @Test
    @DisplayName("메뉴 리스트를 조회한다")
    void list() {
        // given
        given(menuRepository.findAll()).willReturn(Collections.singletonList(후라이드치킨));

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
