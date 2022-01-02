package kitchenpos.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup 추천메뉴_그룹;

    private Product 후라이드;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        추천메뉴_그룹 = menuGroupRepository.save(new MenuGroup("추천메뉴"));
        후라이드 = productRepository.save(new Product("후라이드", BigDecimal.valueOf(9500)));
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void createMenu() {
        // given
        final MenuCreateRequest 후라이드_두마리_메뉴 = new MenuCreateRequest("후라이드+후라이드", BigDecimal.valueOf(19000), 추천메뉴_그룹.getId(), Arrays.asList(new MenuCreateRequest.MenuProductRequest(후라이드.getId(), 2L)));

        // when
        ExtractableResponse<Response> 메뉴_등록_요청_응답 = 메뉴_등록을_요청(후라이드_두마리_메뉴);

        // then
        메뉴_등록됨(메뉴_등록_요청_응답);
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response) {
        MenuResponse 등록된_메뉴 = response.as(MenuResponse.class);
        final MenuResponse.MenuProduct 등록된_메뉴_상품 = 등록된_메뉴.getMenuProducts().get(0);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(등록된_메뉴.getId()).isNotNull(),
                () -> assertThat(등록된_메뉴.getName()).isEqualTo("후라이드+후라이드"),
                () -> assertThat(등록된_메뉴.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(19000)),
                () -> assertThat(등록된_메뉴.getMenuGroupId()).isEqualTo(추천메뉴_그룹.getId()),
                () -> assertThat(등록된_메뉴_상품.getSeq()).isNotNull(),
                () -> assertThat(등록된_메뉴_상품.getMenuId()).isNotNull(),
                () -> assertThat(등록된_메뉴_상품.getProductId()).isEqualTo(후라이드.getId()),
                () -> assertThat(등록된_메뉴_상품.getQuantity()).isEqualTo(2L)
        );
    }

    public ExtractableResponse<Response> 메뉴_등록을_요청(MenuCreateRequest request) {
        return post("/api/menus", request);
    }
}
