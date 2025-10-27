package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CheckboxesPage {

    public static final String CHECKBOXES_CSS = "#checkboxes input[type='checkbox']";

    private WebDriver webDriver;

    /*@FindBy(xpath = "//form[@id='checkboxes']/input[@type='checkbox'][1]")
    private WebElement checkbox1;
    @FindBy(xpath = "//form[@id='checkboxes']/input[@type='checkbox'][2]")
    private WebElement checkbox2;*/

    //should be more robust
    @FindBy(css = CHECKBOXES_CSS)
    private List<WebElement> checkboxes;

    public CheckboxesPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void setCheckbox1(boolean value) {
        //setCheckbox(checkbox1, value);
        setCheckbox(checkboxes.get(0), value);
    }

    public void setCheckbox2(boolean value) {
        setCheckbox(checkboxes.get(1), value);
    }

    public boolean getCheckbox1Value() {
        return checkboxes.get(0).isSelected();
    }

    public boolean getCheckbox2Value() {
        return checkboxes.get(1).isSelected();
    }

    public void clickCheckbox1() {
        checkboxes.get(0).click();
    }

    public void clickCheckbox2() {
        checkboxes.get(1).click();
    }

    private void setCheckbox(WebElement checkbox, boolean value) {
        if (checkbox.isSelected() != value) {
            checkbox.click();
        }
    }
}
