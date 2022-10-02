package com.example.renamer3;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Controller {

    public TextField input;
    public TextField path;
    public CheckBox randomDigit;
    public CheckBox randomChar;
    public CheckBox prefix;
    public CheckBox suffix;
    public CheckBox fileName;

    private final Set<PartType> partTypes = new HashSet<>();

    private final Set<Sorter> sortTypes = new HashSet<>();

    public Button go;
    public TextArea listOfFiles;

    private static final String WARNING = "Choose rename options and enter directory";

    public void renameAction(ActionEvent actionEvent) {
        File file = new File(String.valueOf(path.getText()));
        if (file.exists() &&
                file.isDirectory() &&
                isNotBlank(path.getText()) &&
                (!sortTypes.isEmpty()) ||
                (partTypes.contains(PartType.FILE) &&
                        isNotBlank(input.getText()))) {
            Utils.renameAllFiles(path.getText(), input.getText(), partTypes, sortTypes);
            listOfFiles.setText(String.join("\n", Objects.requireNonNull(file.list())));
        } else {
            listOfFiles.setText(WARNING);
        }
    }

    public void addSortDigit(ActionEvent actionEvent) {
        if (randomDigit.isSelected()) {
            sortTypes.add(Sorter.DIGIT);
            sortTypes.remove(Sorter.CHAR);
            partTypes.remove(PartType.FILE);
            randomChar.setSelected(false);
            fileName.setSelected(false);
        }
        if (!randomDigit.isSelected()) {
            sortTypes.remove(Sorter.DIGIT);
        }
    }

    public void addSortChar(ActionEvent actionEvent) {
        if (randomChar.isSelected()) {
            sortTypes.add(Sorter.CHAR);
            sortTypes.remove(Sorter.DIGIT);
            partTypes.remove(PartType.FILE);
            randomDigit.setSelected(false);
            fileName.setSelected(false);
        }
        if (!randomChar.isSelected()) {
            sortTypes.remove(Sorter.CHAR);
        }
    }

    public void partType(ActionEvent actionEvent) {
        if (prefix.isSelected()) {
            partTypes.add(PartType.PREFIX);
        }
        if (!prefix.isSelected()) {
            partTypes.remove(PartType.PREFIX);
        }
        if (suffix.isSelected()) {
            partTypes.add(PartType.SUFFIX);
        }
        if (!suffix.isSelected()) {
            partTypes.remove(PartType.SUFFIX);
        }
        if (fileName.isSelected()) {
            partTypes.add(PartType.FILE);
            sortTypes.clear();
            randomChar.setSelected(false);
            randomDigit.setSelected(false);
        }
        if (!fileName.isSelected()) {
            partTypes.remove(PartType.FILE);
        }
    }

    public void clearFirstElementAction(ActionEvent actionEvent) throws IOException {
        File file = new File(String.valueOf(path.getText()));
        if (isNotBlank(path.getText()) &&
                file.isDirectory() &&
                (!sortTypes.isEmpty() || !partTypes.isEmpty())) {
            Utils.clearFirstElement(path.getText());
        }
        if (file.exists() && file.isDirectory()) {
            listOfFiles.setText(String.join("\n", Objects.requireNonNull(file.list())));
        } else {
            listOfFiles.setText(WARNING);
        }
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.trim().equals("");
    }
}
