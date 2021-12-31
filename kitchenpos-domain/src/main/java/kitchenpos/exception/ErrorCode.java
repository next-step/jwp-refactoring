package kitchenpos.exception;

public enum ErrorCode {

	WRONG_INPUT("입력값을 확인해주세요"),
	DUPLICATE_INPUT("입력값이 중복입니다"),
	NOT_FOUND("해당 데이터를 찾을 수 없습니다"),
	INTERNAL_SERVER_ERROR("서버 내부 에러"),
	UNAUTHORIZED("인증 실패");

	private String message;

	ErrorCode(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
