package com.hifeful.PhoneBook;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.prefs.Preferences;

public class PhoneBookFrame extends JFrame {
    private Preferences root;
    private Preferences node;

    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 600;
    private int width;
    private int height;

    private JTable table;
    private ContactsTableModel tableModel;
    private int[] columnsWidth = { 150, 150, 150, 150, 300};

    private String dataFileName;
    private boolean isSavedData;
    private JMenuItem saveItem;

    private String lastOpenFileLocation;
    private String lastSaveFileLocation;

    public PhoneBookFrame()
    {
        root = Preferences.userRoot();
        node = root.node("/com/hifeful/PhoneBook");

        isSavedData = false;

        frameProperties();

        createTable();

        dataFileName = tableModel.getDataFile().getName();
        if (dataFileName.contentEquals("") && !isSavedData)
        {
            setTitle("Phone Book (unsaved)");
            isSavedData = true;
        }
        else if (!dataFileName.contentEquals("") && isSavedData)
            setTitle("Phone book " + "(" + dataFileName + ")" + "(saved)");
        else if (!dataFileName.contentEquals("") && !isSavedData)
            setTitle("Phone book " + "(" + dataFileName + ")" + "(unsaved)");

        createMenu();

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
        dataFileName = node.get("dataFile", "");
        lastOpenFileLocation = node.get("lastOpenFileLocation", "");
        lastSaveFileLocation = node.get("lastSaveFileLocation", "");

        setLocation(left, top);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (isSavedData)
                {
                    node.putInt("left", getX());
                    node.putInt("top", getY());
                    node.putInt("width", getWidth());
                    node.putInt("height", getHeight());

                    if (tableModel.getDataFile().getName().contentEquals(""))
                        node.put("dataFile", "");
                    else
                        node.put("dataFile", tableModel.getDataFile().getAbsolutePath());

                    System.exit(0);
                }
                else
                {
                    int selection = JOptionPane.showConfirmDialog(null,
                            "Do you want to save?", "New contact book",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (selection == JOptionPane.YES_OPTION)
                    {
                        saveItem.doClick();
                    }
                    else if (selection == JOptionPane.NO_OPTION)
                    {
                        node.putInt("left", getX());
                        node.putInt("top", getY());
                        node.putInt("width", getWidth());
                        node.putInt("height", getHeight());

                        if (tableModel.getDataFile().getName().contentEquals(""))
                            node.put("dataFile", "");
                        else
                            node.put("dataFile", tableModel.getDataFile().getAbsolutePath());

                        System.exit(0);
                    }
                }
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

        saveItem = new JMenuItem("Save", 'S');
        fileMenu.add(saveItem);

        var saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener(event ->
        {
            var fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            fileChooser.setCurrentDirectory(new File(lastSaveFileLocation));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Data files (*.dat)", "dat"));
            fileChooser.setAcceptAllFileFilterUsed(false);

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION)
            {
                File fileToSave = fileChooser.getSelectedFile();
                lastSaveFileLocation = fileToSave.getAbsolutePath().substring(0, fileToSave.getAbsolutePath()
                                                                                .lastIndexOf(File.separator));
                node.put("lastSaveFileLocation", lastSaveFileLocation);
                if (fileToSave.getAbsolutePath().endsWith(".dat"))
                {
                    tableModel.saveData(fileToSave);
                    isSavedData = true;
                    dataFileName = tableModel.getDataFile().getName();
                    setTitle("Phone book " + "(" + dataFileName + ")" + "(saved)");
                }
                else
                {
                    JOptionPane.showMessageDialog(this,
                            "File extension must be .dat", "File extension error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        fileMenu.add(saveAsItem);

        saveItem.addActionListener(event ->
        {
            if (!tableModel.getDataFile().exists())
                saveAsItem.doClick();
            else
            {
                tableModel.saveData(tableModel.getDataFile());
                isSavedData = true;
                dataFileName = tableModel.getDataFile().getName();
                setTitle("Phone book " + "(" + dataFileName + ")" + "(saved)");
            }
        });

        newItem.addActionListener(event ->
        {
            if (!isSavedData)
            {
                if (!tableModel.isEmpty())
                {
                    int selection = JOptionPane.showConfirmDialog(this,
                            "Do you want to save?", "New contact book",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (selection == JOptionPane.YES_OPTION)
                    {
                        saveItem.doClick();
                    }
                    else if (selection == JOptionPane.NO_OPTION)
                    {
                        tableModel.setDataFile(new File(""));
                        isSavedData = false;
                        setTitle("Phone Book (unsaved)");
                        tableModel.clearRows();
                        tableModel.fireTableDataChanged();
                    }
                }
            }
            else
            {
                tableModel.setDataFile(new File(""));
                isSavedData = false;
                setTitle("Phone Book (unsaved)");
                tableModel.clearRows();
                tableModel.fireTableDataChanged();
            }

        });

        openItem.addActionListener(event ->
        {
            if (isSavedData)
            {
                var fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(lastOpenFileLocation));
                fileChooser.setFileFilter(new FileNameExtensionFilter("Data files (*.dat)", "dat"));
                fileChooser.setAcceptAllFileFilterUsed(false);

                int result = fileChooser.showOpenDialog(this);

                if (result == JFileChooser.APPROVE_OPTION)
                {
                    File selectedFile = fileChooser.getSelectedFile();
                    lastOpenFileLocation = selectedFile.getAbsolutePath().substring(0, selectedFile
                                                                            .getAbsolutePath()
                                                                            .lastIndexOf(File.separator));
                    node.put("lastOpenFileLocation", lastOpenFileLocation);
                    tableModel.openData(selectedFile);
                    isSavedData = true;
                    dataFileName = tableModel.getDataFile().getName();
                    setTitle("Phone book " + "(" + dataFileName + ")" + "(saved)");
                    tableModel.fireTableDataChanged();
                }
            }
            else
            {
                int selection = JOptionPane.showConfirmDialog(this,
                        "Do you want to save?", "New contact book",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (selection == JOptionPane.YES_OPTION)
                {
                    saveItem.doClick();
                }
                else if (selection == JOptionPane.NO_OPTION)
                {
                    isSavedData = true;
                    openItem.doClick();
                }
            }
        });

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
        var addContactDialog = new AddContactDialog(this, tableModel);
        addButton.addActionListener(addContactDialog);
        buttonPanel.add(addButton);

        var deleteButton = new JButton("Delete Contact");
        deleteButton.addActionListener(event ->
        {
            int[] selectedRows = table.getSelectedRows();

            for (int i = selectedRows.length - 1; i >= 0; i--)
            {
                tableModel.removeRow(selectedRows[i]);
            }

            isSavedData = false;
            if (tableModel.getDataFile().getName().contentEquals(""))
                setTitle("Phone book " + "(" + "without file" + ")" + "(unsaved)");
            else
                setTitle("Phone book " + "(" + tableModel.getDataFile().getName() + ")" + "(unsaved)");

            tableModel.fireTableDataChanged();
        });
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createTable()
    {
        tableModel = new ContactsTableModel(dataFileName);
        table = new JTable(tableModel);

        isSavedData = !tableModel.getDataFile().getName().contentEquals("");

        table.getColumnModel().getColumn(1).setCellEditor(new TextCellEditor());

        var sorter = new TableRowSorter<TableModel>(tableModel);
        sorter.setSortable(4, false);
        table.setRowSorter(sorter);

        TableColumnModel tableColumnModel = table.getColumnModel();

        int i = 0;
        for (int width : columnsWidth)
        {
            TableColumn tableColumn = tableColumnModel.getColumn(i++);
            tableColumn.setPreferredWidth(width);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public boolean getSavedData()
    {
        return isSavedData;
    }

    public void setSavedData(boolean aIsSavedData)
    {
        isSavedData = aIsSavedData;
    }
}
