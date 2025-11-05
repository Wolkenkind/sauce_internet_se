package tests;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.FileDownloadPage;
import tests.common.BaseDownloadTest;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileDownloadTest extends BaseDownloadTest {
    private static final String LINK_TEXT = "File Download";
    private static final String CONTENT_DIV_CSS = "#content";
    private static final int TIMEOUT_SECONDS = 7;
    private static final int GLOBAL_TIMEOUT_SECONDS = 120;
    private static final int FILE_CHECK_TIMEOUT_SECONDS = 15;
    private static final int MAX_THREADS = 5;
    private static final long SIZE_CHECK_INTERVAL_MS = 1000;
    private static final long EXISTENCE_CHECK_INTERVAL_MS = 2000;
    private static final long LINK_CLICK_INTERVAL_MS = 2500;
    private static final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

    private String downloadPath = FileUtils.getDownloadPath();

    @BeforeEach
    public void navigateAndWait() {
        driver.findElement(By.linkText(LINK_TEXT)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(CONTENT_DIV_CSS)));

        waitForPageToLoad();
    }

    @BeforeEach
    //@AfterEach
    public void cleanup() {
        FileUtils.cleanupDownloads();
    }

    @AfterAll
    public static void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /*
        This logic assumes there should be no empty files for downloading
        If there could be, we need to change the checkSingleDownload logic
     */
    @Test
    public void testFileDownload() {
        FileDownloadPage page = new FileDownloadPage(driver, TIMEOUT_SECONDS);
        SoftAssertions softly = new SoftAssertions();

        List<String> links = page.getDownloadLinks().stream()
                .filter(link -> {
                    boolean r = page.clickDownloadLink(link);
                    try {
                        Thread.sleep(LINK_CLICK_INTERVAL_MS);
                    } catch (InterruptedException e) {
                        System.out.println("ATTENTION ============= InterruptedException " + e.getMessage());
                        return false;
                    }
                    if (!r) {
                        System.out.println("ATTENTION ============= Clicking link '" + link + "' yields FALSE!");
                    }
                    return r;
                })
                .toList();

        Instant globalTimeout = Instant.now().plusSeconds(GLOBAL_TIMEOUT_SECONDS);

        List<CompletableFuture<Optional<String>>> futures = links.stream()
                    .map(link -> CompletableFuture.supplyAsync(
                            () -> {
                                try {
                                    return checkSingleDownload(link, FILE_CHECK_TIMEOUT_SECONDS, globalTimeout);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }, executor
                    ))
                    .toList();

        System.out.println(LocalTime.now() + ": started all");
        try {
            long remainingMs = Duration.between(Instant.now(), globalTimeout).toMillis();
            long waitMillis = Math.max(100, remainingMs); // At least 100ms
            CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            ).get(waitMillis, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            softly.fail("Error waiting download: " + e.getMessage());
        }

        futures.forEach(
                future -> {
                    if (!future.isDone()) {
                        softly.fail("File check did not complete within global timeout");
                    } else {
                        try {
                            future.get().ifPresent(softly::fail);
                        } catch (Exception e) {
                            softly.fail("Error checking download: " + e.getMessage());
                        }
                    }
                }
        );

        softly.assertAll();
    }

    private Optional<String> checkSingleDownload(String link, int timeoutSec, Instant globalTimeout) throws IOException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + ": starting checking " + link);
        Path path = Paths.get(downloadPath + File.separator + link);
        Instant fileTimeout = Instant.now().plus(timeoutSec, ChronoUnit.SECONDS);
        Instant actualTimeout = fileTimeout.isBefore(globalTimeout) ? fileTimeout : globalTimeout;

        while (Instant.now().isBefore(actualTimeout)) {
            if (Files.exists(path) && Files.size(path) > 0) {
                long size = Files.size(path);
                Thread.sleep(SIZE_CHECK_INTERVAL_MS);
                long newSize = Files.size(path);

                if (size == newSize) {
                    System.out.println("Download completed: " + link + " (" + size + " bytes)");
                    return Optional.empty();
                }
            }
            Thread.sleep(EXISTENCE_CHECK_INTERVAL_MS);
        }

        return Optional.of("Download timeout for " + link + "; " + (fileTimeout.isBefore(globalTimeout) ? "(local)" : "(global)"));
    }
}
