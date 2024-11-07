/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package BioskopSoal1.User;

import BioskopSoal1.Staff.*;
import BioskopSoal1.Connection.DatabaseConnection;
import BioskopSoal1.LoginForm;
import BioskopSoal1.Util.UserSession;
import com.mysql.jdbc.Statement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ACER
 */
public class PembelianTiket extends javax.swing.JFrame {

    private int idSchedule = -1;
    private List<String> selectedSeats = new ArrayList<>();
    private List<Integer> selectedSeatIds = new ArrayList<>();

    public PembelianTiket(int idSchedule) {

        if (isSessionValid()) {
            setTitle("CinemaQ | Pembelian Tiket");

            initComponents();

            this.idSchedule = idSchedule;
            jamTayang.setModel(new SpinnerDateModel());
            JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(jamTayang, "HH:mm:ss");
            jamTayang.setEditor(timeEditor);
            loadFilm();

            if (this.idSchedule != -1) {
                loadKursi();
            } else {
                JOptionPane.showMessageDialog(this, "Mohon pilih kursi terlebih dahulu", "Error", JOptionPane.ERROR_MESSAGE);
                SwingUtilities.invokeLater(() -> {
                    new ListJadwalTayangForUser().setVisible(true);
                });

                SwingUtilities.invokeLater(this::dispose);
            }

            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    exit();
                }
            });
        } else {
            this.dispose();
        }
    }

    private boolean isSessionValid() {
        if (!UserSession.isSessionValid()) {
            JOptionPane.showMessageDialog(this, "Harap Login Terlebih Dahulu", "Error", JOptionPane.ERROR_MESSAGE);

            SwingUtilities.invokeLater(() -> {
                new LoginForm().setVisible(true);
            });

            SwingUtilities.invokeLater(this::dispose);

            return false;
        }
        return true;
    }

    private void exit() {
        int response = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
            return;
        }
    }

    private void loadFilm() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String query = "SELECT m.title, m.duration, fs.price, fs.schedule_date FROM movies m "
                        + "INNER JOIN film_schedule fs ON m.id = fs.movie_id "
                        + "WHERE fs.id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, idSchedule);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    java.sql.Timestamp scheduleDate = rs.getTimestamp("schedule_date");
                    txtJudul.setText(rs.getString("title"));
                    txtDurasi.setText(rs.getString("duration"));
                    txtHarga.setText(String.valueOf(rs.getDouble("price")));

                    tglTayang.setDate(scheduleDate);

                    Date scheduleTime = new Date(scheduleDate.getTime());
                    jamTayang.setValue(scheduleTime);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadKursi() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id, row, col, is_available FROM seat WHERE film_schedule_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idSchedule);

            ResultSet rs = stmt.executeQuery();

            DefaultTableModel tblModel = new DefaultTableModel();
            tblModel.addColumn("ID");
            tblModel.addColumn("Nomor Kursi");
            tblModel.addColumn("Status");

            tblModel.getDataVector().removeAllElements();
            tblModel.fireTableDataChanged();
            tblModel.setRowCount(0);

            while (rs.next()) {
                Object[] data = new Object[]{
                    rs.getInt("ID"),
                    rs.getString("row") + "-" + rs.getInt("col"),
                    rs.getBoolean("is_available") ? "Available" : "Booked"};
                tblModel.addRow(data);
                tblKursi.setModel(tblModel);
            }

            tblKursi.setDefaultRenderer(Object.class, new AlternatingRowColorRenderer());

            tblKursi.getColumnModel().getColumn(0).setMinWidth(0);
            tblKursi.getColumnModel().getColumn(0).setMaxWidth(0);
            tblKursi.getColumnModel().getColumn(0).setWidth(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtJudul = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDurasi = new javax.swing.JTextField();
        tglTayang = new com.toedter.calendar.JDateChooser();
        jamTayang = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKursi = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtKursi = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTotalHarga = new javax.swing.JTextField();
        btnKembali = new javax.swing.JButton();
        btnCheckout = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuDashboard = new javax.swing.JMenu();
        menuJadwalTayang = new javax.swing.JMenu();
        menuHistori = new javax.swing.JMenu();
        menuLogout = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Pembelian Tiket");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Judul Film");

        txtJudul.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Tanggal Tayang");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Durasi");

        txtDurasi.setEnabled(false);

        tglTayang.setEnabled(false);

        jamTayang.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Jam Tayang");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Harga");

        txtHarga.setEnabled(false);

        tblKursi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Kursi", "Status"
            }
        ));
        tblKursi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKursiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKursi);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Kursi yang Anda Pilih");

        txtKursi.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Total Harga");

        txtTotalHarga.setEnabled(false);

        btnKembali.setForeground(new java.awt.Color(54, 92, 245));
        btnKembali.setText("Kembali");
        btnKembali.setAutoscrolls(true);
        btnKembali.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(54, 92, 245), 2, true));
        btnKembali.setContentAreaFilled(false);
        btnKembali.setPreferredSize(new java.awt.Dimension(109, 35));
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });

        btnCheckout.setBackground(new java.awt.Color(54, 92, 245));
        btnCheckout.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckout.setText("Checkout");
        btnCheckout.setPreferredSize(new java.awt.Dimension(109, 35));
        btnCheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckoutActionPerformed(evt);
            }
        });

        menuDashboard.setText("Dashboard");
        menuDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuDashboardMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuDashboard);

        menuJadwalTayang.setText("Film Hari Ini");
        menuJadwalTayang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuJadwalTayangMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuJadwalTayang);

        menuHistori.setText("Histori Pembelian");
        menuHistori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuHistoriMouseClicked(evt);
            }
        });
        menuHistori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHistoriActionPerformed(evt);
            }
        });
        jMenuBar1.add(menuHistori);

        menuLogout.setText("Logout");
        menuLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuLogoutMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuLogout);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(214, 214, 214)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDurasi, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtKursi, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tglTayang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jamTayang, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8)
                                    .addComponent(txtTotalHarga))))
                        .addGap(51, 51, 51))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel1)
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tglTayang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jamTayang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtDurasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel8)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKursi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(70, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuLogoutMouseClicked
        int response = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            UserSession.clearSession();
            new LoginForm().setVisible(true);
            this.dispose();
        } else {
            return;
        }
    }//GEN-LAST:event_menuLogoutMouseClicked

    private void menuDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuDashboardMouseClicked
        new UserDashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuDashboardMouseClicked

    private void menuJadwalTayangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuJadwalTayangMouseClicked
        new ListJadwalTayang().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuJadwalTayangMouseClicked

    private void menuHistoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHistoriMouseClicked
        new HistoriPembelian().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuHistoriMouseClicked

    private void tblKursiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKursiMouseClicked
        int selectedRow = tblKursi.getSelectedRow();
        String status = tblKursi.getValueAt(selectedRow, 2).toString();

        if (selectedRow != -1 && !status.equals("Booked")) {
            int selectedSeatId = Integer.valueOf(tblKursi.getValueAt(selectedRow, 0).toString());
            String selectedSeatNo = tblKursi.getValueAt(selectedRow, 1).toString();

            if (selectedSeatIds.contains(selectedSeatId)) {
                selectedSeats.remove(selectedSeatNo);
                selectedSeatIds.remove((Integer) selectedSeatId);

                tblKursi.setValueAt("Available", selectedRow, 2);
            } else {
                selectedSeats.add(selectedSeatNo);
                selectedSeatIds.add(selectedSeatId);

                tblKursi.setValueAt("Selected", selectedRow, 2);
            }

            float totalHarga = Float.valueOf(txtHarga.getText()) * selectedSeatIds.size();
            txtKursi.setText(String.join(", ", selectedSeats));
            if (totalHarga > 0) {
                txtTotalHarga.setText(String.valueOf(totalHarga));
            } else {
                txtTotalHarga.setText("0");
            }
        }
    }//GEN-LAST:event_tblKursiMouseClicked

    private void menuHistoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuHistoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuHistoriActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        new ListJadwalTayangForUser().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void btnCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckoutActionPerformed
        if (selectedSeatIds.size() <= 0) {
            JOptionPane.showMessageDialog(this, "Harap Login Terlebih Dahulu", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            try (Connection conn = DatabaseConnection.getConnection()) {
                if (conn != null) {
                    String insertSchedule = "INSERT INTO sales (customer_id, total_amount, schedule_id) VALUES (?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertSchedule, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setInt(1, UserSession.getId());
                        ps.setDouble(2, Double.parseDouble(txtTotalHarga.getText()));
                        ps.setInt(3, idSchedule);
                        ps.executeUpdate();

                        ResultSet rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            int saleId = rs.getInt(1);
                            String insertSeat = "INSERT INTO sales_detail (sale_id, seat_id, price) VALUES (?, ?, ?)";
                            try (PreparedStatement psSeat = conn.prepareStatement(insertSeat)) {
                                for (Integer seatId : selectedSeatIds) {
                                    double price = Double.parseDouble(txtHarga.getText());

                                    psSeat.setInt(1, saleId);
                                    psSeat.setInt(2, seatId);
                                    psSeat.setDouble(3, price);
                                    psSeat.addBatch();
                                }

                                psSeat.executeBatch();
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(this, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                            }

                            String updateSeatStatus = "UPDATE seat SET is_available = false WHERE id = ?";
                            try (PreparedStatement psUpdate = conn.prepareStatement(updateSeatStatus)) {
                                for (Integer seatId : selectedSeatIds) {
                                    psUpdate.setInt(1, seatId);
                                    psUpdate.addBatch();
                                }
                                psUpdate.executeBatch();
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(this, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Sukses Checkout!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    new HistoriPembelian().setVisible(true);
                    this.dispose();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btnCheckoutActionPerformed

    class AlternatingRowColorRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status = table.getValueAt(row, 2).toString();

            if (!isSelected) {
                if ("Booked".equals(status)) {
                    component.setBackground(Color.RED);
                } else if ("Selected".equals(status)) {
                    component.setBackground(Color.GREEN);
                } else {
                    if (row % 2 == 0) {
                        component.setBackground(Color.LIGHT_GRAY);
                    } else {
                        component.setBackground(Color.WHITE);
                    }
                }
            }
//            else {
//                component.setBackground(Color.CYAN); 
//            }

            return component;
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PembelianTiket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PembelianTiket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PembelianTiket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PembelianTiket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PembelianTiket(-1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnKembali;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jamTayang;
    private javax.swing.JMenu menuDashboard;
    private javax.swing.JMenu menuHistori;
    private javax.swing.JMenu menuJadwalTayang;
    private javax.swing.JMenu menuLogout;
    private javax.swing.JTable tblKursi;
    private com.toedter.calendar.JDateChooser tglTayang;
    private javax.swing.JTextField txtDurasi;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJudul;
    private javax.swing.JTextField txtKursi;
    private javax.swing.JTextField txtTotalHarga;
    // End of variables declaration//GEN-END:variables
}
