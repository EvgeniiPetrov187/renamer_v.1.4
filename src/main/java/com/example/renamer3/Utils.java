package com.example.renamer3;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Utils {

    private static final String[] chars = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static void renameAllFiles(String location, String partToChange, Set<PartType> statuses, Set<Sorter> sorterSet) {
        File directory = new File(location);
        String[] files = Objects.requireNonNull(directory.list());

        for (String fileName : files) {
            Path path = Paths.get(directory.getAbsolutePath() + "\\" + fileName);

            if (statuses.contains(PartType.SUFFIX) && sorterSet.contains(Sorter.CHAR)) {
                getRenamedFileBySuffix(path, " " + getRandomChar());
            } else if (statuses.contains(PartType.PREFIX) && sorterSet.contains(Sorter.CHAR)) {
                getRenamedFileByPrefix(path, getRandomChar() + " ");
            } else if (statuses.contains(PartType.FILE) && sorterSet.contains(Sorter.CHAR)) {
                getRenamedFile(path, getRandomChar());
            } else if (statuses.contains(PartType.SUFFIX) && sorterSet.contains(Sorter.DIGIT)) {
                getRenamedFileBySuffix(path, " " + getRandomDigit());
            } else if (statuses.contains(PartType.PREFIX) && sorterSet.contains(Sorter.DIGIT)) {
                getRenamedFileByPrefix(path, getRandomDigit() + " ");
            } else if (statuses.contains(PartType.FILE) && sorterSet.contains(Sorter.DIGIT)) {
                getRenamedFile(path, String.valueOf(getRandomDigit()));
            } else if (statuses.contains(PartType.SUFFIX) && sorterSet.isEmpty()) {
                getRenamedFileBySuffix(path, partToChange);
            } else if (statuses.contains(PartType.PREFIX) && sorterSet.isEmpty()) {
                getRenamedFileByPrefix(path, partToChange);
            } else if (statuses.contains(PartType.FILE) && sorterSet.isEmpty()) {
                getRenamedFile(path, partToChange);
            }
        }
    }

    public static void clearFirstElement(String location) {
        try {
            Files.walkFileTree(Paths.get(location), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    getClearedFileByPrefix(path);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getClearedFileByPrefix(Path path) {
        String source = path.toAbsolutePath().toString();
        List<String> fullPath = Arrays.asList(source.split("\\\\"));
        int lastElement = fullPath.size() - 1;
        String newFileName = fullPath.get(lastElement).substring(1).trim();
        moveFiles(path, newFileName, lastElement, fullPath);
    }

    private static void getRenamedFileByPrefix(Path path, String newPrefix) {
        String source = path.toAbsolutePath().toString();
        List<String> fullPath = Arrays.asList(source.split("\\\\"));
        int lastElement = fullPath.size() - 1;
        String newFileName = newPrefix + fullPath.get(lastElement);
        moveFiles(path, newFileName, lastElement, fullPath);
    }

    private static void getRenamedFileBySuffix(Path path, String newSuffix) {
        String source = path.toAbsolutePath().toString();
        List<String> fullPath = Arrays.asList(source.split("\\\\"));
        int lastElement = fullPath.size() - 1;
        String[] fileAndSuffix = fullPath.get(lastElement).split("\\.");
        fileAndSuffix[0] = fileAndSuffix[0] + newSuffix;
        String newFileName = String.join(".", fileAndSuffix);
        moveFiles(path, newFileName, lastElement, fullPath);
    }

    private static void getRenamedFile(Path path, String newFileName) {
        String source = path.toAbsolutePath().toString();
        List<String> fullPath = Arrays.asList(source.split("\\\\"));
        int lastElement = fullPath.size() - 1;
        String[] fileAndSuffix = fullPath.get(lastElement).split("\\.");
        fileAndSuffix[0] = newFileName;
        String newFileNameFull = String.join(".", fileAndSuffix);
        moveFiles(path, newFileNameFull, lastElement, fullPath);
    }

    private static void moveFiles(Path path, String newFileName, int lastElement, List<String> fullPath) {
        fullPath.set(lastElement, newFileName);
        String newPath = String.join("\\", fullPath);
        Path dest = Paths.get(newPath);
        if (Files.exists(dest)) {
            String[] fileAndSuffix = newPath.split("\\.");
            fileAndSuffix[0] = fileAndSuffix[0] + "(" + (int) (Math.random() * Integer.MAX_VALUE) + ")";
            String newFileNameFull = String.join(".", fileAndSuffix);
            dest = Paths.get(newFileNameFull);
        }
        try {
            Files.move(path, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getRandomChar() {
        return chars[(int) (Math.random() * 25)];
    }

    private static int getRandomDigit() {
       return (int) (Math.random() * 9);
    }
}
