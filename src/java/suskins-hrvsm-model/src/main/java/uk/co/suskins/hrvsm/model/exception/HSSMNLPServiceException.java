package uk.co.suskins.hrvsm.model.exception;

public class HSSMNLPServiceException extends HSSMApplicationException {
    public HSSMNLPServiceException(String message) {
        super(message);
    }

    public HSSMNLPServiceException(final String message, final Exception exception) {
        super(message, exception);
    }

    public HSSMNLPServiceException(final Exception e) {
        super(e);
    }
}
