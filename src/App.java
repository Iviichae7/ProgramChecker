package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.List;
import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import src.checks.CombinedChecks;

import java.awt.Color;

public class App {

    private static ImageIcon greenIcon;
    private static ImageIcon redIcon;
    private static List<JLabel> resolutionLabels = new ArrayList<>();
    private static JFrame frame;
    private static JPanel resolutionPanel;
    private static JLabel folderResolutionLabel;
    private static JButton runChecksButton;

    public static void main(String[] args) {
        if (!"Haemonetics".equals(System.getProperty("user.name"))) {
            JOptionPane.showMessageDialog(null,
                    "You are not signed in as Haemonetics user.\nPlease configure to always run as Haemonetic user.\n"
                            + "To do this press 'Windows Key + R' and type 'netplwiz'\nOnce finished, Run the program again.",
                    "User Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        initializeIcons();
        List<Object> uiComponents = setupUI();
        JFrame frame = (JFrame) uiComponents.get(0);
        JLabel label = (JLabel) uiComponents.get(1);
        JLabel eGalaxyLabel = (JLabel) uiComponents.get(2);
        JLabel batFilesLabel = (JLabel) uiComponents.get(3);
        JLabel hasNeotericPermissions = (JLabel) uiComponents.get(4);
        JLabel fireEyeLabel = (JLabel) uiComponents.get(5);
        JLabel isCourierDelay = (JLabel) uiComponents.get(6);
        JLabel isBlackScreen = (JLabel) uiComponents.get(7);
        JLabel folderCheckLabel = (JLabel) uiComponents.get(8);

        updateIconsForChecks(label, eGalaxyLabel, batFilesLabel, hasNeotericPermissions, fireEyeLabel, isCourierDelay,
                isBlackScreen, folderCheckLabel);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    public static List<Object> setupUI() {
        List<Object> uiComponents = new ArrayList<>();
        frame = new JFrame("Kiosk Checker");

        ImageIcon frameIcon = new ImageIcon(App.class.getResource("/resources/images/MyApplicationIcon.png"));
        frame.setIconImage(frameIcon.getImage());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
        panel.add(subPanel, BorderLayout.CENTER);

        int leftPadding = 30;
        int rightPadding = 10;
        subPanel.setBorder(new EmptyBorder(20, leftPadding, 0, rightPadding));

        // Check labels
        JLabel label = new JLabel("Checking if the version of Courier is 4.11.4.3.");
        subPanel.add(label);
        JLabel eGalaxyLabel = new JLabel("Checking if the version of eGalaxy is 5.14.");
        subPanel.add(eGalaxyLabel);
        JLabel batFilesLabel = new JLabel("Checking if batch files and shortcuts exist.");
        subPanel.add(batFilesLabel);
        JLabel hasNeotericPermissions = new JLabel("Checking if neoteric folder has the correct permissions.");
        subPanel.add(hasNeotericPermissions);
        JLabel fireEyeLabel = new JLabel("Checking if FireEye is installed.");
        subPanel.add(fireEyeLabel);
        JLabel isCourierDelay = new JLabel("Checking if there is Courier Delay.");
        subPanel.add(isCourierDelay);
        JLabel autoLogonCheckLabel = new JLabel("Checking if Auto Logon UI is not Hidden.");
        subPanel.add(autoLogonCheckLabel);
        JLabel folderCheckLabel = new JLabel("Checking if 4.14 folder is present in Program Files (x86).");
        subPanel.add(folderCheckLabel);

        // Resolution Panel
        resolutionPanel = new JPanel();
        resolutionPanel.setLayout(new BoxLayout(resolutionPanel, BoxLayout.PAGE_AXIS));
        resolutionPanel.setBorder(new EmptyBorder(20, leftPadding, 0, rightPadding));
        resolutionPanel.setBackground(Color.YELLOW); // Debug step 2
        panel.add(resolutionPanel, BorderLayout.SOUTH);
        subPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Resolution Labels
        JLabel courierResolutionLabel = new JLabel("");
        subPanel.add(courierResolutionLabel);
        resolutionLabels.add(courierResolutionLabel);

        JLabel autoLogonResolutionLabel = new JLabel("");
        subPanel.add(autoLogonResolutionLabel);
        resolutionLabels.add(autoLogonResolutionLabel);

        JLabel batchFilesResolutionLabel = new JLabel("");
        subPanel.add(batchFilesResolutionLabel);
        resolutionLabels.add(batchFilesResolutionLabel);

        folderResolutionLabel = new JLabel("");
        subPanel.add(folderResolutionLabel);
        resolutionLabels.add(folderResolutionLabel);

        JLabel neotericPermissionsResolutionLabel = new JLabel("");
        subPanel.add(neotericPermissionsResolutionLabel);
        resolutionLabels.add(neotericPermissionsResolutionLabel);

        JLabel courierDelayResolutionLabel = new JLabel("");
        subPanel.add(courierDelayResolutionLabel);
        resolutionLabels.add(courierDelayResolutionLabel);

        JLabel fireEyeResolutionLabel = new JLabel("");
        subPanel.add(fireEyeResolutionLabel);
        resolutionLabels.add(fireEyeResolutionLabel);

        JLabel eGalaxTouchResolutionLabel = new JLabel("");
        resolutionPanel.add(eGalaxTouchResolutionLabel);
        resolutionLabels.add(eGalaxTouchResolutionLabel);

        // Add components to uiComponents list
        uiComponents.add(frame);
        uiComponents.add(label);
        uiComponents.add(eGalaxyLabel);
        uiComponents.add(batFilesLabel);
        uiComponents.add(hasNeotericPermissions);
        uiComponents.add(fireEyeLabel);
        uiComponents.add(isCourierDelay);
        uiComponents.add(autoLogonCheckLabel);
        uiComponents.add(folderCheckLabel);

        // Resolution uiComponents list
        uiComponents.add(autoLogonResolutionLabel);
        uiComponents.add(batchFilesResolutionLabel);
        uiComponents.add(folderResolutionLabel);

        JButton resolveButton = new JButton("Resolve");
        resolveButton.setPreferredSize(new Dimension(100, 30));

        runChecksButton = new JButton("Run Checks");
        runChecksButton.setPreferredSize(new Dimension(120, 30));
        runChecksButton.setVisible(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(resolveButton);
        buttonPanel.add(runChecksButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        resolveButton.addActionListener(e -> resolveIssues());
        runChecksButton.addActionListener(e -> {
            // Hide the Run Checks button
            runChecksButton.setVisible(false);

            // Clear the Resolution labels
            for (JLabel resolutionLabel : resolutionLabels) {
                resolutionLabel.setText("");
                resolutionLabel.setIcon(null);
            }

            // Run the checks again
            updateIconsForChecks(label, eGalaxyLabel, batFilesLabel, hasNeotericPermissions, fireEyeLabel,
                    isCourierDelay,
                    autoLogonCheckLabel, folderCheckLabel);

            // Repaint the frame to reflect changes
            frame.repaint();
        });
        return uiComponents;
    }

    public static void initializeIcons() {
        greenIcon = new ImageIcon(App.class.getResource("/resources/images/greetick.png"));
        redIcon = new ImageIcon(App.class.getResource("/resources/images/xtick.png"));
        Image greenImage = greenIcon.getImage();
        Image redImage = redIcon.getImage();
        greenIcon = new ImageIcon(greenImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
        redIcon = new ImageIcon(redImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    }

    public static ImageIcon getGreenIcon() {
        return greenIcon;
    }

    public static ImageIcon getRedIcon() {
        return redIcon;
    }

    public static void updateIconsForChecks(JLabel... labels) {
        if (CombinedChecks.isCourierInstalled()) {
            labels[0].setIcon(getGreenIcon());
        } else {
            labels[0].setIcon(getRedIcon());
        }
        if (CombinedChecks.isEGalaxyInstalled()) {
            labels[1].setIcon(getGreenIcon());
        } else {
            labels[1].setIcon(getRedIcon());
        }
        if (CombinedChecks.areBatchFilesAndShortcutsPresent()) {
            labels[2].setIcon(getGreenIcon());
        } else {
            labels[2].setIcon(getRedIcon());
        }
        if (CombinedChecks.hasNeotericProgramFilesFullControlPermissions()) {
            labels[3].setIcon(getGreenIcon());
        } else {
            labels[3].setIcon(getRedIcon());
        }
        if (CombinedChecks.isFireEyeInstalled()) {
            labels[4].setIcon(getGreenIcon());
        } else {
            labels[4].setIcon(getRedIcon());
        }
        if (CombinedChecks.isCourierDelayPresent()) {
            labels[5].setIcon(getGreenIcon());
        } else {
            labels[5].setIcon(getRedIcon());
        }
        if (CombinedChecks.isAutoLogonUIHidden()) {
            labels[6].setIcon(getRedIcon());
        } else {
            labels[6].setIcon(getGreenIcon());
        }
        if (Files.exists(Paths.get("C:" + File.separator + "Program Files (x86)" + File.separator + "4.14"))) {
            labels[7].setIcon(getGreenIcon());
        } else {
            labels[7].setIcon(getRedIcon());
        }
    }

    public static void resolveIssues() {
        boolean issuesToResolve = false;

        // If Courier 4.11.4.3 is not present, resolve the issue
        if (!CombinedChecks.isCourierInstalled()) {
            resolutionLabels.get(0).setText("Resolving Courier.");
            if (CombinedChecks.resolveCourierIssue()) {
                resolutionLabels.get(0).setText("Courier version adjusted.");
                resolutionLabels.get(0).setIcon(getGreenIcon());
            } else {
                resolutionLabels.get(0).setText("Courier resolution failed");
                resolutionLabels.get(0).setIcon(getRedIcon());
            }
        }

        // If AutoLogonUI is hidden, attempt to enable it
        if (CombinedChecks.isAutoLogonUIHidden()) {
            resolutionLabels.get(1).setText("Enabling Auto Logon UI.");
            if (CombinedChecks.enableAutoLogonUI()) {
                resolutionLabels.get(1).setText("Auto Logon UI enabled.");
                resolutionLabels.get(1).setIcon(getGreenIcon());
            } else {
                resolutionLabels.get(1).setText("Failed to enable Auto Logon UI.");
                resolutionLabels.get(1).setIcon(getRedIcon());
            }
            resolutionLabels.get(1).repaint();
        }

        // If batch files and shortcuts are not present, resolve the issue
        if (!CombinedChecks.areBatchFilesAndShortcutsPresent()) {
            resolutionLabels.get(2).setText("Resolving batch files and shortcuts.");
            if (CombinedChecks.resolveBatchFilesAndShortcuts()) {
                resolutionLabels.get(2).setText("Batch file shortcuts created.");
                resolutionLabels.get(2).setIcon(getGreenIcon());
            } else {
                resolutionLabels.get(2).setText("Batch files and shortcuts resolution failed.");
                resolutionLabels.get(2).setIcon(getRedIcon());
            }
        }
        if (!Files.exists(Paths.get("C:" + File.separator + "Program Files (x86)" + File.separator + "4.14"))) {
            folderResolutionLabel.setText("Copying 4.14 folder to Program Files (x86).");
            if (CombinedChecks.copyFolderToProgramFiles()) {
                folderResolutionLabel.setText("4.14 folder created.");
                folderResolutionLabel.setIcon(getGreenIcon());
            } else {
                folderResolutionLabel.setText("Failed to copy 4.14 folder.");
                folderResolutionLabel.setIcon(getRedIcon());
            }
        }
        if (!CombinedChecks.hasNeotericProgramFilesFullControlPermissions()) {
            resolutionLabels.get(3).setText("Setting permissions for Neoteric folder.");
            if (CombinedChecks.setNeotericProgramFilesFullControlPermissions()) {
                resolutionLabels.get(3).setText("Neoteric folder permissions set successfully.");
                resolutionLabels.get(3).setIcon(getGreenIcon());
            } else {
                resolutionLabels.get(3).setText("Failed to set Neoteric folder permissions.");
                resolutionLabels.get(3).setIcon(getRedIcon());
            }
        }

        if (!CombinedChecks.isCourierDelayPresent()) {
            resolutionLabels.get(4).setText("Copying DelayLaunchCourier.cmd.");
            if (CombinedChecks.copyDelayLaunchCourierCmd()) {
                resolutionLabels.get(4).setText("DelayLaunchCourier.cmd copied.");
                resolutionLabels.get(4).setIcon(getGreenIcon());
            } else {
                resolutionLabels.get(4).setText("Failed to copy DelayLaunchCourier.cmd.");
                resolutionLabels.get(4).setIcon(getRedIcon());
            }

            resolutionLabels.get(4).setText("Modifying BloodTrack Courier shortcut target.");
            if (CombinedChecks.modifyShortcutTarget()) {
                resolutionLabels.get(4).setText("BloodTrack Courier shortcut target modified successfully.");
                resolutionLabels.get(4).setIcon(getGreenIcon());
            } else {
                resolutionLabels.get(4).setText("Failed to modify BloodTrack Courier shortcut target.");
                resolutionLabels.get(4).setIcon(getRedIcon());
            }
        }
        if (!CombinedChecks.isFireEyeInstalled()) {
            resolutionLabels.get(5).setText("Installing FireEye.");
            try {
                String msiPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator
                        + "ProgramChecker" + File.separator + "FireEye" + File.separator
                        + "xagtSetup_32.30.13_universal.msi";
                Process process = new ProcessBuilder("msiexec", "/i", msiPath, "/qn").start();
                process.waitFor();

                if (CombinedChecks.isFireEyeInstalled()) {
                    resolutionLabels.get(5).setText("FireEye installed.");
                    resolutionLabels.get(5).setIcon(getGreenIcon());
                } else {
                    resolutionLabels.get(5).setText("FireEye installation failed.");
                    resolutionLabels.get(5).setIcon(getRedIcon());
                }
            } catch (Exception e) {
                resolutionLabels.get(5).setText("FireEye installation failed due to an error.");
                resolutionLabels.get(5).setIcon(getRedIcon());
            }
        }
        if (!CombinedChecks.isEGalaxyInstalled()) {
            resolutionLabels.get(6).setText("Installing eGalaxyTouch.");
            if (CombinedChecks.installEGalaxTouchDriver()) {
                resolutionLabels.get(6).setText("eGalaxyTouch installed.");
                resolutionLabels.get(6).setIcon(getGreenIcon());
            } else {
                resolutionLabels.get(6).setText("Failed to install eGalaxyTouch.");
                resolutionLabels.get(6).setIcon(getRedIcon());
            }
        }
        runChecksButton.setVisible(true);

        // Check if there were any issues to resolve
        if (!issuesToResolve) {
            JOptionPane.showMessageDialog(null, "No issues to resolve...!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

    }

}