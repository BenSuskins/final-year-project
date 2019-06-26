package uk.co.suskins.hrvsm.model.exception;

public class HSSMRepositoryException extends HSSMApplicationException {
    public HSSMRepositoryException(String message) {
        super(message);
    }

    public HSSMRepositoryException(final String message, final Exception exception) {
        super(message, exception);
    }

    public HSSMRepositoryException(final Exception e) {
        super(e);
    }
}
