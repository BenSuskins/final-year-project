package uk.co.suskins.hrvsm.model.models;

import java.util.Date;

public class ProcessedStatus {
    private final long id;
    private final String originalText;
    private final String processedText;
    private final String countryCode;
    private final String user;
    private final Date createdAt;

    public ProcessedStatus() {
        this.id = 0;
        this.originalText = "";
        this.processedText = "";
        this.countryCode = "";
        this.createdAt = new Date();
        this.user = "";
    }

    public ProcessedStatus(long id,
                           String originalText,
                           String processedText,
                           String countryCode,
                           String user,
                           Date createdAt) {
        this.id = id;
        this.originalText = originalText;
        this.processedText = processedText;
        this.countryCode = countryCode;
        this.createdAt = createdAt;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getProcessedText() {
        return processedText;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUser() {
        return user;
    }
}
