package com.hifeful.PhoneBook;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class TextCellEditor extends AbstractCellEditor implements TableCellEditor {
    private PhoneBookFrame mainFrame;
    private ContactsTableModel table;
    private JComponent textField = new JTextField();

    public TextCellEditor(PhoneBookFrame frame, ContactsTableModel tableModel)
    {
        mainFrame = frame;
        table = tableModel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                 int row, int column) {
        ((JTextField) textField).setText((String) value);
        return textField;
    }

    @Override
    public Object getCellEditorValue() {
        if (table.getDataFile().getName().contentEquals(""))
            mainFrame.setTitle("Phone book " + "(" + "without file" + ")" + "(unsaved)");
        else
            mainFrame.setTitle("Phone book " + "(" + table.getDataFile().getName() + ")" + "(unsaved)");
        mainFrame.setSavedData(false);

        return ((JTextField) textField).getText();
    }
}
