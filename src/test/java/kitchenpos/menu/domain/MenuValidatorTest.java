package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductFactory;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuValidator menuValidator;


    @DisplayName("메뉴의 가격이 구성 상품의 가격 * 수량을 모두 더한 값보다 크면 에러가 발생한다.")
    @Test
    void sumPrice() {
        //given
        Product 후라이드 = ProductFactory.create(1L, "후라이드", BigDecimal.valueOf(15000));
        Product 콜라 = ProductFactory.create(2L, "콜라", BigDecimal.valueOf(1000));

        MenuProductRequest 후라이드메뉴상품요청 = new MenuProductRequest(후라이드.getId(), 1L);
        MenuProductRequest 콜라메뉴상품요청 = new MenuProductRequest(콜라.getId(), 1L);
        MenuRequest request = new MenuRequest("후라이드세트", BigDecimal.valueOf(17000), 1L, Arrays.asList(후라이드메뉴상품요청, 콜라메뉴상품요청));

        given(productRepository.findAllByIdIn(any())).willReturn(Arrays.asList(후라이드, 콜라));

        //when & then
        assertThatThrownBy(() -> menuValidator.validCreate(request))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
