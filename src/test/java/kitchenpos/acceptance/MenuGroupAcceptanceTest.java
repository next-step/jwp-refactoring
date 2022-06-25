package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.helper.KitchenPosBehaviors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MenuGroupAcceptanceTest extends AcceptanceTest {
    @Test
    void 메뉴그룹을_생성하고_조회한다(){
        ExtractableResponse<Response> createResponse = KitchenPosBehaviors.메뉴그룹_생성_요청("치킨");
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<MenuGroup> menuGroups = KitchenPosBehaviors.메뉴그룹_목록조회();
        assertThat(menuGroups).hasSize(1);
    }
}
