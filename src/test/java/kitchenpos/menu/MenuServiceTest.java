package kitchenpos.menu;

import static kitchenpos.menu.MenuFixture.더블강정치킨;
import static kitchenpos.menu.MenuGroupFixture.추천메뉴;
import static kitchenpos.product.ProductFixture.강정치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.dao.MenuProductRepository;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dao.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private MenuProductRepository menuProductRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 생성")
    void createMenu() {
        //given
        when(menuGroupRepository.findById(any()))
            .thenReturn(Optional.of(추천메뉴));
        when(productRepository.findById(any()))
            .thenReturn(Optional.of(강정치킨));
        when(menuRepository.save(any()))
            .thenReturn(더블강정치킨);

        //when
        MenuResponse menuResponse = menuService.create(MenuFixture.createMenuRequest(더블강정치킨));

        //then
        assertThat(menuResponse).isEqualTo(MenuResponse.from(더블강정치킨));
    }

    @Test
    @DisplayName("존재 하지 않는 메뉴 그룹이면 에러 발생")
    void noMenuGroupException() {
        //given
        when(menuGroupRepository.findById(any()))
            .thenReturn(Optional.empty());
        when(productRepository.findById(any()))
            .thenReturn(Optional.of(강정치킨));

        //when & then
        assertThatThrownBy(() -> menuService.create(MenuFixture.createMenuRequest(더블강정치킨)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재 하지 않는 메뉴 그룹 입니다.");
    }

    @Test
    @DisplayName("존재 하지 않는 상품이면 에러 발생")
    void noProductException() {
        //given
        when(productRepository.findById(any()))
            .thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> menuService.create(MenuFixture.createMenuRequest(더블강정치킨)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재 하지 않는 상품 입니다.");
    }

    @Test
    @DisplayName("메뉴 목록 조회")
    void getList() {
        //given
        when(menuRepository.findAll())
            .thenReturn(Collections.singletonList(더블강정치킨));

        //when
        List<MenuResponse> list = menuService.list();

        //then
        assertThat(list)
            .hasSize(1)
            .containsExactly(MenuResponse.from(더블강정치킨));
    }

}