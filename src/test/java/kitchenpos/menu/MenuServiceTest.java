package kitchenpos.menu;

import kitchenpos.AcceptanceTest;
import kitchenpos.application.MenuService;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.global.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 관련 기능")
class MenuServiceTest extends AcceptanceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않을 경우 예외가 발생한다.")
    void nonExistMenuGroup() {
        assertThatThrownBy(() -> {
            menuService.create(new MenuCreateRequest(1L));
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("메뉴에 등록하고자 하는 상품이 존재하지 않을 경우 예외가 발생한다.")
    void nonExistProduct() {
        // given
        final MenuGroup savedMenuGroup = menuGroupRepository.save(MenuGroup.builder().name("추천메뉴").build());

        // when
        assertThatThrownBy(() -> {
            menuService.create(new MenuCreateRequest("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuCreateRequest.MenuProduct(1L, 1L))));
        }).isInstanceOf(EntityNotFoundException.class);
    }
}
