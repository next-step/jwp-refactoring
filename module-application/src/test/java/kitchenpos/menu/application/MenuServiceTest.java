package kitchenpos.menu.application;

import static kitchenpos.util.TestDataSet.원플원_후라이드_리퀘스트;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.MenuGroupRepository;
import kitchenpos.menu.MenuRepository;
import kitchenpos.product.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹에 등록할 경우 실패한다.")
    void already() {
        //given
        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            menuService.create(원플원_후라이드_리퀘스트);
        });

        verify(menuGroupRepository, times(1)).findById(any());
    }

}
