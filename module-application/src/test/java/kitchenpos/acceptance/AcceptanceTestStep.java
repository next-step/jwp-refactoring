package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import kitchenpos.acceptance.utils.RestAssuredUtils;
import org.springframework.http.HttpStatus;

public abstract class AcceptanceTestStep<Q, P> {

    private final Class<P> responseClass;

    public AcceptanceTestStep(Class<P> responseClass) {
        this.responseClass = responseClass;
    }

    public ExtractableResponse<Response> 등록_요청(Q requestBody) {
        return 등록_요청(getRequestPath(), requestBody);
    }

    public List<ExtractableResponse<Response>> 등록_요청(List<Q> requestsBody) {
        return requestsBody.stream()
                           .map(this::등록_요청)
                           .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 등록_요청(String requestPath, Q requestBody) {
        return RestAssuredUtils.post(requestPath, requestBody);
    }

    public P 등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.body()
                       .as(responseClass);
    }

    public List<P> 등록됨(List<ExtractableResponse<Response>> responses) {
        return responses.stream()
                        .map(this::등록됨)
                        .collect(Collectors.toList());
    }

    public P 등록되어_있음(Q requestBody) {
        return 등록됨(등록_요청(requestBody));
    }

    public List<P> 등록되어_있음(List<Q> requestBodyList) {
        return 등록됨(등록_요청(requestBodyList));
    }

    public void 등록_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
    }

    public List<P> 목록_조회() {
        ExtractableResponse<Response> response = RestAssuredUtils.get(getRequestPath());
        return response.body()
                       .jsonPath()
                       .getList(".", responseClass);
    }

    public void 목록_조회됨(List<P> actualList, Long... expectedId) {
        assertThat(actualList)
            .extracting(idExtractor()::applyAsLong)
            .contains(expectedId);
    }

    public ExtractableResponse<Response> 수정_요청(String requestPath, long id, Object requestBody) {
        return RestAssuredUtils.put(requestPath,
                                    id,
                                    requestBody);
    }

    public void 수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public void 수정_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
    }

    public ExtractableResponse<Response> 삭제_요청(String requestPath, Long id) {
        return RestAssuredUtils.delete(requestPath, id);
    }

    public void 삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    protected abstract String getRequestPath();

    protected abstract ToLongFunction<P> idExtractor();
}
