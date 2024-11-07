/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package BioskopSoal1.Staff;

import BioskopSoal1.Connection.DatabaseConnection;
import BioskopSoal1.LoginForm;
import BioskopSoal1.Util.UserSession;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ACER
 */
public class ListJadwalTayang extends javax.swing.JFrame {

    public ListJadwalTayang() {

        if (isSessionValid()) {
            setTitle("CinemaQ | Jadwal Tayang");

            initComponents();
            showData();

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

    private void showData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String query = """
                    SELECT fs.id AS schedule_id,
                           m.title AS movie_title,
                           m.duration,
                           t.name AS theater_name,
                           fs.schedule_date,
                           fs.price,
                           (SELECT COUNT(id)
                            FROM seat
                            WHERE film_schedule_id = fs.id
                            AND is_available = true) AS available_seat_count
                    FROM film_schedule fs
                    INNER JOIN movies m ON fs.movie_id = m.id
                    INNER JOIN theater t ON fs.theater_id = t.id
                    ORDER BY fs.id DESC;
                """;

                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                DefaultTableModel tblModel = new DefaultTableModel();
                tblModel.addColumn("ID");
                tblModel.addColumn("No.");
                tblModel.addColumn("Judul Film");
                tblModel.addColumn("Durasi");
                tblModel.addColumn("Tanggal Tayang");
                tblModel.addColumn("Jam Tayang");
                tblModel.addColumn("Theater");
                tblModel.addColumn("Harga");
                tblModel.addColumn("Kursi Available");

                tblModel.getDataVector().removeAllElements();
                tblModel.fireTableDataChanged();
                tblModel.setRowCount(0);

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                int no = 1;
                while (rs.next()) {
                    String scheduleDate = rs.getString("schedule_date");
                    String showDate = "";
                    String showTime = "";
                    try {
                        Date date = rs.getTimestamp("schedule_date");
                        showDate = dateFormat.format(date);
                        showTime = timeFormat.format(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Object[] data = new Object[]{
                        rs.getString("schedule_id"),
                        no,
                        rs.getString("movie_title"),
                        rs.getString("duration") + " menit",
                        showDate,
                        showTime,
                        rs.getString("theater_name"),
                        "Rp" + rs.getDouble("price"),
                        rs.getInt("available_seat_count")
                    };
                    tblModel.addRow(data);
                    tblJadwal.setModel(tblModel);
                    no++;
                }

                tblJadwal.getColumnModel().getColumn(0).setMinWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setMaxWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setResizable(true);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void search() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                String where = "";
                String dlimWhere = "";
                String judulFilm = txtJudulFilm.getText();

                if (judulFilm != null && !judulFilm.isEmpty()) {
                    where = "m.title Like '%" + judulFilm + "%'";
                    dlimWhere = " AND ";
                }

                if (inputTglTayang.getDate() != null) {
                    String tglTayang = new SimpleDateFormat("yyyy-MM-dd").format(inputTglTayang.getDate());
                    where += dlimWhere + " DATE(fs.schedule_date) = '" + tglTayang + "'";
                }
                String query = """
                    SELECT fs.id AS schedule_id,
                           m.title AS movie_title,
                           m.duration,
                           t.name AS theater_name,
                           fs.schedule_date,
                           fs.price,
                           (SELECT COUNT(id)
                            FROM seat
                            WHERE film_schedule_id = fs.id
                            AND is_available = true) AS available_seat_count
                    FROM film_schedule fs
                    INNER JOIN movies m ON fs.movie_id = m.id
                    INNER JOIN theater t ON fs.theater_id = t.id
                    WHERE 
                """ + " " + where + " ORDER BY fs.ID DESC";

                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                DefaultTableModel tblModel = new DefaultTableModel();
                tblModel.addColumn("ID");
                tblModel.addColumn("No.");
                tblModel.addColumn("Judul Film");
                tblModel.addColumn("Durasi");
                tblModel.addColumn("Tanggal Tayang");
                tblModel.addColumn("Jam Tayang");
                tblModel.addColumn("Theater");
                tblModel.addColumn("Harga");
                tblModel.addColumn("Kursi Available");

                tblModel.getDataVector().removeAllElements();
                tblModel.fireTableDataChanged();
                tblModel.setRowCount(0);

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                int no = 1;
                boolean dataFound = false;
                while (rs.next()) {
                    dataFound = true;
                    String scheduleDate = rs.getString("schedule_date");
                    String showDate = "";
                    String showTime = "";
                    try {
                        Date date = rs.getTimestamp("schedule_date");
                        showDate = dateFormat.format(date);
                        showTime = timeFormat.format(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Object[] data = new Object[]{
                        rs.getString("schedule_id"),
                        no,
                        rs.getString("movie_title"),
                        rs.getString("duration") + " menit",
                        showDate,
                        showTime,
                        rs.getString("theater_name"),
                        "Rp" + rs.getDouble("price"),
                        rs.getInt("available_seat_count")
                    };
                    tblModel.addRow(data);
                    tblJadwal.setModel(tblModel);
                    no++;
                }

                if (!dataFound) {
                    tblModel.setRowCount(0);
                    tblJadwal.setModel(tblModel);
                }

                tblJadwal.getColumnModel().getColumn(0).setMinWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setMaxWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setWidth(0);
                tblJadwal.getColumnModel().getColumn(0).setResizable(true);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void resetFilter() {
        txtJudulFilm.setText(null);
        inputTglTayang.setDate(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtJudulFilm = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        inputTglTayang = new com.toedter.calendar.JDateChooser();
        btnCari = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblJadwal = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuDashboard = new javax.swing.JMenu();
        menuTheater = new javax.swing.JMenu();
        menuFilm = new javax.swing.JMenu();
        menuJadwalTayang = new javax.swing.JMenu();
        menuPenjualanTiket = new javax.swing.JMenu();
        menuLogout = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Data Jadwal Tayang");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Judul Film");

        txtJudulFilm.setPreferredSize(new java.awt.Dimension(330, 22));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Jadwal Tayang");

        inputTglTayang.setPreferredSize(new java.awt.Dimension(330, 22));

        btnCari.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCari.setForeground(new java.awt.Color(54, 92, 245));
        btnCari.setText("Cari");
        btnCari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(54, 92, 245), 2));
        btnCari.setContentAreaFilled(false);
        btnCari.setPreferredSize(new java.awt.Dimension(109, 35));
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(54, 92, 245));
        btnReset.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(54, 92, 245), 2));
        btnReset.setBorderPainted(false);
        btnReset.setPreferredSize(new java.awt.Dimension(109, 35));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnTambah.setBackground(new java.awt.Color(54, 92, 245));
        btnTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah Data");
        btnTambah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(54, 92, 245), 2));
        btnTambah.setBorderPainted(false);
        btnTambah.setPreferredSize(new java.awt.Dimension(109, 35));
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        tblJadwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Judul Film", "Durasi", "Tanggal Tayang", "Jam Tayang", "Theater", "Harga", "Kursi Available"
            }
        ));
        jScrollPane1.setViewportView(tblJadwal);

        menuDashboard.setText("Dashboard");
        menuDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuDashboardMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuDashboard);

        menuTheater.setText("Theater");
        menuTheater.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuTheaterMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuTheater);

        menuFilm.setText("Film");
        menuFilm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuFilmMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuFilm);

        menuJadwalTayang.setText("Jadwal Tayang");
        menuJadwalTayang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuJadwalTayangMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuJadwalTayang);

        menuPenjualanTiket.setText("Penjualan Tiket");
        menuPenjualanTiket.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuPenjualanTiketMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuPenjualanTiket);

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
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1)
                    .addComponent(btnTambah, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtJudulFilm, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE))
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(inputTglTayang, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1))
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputTglTayang, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJudulFilm, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
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
        new StaffDashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuDashboardMouseClicked

    private void menuFilmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuFilmMouseClicked
        new ListFilm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuFilmMouseClicked

    private void menuJadwalTayangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuJadwalTayangMouseClicked
        new ListJadwalTayang().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuJadwalTayangMouseClicked

    private void menuPenjualanTiketMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuPenjualanTiketMouseClicked
        new PenjualanTiket().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuPenjualanTiketMouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        new FormJadwalTayang().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void menuTheaterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuTheaterMouseClicked
        new ListTheater().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuTheaterMouseClicked

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        search();
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        resetFilter();
        showData();
    }//GEN-LAST:event_btnResetActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(ListJadwalTayang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListJadwalTayang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListJadwalTayang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListJadwalTayang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListJadwalTayang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnTambah;
    private com.toedter.calendar.JDateChooser inputTglTayang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menuDashboard;
    private javax.swing.JMenu menuFilm;
    private javax.swing.JMenu menuJadwalTayang;
    private javax.swing.JMenu menuLogout;
    private javax.swing.JMenu menuPenjualanTiket;
    private javax.swing.JMenu menuTheater;
    private javax.swing.JTable tblJadwal;
    private javax.swing.JTextField txtJudulFilm;
    // End of variables declaration//GEN-END:variables
}
