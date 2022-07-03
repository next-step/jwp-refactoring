package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kitchenpos.common.domain.PriceTest.가격_생성;
import static kitchenpos.menu.MenuGenerator.*;
import static kitchenpos.product.ProductGenerator.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@ActiveProfiles("test")
class MenuServiceTest {
    private static final Price PRODUCT_DEFAULT_PRICE = 가격_생성(1_000);

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup 저장된_메뉴_그룹;
    private Product 저장된_상품;
    private MenuProductRequest 메뉴_상품;

    @BeforeEach
    void setUp() {
        저장된_메뉴_그룹 = menuGroupRepository.save(메뉴_그룹_생성("메뉴 그룹"));
        저장된_상품 = productRepository.save(상품_생성("상품", PRODUCT_DEFAULT_PRICE));
        메뉴_상품 = 메뉴_상품_생성_요청(저장된_상품.getId(), 1L);
    }

    @DisplayName("메뉴 생성 시 0원 미만인 경우 예외가 발생해야 한다")
    @Test
    void createMenuByZeroPriceMenuTest() {
        // given
        MenuProductRequest 메뉴_상품 = 메뉴_상품_생성_요청(저장된_상품.getId(), 1L);
        MenuCreateRequest 메뉴_생성_요청 = 메뉴_생성_요청(
                "-1원 메뉴",
                -1,
                0L,
                Collections.singletonList(메뉴_상품)
        );

        // then
        메뉴_생성_실패됨(() -> menuService.create(메뉴_생성_요청));
    }

    @DisplayName("메뉴 생성 시 메뉴 그룹이 없거나 없는 메뉴 그룹으로 메뉴를 생성하면 예외가 발생해야 한다")
    @Test
    void createMenuByIllegalMenuGroupTest() {
        // given
        long 없는_메뉴_그룹_아이디 = -1L;
        MenuCreateRequest 없는_메뉴_그룹의_메뉴_생성_요청 = 메뉴_생성_요청(
                "없는 메뉴 그룹의 메뉴",
                1_000,
                없는_메뉴_그룹_아이디,
                Collections.singletonList(메뉴_상품)
        );
        MenuCreateRequest 메뉴_그룹_정보가_없는_메뉴_생성_요청 = 메뉴_생성_요청(
                "메뉴 그룹 정보가 없는 메뉴",
                1_000,
                null,
                Collections.singletonList(메뉴_상품)
        );

        // then
        메뉴_생성_실패됨(() -> menuService.create(없는_메뉴_그룹의_메뉴_생성_요청));
        메뉴_생성_실패됨(() -> menuService.create(메뉴_그룹_정보가_없는_메뉴_생성_요청));
    }

    @DisplayName("메뉴의 가격과 메뉴에 포함된 상품의 전체 가격의 합이 일치하지 않는 경우 예외가 발생해야 한다")
    @Test
    void createMenuByNotIncludedProductsTest() {
        // given
        MenuCreateRequest 메뉴_가격과_포함된_상품의_가격이_맞지_않는_메뉴_생성_요청 = 메뉴_생성_요청(
                "999원 메뉴",
                999,
                저장된_메뉴_그룹.getId(),
                Collections.singletonList(메뉴_상품)
        );

        // then
        메뉴_생성_실패됨(() -> menuService.create(메뉴_가격과_포함된_상품의_가격이_맞지_않는_메뉴_생성_요청));
    }

    @DisplayName("정상 상태의 메뉴 생성 시 정상 생성되어야 한다")
    @Test
    void createMenuTest() {
        // given
        MenuCreateRequest 메뉴_생성_요청 = 메뉴_생성_요청(
                "1,000원 메뉴",
                1_000,
                저장된_메뉴_그룹.getId(),
                Collections.singletonList(메뉴_상품)
        );

        // when
        MenuResponse 생성_메뉴 = menuService.create(메뉴_생성_요청);

        // then
        메뉴_생성_성공됨(생성_메뉴, 메뉴_생성_요청);
    }

    @DisplayName("메뉴 전체 조회는 정상 동작해야 한다")
    @Test
    void findAllMenuTest() {
        // given
        List<Long> 포함되어야_할_아이디들 = new ArrayList<>();
        MenuProductRequest 메뉴_상품 = 메뉴_상품_생성_요청(저장된_상품.getId(), 1L);
        for (int i = 1; i < 5; i++) {
            포함되어야_할_아이디들.add(menuService.create(
                    메뉴_생성_요청(
                            "메뉴 " + i,
                            1_000,
                            저장된_메뉴_그룹.getId(),
                            Collections.singletonList(메뉴_상품)
                    )
            ).getId());
        }

        // when
        List<MenuResponse> 메뉴_조회_결과 = menuService.listResponse();

        // then
        메뉴_목록_조회됨(메뉴_조회_결과, 포함되어야_할_아이디들);
    }

    void 메뉴_생성_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 메뉴_생성_성공됨(MenuResponse menu, MenuCreateRequest request) {
        assertThat(menu.getId()).isNotNull();
        assertThat(menu.getName()).isEqualTo(request.getName());
        assertThat(menu.getPrice()).isEqualTo(request.getPrice());
        assertThat(menu.getMenuGroup()).isEqualTo(request.getMenuGroupId());
    }

    void 메뉴_목록_조회됨(List<MenuResponse> menus, List<Long> containIds) {
        assertThat(menus.size()).isGreaterThanOrEqualTo(containIds.size());
        assertThat(menus.stream().mapToLong(MenuResponse::getId)).containsAll(containIds);
    }
}
