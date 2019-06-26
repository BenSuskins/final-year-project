package uk.co.suskins.hrvsm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.co.suskins.hrvsm.model.models.Tweet;


@Repository
public interface TweetsRepository extends CrudRepository<Tweet, Long> {
    Tweet findByTweetId(long tweetId);
}
