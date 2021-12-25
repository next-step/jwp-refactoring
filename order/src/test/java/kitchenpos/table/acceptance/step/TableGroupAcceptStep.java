package kitchenpos.table.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.dto.TableResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.utils.RestAssuredUtil.delete;
import static kitchenpos.utils.RestAssuredUtil.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableGroupAcceptStep {
    private static final String BASE_URL = "/api/table-groups";

    public static ExtractableResponse<Response> 단체_지정_요청(TableGroupRequest 등록_요청_데이터) {
        return post(BASE_URL, 등록_요청_데이터);
    }

    public static TableGroupResponse 단체_지정_확인(ExtractableResponse<Response> 단체_지정_응답, TableGroupRequest 등록_요청_데이터) {
        TableGroupResponse 등록된_단체 = 단체_지정_응답.as(TableGroupResponse.class);

        assertAll(
                () -> assertThat(단체_지정_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(단체_지정_응답.header("Location")).isNotBlank(),
                () -> 단체에_등록된_테이블_확인(등록된_단체, 등록_요청_데이터)
        );

        return 등록된_단체;
    }

    private static void 단체에_등록된_테이블_확인(TableGroupResponse 등록된_단체, TableGroupRequest 등록_요청_데이터) {
        List<Long> 등록된_테이블 = 등록된_단체.getOrderTables()
                .stream()
                .map(TableResponse::getId)
                .collect(Collectors.toList());

        assertThat(등록된_테이블).containsExactlyElementsOf(등록_요청_데이터.getOrderTableIds());
    }

    public static ExtractableResponse<Response> 단체_해지_요청(ExtractableResponse<Response> 단체_등록_응답) {
        return delete(단체_등록_응답.header("Location"));
    }

    public static void 단체_해지_확인(ExtractableResponse<Response> 단체_해지_응답) {
        assertThat(단체_해지_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
