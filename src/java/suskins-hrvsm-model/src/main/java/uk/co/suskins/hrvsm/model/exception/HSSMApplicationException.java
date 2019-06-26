package uk.co.suskins.hrvsm.model.exception;

/*
 * This class represents the parent exception for this application. All other custom exception classes inherit it.
 * */
class HSSMApplicationException extends RuntimeException {

    HSSMApplicationException(String message) {
        super(message);
    }

    HSSMApplicationException(final String message, final Exception exception) {
        super(message, exception);
    }

    HSSMApplicationException(final Exception e) {
        super(e);
    }
}
