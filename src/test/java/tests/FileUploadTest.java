package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.FileUploadPage;
import tests.common.BaseTest;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUploadTest extends BaseTest {
    private static final String LINK_TEXT = "File Upload";
    private static final int TIMEOUT_SECONDS = 7;
    private static final String UPLOAD_FILE_NAME = "test.txt";
    private static final String UPLOAD_FILE_CONTENT = "Test file content [@#$%^&]";
    private static final int NUMBER_OF_FILES_FOR_UPLOAD = 5;
    private static final String SUCCESS_TEXT = "File Uploaded!";

    public static List<List<String>> prepareAndGetUploadFilesCases() throws IOException {
        FileUtils.cleanupTestData();

        List<String> oneFileCase = new ArrayList<>();
        FileUtils.createTestFile(UPLOAD_FILE_NAME, UPLOAD_FILE_CONTENT);
        oneFileCase.add(FileUtils.getProjectTestDataPath() + File.separator + UPLOAD_FILE_NAME);

        //if FileUpload element would've supported multiple file upload, following case would also be used

        /*List<String> multipleFilesCase = new ArrayList<>();
        String content = UPLOAD_FILE_CONTENT;
        String name = UPLOAD_FILE_NAME;
        for (int i = 0; i < NUMBER_OF_FILES_FOR_UPLOAD; i++) {
            FileUtils.createTestFile(name, content);
            multipleFilesCase.add(FileUtils.getProjectTestDataPath() + File.separator + name);

            content += "/nThis is content added for file " + (i + 2);
            name += (i + 2);
        }*/

        return List.of(oneFileCase/*, multipleFilesCase*/);
    }

    @BeforeEach
    public void navigateAndWait() {
        driver.findElement(By.linkText(LINK_TEXT)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id(FileUploadPage.FILE_INPUT_ID)));

        waitForPageToLoad();
    }

    @ParameterizedTest
    @MethodSource("prepareAndGetUploadFilesCases")
    public void testUploadFiles(List<String> files) throws IOException {
        FileUploadPage page = new FileUploadPage(driver, TIMEOUT_SECONDS);
        StringBuilder inputStringBuilder = new StringBuilder();
        for (String file: files) {
            inputStringBuilder.append(file).append("\n");
        }
        inputStringBuilder.delete(inputStringBuilder.length()-1, inputStringBuilder.length());

        page.setFileInputFile(inputStringBuilder.toString());
        page.clickUploadButton();

        page.waitForUploadedFilesToBeVisible();

        String uploadedFilesText = page.getUploadedFilesText();

        for (String filePath: files) {
            File file = new File(filePath);
            Assertions.assertTrue(
                    uploadedFilesText.contains(file.getName()),
                    "Uploaded files don't contain uploaded file named '" + file.getName() + "'");
        }

        Assertions.assertFalse(page.isUploadButtonPresent(), "Upload button shouldn't be shown");
        Assertions.assertEquals(SUCCESS_TEXT, page.getSuccessHeaderText(), "Success text isn't equal to expected");
    }
}
