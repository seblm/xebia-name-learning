package fr.xebia;

import fr.xebia.misc.PhantomJsTest;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class ChallengePageTest extends PhantomJsTest {
    @ClassRule
    public static TomcatRule tomcat = new TomcatRule("0 1 true 2 3 false");

    @Override
    protected String defaultUrl() {
        return "http://localhost:8080";
    }

    @Before
    public void restartUI() {
        goTo("/");

        await().until(".guess").withText().contains(Pattern.compile("Où est [Alexandre Dergham|Anne Beauchart] ?")).isPresent();

        if ("Où est Anne Beauchart ?".equals($(".guess").getText())) {
            restartUI(); // servlet is statefull : we need to reset UI as it was when servlet was started for the first time
        }
    }

    @Test
    public void should_have_a_title() throws Exception {
        assertThat(title()).isEqualTo("Xebia Name Learning");
    }

    @Test
    public void should_display_images_name_and_initial_score() {
        assertThat($(".firstImage").find("img").getAttribute("src")).endsWith("/images/Alexandre%20Dergham.jpg");
        assertThat($(".secondImage").find("img").getAttribute("src")).endsWith("/images/Alexandre%20Dutra.jpg");
        assertThat($(".guess").getText()).isEqualTo("Où est Alexandre Dergham ?");
        assertThat($(".score").find("span").getText()).isEqualTo("0");
    }

    @Test
    public void should_click_on_first_image_and_display_next_challenge() throws InterruptedException {
        click(".firstImage");

        assertThat($(".score").find("span").getText()).isEqualTo("1");
        Thread.sleep(2000); // TODO ugly
        assertThat($(".firstImage").find("img").getAttribute("src")).endsWith("/images/Alexis%20Kinsella.jpg");
        assertThat($(".secondImage").find("img").getAttribute("src")).endsWith("/images/Anne%20Beauchart.jpg");
        assertThat($(".guess").getText()).isEqualTo("Où est Anne Beauchart ?");
    }

    @Test
    public void should_click_on_good_image_and_win() {
        click(".firstImage");

        assertThat($(".score").find("span").getText()).isEqualTo("1");
    }

    @Test
    public void should_click_on_bad_image_and_loose() {
        click(".secondImage");

        assertThat($(".score").find("span").getText()).isEqualTo("-1");
    }
}
