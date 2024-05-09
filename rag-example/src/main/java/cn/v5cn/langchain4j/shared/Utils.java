package cn.v5cn.langchain4j.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 工具
 * @author ZYW
 */
public class Utils {

    public static void startConversationWith(Assistant assistant) {
        Logger log = LoggerFactory.getLogger(Assistant.class);
        try(Scanner scanner = new Scanner(System.in)) {
            while (true) {
                log.info("111111==================================================");
                log.info("User: ");
                String userQuery = scanner.nextLine();
                log.info("22222==================================================");

                if ("exit".equalsIgnoreCase(userQuery)) {
                    break;
                }
                String agentAnswer = assistant.answer(userQuery);
                log.info("33333==================================================");
                log.info("Assistant: " + agentAnswer);
            }
        }
    }

    public static PathMatcher glob(String glob) {
        return FileSystems.getDefault().getPathMatcher("glob:" + glob);
    }
    public static Path toPath(String relativePath) {
        try {
            URL fileUrl = Utils.class.getClassLoader().getResource(relativePath);
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
