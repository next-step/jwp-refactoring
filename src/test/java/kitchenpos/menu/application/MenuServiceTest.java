package kitchenpos.menu.application;

import kitchenpos.fixture.MenuProductTextFixture;
import kitchenpos.fixture.ProductTestFixture;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.fixture.MenuGroupTestFixture;
import kitchenpos.fixture.MenuTestFixture;
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
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 후라이드;
    private MenuProduct 후라이드두마리구성;
    private MenuProductRequest 후라이드두마리구성Request;
    private MenuGroup 치킨류;

    private Menu 후라이드두마리세트;


    @BeforeEach
    void setUp() {
        후라이드 = ProductTestFixture.생성("후라이드", new BigDecimal("5000"));

        후라이드두마리구성 = MenuProductTextFixture.생성(1L, 후라이드, 2L);
        후라이드두마리구성Request = MenuProductTextFixture.생성(후라이드.getId(), 2L);
        치킨류 = MenuGroupTestFixture.생성(1L, "치킨");

        후라이드두마리세트 = MenuTestFixture.생성(1L, "후라이드두마리세트", new BigDecimal("10000"), 치킨류);
        후라이드두마리세트.addMenuProducts(Arrays.asList(후라이드두마리구성));
    }

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        //given
        given(menuGroupRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(치킨류));
        given(menuRepository.save(any())).willReturn(후라이드두마리세트);
        given(productRepository.findById(any())).willReturn(java.util.Optional.ofNullable(후라이드));

        Menu creatMenu = menuService.create(MenuRequest.of("후라이드두마리세트", new BigDecimal("10000"), 치킨류.getId(), Arrays.asList(후라이드두마리구성Request)));

        assertAll(
                () -> assertThat(creatMenu).isNotNull(),
                () -> assertThat(creatMenu.getPrice()).isEqualTo("10000")
        );
    }

    @DisplayName("금액은 0원 이상이어야한고 금액이 있어야한다.")
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
        MenuRequest 비싼_후라이드두마리세트 = MenuRequest.of("비싼_후라이드두마리세트", new BigDecimal("100000"), 치킨류.getId(), Arrays.asList(후라이드두마리구성Request));

        assertThatThrownBy(() ->
                menuService.create(비싼_후라이드두마리세트)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("메뉴 목록")
    @Test
    void list() {
        given(menuRepository.findAll()).willReturn(Arrays.asList(후라이드두마리세트));

        List<Menu> menus = menuService.list();

        assertAll(
                () -> assertThat(menus.size()).isEqualTo(1),
                () -> assertThat(menus.contains(후라이드두마리세트)).isTrue()
        );
    }
}
