package uk.co.suskins.hrvsm.service.Config;

import com.twitter.hbc.core.endpoint.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TwitterConfig {
    private final List<String> terms;
    private final List<String> languages;
    private final List<Location> locationList;
    // Twitter API Config from Application.yml
    @Value("${twitter.client.key:}")
    private String twitterConsumerKey;
    @Value("${twitter.client.secret:}")
    private String twitterConsumerSecret;
    @Value("${twitter.access.token:}")
    private String twitterAccessToken;
    @Value("${twitter.access.secret:}")
    private String twitterAccessSecret;

    public TwitterConfig() {
        //Initialise Array Lists
        terms = new ArrayList<>();
        locationList = new ArrayList<>();
        languages = new ArrayList<>();

        //Add track terms 400 Max
        //Top 100 Most Common Words
        terms.add("the");
        terms.add("be");
        terms.add("to");
        terms.add("of");
        terms.add("and");
        terms.add("a");
        terms.add("in");
        terms.add("that");
        terms.add("have");
        terms.add("I");
        terms.add("it");
        terms.add("for");
        terms.add("not");
        terms.add("on");
        terms.add("with");
        terms.add("he");
        terms.add("as");
        terms.add("you");
        terms.add("do");
        terms.add("at");
        terms.add("this");
        terms.add("but");
        terms.add("his");
        terms.add("by");
        terms.add("from");
        terms.add("they");
        terms.add("we");
        terms.add("say");
        terms.add("her");
        terms.add("she");
        terms.add("or");
        terms.add("will");
        terms.add("an");
        terms.add("my");
        terms.add("one");
        terms.add("all");
        terms.add("would");
        terms.add("their");
        terms.add("there");
        terms.add("what");
        terms.add("so");
        terms.add("up");
        terms.add("out");
        terms.add("if");
        terms.add("about");
        terms.add("who");
        terms.add("get");
        terms.add("which");
        terms.add("when");
        terms.add("me");
        terms.add("make");
        terms.add("can");
        terms.add("like");
        terms.add("time");
        terms.add("no");
        terms.add("just");
        terms.add("him");
        terms.add("know");
        terms.add("take");
        terms.add("person");
        terms.add("into");
        terms.add("year");
        terms.add("your");
        terms.add("good");
        terms.add("could");
        terms.add("them");
        terms.add("see");
        terms.add("other");
        terms.add("than");
        terms.add("then");
        terms.add("now");
        terms.add("look");
        terms.add("only");
        terms.add("its");
        terms.add("it's");
        terms.add("over");
        terms.add("think");
        terms.add("also");
        terms.add("back");
        terms.add("after");
        terms.add("use");
        terms.add("two");
        terms.add("to");
        terms.add("too");
        terms.add("how");
        terms.add("our");
        terms.add("work");
        terms.add("first");
        terms.add("well");
        terms.add("way");
        terms.add("even");
        terms.add("new");
        terms.add("want");
        terms.add("because");
        terms.add("any");
        terms.add("these");
        terms.add("give");
        terms.add("day");
        terms.add("most");
        terms.add("us");
        terms.add("war");

        //Hatebase Words
        terms.add("government");
        terms.add("violence");
        terms.add("threat");
        terms.add("human");
        terms.add("rights");
        terms.add("violation");
        terms.add("hate faggots");
        terms.add("faggots like you");
        terms.add("faggots like");
        terms.add("this nigger");
        terms.add("allah akbar");
        terms.add("your a dirty");
        terms.add("full of white trash");
        terms.add("all niggers");
        terms.add("you nigger");
        terms.add("fucking nigger");
        terms.add("is a fucking");
        terms.add("full of white");
        terms.add("is full of white");
        terms.add("get raped");
        terms.add("shut up nigger");
        terms.add("savages");
        terms.add("faggots usually");
        terms.add("spic cop");
        terms.add("the niggers");
        terms.add("a fucking queer");
        terms.add("a white person");
        terms.add("faggots usually have");
        terms.add("fuck outta here");
        terms.add("they all look");
        terms.add("you a fag");
        terms.add("faggot ass");
        terms.add("raped by");
        terms.add("you niggers");
        terms.add("spic");
        terms.add("all white");
        terms.add("nigger music");
        terms.add("are all white");
        terms.add("is a fag");
        terms.add("how many niggers are");
        terms.add("fucking hate you");
        terms.add("raped");
        terms.add("many niggers are");
        terms.add("niggers are in");
        terms.add("niggers are in my");
        terms.add("of fags");
        terms.add("wetbacks");
        terms.add("fucking hate");
        terms.add("faggots");
        terms.add("homo");

        //Add locations 25 Max
//        locationList.add(
//                new Location(
//                        new Location.Coordinate(-7.57216793459, 49.959999905),
//                        new Location.Coordinate(1.68153079591, 58.6350001085))); //UK

        //Add Languages
        languages.add("en");
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public List<String> getTerms() {
        return terms;
    }

    public String getTwitterConsumerKey() {
        return twitterConsumerKey;
    }

    public void setTwitterConsumerKey(String twitterConsumerKey) {
        this.twitterConsumerKey = twitterConsumerKey;
    }

    public String getTwitterConsumerSecret() {
        return twitterConsumerSecret;
    }

    public void setTwitterConsumerSecret(String twitterConsumerSecret) {
        this.twitterConsumerSecret = twitterConsumerSecret;
    }

    public String getTwitterAccessToken() {
        return twitterAccessToken;
    }

    public void setTwitterAccessToken(String twitterAccessToken) {
        this.twitterAccessToken = twitterAccessToken;
    }

    public String getTwitterAccessSecret() {
        return twitterAccessSecret;
    }

    public void setTwitterAccessSecret(String twitterAccessSecret) {
        this.twitterAccessSecret = twitterAccessSecret;
    }

    public List<String> getLanguages() {
        return languages;
    }
}
