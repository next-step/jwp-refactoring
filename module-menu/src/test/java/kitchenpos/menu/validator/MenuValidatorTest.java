package kitchenpos.menu.validator;

import static kitchenpos.menu.fixture.MenuFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Optional;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuValidator menuValidator;

    private MenuGroup 파스타메뉴;
    private Menu 봉골레파스타세트;

    @BeforeEach
    void setUp() {
        파스타메뉴 = 메뉴그룹_생성(1L, "파스타메뉴");
        봉골레파스타세트 = 메뉴_생성("봉골레파스타세트", new Price(15000), 1L, 파스타메뉴_상품_생성());
    }

    @DisplayName("존재하지 않는 메뉴 상품이 포함되면, IllegalArgumentException 이 발생한다.")
    @Test
    void create_invalidNotExistsMenuProduct() {
        //given
        MenuRequest menuRequest = 메뉴요청_생성("봉골레파스타세트", BigDecimal.valueOf(15000), 파스타메뉴.getId(), 파스타메뉴_상품_생성());
        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> menuValidator.validate(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 메뉴그룹입니다.");
    }

    @DisplayName("메뉴 내 제품가격의 합보다 메뉴 가격이 크면, IllegalArgumentException 이 발생한다.")
    @Test
    void create_invalidPrice() {
        //given
        MenuRequest menuRequest = 메뉴요청_생성("봉골레파스타세트", BigDecimal.valueOf(30000), 파스타메뉴.getId(), 파스타메뉴_상품_생성());
        Product product = new Product("봉골레파스타", BigDecimal.valueOf(10000));
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(파스타메뉴));
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        //when & then
        assertThatThrownBy(() -> menuValidator.validate(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 내 제품가격의 합보다 메뉴가격이 클 수 없습니다.");
    }

    @DisplayName("존재하지 않는 상품이 포함되어 있으면, IllegalArgumentException 이 발생한다.")
    @Test
    void create_invalidPriceSum() {
        //given
        MenuRequest menuRequest = 메뉴요청_생성("봉골레파스타세트", BigDecimal.valueOf(15000), 파스타메뉴.getId(), 파스타메뉴_상품_생성());
        given(menuGroupRepository.findById(any())).willReturn(Optional.of(파스타메뉴));
        given(productRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> menuValidator.validate(menuRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 상품입니다.");
    }

}
