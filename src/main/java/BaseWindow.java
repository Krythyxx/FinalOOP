import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//Parent Class
public abstract class BaseWindow {
    protected static final String SEARCH_HINT = "Search by guest name or room";

    // shared table columns
    protected static final String[] DASHBOARD_COLUMNS =
            {"ID", "GUEST", "ROOM", "CHECK-IN", "CHECK-OUT", "AMOUNT", "STATUS"};
    protected static final String[] RESERVATION_COLUMNS =
            {"ID", "GUEST", "EMAIL", "PHONE", "ROOM", "CHECK-IN", "CHECK-OUT",
                    "BASE AMT", "EXTRA CHARGE", "TOTAL", "STATUS"};
    protected static final String[] ROOM_COLUMNS =
            {"ID", "ROOM NO.", "TYPE", "PRICE/NIGHT", "MAX CAPACITY", "STATUS", "DESCRIPTION"};

    // column positions
    protected static final int COL_GUEST = 1, COL_ROOM = 4, COL_STATUS = 10;

    private static final Color HEADER_BG = new Color(40, 50, 60);
    private static final Color PANEL_BG = new Color(30, 40, 50);

    // encapsulation
    private final String username;

    protected BaseWindow(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    //abstract method for each window difference
    public abstract JPanel getPanel();
    public abstract String getWindowTitle();
    protected abstract void loadRooms();
    protected abstract void loadReservations();

    //table designing
    protected static void setColumns(DefaultTableModel model, String... names) {
        for (String name : names) model.addColumn(name);
    }

    protected void styleFields(JTextField... fields) {
        for (JTextField f : fields) {
            f.setBorder(BorderFactory.createCompoundBorder(
                    f.getBorder(),
                    BorderFactory.createEmptyBorder(0, 8, 0, 8)));
            f.setCaretColor(Color.WHITE);
        }
    }

    protected void styleTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setOpaque(true);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                c.setBackground(HEADER_BG);
                c.setForeground(Color.WHITE);
                c.setFont(new Font("SansSerif", Font.BOLD, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
    }

    //header styled scroller
    protected JScrollPane createScrollPane(JTable table) {
        styleTableHeader(table);
        JScrollPane pane = new JScrollPane(table);
        pane.getViewport().setBackground(PANEL_BG);
        pane.setBackground(PANEL_BG);
        return pane;
    }

    //search hint info color design
    protected void setSearchHint(JTextField field) {
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(SEARCH_HINT)) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isBlank()) {
                    field.setText(SEARCH_HINT);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    //table helpers
    // search is optional, due to guest
    protected void applyFilter(TableRowSorter<DefaultTableModel> sorter, JTextField search, JComboBox<?> statusBox) {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        //only searches the guest name and room
        if (search != null) {
            String text = search.getText().trim();
            if (!text.isEmpty() && !text.equals(SEARCH_HINT)) {
                filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(text), COL_GUEST, COL_ROOM));
            }
        }

        //all status
        if (statusBox.getSelectedIndex() > 0) {
            String status = statusBox.getSelectedItem().toString();
            filters.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(status) + "$", COL_STATUS));
        }

        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    //Returns the ID of the selected row
    protected int selectedId(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row first.");
            return -1;
        }
        return (int) table.getValueAt(row, 0);
    }

    // text of a table cell
    protected static String cellText(JTable table, int row, int col) {
        Object value = table.getValueAt(row, col);
        return value != null ? value.toString() : "";
    }

    //convert string to double
    protected static double parseMoney(JTextField f) {
        try {
            return Double.parseDouble(f.getText().trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    //Reservations

    //Saves to database
    protected boolean saveReservation(Reservation rsv, String status) {
        String error = rsv.validate();
        if (error != null) {
            JOptionPane.showMessageDialog(null, error);
            return false;
        }
        String dbError = DbManager.addReservation(rsv, status);
        if (dbError != null) {
            JOptionPane.showMessageDialog(null, dbError);
            return false;
        }
        loadReservations();
        return true;
    }

    // handles statuses to make sense, and saves the new status to the database
    protected void changeStatus(JTable table, DefaultTableModel model, String newStatus, String... allowedFrom) {
        int id = selectedId(table);
        if (id == -1) return;

        int modelRow = table.convertRowIndexToModel(table.getSelectedRow());
        String current = String.valueOf(model.getValueAt(modelRow, COL_STATUS));

        for (String allowed : allowedFrom) {
            if (allowed.equalsIgnoreCase(current)) {
                String error = DbManager.updateReservationStatus(id, newStatus);
                if (error != null) {
                    JOptionPane.showMessageDialog(null, error);
                    return;
                }
                loadReservations();
                return;
            }
        }
        JOptionPane.showMessageDialog(null,
                "Cannot change status to \"" + newStatus + "\" when it is \"" + current + "\".");
    }

    //delete the selected reservation on the table
    protected boolean deleteSelectedReservation(JTable table) {
        int id = selectedId(table);
        if (id == -1) return false;
        if (JOptionPane.showConfirmDialog(null, "Delete this reservation permanently?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return false;
        String error = DbManager.deleteReservation(id);
        if (error != null) JOptionPane.showMessageDialog(null, error);
        loadReservations();
        return error == null;
    }

    //log out button on the top right
    protected void logOut(Component source) {
        try {
            Main mainWindow = new Main();
            JPanel loginPanel = mainWindow.getMainPanel();
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(source);
            if (currentFrame != null && loginPanel != null) {
                currentFrame.setContentPane(loginPanel);
                currentFrame.setSize(550, 750);
                currentFrame.setTitle("Hotel and Resort Management System");
                currentFrame.setLocationRelativeTo(null);
                currentFrame.revalidate();
                currentFrame.repaint();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}