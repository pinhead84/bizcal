/*
 * Decompiled with CFR 0_114.
 */
package lu.tudor.santec.i18n;

import java.io.PrintStream;
import java.util.Enumeration;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import lu.tudor.santec.i18n.Translatrix;

public class SwingLocalizer {
    public static void printUIManagerDefaults() {
        UIDefaults uIDefaults = UIManager.getDefaults();
        Enumeration enumeration = uIDefaults.keys();
        while (enumeration.hasMoreElements()) {
            System.out.println("Key: " + enumeration.nextElement());
        }
    }

    public static void localizeJOptionPane() {
        Translatrix.addBundle("lu.tudor.santec.i18n.resources.WidgetResources-JOptionPane");
        UIManager.put("OptionPane.yesButtonText", Translatrix.getTranslationString("OptionPane.yesButtonText"));
        UIManager.put("OptionPane.noButtonText", Translatrix.getTranslationString("OptionPane.noButtonText"));
        UIManager.put("OptionPane.cancelButtonText", Translatrix.getTranslationString("OptionPane.cancelButtonText"));
    }

    public static void localizeJFileChooser() {
        Translatrix.addBundle("lu.tudor.santec.i18n.resources.WidgetResources-JFileChooser");
        UIManager.put("FileChooser.lookInLabelText", Translatrix.getTranslationString("FileChooser.lookInLabelText"));
        UIManager.put("FileChooser.saveInLabelText", Translatrix.getTranslationString("FileChooser.saveInLabelText"));
        UIManager.put("FileChooser.fileNameLabelText", Translatrix.getTranslationString("FileChooser.fileNameLabelText"));
        UIManager.put("FileChooser.filesOfTypeLabelText", Translatrix.getTranslationString("FileChooser.filesOfTypeLabelText"));
        UIManager.put("FileChooser.fileNameHeaderText", Translatrix.getTranslationString("FileChooser.fileNameHeaderText"));
        UIManager.put("FileChooser.fileSizeHeaderText", Translatrix.getTranslationString("FileChooser.fileSizeHeaderText"));
        UIManager.put("FileChooser.fileTypeHeaderText", Translatrix.getTranslationString("FileChooser.fileTypeHeaderText"));
        UIManager.put("FileChooser.fileDateHeaderText", Translatrix.getTranslationString("FileChooser.fileDateHeaderText"));
        UIManager.put("FileChooser.fileAttrHeaderText", Translatrix.getTranslationString("FileChooser.fileAttrHeaderText"));
        UIManager.put("FileChooser.saveButtonText", Translatrix.getTranslationString("FileChooser.saveButtonText"));
        UIManager.put("FileChooser.openButtonText", Translatrix.getTranslationString("FileChooser.openButtonText"));
        UIManager.put("FileChooser.directoryOpenButtonText", Translatrix.getTranslationString("FileChooser.directoryOpenButtonText"));
        UIManager.put("FileChooser.cancelButtonText", Translatrix.getTranslationString("FileChooser.cancelButtonText"));
        UIManager.put("FileChooser.updateButtonText", Translatrix.getTranslationString("FileChooser.updateButtonText"));
        UIManager.put("FileChooser.helpButtonText", Translatrix.getTranslationString("FileChooser.helpButtonText"));
        UIManager.put("FileChooser.upFolderToolTipText", Translatrix.getTranslationString("FileChooser.upFolderToolTipText"));
        UIManager.put("FileChooser.homeFolderToolTipText", Translatrix.getTranslationString("FileChooser.homeFolderToolTipText"));
        UIManager.put("FileChooser.newFolderToolTipText", Translatrix.getTranslationString("FileChooser.newFolderToolTipText"));
        UIManager.put("FileChooser.listViewButtonToolTipText", Translatrix.getTranslationString("FileChooser.listViewButtonToolTipText"));
        UIManager.put("FileChooser.detailsViewButtonToolTipText", Translatrix.getTranslationString("FileChooser.detailsViewButtonToolTipText"));
        UIManager.put("FileChooser.saveButtonToolTipText", Translatrix.getTranslationString("FileChooser.saveButtonToolTipText"));
        UIManager.put("FileChooser.openButtonToolTipText", Translatrix.getTranslationString("FileChooser.openButtonToolTipText"));
        UIManager.put("FileChooser.cancelButtonToolTipText", Translatrix.getTranslationString("FileChooser.cancelButtonToolTipText"));
        UIManager.put("FileChooser.updateButtonToolTipText", Translatrix.getTranslationString("FileChooser.updateButtonToolTipText"));
        UIManager.put("FileChooser.helpButtonToolTipText", Translatrix.getTranslationString("FileChooser.helpButtonToolTipText"));
    }
}

