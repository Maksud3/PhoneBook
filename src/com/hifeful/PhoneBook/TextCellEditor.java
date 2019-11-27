package com.hifeful.PhoneBook;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class TextCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JComponent textField = new JTextField();

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                 int row, int column) {
        ((JTextField) textField).setText((String) value);
        return textField;
    }

    @Override
    public Object getCellEditorValue() {
        return ((JTextField) textField).getText();
    }
}
