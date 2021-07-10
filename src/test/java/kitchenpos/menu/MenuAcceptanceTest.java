package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.MenuGroupAcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuRequest menuRequest;
    private MenuGroupResponse givenMenuGroup;
    private Product givenProduct;


    @BeforeEach
    public void setUp() {
        super.setUp();
        givenMenuGroup = MenuGroupAcceptanceTest.메뉴그룹_등록되어_있음(new MenuGroupRequest("테스트메뉴그룹"));

        menuRequest = new MenuRequest("일번메뉴", BigDecimal.valueOf(1000), givenMenuGroup.getId(), new ArrayList<>());
    }

    @DisplayName("Dto와 JPA를 사용하여 메뉴를 등록할 수 있다")
    @Test
    void createTest() {

        //when
        ExtractableResponse<Response> response = 메뉴_등록_요청(menuRequest);

        //then
        정상_등록(response);
        MenuResponse menuResponse = response.as(MenuResponse.class);
        assertThat(menuRequest.getName()).isEqualTo(menuResponse.getName());
    }

    private ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest menuRequest) {
        return RestAssured
                .given().log().all()
                .body(menuRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus/temp")
                .then().log().all()
                .extract();

    }

}
