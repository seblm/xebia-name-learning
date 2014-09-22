package fr.xebia;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Challenge {
    private static final Pattern IMAGE_NAME_EXTRACTOR = Pattern.compile("/images/(.+).jpg");

    private final transient List<String> images;
    private final transient Questions questions;

    private String firstImage;
    private String secondImage;
    private String name;
    private String answer;

    public Challenge(Set<String> imagesPaths, Questions questions) throws RuntimeException {
        this.images = imagesPaths.stream()
                .map((imagePath) -> IMAGE_NAME_EXTRACTOR.matcher(imagePath))
                .filter((imagePathMatcher) -> imagePathMatcher.matches())
                .map((imagePathMatcher) -> imagePathMatcher.group(1))
                .sorted()
                .collect(Collectors.<String>toList());
        this.questions = questions;
        this.next();
    }

    public void next() throws RuntimeException {
        Question question = questions.next(images.size());
        firstImage = format("/images/%s.jpg", images.get(question.first));
        secondImage = format("/images/%s.jpg", images.get(question.second));
        name = images.get(question.firstIsAnswer ? question.first : question.second);
        answer = question.firstIsAnswer ? "firstImage" : "secondImage";
    }

    public String getFirstImage() {
        return firstImage;
    }

    public String getSecondImage() {
        return secondImage;
    }

    public String getName() {
        return name;
    }

    public String getAnswer() {
        return answer;
    }
}
