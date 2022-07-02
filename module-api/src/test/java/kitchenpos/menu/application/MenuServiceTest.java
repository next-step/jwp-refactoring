package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.api.Assertions;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuService menuService;

    @DisplayName("메뉴 구성 상품들의 금액 총합보다 메뉴 가격이 더 크면 생성에 실패한다.")
    @Test
    void 생성_예외_가격_초과() {
        // given
        Product 스테이크 = new Product(1L, "스테이크", 200);
        Product 샐러드 = new Product(2L, "샐러드", 100);
        Product 에이드 = new Product(3L, "에이드", 50);
        doReturn(스테이크, 샐러드, 에이드).when(productRepository).getById(any(Long.class));

        // when, then
        assertThatThrownBy(
                () -> menuService.create(new MenuRequest(
                        "커플 메뉴",
                        401,
                        1L,
                        Arrays.asList(
                                new MenuProductRequest(1L, 1),
                                new MenuProductRequest(2L, 1),
                                new MenuProductRequest(3L, 2)))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록 조회에 성공한다.")
    @Test
    void 목록_조회() {
        // given
        Product 스테이크 = new Product(1L, "스테이크", 200);
        Product 샐러드 = new Product(2L, "샐러드", 100);
        Product 에이드 = new Product(3L, "에이드", 50);
        Menu 메뉴1 = Menu.createMenu(
                "메뉴1",
                BigDecimal.valueOf(400),
                new MenuGroup("그룹"),
                Arrays.asList(new MenuProduct(1L, 1)),
                Menu.DEFAULT_VERSION
        );
        Menu 메뉴2 = Menu.createMenu(
                "메뉴2",
                BigDecimal.valueOf(350),
                new MenuGroup("그룹"),
                Arrays.asList(new MenuProduct(2L, 2)),
                Menu.DEFAULT_VERSION
        );
        given(menuRepository.findAll()).willReturn(Arrays.asList(메뉴1, 메뉴2));
        doReturn(스테이크, 샐러드, 에이드).when(productRepository).getById(any(Long.class));

        // when
        List<MenuResponse> menuResponses = menuService.list();

        // then
        Assertions.assertThat(menuResponses).hasSize(2);
        assertThat(menuResponses.stream()
                .map(menuResponse -> menuResponse.getName())
        ).containsExactly("메뉴1", "메뉴2");
    }
}
