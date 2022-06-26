package kitchenpos.application;

import kitchenpos.domain.MenuEntity;
import kitchenpos.domain.MenuGroupEntity;
import kitchenpos.domain.MenuProductEntity;
import kitchenpos.domain.ProductEntity;
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

    ProductEntity 스테이크;
    ProductEntity 샐러드;
    ProductEntity 에이드;
    MenuProductEntity 스테이크_1개;
    MenuProductEntity 샐러드_1개;
    MenuProductEntity 에이드_1개;
    MenuProductEntity 에이드_2개;
    MenuGroupEntity 양식;

    @BeforeEach
    void init() {
        // given
        스테이크 = new ProductEntity(1L, "스테이크", 200L);
        샐러드 = new ProductEntity(2L, "샐러드", 100L);
        에이드 = new ProductEntity(3L, "에이드", 50L);
        스테이크_1개 = new MenuProductEntity(스테이크, 1);
        샐러드_1개 = new MenuProductEntity(샐러드, 1);
        에이드_1개 = new MenuProductEntity(에이드, 1);
        에이드_2개 = new MenuProductEntity(에이드, 2);
        양식 = new MenuGroupEntity(1L, "양식");
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 목록_조회() {
        // given
        MenuEntity 커플_메뉴 = MenuEntity.createMenu(
                "커플 메뉴",
                BigDecimal.valueOf(400),
                양식,
                Arrays.asList(스테이크_1개, 샐러드_1개, 에이드_2개)
        );
        MenuEntity 싱글_메뉴 = MenuEntity.createMenu(
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
