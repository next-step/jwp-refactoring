package kitchenpos.menugroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    private MenuGroupRequest menuGroupRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        menuGroupRequest = new MenuGroupRequest("테스트메뉴그룹");
    }

    @DisplayName("JPA를 사용하여 메뉴그룹을 등록할 수 있다")
    @Test
    @Order(1)
    void createTest() {
        //when
        ExtractableResponse<Response> response = 메뉴그룹_등록_요청(menuGroupRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        MenuGroupResponse menuGroupResponse = response.as(MenuGroupResponse.class);
        assertThat(menuGroupResponse.getName()).isEqualTo(menuGroupRequest.getName());
    }

    @DisplayName("JPA를 사용하여 메뉴그룹을 조회할 수 있다")
    @Test
    @Order(2)
    void listTest() {
        //given
//        메뉴그룹_등록_요청(menuGroupRequest);

        //when
        ExtractableResponse<Response> response = 메뉴그룹_조회_요청();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> menuGroupNames = response.jsonPath().getList(".", MenuGroupResponse.class).stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList());
        assertThat(menuGroupNames).contains(menuGroupRequest.getName());
    }


    private ExtractableResponse<Response> 메뉴그룹_등록_요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured
                .given().log().all()
                .body(menuGroupRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups/temp")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴그룹_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups/temp")
                .then().log().all()
                .extract();

    }


}
