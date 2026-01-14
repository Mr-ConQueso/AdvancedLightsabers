package net.mrconqueso.lightsabers.common.lightsaber.builder;

import net.mrconqueso.lightsabers.common.lightsaber.CrystalColor;
import net.mrconqueso.lightsabers.common.lightsaber.FocusingCrystal;
import net.mrconqueso.lightsabers.common.lightsaber.PartType;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SaberBuilder extends JFrame {
    // Manually populate this list or load from a file since we are outside the game instance
    private static final List<String> KNOWN_PARTS = Arrays.asList(
            "advlightsabers:emitter_vader",
            "advlightsabers:emitter_luke",
            "advlightsabers:switch_obiwan",
            "advlightsabers:grip_anakin",
            "advlightsabers:pommel_yoda",
            "advlightsabers:standard_assembly" // Placeholder for testing
    );

    private final int margin = 5;
    private final int width;

    // Data holders
    private final Map<PartType, String> selectedParts = new HashMap<>();
    private final List<FocusingCrystal> selectedCrystals = new ArrayList<>();
    private CrystalColor selectedColor = CrystalColor.RED;

    private final String[] partNames = {"Emitter", "Switch Section", "Grip", "Pommel"};
    private final JComboBox<String>[] hiltParts = new JComboBox[partNames.length];
    private final JComboBox<String>[] focusingCrystals = new JComboBox[2];

    private final JComboBox<CrystalColor> colorCrystal = new JComboBox<>(CrystalColor.values());
    private final JButton colorPreview = new JButton();

    private final JTextField commandField = new JTextField();

    private SaberBuilder(String title, int width, int height) {
        super(title);
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.width = width;

        // Initialize default selection
        for (PartType type : PartType.values()) {
            selectedParts.put(type, KNOWN_PARTS.get(0));
        }

        // 1. Part Selectors
        {
            Vector<String> v = new Vector<>(KNOWN_PARTS);

            for (int i = 0; i < partNames.length; ++i) {
                JComboBox<String> box = hiltParts[i] = new JComboBox<>(v);
                add(i % 2 + 1, 2, partNames[i], box, i / 2);

                int j = i; // final for lambda
                box.addActionListener(e -> {
                    selectedParts.put(PartType.values()[j], (String) box.getSelectedItem());
                    updateScreen();
                });
            }
        }

        // 2. Focusing Crystals
        {
            List<String> list = new ArrayList<>();
            list.add("- None -");
            for (FocusingCrystal c : FocusingCrystal.values()) list.add(c.name());
            Vector<String> v = new Vector<>(list);

            add(1, 2, "Focusing Crystals", focusingCrystals[0] = new JComboBox<>(v), 2);
            add(2, 2, "", focusingCrystals[1] = new JComboBox<>(v), 2);

            for (int i = 0; i < 2; ++i) {
                int index = i;
                focusingCrystals[i].addActionListener(e -> {
                    updateCrystals();
                    updateScreen();
                });
            }
        }

        // 3. Color
        {
            colorPreview.setFocusable(false);
            colorPreview.setBorderPainted(true);
            colorCrystal.addActionListener(e -> {
                selectedColor = (CrystalColor) colorCrystal.getSelectedItem();
                updateScreen();
            });
            add(1, 2, "Color", colorCrystal, 3);
            add(2, 2, "", colorPreview, 3);
        }

        // 4. Command Output
        {
            Font f = commandField.getFont();
            commandField.setEditable(false);
            commandField.setFont(new Font(f.getName(), Font.BOLD, f.getSize()));
            add(1, 1, "Command (Copy this)", commandField, 4);
        }

        updateScreen();
        this.setVisible(true);
    }

    private void updateCrystals() {
        selectedCrystals.clear();
        for (JComboBox<String> box : focusingCrystals) {
            String sel = (String) box.getSelectedItem();
            if (sel != null && !sel.startsWith("-")) {
                selectedCrystals.add(FocusingCrystal.valueOf(sel));
            }
        }
    }

    private void updateScreen() {
        // Build NBT String
        StringBuilder nbt = new StringBuilder();
        nbt.append("{Lightsaber:{");

        // Parts
        for (PartType type : PartType.values()) {
            nbt.append(type.name()).append(":\"").append(selectedParts.get(type)).append("\",");
        }

        // Color
        nbt.append("Color:").append(selectedColor.id).append(",");

        // Crystals
        nbt.append("FocusingCrystals:[");
        for (int i = 0; i < selectedCrystals.size(); i++) {
            nbt.append("\"").append(selectedCrystals.get(i).name()).append("\"");
            if (i < selectedCrystals.size() - 1) nbt.append(",");
        }
        nbt.append("]");

        nbt.append("}}");

        // Visuals
        colorPreview.setBackground(new Color(selectedColor.color));
        commandField.setText("/give @p advlightsabers:lightsaber" + nbt.toString());
    }

    private void add(int index, int stack, String text, JComponent comp, int row) {
        int w = width - margin * (stack - 1);
        add(text, comp, margin * index + w / stack * (index - 1), margin + row * 45, w / stack);
    }

    private void add(String text, JComponent comp, int x, int y, int width) {
        if (!text.isEmpty()) {
            JLabel label = new JLabel(text);
            label.setBounds(x, y, width - 5, 20);
            this.add(label);
        }
        comp.setBounds(x, y + 20, width, 20);
        this.add(comp);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SaberBuilder("Lightsaber Builder 2.0 (NBT)", 500, 350));
    }
}
