package kitchenpos.dto.dto;

public class ExceptionDTO {

    private final String classification;
    private final String message;

    public ExceptionDTO(String classification, String message) {
        this.classification = classification;
        this.message = message;
    }

    public String getClassification() {
        return classification;
    }

    public String getMessage() {
        return message;
    }
}
