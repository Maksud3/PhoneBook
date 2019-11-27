package com.hifeful.PhoneBook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AddContactDialog extends JPanel implements ActionListener {
    private JFrame ownerFrame;
    private ContactsTableModel table;

    private JDialog addContactDialog;

    private JButton okButton;

    public AddContactDialog(JFrame frame, ContactsTableModel aTable)
    {
        ownerFrame = frame;
        table = aTable;

        addContactDialog = new JDialog(ownerFrame, true);

        setLayout(new BorderLayout());

        var fieldsPanel = new JPanel(new GridLayout(5 ,2));

        fieldsPanel.add(new JLabel("First name: "));
        var firstNameField = new JTextField(20);
        fieldsPanel.add(firstNameField);

        fieldsPanel.add(new JLabel("Surname: "));
        var surnameField = new JTextField(20);
        fieldsPanel.add(surnameField);

        fieldsPanel.add(new JLabel("Last name: "));
        var lastNameField = new JTextField(20);
        fieldsPanel.add(lastNameField);

        fieldsPanel.add(new JLabel("Phone number: "));
        var phoneNumberField = new JTextField(20);
        fieldsPanel.add(phoneNumberField);

        fieldsPanel.add(new JLabel("Address"));
        var addressField = new JTextField(20);
        fieldsPanel.add(addressField);

        add(fieldsPanel, BorderLayout.CENTER);

        var buttonsPanel = new JPanel();

        okButton = new JButton("OK");
        okButton.addActionListener(event ->
        {
            ArrayList<Object> row = new ArrayList<>();
            row.add(firstNameField.getText());
            row.add(surnameField.getText());
            row.add(lastNameField.getText());
            row.add(phoneNumberField.getText());
            row.add(addressField.getText());

            table.addRow(row);

            table.fireTableDataChanged();

            WindowListener[] windowListeners = addContactDialog.getWindowListeners();
            windowListeners[0].windowClosing(null);
        });

        buttonsPanel.add(okButton);

        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event ->
        {
            WindowListener[] windowListeners = addContactDialog.getWindowListeners();
            windowListeners[0].windowClosing(null);
        });
        buttonsPanel.add(cancelButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        addContactDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                firstNameField.setText("");
                surnameField.setText("");
                lastNameField.setText("");
                phoneNumberField.setText("");
                addressField.setText("");

                addContactDialog.dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e)
    {
        addContactDialog.setTitle("Add contact");
        addContactDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addContactDialog.getRootPane().setDefaultButton(okButton);
        addContactDialog.add(this);
        addContactDialog.pack();
        addContactDialog.setLocation((ownerFrame.getX() + ownerFrame.getWidth() / 2) - getWidth() / 2,
                                     (ownerFrame.getY() + ownerFrame.getHeight() / 2) - getHeight() / 2);
        addContactDialog.setVisible(true);
    }
}
