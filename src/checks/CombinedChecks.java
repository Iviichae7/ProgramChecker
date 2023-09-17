package src.checks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
// import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.File;

public class CombinedChecks {
    public static boolean isCourierInstalled() {
        Path path = Paths.get("C:\\Program Files (x86)\\Neoteric\\Bloodtrack Courier\\Courier.exe");
        if (Files.exists(path)) {
            try {
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
                        "wmic datafile where name='C:\\\\Program Files (x86)\\\\Neoteric\\\\BloodTrack Courier\\\\Courier.exe' get Version");
                builder.redirectErrorStream(true);
                Process process = builder.start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().equals("4.11.4.3")) {
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean resolveCourierIssue() {
        Path appFolderPath = Paths.get(System.getProperty("user.home"), "Desktop", "ProgramChecker");
        Path sourceExePath = appFolderPath.resolve("Courier4.11.4.3").resolve("Courier.exe");
        Path sourcePdbPath = appFolderPath.resolve("Courier4.11.4.3").resolve("Courier.pdb");

        Path targetExePath = Paths.get("C:\\Program Files (x86)\\Neoteric\\Bloodtrack Courier\\Courier.exe");
        Path targetPdbPath = Paths.get("C:\\Program Files (x86)\\Neoteric\\Bloodtrack Courier\\Courier.pdb");

        try {
            Files.copy(sourceExePath, targetExePath, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(sourcePdbPath, targetPdbPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean areBatchFilesAndShortcutsPresent() {
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        Path bat414Path = Paths.get(desktopPath, "4.14.bat");
        Path reverseBatPath = Paths.get(desktopPath, "Reverse.bat");
        Path bat414Shortcut = Paths.get(desktopPath, "4.14.lnk");
        Path reverseBatShortcut = Paths.get(desktopPath, "Reverse.lnk");

        return Files.exists(bat414Path) && Files.exists(reverseBatPath) &&
                Files.exists(bat414Shortcut) && Files.exists(reverseBatShortcut);
    }

    public static void ensureBatchFilesAndShortcuts() {
        if (!areBatchFilesAndShortcutsPresent()) {
            resolveBatchFilesAndShortcuts();
        }
    }

    public static boolean resolveBatchFilesAndShortcuts() {
        try {
            String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
            Path programCheckerPath = Paths.get(desktopPath, "ProgramChecker", "BatchFiles");
            Path bat414Path = programCheckerPath.resolve("4.14.bat");
            Path reverseBatPath = programCheckerPath.resolve("Reverse.bat");

            // Copy the batch files to desktop
            Path copiedBat414Path = Paths.get(desktopPath, "4.14.bat");
            Path copiedReverseBatPath = Paths.get(desktopPath, "Reverse.bat");

            Files.copy(bat414Path, copiedBat414Path, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(reverseBatPath, copiedReverseBatPath, StandardCopyOption.REPLACE_EXISTING);

            // Set the copied batch files as hidden
            Files.setAttribute(copiedBat414Path, "dos:hidden", true);
            Files.setAttribute(copiedReverseBatPath, "dos:hidden", true);

            // Create shortcuts on the desktop
            createShortcut(desktopPath + File.separator + "4.14.bat", "4.14");
            createShortcut(desktopPath + File.separator + "Reverse.bat", "Reverse");

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    private static void createShortcut(String targetPath, String shortcutName) throws IOException {
        String psScriptPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator
                + "ProgramChecker" + File.separator + "CreateAdminShortcut.ps1";

        String[] cmd = { "powershell", "-ExecutionPolicy", "Bypass", "-File", psScriptPath, targetPath, shortcutName };

        Process process = new ProcessBuilder(cmd).start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Error waiting for shortcut creation process", e);
        }
    }

    public static boolean isEGalaxyInstalled() {
        // Corrected path based on the wmic command
        Path path = Paths.get("C:\\Program Files (x86)\\eGalaxTouch\\eGalaxTouch.exe");

        if (Files.exists(path)) {
            try {
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
                        "wmic datafile where name='C:\\\\Program Files (x86)\\\\eGalaxTouch\\\\eGalaxTouch.exe' get Version");
                builder.redirectErrorStream(true);
                Process process = builder.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {

                        if (line.trim().equals("5.1.0.6005")) {
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean installEGalaxTouchDriver() {
        try {
            String installerPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator
                    + "ProgramChecker" + File.separator + "eGalaxTouch" + File.separator + "setup.exe";

            ProcessBuilder builder = new ProcessBuilder(installerPath, "/q");
            Process process = builder.start();
            process.waitFor();

            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasNeotericProgramFilesFullControlPermissions() {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
                    "icacls \"C:\\Program Files (x86)\\Neoteric\"");
            builder.redirectErrorStream(true);
            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("APPLICATION PACKAGE AUTHORITY\\ALL APPLICATION PACKAGES:(OI)(CI)(F)")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setNeotericProgramFilesFullControlPermissions() {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
                    "icacls \"C:\\Program Files (x86)\\Neoteric\" /grant \"ALL APPLICATION PACKAGES\":(OI)(CI)F");
            Process process = builder.start();
            process.waitFor();

            // Check exit value. 0 usually indicates successful execution.
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isFireEyeInstalled() {
        Path path = Paths.get("C:\\Program Files (x86)\\FireEye");
        return Files.exists(path);
    }

    public static boolean isCourierDelayPresent() {
        Path delayPath = Paths.get("C:\\Program Files (x86)\\Neoteric\\BloodTrack Courier\\DelayLaunchCourier.cmd");
        return Files.exists(delayPath);
    }

    public static boolean copyDelayLaunchCourierCmd() {
        Path sourcePath = Paths.get(System.getProperty("user.home") + File.separator + "Desktop" + File.separator
                + "ProgramChecker" + File.separator + "CourierDelay" + File.separator + "DelayLaunchCourier.cmd");
        Path destinationPath = Paths
                .get("C:\\Program Files (x86)\\Neoteric\\BloodTrack Courier\\DelayLaunchCourier.cmd");

        try {
            Files.copy(sourcePath, destinationPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean modifyShortcutTarget() {
        try {
            String scriptPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator
                    + "ProgramChecker";

            ProcessBuilder builder = new ProcessBuilder("powershell.exe", ".\\ModifyShortcutTarget.ps1");
            builder.directory(new File(scriptPath)); // Set the working directory
            Process process = builder.start();
            process.waitFor();

            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAutoLogonUIHidden() {
        try {
            Process process = Runtime.getRuntime().exec(
                    "reg query \"HKLM\\SOFTWARE\\Microsoft\\Windows Embedded\\EmbeddedLogon\" /v HideAutoLogonUI");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("HideAutoLogonUI") && line.contains("0x1")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean enableAutoLogonUI() {
        if (isAutoLogonUIHidden()) {
            String userHome = System.getProperty("user.home");
            Path cmdPath = Paths.get(userHome, "Desktop", "ProgramChecker", "BlackScreenFix", "EnableAutoLogonUI.cmd");
            try {
                Process process = new ProcessBuilder(cmdPath.toString()).start();
                process.waitFor();
                return true;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean copyFolderToProgramFiles() {
        String sourcePath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator
                + "ProgramChecker" + File.separator + "4.14";
        String destinationPath = "C:" + File.separator + "Program Files (x86)" + File.separator + "4.14";

        Path source = Paths.get(sourcePath);
        Path target = Paths.get(destinationPath);

        try {
            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                        throws IOException {
                    Files.createDirectories(target.resolve(source.relativize(dir)));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
