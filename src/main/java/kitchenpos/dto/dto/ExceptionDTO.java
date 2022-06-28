package kitchenpos.dto.dto;

public class ExceptionDTO {

    private String classification;
    private String message;

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
