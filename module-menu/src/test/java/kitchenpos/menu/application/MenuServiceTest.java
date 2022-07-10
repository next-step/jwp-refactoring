package kitchenpos.menu.application;

import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;


@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuValidator menuValidator;
    @InjectMocks
    private MenuService menuService;

    private Product 지코바치킨;
    private MenuGroup 메뉴그룹;
    private MenuProduct 메뉴상품;
    private Menu 오늘의메뉴;

    @BeforeEach
    void setUp() {
        지코바치킨 = new Product("지코바치킨", Price.from(20000));
        메뉴그룹 = new MenuGroup("메뉴그룹");
        메뉴상품 = new MenuProduct(지코바치킨.getId(), 1L);
        오늘의메뉴 = new Menu("오늘의메뉴", 지코바치킨.getPrice().getPriceValue(), 1L, Arrays.asList(메뉴상품));
    }

    @Test
    void 메뉴를_등록할_수_있다() {
        MenuRequest 메뉴요청 = new MenuRequest(오늘의메뉴.getName(), 지코바치킨.getPriceValue(), 메뉴그룹.getId(), Arrays.asList(new MenuProductRequest(지코바치킨.getId(), 1L)));
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

        doThrow(IllegalArgumentException.class).when(menuValidator).validate(menuRequest);

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
