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
    public void restartUI() throws Exception {
        goTo("/");

        await().until("#name").withText().contains(Pattern.compile("[Christophe Heubès|Julien Buret]"));

        if ("Julien Buret".equals($("#name").getText())) {
            restartUI(); // servlet is statefull : we need to reset UI as it was when servlet was started for the first time
        }
    }

    @Test
    public void should_have_a_title() throws Exception {
        assertThat(title()).isEqualTo("Xebia Face Match");
        assertThat($("h1").getText()).isEqualTo("Xebia Face Match");
    }

    @Test
    public void should_display_images_and_name() throws Exception {
        assertThat($("#firstImage").find("img").getAttribute("src")).endsWith("/images/Christophe%20Heub%C3%A8s.jpg");
        assertThat($("#secondImage").find("img").getAttribute("src")).endsWith("/images/Florent%20Le%20Gall.jpg");
        assertThat($("#name").getText()).isEqualTo("Christophe Heubès");
    }

    @Test
    public void should_click_on_first_image() throws Exception {
        click("#firstImage");

        await().until("#name").withText().equalTo("Julien Buret");
        assertThat($("#firstImage").find("img").getAttribute("src")).endsWith("/images/Gilles%20Mantel.jpg");
        assertThat($("#secondImage").find("img").getAttribute("src")).endsWith("/images/Julien%20Buret.jpg");
        assertThat($("#name").getText()).isEqualTo("Julien Buret");
    }
}
