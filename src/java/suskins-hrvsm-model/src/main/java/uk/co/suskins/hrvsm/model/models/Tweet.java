package uk.co.suskins.hrvsm.model.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tweets", schema = "public")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "tweet_id")
    private long tweetId;
    @Column(name = "tweet")
    private String tweet;
    @Column(name = "processed_tweet")
    private String processedTweet;
    @Column(name = "probability")
    private double probability;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "created_at")
    private Date createdAt;

    public Tweet() {

    }

    public Tweet(ProcessedStatus tweet, double probability) {
        this.tweetId = tweet.getId();
        this.userName = tweet.getUser();
        this.tweet = tweet.getOriginalText();
        this.processedTweet = tweet.getProcessedText();
        this.countryCode = tweet.getCountryCode();
        this.createdAt = tweet.getCreatedAt();
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getProcessedTweet() {
        return processedTweet;
    }

    public void setProcessedTweet(String processedTweet) {
        this.processedTweet = processedTweet;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
