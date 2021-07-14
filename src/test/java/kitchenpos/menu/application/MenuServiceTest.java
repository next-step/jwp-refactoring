package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 기능 테스트")
@SpringBootTest
@Transactional
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    private static final int 콜라가격 = 1000;
    private static final int 감튀가격 = 2000;
    private static final int 버거가격 = 7000;
    private MenuGroupResponse 메뉴그룹;
    private ProductResponse 콜라;
    private ProductResponse 감튀;
    private ProductResponse 버거;
    private MenuProductRequest 요청_콜라;
    private MenuProductRequest 요청_감튀;
    private MenuProductRequest 요청_버거;
    private MenuRequest 메뉴_생성_요청_버거세트;

    @BeforeEach
    void setUp() {
        메뉴그룹 = menuGroupService.create(new MenuGroupRequest("버거킹"));
        콜라 = productService.create(new ProductRequest("콜라",  new BigDecimal(콜라가격)));
        감튀 = productService.create(new ProductRequest("감자튀김", new BigDecimal(감튀가격)));
        버거 = productService.create(new ProductRequest("콰트로치즈와퍼", new BigDecimal(버거가격)));
        요청_콜라 = new MenuProductRequest(콜라.getId(), 1L);
        요청_감튀 = new MenuProductRequest(감튀.getId(), 1L);
        요청_버거 = new MenuProductRequest(버거.getId(), 1L);

        메뉴_생성_요청_버거세트 = new MenuRequest("콰트로치즈와퍼세트",
                new BigDecimal(콜라가격 + 감튀가격 + 버거가격 - 2000L),
                메뉴그룹.getId(),
                Arrays.asList(요청_콜라, 요청_감튀, 요청_버거));
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    public void createTest() throws Exception {
        MenuResponse menuResponse = menuService.create(메뉴_생성_요청_버거세트);

        // then
        assertThat(menuResponse).isNotNull();
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    public void findAllTest() throws Exception {
        MenuResponse menuResponse = menuService.create(메뉴_생성_요청_버거세트);
        // when
        List<MenuResponse> menus = menuService.findAll();

        // then
        assertThat(menus).isNotEmpty();
    }
}