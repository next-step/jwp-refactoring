package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuFactory;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupFactory;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductFactory;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductFactory;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;
    @Mock
    MenuGroupRepository menuGroupRepository;
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuService menuService;

    private MenuGroup 메뉴분류세트;
    private Product 후라이드;
    private Product 콜라;
    private Menu 후라이드세트;
    private MenuProductRequest 후라이드메뉴상품요청;
    private MenuProductRequest 콜라메뉴상품요청;

    @BeforeEach
    public void Setup() {
        BigDecimal 후라이드가격 = BigDecimal.valueOf(15000);
        BigDecimal 콜라가격 = BigDecimal.valueOf(1000);

        메뉴분류세트 = MenuGroupFactory.create(1L, "메뉴분류세트");

        후라이드 = ProductFactory.create(1L, "후라이드", 후라이드가격);
        콜라 = ProductFactory.create(2L, "콜라", 콜라가격);

        MenuProduct 후라이드메뉴상품 = MenuProductFactory.create(1L, 후라이드세트, 후라이드, 1L);
        MenuProduct 콜라메뉴상품 = MenuProductFactory.create(2L, 후라이드세트, 콜라, 1L);

        후라이드메뉴상품요청 = new MenuProductRequest(후라이드.getId(), 1L);
        콜라메뉴상품요청 = new MenuProductRequest(콜라.getId(), 1L);

        후라이드세트 = MenuFactory.create(1L, "후라이드세트", BigDecimal.valueOf(16000), 메뉴분류세트, Arrays.asList(후라이드메뉴상품, 콜라메뉴상품));
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void create() {
        //given
        MenuRequest menuRequest = new MenuRequest("후라이드세트", BigDecimal.valueOf(16000), 메뉴분류세트.getId(), Arrays.asList(후라이드메뉴상품요청, 콜라메뉴상품요청));

        given(menuGroupRepository.findById(any())).willReturn(Optional.ofNullable(메뉴분류세트));
        given(productRepository.findById(1L)).willReturn(Optional.ofNullable(후라이드));
        given(productRepository.findById(2L)).willReturn(Optional.ofNullable(콜라));
        given(menuRepository.save(any())).willReturn(후라이드세트);

        //when
        MenuResponse menuResponse = menuService.create(menuRequest);
        //then
        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getName()).isEqualTo("후라이드세트"),
                () -> assertThat(menuResponse.getPrice()).isEqualTo(BigDecimal.valueOf(16000)),
                () -> assertThat(menuResponse.getMenuGroupId()).isEqualTo(메뉴분류세트.getId()),
                () -> assertThat(menuResponse.getMenuProducts().get(0).getProductId()).isEqualTo(후라이드.getId())
        );
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        given(menuRepository.findAll()).willReturn(Collections.singletonList(후라이드세트));

        //when
        List<MenuResponse> actual = menuService.list();

        //then
        assertThat(actual.get(0).getId()).isEqualTo(후라이드세트.getId());

    }


    @DisplayName("메뉴의 상품 가격이 null 이면 에러가 발생한다.")
    @Test
    void priceNull() {
        //given
        MenuRequest request = new MenuRequest("후라이드세트", null, 메뉴분류세트.getId(), Arrays.asList(후라이드메뉴상품요청, 콜라메뉴상품요청));
        //when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("메뉴의 상품 가격이 0 보다 작으면 에러가 발생한다.")
    @Test
    void priceZero() {
        //given
        MenuRequest request = new MenuRequest("후라이드세트", BigDecimal.valueOf(-1), 메뉴분류세트.getId(), Arrays.asList(후라이드메뉴상품요청, 콜라메뉴상품요청));
        //when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 메뉴 그룹이 존재하지 않으면 에러가 발생한다.")
    @Test
    void menuGroup() {
        //given
        MenuRequest request = new MenuRequest("후라이드세트", BigDecimal.valueOf(16000), 메뉴분류세트.getId(), Arrays.asList(후라이드메뉴상품요청, 콜라메뉴상품요청));
        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 구성 상품의 가격 * 수량을 모두 더한 값보다 크면 에러가 발생한다.")
    @Test
    void sumPrice() {
        //given
        MenuRequest request = new MenuRequest("후라이드세트", BigDecimal.valueOf(17000), 메뉴분류세트.getId(), Arrays.asList(후라이드메뉴상품요청, 콜라메뉴상품요청));

        given(menuGroupRepository.findById(any())).willReturn(Optional.ofNullable(메뉴분류세트));
        given(productRepository.findById(1L)).willReturn(Optional.ofNullable(후라이드));
        given(productRepository.findById(2L)).willReturn(Optional.ofNullable(콜라));

        //when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);

    }


}
