package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.utils.RestAssuredUtils;

public abstract class AcceptanceTestStep<Q, P> {

    private final Class<Q> requestClass;
    private final Class<P> responseClass;

    public AcceptanceTestStep(Class<Q> requestClass, Class<P> responseClass) {
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    public ExtractableResponse<Response> 등록_요청(Q requestBody) {
        return 등록_요청(getRequestPath(), requestBody);
    }

    public List<ExtractableResponse<Response>> 등록_요청(List<Q> requestsBody) {
        return requestsBody.stream().map(this::등록_요청)
            .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 등록_요청(String requestPath, Q requestBody) {
        return RestAssuredUtils.post(requestPath, requestBody);
    }

    public P 등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.body().as(responseClass);
    }

    public List<P> 등록됨(List<ExtractableResponse<Response>> responses) {
        return responses.stream().map(this::등록됨).collect(Collectors.toList());
    }

    public void 등록_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
    }

    public List<P> 목록_조회() {
        ExtractableResponse<Response> response = RestAssuredUtils.get(getRequestPath());
        return response.body().jsonPath().getList(".", responseClass);
    }

    public void 목록_조회됨(List<P> actualList, Long ...expectedId) {
        assertThat(actualList)
            .extracting(idExtractor()::applyAsLong)
            .contains(expectedId);
    }

    public ExtractableResponse<Response> 수정_요청(String requestPath, long id, Q requestBody) {
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

    public void 삭제_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.NO_CONTENT.value());
    }

    protected abstract String getRequestPath();

    protected abstract ToLongFunction<P> idExtractor();
}
