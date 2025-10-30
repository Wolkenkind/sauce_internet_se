package exceptions;

import pages.common.SelectorType;

public class ElementNotFoundException extends AutomationException {
    private final String elementName;
    private final SelectorType type;
    private final String selectorString;
    public ElementNotFoundException(String component, String elementName, SelectorType type, String selectorString, String message) {
        super(component, message);
        this.elementName = elementName;
        this.type = type;
        this.selectorString = selectorString;
    }

    public String getElementName() {
        return elementName;
    }

    public SelectorType getType() {
        return type;
    }

    public String getSelectorString() {
        return selectorString;
    }
}
