package kitchenpos.menu.application;

import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.MenuProductRequestFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {
    @Mock
    private ProductService productService;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuValidator menuValidator;

    private Product 후라이드;
    private MenuProductRequest 후라이드두마리구성Request;
    private MenuGroup 치킨류;

    @DisplayName("가격 x 수량 = 금액 보다 등록한 금액이 더 작아야 한다.")
    @Test
    void validateOverPrice() {

        후라이드 = ProductFixture.생성("후라이드", new BigDecimal("5000"));
        후라이드두마리구성Request = MenuProductRequestFixture.request생성(후라이드.getId(), 2L);
        치킨류 = MenuGroupFixture.치킨류();

        assertThatThrownBy(() ->
                menuValidator.validateOverPrice(new Menu("비싼_후라이드두마리세트", new BigDecimal("100000"), 치킨류.getId()))
        ).isInstanceOf(IllegalArgumentException.class);
    }


}
