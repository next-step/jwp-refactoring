package kitchenpos.menu.application;

import static kitchenpos.menu.domain.MenuProductTest.메뉴상품_생성;
import static kitchenpos.menu.domain.MenuTest.메뉴_생성;
import static kitchenpos.menu.dto.MenuProductRequestTest.메뉴상품_요청_객체_생성;
import static kitchenpos.menu.dto.MenuRequestTest.메뉴_요청_객체_생성;
import static kitchenpos.menu.dto.MenuResponseTest.메뉴_응답_객체들_생성;
import static kitchenpos.menugroup.domain.MenuGroupTest.메뉴그룹_생성;
import static kitchenpos.product.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 미역국;
    private Product 소머리국밥;
    private MenuGroup 식사_메뉴그룹;
    private MenuProduct 미역국_메뉴상품;
    private MenuProduct 소머리국밥_메뉴상품;
    private Menu 미역국_메뉴;
    private Menu 소머리국밥_메뉴;

    @BeforeEach
    public void setUp() {
        미역국 = 상품_생성(1L, "미역국", BigDecimal.valueOf(6000));
        소머리국밥 = 상품_생성(2L, "소머리국밥", BigDecimal.valueOf(8000));
        식사_메뉴그룹 = 메뉴그룹_생성(1L, "식사");

        미역국_메뉴상품 = 메뉴상품_생성(1L, null, 미역국, 1L);
        미역국_메뉴 = 메뉴_생성(1L, "미역국", BigDecimal.valueOf(6000), 식사_메뉴그룹, Arrays.asList(미역국_메뉴상품));

        소머리국밥_메뉴상품 = 메뉴상품_생성(2L, null, 소머리국밥, 1L);
        소머리국밥_메뉴 = 메뉴_생성(2L, "소머리국밥", BigDecimal.valueOf(8000), 식사_메뉴그룹, Arrays.asList(소머리국밥_메뉴상품));
    }


    @Test
    @DisplayName("메뉴 생성")
    void create() {
        // given
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(식사_메뉴그룹));
        when(productRepository.findById(any())).thenReturn(Optional.of(소머리국밥));
        when(menuRepository.save(any(Menu.class))).thenReturn(소머리국밥_메뉴);
        MenuProductRequest productRequest = 메뉴상품_요청_객체_생성(소머리국밥.getId(), 1L);
        MenuRequest 메뉴_요청_객체_생성 = 메뉴_요청_객체_생성(소머리국밥_메뉴.getNameValue(), 소머리국밥_메뉴.getPriceValue(), 식사_메뉴그룹.getId(), Arrays.asList(productRequest));

        // when
        MenuResponse 메뉴 = menuService.create(메뉴_요청_객체_생성);

        // then
        assertThat(메뉴.getId()).isEqualTo(소머리국밥_메뉴.getId());
    }

    @Test
    @DisplayName("메뉴 목록 조회")
    void list() {
        // given
        when(menuRepository.findAll()).thenReturn(Arrays.asList(미역국_메뉴, 소머리국밥_메뉴));

        // when
        List<MenuResponse> 메뉴_목록 = menuService.list();

        // then
        assertThat(메뉴_목록).hasSize(2);
        assertThat(메뉴_목록).containsAll(메뉴_응답_객체들_생성(미역국_메뉴, 소머리국밥_메뉴));
    }
}