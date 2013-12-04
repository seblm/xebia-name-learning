package fr.xebia;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Challenge {

    private static final Pattern IMAGE_NAME_EXTRACTOR = Pattern.compile("/images/(.+).jpg");

    private List<String> images;
    private Random random;
    private String firstImage;
    private String secondImage;
    private String name;
    private String answer;

    public Challenge(Set<String> imagesPaths, Random random) throws RuntimeException {
        this.images = imagesPaths.stream()
                .map((imagePath) -> IMAGE_NAME_EXTRACTOR.matcher(imagePath))
                .filter((imagePathMatcher) -> imagePathMatcher.matches())
                .map((imagePathMatcher) -> imagePathMatcher.group(1))
                .collect(Collectors.<String>toList());
        this.random = random;
        this.next();
    }

    public void next() throws RuntimeException {
        int firstIndex = random.nextInt(images.size());
        int secondIndex;
        do {
            secondIndex = random.nextInt(images.size());
        } while (secondIndex == firstIndex);
        boolean answerIsFirst = random.nextBoolean();

        try {
            firstImage = format("/images/%s.jpg", URLEncoder.encode(images.get(firstIndex), "UTF-8").replaceAll("\\+", "%20"));
            secondImage = format("/images/%s.jpg", URLEncoder.encode(images.get(secondIndex), "UTF-8").replaceAll("\\+", "%20"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        name = images.get(answerIsFirst ? firstIndex : secondIndex);
        answer = answerIsFirst ? "firstImage" : "secondImage";
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
