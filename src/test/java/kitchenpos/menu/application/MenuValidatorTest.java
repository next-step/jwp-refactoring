package kitchenpos.menu.application;

import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.domain.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuValidator menuValidator;

    private Product 후라이드;
    private MenuProductRequest 후라이드두마리구성Request;
    private MenuGroup 치킨류;

    @BeforeEach
    void setUp() {
        후라이드 = ProductFixture.생성("후라이드", new BigDecimal("5000"));
        후라이드두마리구성Request = MenuProductFixture.request생성(후라이드.getId(), 2L);
        치킨류 = MenuGroupFixture.치킨류();
    }

    @DisplayName("메뉴 생성")
    @Test
    void createMenu() {
        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findById(any())).willReturn(java.util.Optional.ofNullable(후라이드));

        Menu creatMenu = menuValidator.createMenu(MenuRequest.of("후라이드두마리세트", new BigDecimal("10000"), 치킨류.getId(), Arrays.asList(후라이드두마리구성Request)));

        assertAll(
                () -> assertThat(creatMenu).isNotNull(),
                () -> assertThat(creatMenu.getPrice()).isEqualTo("10000")
        );
    }

    @DisplayName("금액은 0원 이상이어야하고 금액이 있어야한다.")
    @Test
    void createPriceError() {
        MenuRequest 금액_없는_세트 = new MenuRequest();
        금액_없는_세트.setPrice(null);
        MenuRequest 금액이_음수인_세트 = new MenuRequest();
        금액이_음수인_세트.setPrice(new BigDecimal("-1000"));

        assertThatThrownBy(() ->
                menuValidator.createMenu(금액_없는_세트)
        ).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
                menuValidator.createMenu(금액이_음수인_세트)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 x 수량 = 금액 보다 등록한 금액이 더 작아야 한다.")
    @Test
    void createPriceLessError() {
        MenuGroup 치킨류 = MenuGroupFixture.치킨류();
        Product 후라이드 = ProductFixture.생성("후라이드", new BigDecimal("5000"));
        MenuProduct 후라이드두마리구성 = MenuProductFixture.생성(후라이드, 2L);
        MenuProductRequest 후라이드두마리구성Request = MenuProductFixture.request생성(후라이드.getId(), 2L);
        MenuRequest 비싼_후라이드두마리세트 = MenuRequest.of("비싼_후라이드두마리세트", new BigDecimal("100000"), 치킨류.getId(), Arrays.asList(후라이드두마리구성Request));

        assertThatThrownBy(() ->
                menuValidator.createMenu(비싼_후라이드두마리세트)
        ).isInstanceOf(IllegalArgumentException.class);

    }
}
