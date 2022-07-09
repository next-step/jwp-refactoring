package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import kitchenpos.menu.application.MenuValidator;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;


    @DisplayName("메뉴그룹은 필수 이어야 한다.")
    @Test
    void existMenuGroup() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.existMenuGroup(null));
    }

    @DisplayName("메뉴 상품은 존재 해야한다.")
    @Test
    void validateMenuProductExist() {
        //given
        List<MenuProductRequest> menuProductRequests = Arrays.asList(new MenuProductRequest(1L, 2),
                new MenuProductRequest(2L, 1));
        MenuRequest menuRequest = new MenuRequest("메뉴", BigDecimal.valueOf(2000), 1L, menuProductRequests);
        given(productRepository.findById(1L)).willReturn(Optional.empty());

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.validateMenuProduct(menuRequest));
    }

    @DisplayName("메뉴의 가격은 구성하고 있는 메뉴 상품들의 가격(상품가격 * 수량)의 합계보다 작아야 합니다.")
    @Test
    void validateMenuProductPrice() {
        List<MenuProductRequest> menuProductRequests = Arrays.asList(new MenuProductRequest(1L, 2),
                new MenuProductRequest(2L, 1));

        MenuRequest menuRequest = new MenuRequest("메뉴", BigDecimal.valueOf(1500), 1L, menuProductRequests);


        given(productRepository.findById(1L)).willReturn(Optional.of(new Product("상품1" , BigDecimal.valueOf(500))));
        given(productRepository.findById(2L)).willReturn(Optional.of(new Product("상품2" , BigDecimal.valueOf(100))));


        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.validateMenuProduct(menuRequest));

    }

}
