package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @InjectMocks
    MenuService menuService;

    Product 스테이크;
    Product 샐러드;
    Product 에이드;
    MenuProduct 스테이크_1개;
    MenuProduct 샐러드_1개;
    MenuProduct 에이드_1개;
    MenuProduct 에이드_2개;
    MenuGroup 양식;

    @BeforeEach
    void init() {
        // given
        스테이크 = new Product(1L, "스테이크", 200L);
        샐러드 = new Product(2L, "샐러드", 100L);
        에이드 = new Product(3L, "에이드", 50L);
        스테이크_1개 = new MenuProduct(스테이크, 1);
        샐러드_1개 = new MenuProduct(샐러드, 1);
        에이드_1개 = new MenuProduct(에이드, 1);
        에이드_2개 = new MenuProduct(에이드, 2);
        양식 = new MenuGroup(1L, "양식");
    }

    @DisplayName("메뉴 목록 조회에 성공한다.")
    @Test
    void 목록_조회() {
        // given
        Menu 커플_메뉴 = Menu.createMenu(
                "커플 메뉴",
                BigDecimal.valueOf(400),
                양식,
                Arrays.asList(스테이크_1개, 샐러드_1개, 에이드_2개)
        );
        Menu 싱글_메뉴 = Menu.createMenu(
                "커플 메뉴",
                BigDecimal.valueOf(350),
                양식,
                Arrays.asList(스테이크_1개, 샐러드_1개, 에이드_1개)
        );
        given(menuRepository.findAll()).willReturn(Arrays.asList(커플_메뉴, 싱글_메뉴));

        // when
        List<MenuResponse> menuList = menuService.list();

        // then
        assertThat(menuList).containsExactly(MenuResponse.of(커플_메뉴), MenuResponse.of(싱글_메뉴));
    }
}
