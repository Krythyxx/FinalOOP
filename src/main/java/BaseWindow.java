import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    // column positions of the reservation table
    protected static final int COL_GUEST = 1, COL_EMAIL = 2, COL_PHONE = 3, COL_ROOM = 4,
            COL_CHECK_IN = 5, COL_CHECK_OUT = 6, COL_BASE = 7, COL_EXTRA = 8, COL_TOTAL = 9,
            COL_STATUS = 10;

    // reservations the dashboard table still shows
    private static final List<String> ACTIVE_STATUSES = List.of("Pending", "Confirmed", "Checked In");

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

    //gives the table its columns and model
    protected static void initTable(JTable table, DefaultTableModel model, String... columns) {
        for (String name : columns) model.addColumn(name);
        table.setModel(model);
    }

    //replaces every row of the model
    protected static void fillTable(DefaultTableModel model, List<Object[]> rows) {
        model.setRowCount(0);
        for (Object[] row : rows) model.addRow(row);
    }

    protected static void styleFields(JTextField... fields) {
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

    //runs the action every time the text of the field changes
    protected static void onTextChange(JTextComponent field, Runnable action) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { action.run(); }
            public void removeUpdate(DocumentEvent e)  { action.run(); }
            public void changedUpdate(DocumentEvent e) { action.run(); }
        });
    }

    //table helpers

    //sorting, status, and search bar, guest does not have search option
    protected void installFilter(JTable table, DefaultTableModel model, JTextField search, JComboBox<?> statusBox) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        Runnable refilter = () -> applyFilter(sorter, search, statusBox);
        statusBox.addActionListener(e -> refilter.run());
        if (search != null) {
            styleFields(search);
            onTextChange(search, refilter);
            setSearchHint(search);
        }
    }

    private void applyFilter(TableRowSorter<DefaultTableModel> sorter, JTextField search, JComboBox<?> statusBox) {
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

    //double digit format
    private static String money(Object value) {
        return value instanceof Number ? String.format("%.2f", ((Number) value).doubleValue()) : "";
    }

    //Room combo box

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static void loadRoomChoices(JComboBox box, boolean availableOnly) {
        box.removeAllItems();
        for (DbManager.RoomOption opt : DbManager.getRoomOptions(availableOnly)) box.addItem(opt);
    }

    protected static String roomNumberOf(JComboBox<?> box) {
        Object selected = box.getSelectedItem();
        if (selected instanceof DbManager.RoomOption) return ((DbManager.RoomOption) selected).roomNumber;
        return selected == null ? "" : selected.toString();
    }

    protected static void selectRoom(JComboBox<?> box, String roomNumber) {
        for (int i = 0; i < box.getItemCount(); i++) {
            Object item = box.getItemAt(i);
            if (item instanceof DbManager.RoomOption && ((DbManager.RoomOption) item).roomNumber.equals(roomNumber)) {
                box.setSelectedIndex(i);
                return;
            }
        }
    }

    //reservation form
    protected static class ReservationForm {
        private final JTextField guest, email, phone, checkIn, checkOut, request, comments;
        private final JSpinner guests;
        private final JComboBox<?> rooms;
        private final JTextArea base, extra, total;
        private final JLabel capacityLabel;
        private final JTable table;
        private final String fixedGuestName;

        ReservationForm(String fixedGuestName, JTable table,
                        JTextField guest, JTextField email, JTextField phone, JComboBox<?> rooms,
                        JTextField checkIn, JTextField checkOut, JSpinner guests,
                        JTextField request, JTextField comments,
                        JTextArea base, JTextArea extra, JTextArea total, JLabel capacityLabel) {
            this.fixedGuestName = fixedGuestName; this.table = table;
            this.guest = guest; this.email = email; this.phone = phone; this.rooms = rooms;
            this.checkIn = checkIn; this.checkOut = checkOut; this.guests = guests;
            this.request = request; this.comments = comments;
            this.base = base; this.extra = extra; this.total = total;
            this.capacityLabel = capacityLabel;

            styleFields(guest, email, phone, checkIn, checkOut, request, comments);
            guests.setModel(new SpinnerNumberModel(1, 1, 30, 1));

            // read only
            for (JTextArea area : new JTextArea[]{base, extra, total}) {
                area.setEditable(false);
                area.setEnabled(true);
                area.setForeground(Color.WHITE);
            }
            if (fixedGuestName != null) {
                guest.setEnabled(true);
                guest.setEditable(false);
            }

            // the price follows whatever the user changes
            onTextChange(checkIn, this::refreshQuote);
            onTextChange(checkOut, this::refreshQuote);
            rooms.addActionListener(e -> {
                refreshCapacity();
                refreshQuote();
            });
            guests.addChangeListener(e -> refreshQuote());

            //Clicking a row moves its values to the form
            table.getSelectionModel().addListSelectionListener(event -> {
                int row = table.getSelectedRow();
                if (event.getValueIsAdjusting() || row == -1) return;
                showRow(row);
            });

            clear();
        }

        Reservation toReservation() {
            return new Reservation(
                    0,
                    guest.getText().trim(),
                    email.getText().trim(),
                    phone.getText().trim(),
                    roomNumberOf(rooms),
                    checkIn.getText().trim(),
                    checkOut.getText().trim(),
                    (Integer) guests.getValue(),
                    request.getText(),
                    comments.getText());
        }

        void clear() {
            table.clearSelection();
            guest.setText(fixedGuestName == null ? "" : fixedGuestName);
            email.setText("");
            phone.setText("");
            checkIn.setText("");
            checkOut.setText("");
            request.setText("");
            comments.setText("");
            guests.setValue(1);
            if (rooms.getItemCount() > 0) rooms.setSelectedIndex(0);
            refreshQuote();
        }

        private void showRow(int row) {
            guest.setText(cellText(table, row, COL_GUEST));
            email.setText(cellText(table, row, COL_EMAIL));
            phone.setText(cellText(table, row, COL_PHONE));
            selectRoom(rooms, cellText(table, row, COL_ROOM));
            checkIn.setText(cellText(table, row, COL_CHECK_IN));
            checkOut.setText(cellText(table, row, COL_CHECK_OUT));

            //save value replace viewed value right away
            base.setText(money(table.getValueAt(row, COL_BASE)));
            extra.setText(money(table.getValueAt(row, COL_EXTRA)));
            total.setText(money(table.getValueAt(row, COL_TOTAL)));
        }

        //max capacity displayer
        private void refreshCapacity() {
            int max = DbManager.getCapacity(roomNumberOf(rooms));
            capacityLabel.setText(max == 0 ? "Max. Capacity: -"
                    : "Max. Capacity: " + max + (max == 1 ? " guest" : " guests"));
        }

        //data preview displayer
        private void refreshQuote() {
            double[] quote = null;
            try {
                LocalDate in = LocalDate.parse(checkIn.getText().trim(), Reservation.DATE_FMT);
                LocalDate out = LocalDate.parse(checkOut.getText().trim(), Reservation.DATE_FMT);
                String room = roomNumberOf(rooms);
                if (!room.isEmpty()) quote = DbManager.getQuote(room, in, out, (Integer) guests.getValue());
            } catch (DateTimeParseException ex) {
                // dates are still being typed
            }
            base.setText(quote == null ? "" : money(quote[0]));
            extra.setText(quote == null ? "" : money(quote[1]));
            total.setText(quote == null ? "" : money(quote[2]));
        }
    }

    //add clear button reservation
    protected void bindBookingButtons(ReservationForm form, JButton add, JButton clear) {
        add.addActionListener(e -> saveReservation(form.toReservation(), "Pending"));
        clear.addActionListener(e -> form.clear());
    }

    //status update and delete
    protected void bindStatusButtons(JTable table, DefaultTableModel model, ReservationForm form,
                                     JButton accept, JButton decline, JButton checkIn,
                                     JButton checkOut, JButton cancel, JButton delete) {
        accept.addActionListener(e -> changeStatus(table, model, "Confirmed", "Pending"));
        decline.addActionListener(e -> changeStatus(table, model, "Declined", "Pending"));
        checkIn.addActionListener(e -> changeStatus(table, model, "Checked In", "Confirmed"));
        checkOut.addActionListener(e -> changeStatus(table, model, "Checked Out", "Checked In"));
        cancel.addActionListener(e -> changeStatus(table, model, "Cancelled", "Pending", "Confirmed"));
        delete.addActionListener(e -> {
            if (deleteSelectedReservation(table)) form.clear();
        });
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

    //The Rooms tab
    protected class RoomForm {
        private final JTable table;
        private final JTextField number, price, description;
        private final JComboBox<?> type, status;
        private final JSpinner capacity;

        RoomForm(JTable table, JTextField number, JComboBox<?> type, JTextField price,
                 JSpinner capacity, JComboBox<?> status, JTextField description,
                 JButton add, JButton update, JButton delete) {
            this.table = table; this.number = number; this.type = type; this.price = price;
            this.capacity = capacity; this.status = status; this.description = description;

            capacity.setModel(new SpinnerNumberModel(2, 1, 20, 1));

            add.addActionListener(e -> save(null));
            update.addActionListener(e -> {
                int id = selectedId(table);
                if (id != -1) save(id);
            });
            delete.addActionListener(e -> deleteSelected());

            //Clicking a room moves its values to the room fields
            table.getSelectionModel().addListSelectionListener(event -> {
                int row = table.getSelectedRow();
                if (event.getValueIsAdjusting() || row == -1) return;

                number.setText(cellText(table, row, 1));
                type.setSelectedItem(cellText(table, row, 2));
                price.setText(cellText(table, row, 3));
                capacity.setValue(Integer.parseInt(cellText(table, row, 4)));
                status.setSelectedItem(cellText(table, row, 5));
                description.setText(cellText(table, row, 6));
            });
        }

        private void save(Integer id) {
            double roomPrice;
            try {
                roomPrice = Double.parseDouble(price.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Price must be a number.");
                return;
            }
            String roomNumber = number.getText().trim();
            String roomType = String.valueOf(type.getSelectedItem());
            int roomCapacity = (Integer) capacity.getValue();
            String roomStatus = String.valueOf(status.getSelectedItem());
            String roomDescription = description.getText().trim();

            String error = (id == null)
                    ? DbManager.addRoom(roomNumber, roomType, roomPrice, roomCapacity, roomStatus, roomDescription)
                    : DbManager.updateRoom(id, roomNumber, roomType, roomPrice, roomCapacity, roomStatus, roomDescription);
            if (error != null) { JOptionPane.showMessageDialog(null, error); return; }

            number.setText(""); price.setText(""); description.setText("");
            table.clearSelection();
            loadRooms();
            if (id != null) loadReservations();
        }

        private void deleteSelected() {
            int id = selectedId(table);
            if (id == -1) return;
            if (JOptionPane.showConfirmDialog(null, "Delete this room?", "Confirm",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            String error = DbManager.deleteRoom(id);
            if (error != null) JOptionPane.showMessageDialog(null, error);
            loadRooms();
        }
    }

    //Dashboard
    protected void loadDashboard(DefaultTableModel model, List<Object[]> reservations,
                                 JTextField available, JTextField occupied, JTextField checkIns,
                                 JTextField pending, JTextField revenue) {
        List<Object[]> rows = new ArrayList<>();
        for (Object[] r : reservations) {
            if (!ACTIVE_STATUSES.contains(String.valueOf(r[COL_STATUS]))) continue;
            rows.add(new Object[]{r[0], r[COL_GUEST], r[COL_ROOM], r[COL_CHECK_IN],
                    r[COL_CHECK_OUT], r[COL_TOTAL], r[COL_STATUS]});
        }
        fillTable(model, rows);

        DbManager.DashboardStats stats = DbManager.getDashboardStats();
        showStat(available, String.valueOf(stats.availableRooms));
        showStat(occupied, String.valueOf(stats.occupiedRooms));
        showStat(checkIns, String.valueOf(stats.checkInsToday));
        showStat(pending, String.valueOf(stats.pendingRequests));
        showStat(revenue, String.format("₱%,.2f", stats.potentialRevenue));
    }

    private static void showStat(JTextField field, String value) {
        field.setEditable(false);
        field.setText(value);
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
