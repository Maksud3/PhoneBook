package com.hifeful.PhoneBook;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.prefs.Preferences;

public class PhoneBookFrame extends JFrame {
    private Preferences root;
    private Preferences node;

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;
    private int width;
    private int height;

    private JTable table;
    private ContactsTableModel tableModel;
    private int[] columnsWidth = { 30, 150, 150, 150, 150, 300};

    private int comeBack;

    public PhoneBookFrame()
    {
        root = Preferences.userRoot();
        node = root.node("/com/hifeful/PhoneBook");

        frameProperties();

        createMenu();

        createTable();

        createButtons();

        pack();
        setSize(width, height);
    }
    private void frameProperties()
    {
        int left = node.getInt("left", 0);
        int top = node.getInt("top", 0);
        width = node.getInt("width", DEFAULT_WIDTH);
        height = node.getInt("height", DEFAULT_HEIGHT);

        setLocation(left, top);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                node.putInt("left", getX());
                node.putInt("top", getY());
                node.putInt("width", getWidth());
                node.putInt("height", getHeight());

                System.exit(0);
            }
        });
    }

    private void createMenu()
    {
        var menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        var fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        menuBar.add(fileMenu);

        var newItem = new JMenuItem("New", 'N');
        fileMenu.add(newItem);

        var openItem = new JMenuItem("Open", 'O');
        fileMenu.add(openItem);

        var saveItem = new JMenuItem("Save", 'S');
        fileMenu.add(saveItem);

        var saveAsItem = new JMenuItem("Save As");
        fileMenu.add(saveAsItem);

        fileMenu.addSeparator();

        var exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(event ->
        {
            WindowListener[] windowListeners = getWindowListeners();
            windowListeners[0].windowClosing(null);
        });

        fileMenu.add(exitItem);
    }

    private void createButtons()
    {
        var buttonPanel = new JPanel(new FlowLayout());

        var addButton = new JButton("Add Contact");
        addButton.addActionListener(new AddContactDialog(this, tableModel));
        buttonPanel.add(addButton);

        var deleteButton = new JButton("Delete Contact");
        deleteButton.addActionListener(event ->
        {
            int[] selectedRows = table.getSelectedRows();

            for (int i = selectedRows.length - 1; i >= 0; i--)
            {
                tableModel.removeRow(selectedRows[i]);
            }

            tableModel.setDeletedIndexes(selectedRows.length);

            tableModel.fireTableDataChanged();
        });
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createTable()
    {
        tableModel = new ContactsTableModel();
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(1).setCellEditor(new TextCellEditor());

        TableColumnModel tableColumnModel = table.getColumnModel();
        tableColumnModel.getColumn(0).setResizable(false);

        int i = 0;
        for (int width : columnsWidth)
        {
            TableColumn tableColumn = tableColumnModel.getColumn(i++);
            tableColumn.setPreferredWidth(width);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
