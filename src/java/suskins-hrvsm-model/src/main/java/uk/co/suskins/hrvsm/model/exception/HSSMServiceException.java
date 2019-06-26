package uk.co.suskins.hrvsm.model.exception;

public class HSSMServiceException extends HSSMApplicationException {
    public HSSMServiceException(String message) {
        super(message);
    }

    public HSSMServiceException(final String message, final Exception exception) {
        super(message, exception);
    }

    public HSSMServiceException(final Exception e) {
        super(e);
    }
}
