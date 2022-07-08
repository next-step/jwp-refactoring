package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
import java.util.Optional;

import static kitchenpos.common.ServiceTestFactory.메뉴그룹생성;
import static kitchenpos.common.ServiceTestFactory.메뉴상품생성;
import static kitchenpos.common.ServiceTestFactory.메뉴생성;
import static kitchenpos.common.ServiceTestFactory.상품생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@DisplayName("메뉴 서비스 테스트")
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

    private Product 지코바치킨;
    private MenuGroup 메뉴그룹;
    private MenuProduct 메뉴상품;
    private Menu 오늘의메뉴;

    @BeforeEach
    void setUp() {
        지코바치킨 = 상품생성("지코바치킨", BigDecimal.valueOf(20000));
        메뉴그룹 = 메뉴그룹생성("메뉴그룹");
        메뉴상품 = 메뉴상품생성(지코바치킨, 1L);
        오늘의메뉴 = 메뉴생성("오늘의메뉴", 지코바치킨.getPrice().getPriceValue(), 메뉴그룹, Arrays.asList(메뉴상품));
    }

    @Test
    void 메뉴를_등록할_수_있다() {
        MenuRequest 메뉴요청 = new MenuRequest(오늘의메뉴.getName(), 지코바치킨.getPriceValue(), 메뉴그룹.getId(), Arrays.asList(new MenuProductRequest(지코바치킨.getId(), 1L)));
        given(menuGroupRepository.findById(오늘의메뉴.getMenuGroup().getId())).willReturn(Optional.of(메뉴그룹));
        given(productRepository.findById(any())).willReturn(Optional.of(지코바치킨));
        given(menuRepository.save(any())).willReturn(오늘의메뉴);

        MenuResponse createdMenu = menuService.create(메뉴요청);

        assertAll(
                () -> assertThat(createdMenu.getName()).isEqualTo(오늘의메뉴.getName()),
                () -> assertThat(createdMenu).isNotNull()
        );
    }

    @Test
    void 메뉴그룹이_존재하지_않으면_메뉴를_등록할_수_없다() {
        long 존재하지않는_메뉴그룹ID = 1000L;
        MenuRequest menuRequest = new MenuRequest("메뉴이름", 1000, 존재하지않는_메뉴그룹ID, Arrays.asList());
        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품이_존재하지않으면_메뉴를_등록할_수_없다() {
        MenuProductRequest 존재하지않는_메뉴상품 = new MenuProductRequest(999L, 1L);
        MenuRequest menuRequest = new MenuRequest("메뉴이름", 1000, 메뉴그룹.getId(), Arrays.asList(존재하지않는_메뉴상품));
        given(menuGroupRepository.findById(menuRequest.getMenuGroupId())).willReturn(Optional.of(메뉴그룹));
        given(productRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_조회할_수_있다() {
        given(menuRepository.findAll()).willReturn(Arrays.asList(오늘의메뉴));

        List<MenuResponse> list = menuService.list();

        assertAll(
                () -> assertThat(list.size()).isEqualTo(1),
                () -> assertThat(list.get(0).getName()).isEqualTo("오늘의메뉴")
        );
    }


}
