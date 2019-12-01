package com.hifeful.PhoneBook;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.ArrayList;

public class ContactsTableModel extends AbstractTableModel {
    private String[] columnNames = { "First name", "Surname", "Last name", "Phone number", "Address" };
    private File dataFile;

    private ArrayList<ArrayList<Object>> rows;

    public ContactsTableModel(String aDataFileName)
    {
        rows = new ArrayList<>();

        if (aDataFileName.contentEquals(""))
            dataFile = new File("");
        else
        {
            dataFile = new File(aDataFileName);
            openData(dataFile);
        }
    }

    public int getRowCount()
    {
        return rows.size();
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public Object getValueAt(int r, int c)
    {
        return rows.get(r).get(c);
    }

    public String getColumnName(int c)
    {
        return columnNames[c];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        rows.get(rowIndex).set(columnIndex, aValue);
    }

    public void addRow(ArrayList<Object> row)
    {
        rows.add(row);
    }

    public void removeRow(int index)
    {
        rows.remove(index);
    }

    public void clearRows()
    {
        rows.clear();
    }

    public boolean isEmpty()
    {
        try
        {
             String result = (String)rows.get(0).get(0);
        }
        catch(IndexOutOfBoundsException e)
        {
            return true;
        }
        return false;
    }

    public File getDataFile()
    {
        return dataFile;
    }

    public void setDataFile(File aDataFile)
    {
        dataFile = aDataFile;
    }

    public void saveData(File aFileToSave)
    {
        dataFile = aFileToSave;
        try (var out = new ObjectOutputStream(new FileOutputStream(aFileToSave)))
        {
            out.writeObject(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openData(File aFileToOpen)
    {
        dataFile = aFileToOpen;

        try (var in = new ObjectInputStream(new FileInputStream(aFileToOpen)))
        {
            rows = (ArrayList<ArrayList<Object>>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            JOptionPane.showMessageDialog(null,
                    "The file cannot be open", "File open error",
                    JOptionPane.ERROR_MESSAGE);

            dataFile = new File("");

        }
    }
}
