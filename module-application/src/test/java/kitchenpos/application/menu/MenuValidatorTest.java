package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    @Test
    @DisplayName("메뉴 상품이 존재하지 않으면, 예외를 반환한다.")
    void createWithNotExistingMenuProducts() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
        MenuRequest menuRequest = new MenuRequest("허니콤보", 19_000L, 1L, Lists.list(menuProductRequest));

        assertThatThrownBy(() -> {
            menuValidator.validate(menuRequest.toEntity());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면, 예외를 반환한다.")
    void createWithNotExistingMenuGroup() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
        MenuRequest menuRequest = new MenuRequest("허니콤보", 19_000L, 1L, Lists.list(menuProductRequest));
        when(menuGroupRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> {
            menuValidator.validate(menuRequest.toEntity());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 메뉴 상품 가격 총합보다 크면 예외를 반환한다.")
    void createOverPrice() {
        Product product = Product.of("허니콤보", 19_000L);
        MenuGroup menuGroup = MenuGroup.from("한마리메뉴");
        when(menuGroupRepository.existsById(any())).thenReturn(true);
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("허니콤보", 20_000L, menuGroup.getId(), Lists.list(menuProductRequest));

        assertThatThrownBy(() -> {
            menuValidator.validate(menuRequest.toEntity());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
