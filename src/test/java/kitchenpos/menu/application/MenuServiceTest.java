package kitchenpos.menu.application;

import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuFixture;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuValidator menuValidator;
    @Mock
    private ProductService productService;

    @InjectMocks
    private MenuService menuService;

    private Product 후라이드;
    private MenuProduct 후라이드두마리구성;
    private MenuProductRequest 후라이드두마리구성Request;
    private MenuGroup 치킨류;

    private Menu 후라이드두마리세트;


    @BeforeEach
    void setUp() {
        후라이드 = ProductFixture.생성("후라이드", new BigDecimal("5000"));

        후라이드두마리구성 = MenuProductFixture.생성(후라이드, 2L);
        후라이드두마리구성Request = MenuProductFixture.request생성(후라이드.getId(), 2L);
        치킨류 = MenuGroupFixture.치킨류();

        후라이드두마리세트 = MenuFixture.생성("후라이드두마리세트", new BigDecimal("10000"), 치킨류);
        후라이드두마리세트.addMenuProducts(Arrays.asList(후라이드두마리구성));
    }

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        //given
        given(productService.getMenuProducts(any())).willReturn(Arrays.asList(후라이드두마리구성));
        given(menuRepository.save(any())).willReturn(후라이드두마리세트);

        MenuResponse creatMenu = menuService.create(MenuRequest.of("후라이드두마리세트", new BigDecimal("10000"), 치킨류.getId(), Arrays.asList(후라이드두마리구성Request)));

        assertAll(
                () -> assertThat(creatMenu).isNotNull(),
                () -> assertThat(creatMenu.getPrice()).isEqualTo("10000")
        );
    }

    @DisplayName("금액은 0원 이상이어야하고 금액이 있어야한다.")
    @Test
    void createPriceError() {
        MenuRequest 금액_없는_세트 = new MenuRequest();
        MenuRequest 금액이_음수인_세트 = new MenuRequest();
        금액이_음수인_세트.setPrice(new BigDecimal("-1000"));

        assertThatThrownBy(() ->
                menuService.create(금액_없는_세트)
        ).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
                menuService.create(금액이_음수인_세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 x 수량 = 금액 보다 등록한 금액이 더 작아야 한다.")
    @Test
    void createPriceLessError() {
        given(productService.getMenuProducts(any())).willReturn(Arrays.asList(후라이드두마리구성));
        MenuRequest 비싼_후라이드두마리세트 = MenuRequest.of("비싼_후라이드두마리세트", new BigDecimal("100000"), 치킨류.getId(), Arrays.asList(후라이드두마리구성Request));

        assertThatThrownBy(() ->
                menuService.create(비싼_후라이드두마리세트)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("메뉴 목록")
    @Test
    void list() {
        given(menuRepository.findAll()).willReturn(Arrays.asList(후라이드두마리세트));

        List<MenuResponse> menus = menuService.list();

        assertAll(
                () -> assertThat(menus.size()).isEqualTo(1),
                () -> assertThat(menus.get(0).getName()).isEqualTo("후라이드두마리세트")
        );
    }
}
