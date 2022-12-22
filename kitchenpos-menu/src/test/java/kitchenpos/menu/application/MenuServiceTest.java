package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.NotExistIdException;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    @DisplayName("[메뉴 등록] 존재하지 않는 메뉴그룹id 인 경우 등록할 수 없다")
    @Test
    void test1() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(new MenuRequest()))
            .isInstanceOf(NotExistIdException.class);
    }

    @DisplayName("[메뉴 등록] 등록된 상품이어야 한다")
    @Test
    void test2() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(new MenuGroup()));
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.create(new MenuRequest(
            Collections.singletonList(new MenuProductRequest()))))
            .isInstanceOf(NotExistIdException.class);
    }
}
