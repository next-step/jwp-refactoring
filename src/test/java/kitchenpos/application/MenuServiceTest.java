package kitchenpos.application;

import kitchenpos.domain.MenuEntity;
import kitchenpos.domain.MenuGroupEntity;
import kitchenpos.domain.MenuProductEntity;
import kitchenpos.domain.ProductEntity;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    ProductService productService;

    @Mock
    MenuGroupService menuGroupService;

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
        스테이크 = new ProductEntity(1L, "스테이크", 200L);
        샐러드 = new ProductEntity(2L, "샐러드", 100L);
        에이드 = new ProductEntity(3L, "에이드", 50L);
        스테이크_1개 = new MenuProductEntity(스테이크, 1);
        샐러드_1개 = new MenuProductEntity(샐러드, 1);
        에이드_1개 = new MenuProductEntity(에이드, 1);
        에이드_2개 = new MenuProductEntity(에이드, 2);
        양식 = new MenuGroupEntity(1L, "양식");
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성_성공() {
        // given
        MenuEntity 커플_메뉴 = MenuEntity.createMenu(
                "커플 메뉴",
                BigDecimal.valueOf(400),
                양식,
                Arrays.asList(스테이크_1개, 샐러드_1개, 에이드_2개)
        );
        doReturn(스테이크, 샐러드, 에이드).when(productService).findProductById(any(Long.class));
        doReturn(양식).when(menuGroupService).findMenuGroupById(any(Long.class));
        given(menuRepository.save(any(MenuEntity.class))).willReturn(커플_메뉴);

        // when
        MenuResponse saved = menuService.create(
                new MenuRequest(
                        커플_메뉴.getName(),
                        커플_메뉴.getPrice().longValue(),
                        커플_메뉴.getMenuGroup().getId(),
                        Arrays.asList(
                                new MenuProductRequest(스테이크.getId(), 1),
                                new MenuProductRequest(샐러드.getId(), 1),
                                new MenuProductRequest(에이드.getId(), 2)
                        )
                )
        );

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(커플_메뉴.getName());
        assertThat(saved.getProducts()).hasSize(커플_메뉴.getMenuProducts().size());
    }

    @DisplayName("메뉴 생성에 실패한다.")
    @Test
    void 메뉴_생성_예외_가격() {
        // given
        MenuRequest 메뉴1 = new MenuRequest(
                "커플 메뉴",
                -5,
                양식.getId(),
                Arrays.asList(
                        new MenuProductRequest(스테이크.getId(), 1),
                        new MenuProductRequest(샐러드.getId(), 1),
                        new MenuProductRequest(에이드.getId(), 2)
                )
        );
        MenuRequest 메뉴2 = new MenuRequest(
                "커플 메뉴",
                405,
                양식.getId(),
                Arrays.asList(
                        new MenuProductRequest(스테이크.getId(), 1),
                        new MenuProductRequest(샐러드.getId(), 1),
                        new MenuProductRequest(에이드.getId(), 2)
                )
        );
        doReturn(스테이크, 샐러드, 에이드).when(productService).findProductById(any(Long.class));
        doReturn(양식).when(menuGroupService).findMenuGroupById(any(Long.class));

        // when, then
        assertThatThrownBy(() -> menuService.create(메뉴1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0 이상이어야 합니다.");
        // when, then
        assertThatThrownBy(() -> menuService.create(메뉴2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 상품의 총 가격보다 클 수 없습니다.");
    }

    @DisplayName("메뉴가 속한 그룹이 올바르지 않아 생성에 실패한다.")
    @Test
    void 메뉴_생성_예외_잘못된_그룹() {
        // given
        MenuRequest 커플_메뉴 = new MenuRequest(
                "커플 메뉴",
                400,
                0L,
                Arrays.asList(
                        new MenuProductRequest(스테이크.getId(), 1),
                        new MenuProductRequest(샐러드.getId(), 1),
                        new MenuProductRequest(에이드.getId(), 2)
                )
        );
        doReturn(스테이크, 샐러드, 에이드).when(productService).findProductById(any(Long.class));
        doReturn(null).when(menuGroupService).findMenuGroupById(any(Long.class));

        // when, then
        assertThatThrownBy(() -> menuService.create(커플_메뉴))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 필요합니다.");
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
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
