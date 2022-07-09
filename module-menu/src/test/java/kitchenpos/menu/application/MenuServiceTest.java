package kitchenpos.menu.application;

import static kitchenpos.menu.__fixture__.MenuGroupTestFixture.메뉴_그룹_생성;
import static kitchenpos.menu.__fixture__.MenuProductTestFixture.메뉴_상품_1개_생성;
import static kitchenpos.menu.__fixture__.MenuProductTestFixture.메뉴_상품_요청_생성;
import static kitchenpos.menu.__fixture__.MenuTestFixture.메뉴_생성;
import static kitchenpos.menu.__fixture__.MenuTestFixture.메뉴_요청_생성;
import static kitchenpos.menu.__fixture__.ProductTestFixture.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.infra.MenuRepository;
import kitchenpos.menu.infra.ProductRepository;
import kitchenpos.menu.request.MenuProductRequest;
import kitchenpos.menu.request.MenuRequest;
import kitchenpos.menu.response.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuService 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    private MenuRequest 한마리치킨;
    private MenuGroup 한마리_메뉴_그룹;
    private MenuProduct 후라이드치킨;
    private Product 후라이드치킨_상품;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupService menuGroupService;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        한마리_메뉴_그룹 = 메뉴_그룹_생성(1L, "한마리메뉴");
        후라이드치킨_상품 = 상품_생성(1L, "후라이드치킨", BigDecimal.valueOf(16_000));
        후라이드치킨 = 메뉴_상품_1개_생성(후라이드치킨_상품);
        한마리치킨 = 메뉴_요청_생성("한마리치킨", BigDecimal.valueOf(16_000), 한마리_메뉴_그룹, Arrays.asList(메뉴_상품_요청_생성(후라이드치킨)));
    }

    @Test
    @DisplayName("메뉴 생성 시도 시 메뉴 가격이 0보다 작을 경우 Exception")
    public void createPriceException() {
        final MenuProductRequest 메뉴_상품_요청 = 메뉴_상품_요청_생성(후라이드치킨);
        final MenuRequest 메뉴 = 메뉴_요청_생성("한마리치킨", BigDecimal.valueOf(-16_000), 한마리_메뉴_그룹, Arrays.asList(메뉴_상품_요청));

        given(menuGroupService.findMenuGroupById(한마리_메뉴_그룹.getId())).willReturn(한마리_메뉴_그룹);
        given(productRepository.findById(메뉴_상품_요청.getProductId()))
                .willReturn(Optional.ofNullable(후라이드치킨_상품));

        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 메뉴 그룹으로 메뉴 등록 시 Exception")
    public void createNotExistsMenuGroupException() {
        given(menuGroupService.findMenuGroupById(한마리_메뉴_그룹.getId()))
                .willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> menuService.create(한마리치킨)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 상품의 합계보다 클 경우 Exception")
    public void createPriceIsGreaterThanProductsPriceSumException() {
        final MenuProductRequest 메뉴_상품_요청 = 메뉴_상품_요청_생성(후라이드치킨);
        final MenuRequest 메뉴 = 메뉴_요청_생성("한마리치킨", BigDecimal.valueOf(17_000), 한마리_메뉴_그룹, Arrays.asList(메뉴_상품_요청));

        given(menuGroupService.findMenuGroupById(한마리_메뉴_그룹.getId())).willReturn(한마리_메뉴_그룹);
        given(productRepository.findById(후라이드치킨_상품.getId())).willReturn(Optional.of(후라이드치킨_상품));

        assertThatThrownBy(() -> menuService.create(메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성")
    public void create() {
        final MenuProductRequest 메뉴_상품_요청 = 메뉴_상품_요청_생성(후라이드치킨);
        final MenuRequest 메뉴_요청 = 메뉴_요청_생성("한마리치킨", BigDecimal.valueOf(16_000), 한마리_메뉴_그룹, Arrays.asList(메뉴_상품_요청));
        final Menu 메뉴 = 메뉴_생성(1L, 메뉴_요청.getName(), 메뉴_요청.getPrice(), 한마리_메뉴_그룹, new ArrayList<>());
        final MenuProduct 후라이드치킨 = 메뉴_상품_1개_생성(후라이드치킨_상품, 메뉴);
        메뉴.addMenuProduct(후라이드치킨);

        given(menuGroupService.findMenuGroupById(한마리_메뉴_그룹.getId())).willReturn(한마리_메뉴_그룹);
        given(productRepository.findById(후라이드치킨_상품.getId())).willReturn(Optional.of(후라이드치킨_상품));
        given(menuRepository.save(any())).willReturn(메뉴);

        assertThat(menuService.create(메뉴_요청).getId()).isEqualTo(메뉴.getId());
    }

    @Test
    @DisplayName("메뉴 조회")
    public void list() {
        final Menu 한마리치킨 = 메뉴_생성(1L, "한마리치킨", BigDecimal.valueOf(16_000), 한마리_메뉴_그룹,
                new ArrayList<>());
        final MenuProduct 후라이드치킨 = 메뉴_상품_1개_생성(후라이드치킨_상품, 한마리치킨);
        한마리치킨.addMenuProduct(후라이드치킨);

        given(menuRepository.findAll()).willReturn(Arrays.asList(한마리치킨));

        assertThat(menuService.list()).containsAll(MenuResponse.of(Arrays.asList(한마리치킨)));
    }
}
