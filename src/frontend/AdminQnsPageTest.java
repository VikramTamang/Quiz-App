package frontend;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

/**
 * Class AdminQnsPageTest.
 * This class is responsible for handling specific functionalities.
 */
class AdminQnsPageTest {
    private AdminQnsPage adminQnsPage;

    @BeforeEach
    void setUp() {
        adminQnsPage = new AdminQnsPage();
    }

    @AfterEach
    void tearDown() {
        adminQnsPage.dispose();
    }

    @Test
    void testEmptyFieldsValidation() {
        SwingUtilities.invokeLater(() -> {
            adminQnsPage.getAddButton().doClick();
        });

        try {
            SwingUtilities.invokeAndWait(() -> {});
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }

        assertEquals("", adminQnsPage.getQuestionField().getText());
        assertEquals("", adminQnsPage.getOptionAField().getText());
        assertEquals("", adminQnsPage.getOptionBField().getText());
        assertEquals("", adminQnsPage.getOptionCField().getText());
        assertEquals("", adminQnsPage.getOptionDField().getText());
    }

    @Test
    void testClearFieldsAfterSubmission() {
        adminQnsPage.getQuestionField().setText("Sample Question?");
        adminQnsPage.getOptionAField().setText("Option A");
        adminQnsPage.getOptionBField().setText("Option B");
        adminQnsPage.getOptionCField().setText("Option C");
        adminQnsPage.getOptionDField().setText("Option D");
        adminQnsPage.getCorrectOptionBox().setSelectedIndex(1);
        adminQnsPage.getDifficultyBox().setSelectedIndex(2);

        SwingUtilities.invokeLater(() -> {
            adminQnsPage.clearFieldsPublic();
        });

        try {
            SwingUtilities.invokeAndWait(() -> {});
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }

        assertEquals("", adminQnsPage.getQuestionField().getText());
        assertEquals("", adminQnsPage.getOptionAField().getText());
        assertEquals("", adminQnsPage.getOptionBField().getText());
        assertEquals("", adminQnsPage.getOptionCField().getText());
        assertEquals("", adminQnsPage.getOptionDField().getText());
        assertEquals(0, adminQnsPage.getCorrectOptionBox().getSelectedIndex());
        assertEquals(0, adminQnsPage.getDifficultyBox().getSelectedIndex());
    }
}
